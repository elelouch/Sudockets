package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static game.SudokuSettings.*;
import static game.connection.ConnectionOptions.*;

public class SudokuClient {
    private static final int PORT = 31145;
    private Socket clientSocket;
    private String ip;
    private SudokuBoard board;
    private Thread listenerThread;
    private UpdateSender sender;

    public SudokuClient(String newIp, SudokuBoard newBoard) {
        ip = newIp;
        board = newBoard;
    }

    public void startConnection() {
        try {
            byte[] buffer = new byte[CELLS_AMOUNT.value];
            clientSocket = new Socket(ip, PORT);
            sender = new UpdateSender(clientSocket.getOutputStream());
            InputStream in = clientSocket.getInputStream();
            in.read(buffer);
            board.startNewBoard(unflatBoard(buffer));
            listenerThread = new Thread(new UpdateListener(in, board));
            listenerThread.start();
        } catch (IOException e) {
            System.err.println("Couldn't connect client to server");
            e.printStackTrace();
        }
    }

    public void endConnection() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Socket is already closed");
                e.printStackTrace();
            }
        }
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
