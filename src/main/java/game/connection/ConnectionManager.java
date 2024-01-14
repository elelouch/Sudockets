package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    private static final int PORT = 6789;
    private SudokuBoard game;
    private UpdateListener updateListener;
    private OutputStream outputStream;
    private ServerSocket server;
    private Socket client;

    public ConnectionManager(SudokuBoard sudokuGame) {
        game = sudokuGame;
        updateListener = new UpdateListener(sudokuGame);
        sudokuGame.setConnectionManager(this);
    }

    public static byte[] flatBoard(int[][] sudoku) {
        byte[] buffer = new byte[81];
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                buffer[i * sudoku.length + j] = (byte) sudoku[i][j];
            }
        }
        return buffer;
    }

    public static int[][] unflatBoard(byte[] buffer) {
        int[][] sudoku = new int[9][9];
        for (int i = 0; i < sudoku.length; i++) {
            sudoku[i] = new int[9];
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j] = (int) buffer[i * sudoku.length + j];
            }
        }
        return sudoku;
    }

    public void startAsServer() {
        try {
            server = new ServerSocket(PORT);
            client = server.accept();
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();

            outputStream = out;
            updateListener.setSharingStream(in);
            Thread updateListenerThread = new Thread(updateListener);
            updateListenerThread.start();

            int[][] sudokuBoard = game.getBoardCopy();
            outputStream.write(flatBoard(sudokuBoard));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAsClient(String ip) {
        try {
            client = new Socket(ip, PORT);
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            // se esta saliendo de la rutina
            outputStream = out;
            byte[] flatGame = new byte[81];
            in.read(flatGame);
            game.startNewBoard(unflatBoard(flatGame));

            updateListener.setSharingStream(in);
            Thread updateListenerThread = new Thread(updateListener);
            updateListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendUpdate(int i, int j, int number) {
        try {
            byte[] buffer = new byte[]{(byte) i, (byte) j, (byte) number};
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
