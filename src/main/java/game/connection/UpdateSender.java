package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.io.OutputStream;

public class UpdateSender {
    private OutputStream outputStream;

    UpdateSender(OutputStream out) {
        outputStream = out;
    }

    public void sendUpdate(int i, int j, int number) throws IOException {
        outputStream.write(new byte[]{(byte)Updates.UPDATE.getValue(), (byte) i, (byte) j, (byte) number});
    }

    public void sendUndo(int i, int j) throws IOException {
        outputStream.write(new byte[]{(byte)Updates.UNDO.getValue(), (byte) i, (byte) j});
    }

    public void sendFullUpdate(int[][] sudoku) throws IOException {
        outputStream.write((byte)Updates.FULL_UPDATE.getValue());
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                outputStream.write((byte)sudoku[i][j]);
            }
        }
    }

}
