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

    public ConnectionManager (SudokuBoard sudokuGame) {
        game = sudokuGame;
        updateListener = new UpdateListener(sudokuGame);
        sudokuGame.setConnectionManager(this);
    }

    public void startAsServer() {
        try (ServerSocket server = new ServerSocket(PORT);
             Socket client = server.accept();
             InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()
        ) {
            updateListener.setSharingStream(in);
            Thread updateListenerThread = new Thread(updateListener);
            updateListenerThread.start();
            outputStream = out;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAsClient(String ip) {
        try (Socket client = new Socket(ip, PORT);
             InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()
        ) {
            updateListener.setSharingStream(in);
            Thread updateListenerThread = new Thread(updateListener);
            updateListenerThread.start();
            outputStream = out;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendUpdate(int i, int j, int number) {
        if (outputStream != null) {
            try{
                byte[] buffer = new byte[]{(byte)i, (byte)j, (byte)number};
                outputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
