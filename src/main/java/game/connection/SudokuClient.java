package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

import static game.SudokuSettings.*;

public class SudokuClient extends UpdateSender {
    private static final int PORT = 31145;
    private Socket clientSocket;
    private SudokuBoard board;

    public SudokuClient(String ip, SudokuBoard newBoard) throws IOException {
        byte[] buffer = new byte[CELLS_AMOUNT.value];
        clientSocket = new Socket(ip, PORT);
        board = newBoard;

        setOutputStream(clientSocket.getOutputStream());

        InputStream in = clientSocket.getInputStream();
        in.read(buffer);
        board.startNewBoard(unflatBoard(buffer));

        UpdateListener listener = new UpdateListener(board);
        listener.setSharingStream(in);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    private static int[][] unflatBoard(byte[] flatSudoku) {
        int[][] sudoku = new int[BOARD_WIDTH.value][BOARD_WIDTH.value];
        for (int i = 0; i < sudoku.length; i++) {
            sudoku[i] = new int[sudoku.length];
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j] = flatSudoku[i * sudoku.length + j];
            }
        }
        return sudoku;
    }

}
