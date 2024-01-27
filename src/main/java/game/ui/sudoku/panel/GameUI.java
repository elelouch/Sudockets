package game.ui.sudoku.panel;

import game.ui.painter.SudokuCellsPainter;
import game.ui.sudoku.game.SudokuGame;
import game.ui.sudoku.tracker.SolutionTester;
import game.ui.sudoku.tracker.SudokuGameUpdater;
import game.ui.sudoku.exceptions.UnsolvableSudokuException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

import static game.SudokuSettings.*;

public class GameUI extends JPanel implements SudokuGame {
    private static final GridLayout SUDOKU_LAYOUT = new GridLayout(BOX_WIDTH.getValue(), BOX_WIDTH.getValue());
    private static final int BORDER_PIXELS = 1;
    private static final Border DEFAULT_BORDER = LineBorder.createBlackLineBorder();
    private static final Border THICKER_BORDER = BorderFactory.createLineBorder(Color.black, BORDER_PIXELS + 2);

    private final JPanel[][] boxes;
    private final GameButton[][] cells;
    private final SudokuCellsPainter painter;
    private GameButton selectedCell;
    private SolutionTester solutionTester;

    public GameUI(SolutionTester tester) {
        solutionTester = tester;
        boxes = new JPanel[BOX_WIDTH.getValue()][BOX_WIDTH.getValue()];
        cells = new GameButton[BOARD_WIDTH.getValue()][BOARD_WIDTH.getValue()];
        painter = new SudokuCellsPainter(cells);
        setBoxes();
        setLayout(SUDOKU_LAYOUT);
        setCells();
        setVisible(true);
    }

    public void setCells() {
        for (int i = 0; i < BOARD_WIDTH.getValue(); i++) {
            cells[i] = new GameButton[BOARD_WIDTH.getValue()];
            for (int j = 0; j < BOARD_WIDTH.getValue(); j++) {
                GameButton newCell = generateNewCell(i, j);
                cells[i][j] = newCell;
                boxes[i / BOX_WIDTH.getValue()][j / BOX_WIDTH.getValue()].add(newCell);
            }
        }
    }

    private void setBoxes() {
        for (int i = 0; i < BOX_WIDTH.getValue(); i++) {
            boxes[i] = new JPanel[BOX_WIDTH.getValue()];
            for (int j = 0; j < boxes.length; j++) {
                JPanel newBox = new JPanel();
                boxes[i][j] = newBox;
                newBox.setLayout(SUDOKU_LAYOUT);
                newBox.setBorder(THICKER_BORDER);
                add(newBox);
            }
        }
    }

    private GameButton generateNewCell(int i, int j) {
        GameButton newCell = new GameButton(i, j);
        newCell.setBorder(DEFAULT_BORDER);
        newCell.addActionListener(e -> {
            if (selectedCell != null) {
                selectedCell.setBorder(DEFAULT_BORDER);
            }
            selectedCell = (GameButton) e.getSource();
            selectedCell.setBorder(THICKER_BORDER);

            painter.setSelectedCell(selectedCell.getRow(), selectedCell.getCol());
            painter.paintCells();
        });
        return newCell;
    }

    @Override
    public void setAllCells(int[][] newBoard) {
        solutionTester = new SolutionTester(newBoard);
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                int value = newBoard[i][j];
                undoCell(i, j);
                if (value != EMPTY_CELL.getValue()) {
                    setCell(i, j, value);
                }
            }
        }
    }

    @Override
    public synchronized void undoCell(int i, int j) {
        GameButton cell = cells[i][j];
        if (solutionTester != null && solutionTester.isSolution(i, j, cell.getValue()))
            return;
        cell.setModifiable();
        cell.undo();
    }

    public void undoCell() {
        undoCell(selectedCell.getRow(), selectedCell.getCol());
    }

    @Override
    public synchronized void setCell(int i, int j, int number) {
        GameButton cellToModify = cells[i][j];
        cellToModify.setValue(number);
        cellToModify.setForeground(Color.black);

        if (solutionTester != null && !solutionTester.isSolution(i, j, number)) {
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
