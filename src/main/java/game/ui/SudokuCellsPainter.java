package game.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;

import static game.SudokuSettings.BOARD_WIDTH;
import static game.SudokuSettings.BOX_WIDTH;

public class SudokuCellsPainter {
    private static final int MAX_COLORED_CELLS = 21;
    private final ArrayDeque<SudokuCell> paintedCells = new ArrayDeque<>(MAX_COLORED_CELLS);
    private SudokuCell selectedCell;
    private SudokuCell[][] cells;

    public SudokuCellsPainter(SudokuCell[][] cells) {
        this.cells = cells;
    }

    private void unpaintCells() {
        while (!paintedCells.isEmpty()) {
            paintedCells.removeLast().setBackground(Color.white);
        }
    }

    private void paintCellAndAddToStack(SudokuCell cell) {
        cell.setBackground(Color.lightGray);
        paintedCells.addLast(cell);
    }

    private void paintCellsBasedOnSelectedCell() {
        int boxWidth = BOX_WIDTH.value;
        int row = selectedCell.getRow();
        int col = selectedCell.getCol();
        int boxRowLowerBound = row / BOX_WIDTH.value;
        int boxColLowerBound = col / BOX_WIDTH.value;
        int boxRowUpperBound = boxRowLowerBound + boxWidth;
        int boxColUpperBound = boxColLowerBound + boxWidth;

        for (int k = 0; k < BOARD_WIDTH.value; k++) {
            if (k / boxWidth != boxColLowerBound) {
                paintCellAndAddToStack(cells[row][k]);
            }
            if (k / boxWidth != boxRowLowerBound) {
                paintCellAndAddToStack(cells[k][col]);
            }
        }
        for (int i = boxRowLowerBound; i < boxRowUpperBound; i++) {
            for (int j = boxColLowerBound; j < boxColUpperBound; j++) {
                paintCellAndAddToStack(cells[i][j]);
            }
        }
    }

    public void setSelectedCell(int i, int j) {
        selectedCell = cells[i][j];
    }

    public void paintCells() {
        if(selectedCell == null)
            return;
        unpaintCells();
        paintCellsBasedOnSelectedCell();
    }
}
