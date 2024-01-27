package game.connection;


import game.ui.sudoku.panel.GameUI;
import game.ui.sudoku.exceptions.UnsolvableSudokuException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static game.SudokuSettings.*;
import static game.connection.Updates.*;

public class UpdateListener implements Runnable {
    public static final HashMap<Integer, Updates> options;

    static {
        options = new HashMap<>();
        options.put(0, UPDATE);
        options.put(1, UNDO);
        options.put(2, FULL_UPDATE);
        options.put(3, END_CONNECTION);
    }

    InputStream sharingStream;
    GameUI boardToUpdate;
    int lastOptionReceived;

    public UpdateListener(InputStream in, GameUI board) {
        sharingStream = in;
        boardToUpdate = board;
    }

    @Override
    public void run() {
        try {
            while (true) {
                lastOptionReceived = sharingStream.read();
                parseBufferAndUpdateBoard();
            }
        } catch (IOException e) {
            System.err.println("Couldn't read from buffer, connection might be closed. Ending thread.");
        }
    }

    public void parseBufferAndUpdateBoard() throws IOException {
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
                boardToUpdate.setAllCells(parseFullUpdate());
                break;
            case END_CONNECTION:
                sharingStream.close();
                break;
        }
    }

    public int[][] parseFullUpdate() throws IOException {
        int size = BOARD_WIDTH.getValue();
        int[][] sudoku = new int[size][size];
        for(int[] row : sudoku) {
            row = new int[size];
            for (int j = 0; j < size; j++) {
                row[j] = sharingStream.read();
            }
        }
        return sudoku;
    }

}
