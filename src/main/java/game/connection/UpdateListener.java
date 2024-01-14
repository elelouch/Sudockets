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

    @Override
    public void run() {
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
