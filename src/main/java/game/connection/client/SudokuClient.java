package game.connection.client;

import game.connection.updaters.SudokuUpdateSender;
import game.connection.updaters.SudokuUpdateListener;
import game.ui.sudoku.panel.SudokuGameUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SudokuClient {
    private static final int PORT = 31145;
    private static Thread clientThread = null;

    public static void initializeClient(SudokuGameUI newBoard, String address) {
        if (clientThread != null)
            return;

        clientThread = new Thread(() -> {
            SudokuUpdateSender sender = null;
            try (Socket client = new Socket(address, PORT);
                 InputStream in = client.getInputStream();
                 OutputStream out = client.getOutputStream()) {
                sender = new SudokuUpdateSender(out);
                newBoard.subscribe(sender);
                Thread listenerThread = new Thread(new SudokuUpdateListener(in, newBoard));
                listenerThread.start();
                try {
                    listenerThread.join();
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted while waiting for thread " + listenerThread.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (sender != null) {
                    newBoard.unSubscribe(sender);
                }
                clientThread = null;
            }
        });
        clientThread.start();
    }

    public static void finalizeClient() {
        if (clientThread != null) {
            clientThread.interrupt();
            clientThread = null;
        }
    }

}
