package game.connection.server;

import game.ui.sudoku.panel.SudokuGameUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SudokuServer {
    private static final int PORT = 31145;
    private static final int MAX_CLIENTS = 5;
    private static Thread serverThread = null;

    public static void initializeServer(SudokuGameUI newBoard) {
        if (serverThread != null)
            return;

        // since each client is gonna require at least one update listener,
        // then we need 2 thread, one for the client and another for its updater
        // thus the * 2 to clients
        serverThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(PORT)) {
                ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CLIENTS * 2);
                while (!Thread.currentThread().isInterrupted()) {
                    try (Socket request = server.accept()){
                        threadPool.submit(new SudokuClientRequest(request, newBoard, threadPool));
                    } catch (IOException e) {
                        System.err.println("Client couldn't connect listening again");
                    }
                }
                threadPool.shutdownNow();
            } catch (IOException e) {
                System.err.println("Couldn't create server");
                e.printStackTrace();
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
