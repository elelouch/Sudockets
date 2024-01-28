package game.connection.server;

import game.ui.sudoku.panel.SudokuGameUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static game.SudokuSettings.SERVER_PORT;

public class SudokuServer {
    private static final int MAX_CLIENTS = 5;
    private static Thread serverThread = null;

    public static void initializeServer(SudokuGameUI newBoard) {
        if (serverThread != null)
            return;

        // since each client is gonna require at least one update listener,
        // then we need 2 thread, one for the client and another for its updater
        // thus the * 2 to clients
        serverThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(SERVER_PORT.getValue())) {
                ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CLIENTS * 2);
                while (!Thread.currentThread().isInterrupted()) {
                    Socket request = server.accept();
                    threadPool.submit(new SudokuClientRequest(request, newBoard, threadPool));
                }
                threadPool.shutdownNow();
            } catch (IOException e) {
                System.err.println("Couldn't create server (Might be on 2min timeout, another " +
                        "server is up) or there " +
                        "was an error on the client side");
            } finally {
                serverThread = null;
            }
        });
        serverThread.start();
    }

    public static void finalizeServer() {
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }
    }

}
