package game.connection.updaters;


import game.ui.sudoku.panel.SudokuGameUI;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static game.SudokuSettings.*;

public class SudokuUpdateListener implements Runnable {
    public static final HashMap<Integer, SudokuUpdate> options;

    static {
        options = new HashMap<>();
        for (SudokuUpdate sudokuUpdate : SudokuUpdate.values()) {
            options.put(sudokuUpdate.getValue(), sudokuUpdate);
        }
    }

    InputStream sharingStream;
    SudokuGameUI boardToUpdate;
    int lastOptionReceived;

    public SudokuUpdateListener(InputStream in, SudokuGameUI board) {
        sharingStream = in;
        boardToUpdate = board;
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        try {
//            Asking for interruption was added since ExecutorService was utilized for managing
//            threads, ExecutorService.shutdownNow() interrupts all threads currently running
//            in its queue.

//            Reading a closed end (signalized by the end of the stream) will return -1.
//            Thus the condition.
            while (!current.isInterrupted() && (lastOptionReceived = sharingStream.read()) != -1) {
                parseBufferAndUpdateBoard();
            }
        } catch (IOException e) {
            System.err.println("Couldn't read from buffer, connection might be closed. Ending " +
                    "thread.");
        }
    }

    private void parseBufferAndUpdateBoard() throws IOException {
        int i, j, number;
        switch (options.get(lastOptionReceived)) {
            case UPDATE:
                i = sharingStream.read();
                j = sharingStream.read();
                number = sharingStream.read();
                boardToUpdate.setCell(i, j, number);
                break;
            case UNDO:
                i = sharingStream.read();
                j = sharingStream.read();
                boardToUpdate.undoCell(i, j);
                break;
            case FULL_UPDATE:
                boardToUpdate.setAllCells(readBoard());
                break;
            case SOLUTION:
                boardToUpdate.setSolution(readBoard());
                break;
            case END_CONNECTION:
                sharingStream.close();
                break;
        }
    }

//    It a sequence of 81 bytes and set it as a 9x9 cell
    public int[][] readBoard() throws IOException {
        int size = BOARD_WIDTH.getValue();
        int[][] board = new int[size][size];
        for (int i = 0; i < size; i++) {
            board[i] = new int[size];
            for (int j = 0; j < size; j++) {
                board[i][j] = sharingStream.read();
            }
        }
        return board;
    }

}
