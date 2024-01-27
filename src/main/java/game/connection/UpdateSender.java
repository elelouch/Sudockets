package game.connection;

import game.ui.sudoku.game.SudokuGame;

import java.io.IOException;
import java.io.OutputStream;

import static game.SudokuSettings.CELLS_AMOUNT;

public class UpdateSender implements SudokuGame {
    private final OutputStream outputStream;

    UpdateSender(OutputStream out) {
        outputStream = out;
    }

    @Override
    public void setCell(int i, int j, int num) {
        writeBuffer(new byte[]{(byte) Updates.UPDATE.getValue(), (byte) i, (byte) j, (byte) num});
    }

    @Override
    public void undoCell(int i, int j) {
        writeBuffer(new byte[]{(byte) Updates.UNDO.getValue(), (byte) i, (byte) j});
    }

    @Override
    public void setAllCells(int[][] game) {
        byte[] flatBoard = new byte[CELLS_AMOUNT.getValue()];
        int count = 0;
        for (int[] row : game) {
            for (int cellNumber : row) {
                flatBoard[count++] = (byte) cellNumber;
            }
        }
        writeBuffer(new byte[]{(byte) Updates.FULL_UPDATE.getValue()});
        writeBuffer(flatBoard);
    }

    private void writeBuffer(byte[] buffer) {
        try {
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
