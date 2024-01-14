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

    public ConnectionManager(SudokuBoard sudokuGame) {
        game = sudokuGame;
        updateListener = new UpdateListener(sudokuGame);
        sudokuGame.setConnectionManager(this);
    }

    public byte[] flatBoard(int[][] sudoku) {
        byte[] buffer = new byte[81];
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                buffer[i * sudoku.length + j] = (byte) sudoku[i][j];
            }
        }
        return buffer;
    }

    public void startAsServer() {
        try (ServerSocket server = new ServerSocket(PORT);
             Socket client = server.accept();
             InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()
        ) {
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
        try (Socket client = new Socket(ip, PORT);
             InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()
        ) {
            outputStream = out;
            updateListener.setSharingStream(in);
            Thread updateListenerThread = new Thread(updateListener);
            updateListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendUpdate(int i, int j, int number) {
        if (outputStream != null) {
            try {
                byte[] buffer = new byte[]{(byte) i, (byte) j, (byte) number};
                outputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
