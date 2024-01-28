package game.connection.updaters;

import game.ui.sudoku.game.SudokuGame;

import java.io.IOException;
import java.io.OutputStream;

import static game.SudokuSettings.CELLS_AMOUNT;

public class SudokuUpdateSender implements SudokuGame {
    private final OutputStream outputStream;

    public SudokuUpdateSender(OutputStream out) {
        outputStream = out;
    }

    @Override
    public void setCell(int i, int j, int num) {
        writeBuffer(new byte[]{(byte) SudokuUpdate.UPDATE.getValue(), (byte) i, (byte) j, (byte) num});
    }

    @Override
    public void undoCell(int i, int j) {
        writeBuffer(new byte[]{(byte) SudokuUpdate.UNDO.getValue(), (byte) i, (byte) j});
    }

    @Override
    public void setAllCells(int[][] board) {
        byte[] flatBoard = new byte[CELLS_AMOUNT.getValue()];
        int count = 0;
        for (int[] row : board) {
            for (int cellNumber : row) {
                flatBoard[count++] = (byte) cellNumber;
            }
        }
        writeBuffer(new byte[]{(byte) SudokuUpdate.FULL_UPDATE.getValue()});
        writeBuffer(flatBoard);
    }

    @Override
    public void setSolution(int[][] solution) {
        byte[] flatBoardSolution = new byte[CELLS_AMOUNT.getValue()];
        int count = 0;
        for (int[] row : solution) {
            for (int cellNumber : row) {
                flatBoardSolution[count++] = (byte) cellNumber;
            }
        }
        writeBuffer(new byte[]{(byte) SudokuUpdate.SOLUTION.getValue()});
        writeBuffer(flatBoardSolution);
    }

    private void writeBuffer(byte[] buffer) {
        try {
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
