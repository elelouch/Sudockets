package game.connection;


import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;

public class UpdateListener implements Runnable {
    private static final int BUFFER_SIZE = 3;
    InputStream sharingStream;
    SudokuBoard boardGame;

    public UpdateListener(SudokuBoard board) {
        boardGame = board;
    }

    public void setSharingStream(InputStream in) {
        sharingStream = in;
    }

    public int[][] unflatBoard(byte[] buffer) {
        int[][] sudoku = new int[9][9];
        for (int i = 0; i < sudoku.length; i++) {
            sudoku[i] = new int[9];
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j] = (int)buffer[i * sudoku.length + j];
            }
        }
        return sudoku;
    }

    @Override
    public void run() {
        byte[] flatGame = new byte[81];
        int[][] sudoku = unflatBoard(flatGame);
        boardGame.startNewBoard(sudoku);
        while(true) {
            try {
                byte[] buffer = new byte[3];
                int cellRow;
                int cellCol;
                int cellNum;
                sharingStream.read(buffer);
                cellRow = buffer[0];
                cellCol = buffer[1];
                cellNum = buffer[2];
                boardGame.fillCell(cellRow,cellCol, cellNum);
            } catch(IOException e) {
                System.err.println("Couldn't read from buffer");
                e.printStackTrace();
            }
        }
    }
}
