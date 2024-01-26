package game.ui.sudoku.panel;

import game.ui.painter.SudokuCellsPainter;
import game.ui.sudoku.game.SudokuGame;
import game.ui.sudoku.tracker.SudokuTracker;
import game.ui.sudoku.exceptions.UnsolvableSudokuException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

import static game.SudokuSettings.*;

public class SudokuPanel extends JPanel implements SudokuGame {
    private static final GridLayout SUDOKU_LAYOUT = new GridLayout(BOX_WIDTH.value, BOX_WIDTH.value);
    private static final int BORDER_PIXELS = 1;
    private static final Border DEFAULT_BORDER = LineBorder.createBlackLineBorder();
    private static final Border THICKER_BORDER = BorderFactory.createLineBorder(Color.black, BORDER_PIXELS + 2);

    private final JPanel[][] boxes;
    private final SudokuButton[][] cells;
    private final SudokuCellsPainter painter;
    private final SudokuTracker gameTracker;
    private SudokuButton selectedCell;

    public SudokuPanel(SudokuTracker newGameTracker) {
        gameTracker = newGameTracker;
        boxes = new JPanel[BOX_WIDTH.value][BOX_WIDTH.value];
        cells = new SudokuButton[BOARD_WIDTH.value][BOARD_WIDTH.value];
        painter = new SudokuCellsPainter(cells);
        setBoxes();
        setLayout(SUDOKU_LAYOUT);
        setCells();
        setVisible(true);
    }

    public void setCells() {
        for (int i = 0; i < BOARD_WIDTH.value; i++) {
            cells[i] = new SudokuButton[BOARD_WIDTH.value];
            for (int j = 0; j < BOARD_WIDTH.value; j++) {
                SudokuButton newCell = generateNewCell(i, j);
                cells[i][j] = newCell;
                boxes[i / BOX_WIDTH.value][j / BOX_WIDTH.value].add(newCell);
            }
        }
    }


    private void setBoxes() {
        for (int i = 0; i < BOX_WIDTH.value; i++) {
            boxes[i] = new JPanel[BOX_WIDTH.value];
            for (int j = 0; j < boxes.length; j++) {
                JPanel newBox = new JPanel();
                boxes[i][j] = newBox;
                newBox.setLayout(SUDOKU_LAYOUT);
                newBox.setBorder(THICKER_BORDER);
                add(newBox);
            }
        }
    }

    private SudokuButton generateNewCell(int i, int j) {
        SudokuButton newCell = new SudokuButton(i, j);
        newCell.setBorder(DEFAULT_BORDER);
        newCell.addActionListener(e -> {
            if (selectedCell != null) {
                selectedCell.setBorder(DEFAULT_BORDER);
            }
            selectedCell = (SudokuButton) e.getSource();
            selectedCell.setBorder(THICKER_BORDER);

            painter.setSelectedCell(selectedCell.getRow(), selectedCell.getCol());
            painter.paintCells();
        });
        return newCell;
    }

    public void updateBoard() throws UnsolvableSudokuException {
        int[][] newBoard = gameTracker.getBoardCopy();
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard.length; j++) {
                int value = newBoard[i][j];
                undoCell(i, j);
                if (value != EMPTY_CELL.value) {
                    setCell(i, j, value);
                }
            }
        }
    }

    @Override
    public synchronized void undoCell(int i, int j) {
        SudokuButton cell = cells[i][j];
        if (gameTracker.isSolution(i, j, cell.getValue()))
            return;
        cell.setModifiable();
        cell.undo();
        gameTracker.undoCell(i, j);
    }

    public void undoCell() {
        undoCell(selectedCell.getRow(), selectedCell.getCol());
    }

    @Override
    public synchronized void setCell(int i, int j, int number) {
        SudokuButton cellToModify = cells[i][j];
        cellToModify.setValue(number);
        cellToModify.setForeground(Color.black);
        gameTracker.setCell(i, j, number);

        if (!gameTracker.isSolution(i, j, number)) {
            cellToModify.setForeground(Color.red);
        }
    }

    public void fillCell(int number) {
        setCell(selectedCell.getRow(), selectedCell.getCol(), number);
    }

    public void addNote(int number) {
        selectedCell.addNote(number);
    }
}
