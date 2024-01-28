package game.connection;

import game.ui.sudoku.panel.GameUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SudokuServer {
    private static final int PORT = 31145;
    private static Thread serverThread = null;

    public static void initializeServer(GameUI newBoard) {
        if(serverThread != null)
            return;

        Thread serverThread = new Thread(() -> {
            UpdateSender sender = null;
            try (ServerSocket server = new ServerSocket(PORT);
                 Socket client = server.accept();
                 InputStream in = client.getInputStream();
                 OutputStream out = client.getOutputStream()) {
                sender = new UpdateSender(out);
                newBoard.subscribe(sender);
                newBoard.notifyFullUpdate();
                Thread listenerThread = new Thread(new UpdateListener(in, newBoard));
                listenerThread.start();
                try {
                    listenerThread.join();
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while waiting for thread " + listenerThread.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (sender != null){
                    newBoard.unSubscribe(sender);
                }
            }
        });
        serverThread.start();
    }

    public static void finalizeServer() {
        if (serverThread != null){
            serverThread.interrupt();
            serverThread = null;
        }
    }

}
