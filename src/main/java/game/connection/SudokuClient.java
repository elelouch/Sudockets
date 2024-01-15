package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SudokuClient extends UpdateSender {
    private static final int SIZE = 9;
    Socket clientSocket;
    SudokuBoard board;

    public SudokuClient(String ip, SudokuBoard newBoard) throws IOException {
        byte[] buffer = new byte[81];
        clientSocket = new Socket(ip, 6789);
        board = newBoard;

        setOutputStream(clientSocket.getOutputStream());

        InputStream in = clientSocket.getInputStream();
        in.read(buffer);
        board.startNewBoard(unflatBoard(buffer));

        UpdateListener listener = new UpdateListener(board);
        listener.setSharingStream(in);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();

        board.setUpdateSender(this);
    }

    private static int[][] unflatBoard(byte[] flatSudoku) {
        int[][] sudoku = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sudoku[i][j] = flatSudoku[i * SIZE + j];
            }
        }
        return sudoku;
    }

}
