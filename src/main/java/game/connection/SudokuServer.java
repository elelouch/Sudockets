package game.connection;

import game.ui.sudoku.panel.GameUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SudokuServer implements Connecter {
    private static final int PORT = 31145;
    private ServerSocket server;
    private Socket client;
    private Thread listenerThread;
    private GameUI board;
    private UpdateSender sender;

    public SudokuServer(GameUI newBoard) {
        try {
            board = newBoard;
            server = new ServerSocket(PORT);
            client = server.accept();
            UpdateListener listener = new UpdateListener(client.getInputStream(), board);
            listenerThread = new Thread(listener);
            sender = new UpdateSender(client.getOutputStream());
            board.suscribe(sender);
        } catch (IOException e) {
            System.err.println("Couldn't connect to server");
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
                client.getInputStream().close();
                client.getOutputStream().close();
                client.close();
                server.close();
                board.unsuscribe(sender);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
