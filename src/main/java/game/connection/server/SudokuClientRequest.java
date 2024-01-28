package game.connection.server;

import game.connection.updaters.SudokuUpdateListener;
import game.connection.updaters.SudokuUpdateSender;
import game.ui.sudoku.panel.SudokuGameUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//The idea of this class is to make a concurrent response to a client request.
//This class enables the server to support many request concurrently
public class SudokuClientRequest implements Runnable {
    private Socket client;
    private ExecutorService threadPool;
    private SudokuGameUI sudokuGameUI;

    SudokuClientRequest(Socket client, SudokuGameUI sudokuGameUI, ExecutorService threadPool) {
        this.client = client;
        this.threadPool = threadPool;
        this.sudokuGameUI = sudokuGameUI;
    }


    @Override
    public void run() {
        SudokuUpdateSender sender = null;
        try (InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {
            sender = new SudokuUpdateSender(out);
//            The order of notifications is important, since
//            the gameUI can't work properly if the solution is not yet available
//            Note that its peer (SudokuClient), doesn't notify neither a solution neither
//            a fullUpdate, since it only has to update its own board.
            sudokuGameUI.subscribe(sender);
            sudokuGameUI.notifySolution();
            sudokuGameUI.notifyFullUpdate();
            Future<?> listenerSubmitted = threadPool.submit(new SudokuUpdateListener(in, sudokuGameUI));
            try {
                listenerSubmitted.get();
            } catch (InterruptedException e) {
                System.err.println("Thread was interrupted while waiting for thread");
            } catch (ExecutionException e) {
                System.err.println("The listener thread (the one its waiting for) threw an exception");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sender != null) {
                sudokuGameUI.unSubscribe(sender);
            }
        }
    }


}
