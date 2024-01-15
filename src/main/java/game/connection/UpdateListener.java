package game.connection;


import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class UpdateListener implements Runnable {
    private static final int BUFFER_SIZE = 82;
    private static final int SIZE = 9;
    private static final byte UPDATE = 0;
    private static final byte UNDO = 1;
    private static final byte FULL_UPDATE = 2;

    InputStream sharingStream;
    SudokuBoard boardGame;

    public UpdateListener(SudokuBoard board) {
        boardGame = board;
    }

    public void setSharingStream(InputStream in) {
        sharingStream = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                sharingStream.read(buffer);
                parseBufferAndUpdateBoard(buffer);
            } catch (IOException e) {
                System.err.println("Couldn't read from buffer");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void parseBufferAndUpdateBoard(byte[] buffer) {
        byte option = buffer[0];
        int i, j, number;
        switch (option) {
            case UPDATE:
                i = buffer[1];
                j = buffer[2];
                number = buffer[3];
                boardGame.fillCell(i, j, number);
                break;
            case UNDO:
                i = buffer[1];
                j = buffer[2];
                boardGame.undoCell(i, j);
                break;
            case FULL_UPDATE:
                int[][] fullUpdate = parseFullUpdate(buffer);
                boardGame.startNewBoard(fullUpdate);
                break;
        }
    }

    public static int[][] parseFullUpdate(byte[] buffer) {
        int[][] sudoku = new int[SIZE][SIZE];
        for (int i = 0; i < sudoku.length; i++) {
            sudoku[i] = new int[SIZE];
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j] = (int) buffer[i * SIZE + j + 1];
            }
        }
        return sudoku;
    }

}
