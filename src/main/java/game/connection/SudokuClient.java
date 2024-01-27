package game.connection;

import game.ui.sudoku.panel.GameUI;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SudokuClient implements Connecter {
    private static final int PORT = 31145;
    private Socket clientSocket;
    private Thread listenerThread;
    private UpdateSender sender;
    private GameUI board;


    public SudokuClient(String ip, GameUI newBoard) {
        try {
            board = newBoard;
            clientSocket = new Socket(ip, PORT);
            InputStream in = clientSocket.getInputStream();
            listenerThread = new Thread(new UpdateListener(in, board));
            sender = new UpdateSender(clientSocket.getOutputStream());
            board.suscribe(sender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startConnection() {
            listenerThread.start();
    }

    public void endConnection() {
        if (clientSocket != null) {
            try {
                clientSocket.getOutputStream().close();
                clientSocket.getInputStream().close();
                clientSocket.close();
                board.desuscribe(sender);
            } catch (IOException e) {
                System.err.println("Socket is already closed");
                e.printStackTrace();
            }
        }
    }

}
