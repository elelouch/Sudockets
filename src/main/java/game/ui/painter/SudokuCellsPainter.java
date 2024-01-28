package game.ui.painter;

import game.ui.sudoku.panel.GameButton;

import java.awt.*;
import java.util.ArrayDeque;

import static game.SudokuSettings.BOARD_WIDTH;
import static game.SudokuSettings.BOX_WIDTH;

public class SudokuCellsPainter implements CellsPainter {
    private static final int MAX_COLORED_CELLS = 21;
    private final ArrayDeque<GameButton> paintedCells = new ArrayDeque<>(MAX_COLORED_CELLS);
    private GameButton selectedCell;
    private GameButton[][] cells;

    public SudokuCellsPainter(GameButton[][] cells) {
        this.cells = cells;
    }

    private void unpaintCells() {
        while (!paintedCells.isEmpty()) {
            paintedCells.removeLast().setBackground(Color.white);
        }
    }

    private void paintCellAndAddToStack(GameButton cell) {
        cell.setBackground(Color.lightGray);
        paintedCells.addLast(cell);
    }

    private void paintCellsBasedOnSelectedCell() {
        int boxWidth = BOX_WIDTH.getValue();
        int row = selectedCell.getRow();
        int col = selectedCell.getCol();
        int boxRowLowerBound = row / BOX_WIDTH.getValue();
        int boxColLowerBound = col / BOX_WIDTH.getValue();

        for (int k = 0; k < BOARD_WIDTH.getValue(); k++) {
            if (k / boxWidth != boxColLowerBound) {
                paintCellAndAddToStack(cells[row][k]);
            }
            if (k / boxWidth != boxRowLowerBound) {
                paintCellAndAddToStack(cells[k][col]);
            }
        }

        boxRowLowerBound *= BOX_WIDTH.getValue();
        boxColLowerBound *= BOX_WIDTH.getValue();

        for (int i = boxRowLowerBound; i < boxRowLowerBound + 3; i++) {
            for (int j = boxColLowerBound; j < boxColLowerBound + 3; j++) {
                paintCellAndAddToStack(cells[i][j]);
            }
        }
    }

    public void setSelectedCell(int i, int j) {
        selectedCell = cells[i][j];
    }

    @Override
    public void paintCells() {
        if (selectedCell == null) {
            return;
        }
        unpaintCells();
        paintCellsBasedOnSelectedCell();
    }
}
