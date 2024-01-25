package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SudokuServer implements Connecter {
    private static final int PORT = 31145;
    ServerSocket server;
    Socket client;
    SudokuBoard boardToUpdate;
    Thread listenerThread;

    public SudokuServer(SudokuBoard newBoard) {
        try {
            server = new ServerSocket(PORT);
            boardToUpdate = newBoard;
            UpdateSender sender = new UpdateSender(client.getOutputStream());
            boardToUpdate.setUpdateSender(sender);
            UpdateListener listener = new UpdateListener(client.getInputStream(), boardToUpdate);
            listenerThread = new Thread(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startConnection() {
        listenerThread.start();
    }

    @Override
    public void endConnection() {
        try {
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
