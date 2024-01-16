package game.connection;


import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static game.SudokuSettings.*;
import static game.connection.ConnectionOptions.*;

public class UpdateListener implements Runnable {
    private static final Map<Byte, ConnectionOptions> byteToOptions;
    static {
        byteToOptions = new HashMap<>();
        byteToOptions.put((byte)0, UPDATE);
        byteToOptions.put((byte)1, UNDO);
        byteToOptions.put((byte)2, FULL_UPDATE);
        byteToOptions.put((byte)3, END_CONNECTION);
    }

    InputStream sharingStream;
    SudokuBoard boardToUpdate;
    byte[] buffer;

    public UpdateListener(InputStream in, SudokuBoard board) {
        buffer = new byte[CELLS_AMOUNT.value + 1];
        sharingStream = in;
        boardToUpdate = board;
        try {
            sharingStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseFullUpdate();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sharingStream.read(buffer);
                parseBufferAndUpdateBoard();
            } catch (IOException e) {
                System.err.println("Couldn't read from buffer, connection might be closed");
                e.printStackTrace();
                break;
            }
        }
    }

    public void parseBufferAndUpdateBoard() {
        ConnectionOptions option = byteToOptions.get(buffer[0]);
        int i, j, number;
        switch (option) {
            case UPDATE:
                i = buffer[1];
                j = buffer[2];
                number = buffer[3];
                boardToUpdate.fillCell(i, j, number);
                break;
            case UNDO:
                i = buffer[1];
                j = buffer[2];
                boardToUpdate.undoCell(i, j);
                break;
            case FULL_UPDATE:
                int[][] fullUpdate = parseFullUpdate();
                boardToUpdate.startNewBoard(fullUpdate);
                break;
            case END_CONNECTION:
                try {
                    sharingStream.close();
                } catch(IOException e) {
                    System.err.println("Couldn't close the socket stream in UpdateListener");
                    e.printStackTrace();
                }
                break;
        }
    }

    public int[][] parseFullUpdate() {
        int[][] sudoku = new int[BOARD_WIDTH.value][BOARD_WIDTH.value];
        for (int i = 0; i < sudoku.length; i++) {
            sudoku[i] = new int[sudoku.length];
            for (int j = 0; j < sudoku[i].length; j++) {
                sudoku[i][j] = (int) buffer[i * sudoku.length + j + 1];
            }
        }
        return sudoku;
    }

}
