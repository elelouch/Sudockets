package game.ui.sudoku.panel;

import game.ui.painter.SudokuCellsPainter;
import game.ui.sudoku.exceptions.UnsolvableSudokuException;
import game.ui.sudoku.game.SudokuGame;
import game.ui.sudoku.game.SudokuGameObserver;
import game.utils.SudokuSolver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.SudokuSettings.*;

public class SudokuGameUI extends JPanel implements SudokuGame, SudokuGameObserver {
    private static final GridLayout SUDOKU_LAYOUT = new GridLayout(BOX_WIDTH.getValue(),
            BOX_WIDTH.getValue());
    private static final int BORDER_PIXELS = 1;
    private static final Border DEFAULT_BORDER = LineBorder.createBlackLineBorder();
    private static final Border THICKER_BORDER = BorderFactory.createLineBorder(Color.black,
            BORDER_PIXELS + 2);

    private final List<SudokuGame> spectators;
    private final JPanel[][] boxes;
    private final SudokuGameButton[][] sudokuGameButtons;
    private final SudokuCellsPainter painter;
    private SudokuGameButton selectedCell;
    private int[][] solution;

    public SudokuGameUI(int[][] newBoard) {
        spectators = new ArrayList<>();
        boxes = new JPanel[BOX_WIDTH.getValue()][BOX_WIDTH.getValue()];
        sudokuGameButtons = new SudokuGameButton[BOARD_WIDTH.getValue()][BOARD_WIDTH.getValue()];
        painter = new SudokuCellsPainter(sudokuGameButtons);
        setBoxes();
        setLayout(SUDOKU_LAYOUT);
        setCells();
        setSolution(newBoard);
        setAllCells(newBoard);
        setVisible(true);
    }

    private void setCells() {
        for (int i = 0; i < BOARD_WIDTH.getValue(); i++) {
            sudokuGameButtons[i] = new SudokuGameButton[BOARD_WIDTH.getValue()];
            for (int j = 0; j < BOARD_WIDTH.getValue(); j++) {
                SudokuGameButton newCell = generateNewButton(i, j);
                sudokuGameButtons[i][j] = newCell;
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

    private SudokuGameButton generateNewButton(int i, int j) {
        SudokuGameButton newButton = new SudokuGameButton(i, j);
        newButton.setBorder(DEFAULT_BORDER);
        newButton.addActionListener(e -> {
            if (selectedCell != null) {
                selectedCell.setBorder(DEFAULT_BORDER);
            }
            selectedCell = (SudokuGameButton) e.getSource();
            selectedCell.setBorder(THICKER_BORDER);

            painter.setSelectedCell(selectedCell.getRow(), selectedCell.getCol());
            painter.paintCells();
        });
        return newButton;
    }

    @Override
    public synchronized void setAllCells(int[][] newBoard) {
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                undoCell(i,j);
                setCell(i,j, newBoard[i][j]);
            }
        }
    }


    @Override
    public synchronized void setSolution(int[][] board) {
        List<int[][]> possibleSolutions = SudokuSolver.solveSudoku(board);
        if (possibleSolutions.isEmpty()) {
            throw new UnsolvableSudokuException("Sudoku must be solvable to set a solution");
        }
        solution = possibleSolutions.get(0);
    }

    @Override
    public synchronized void undoCell(int i, int j) {
        SudokuGameButton button = sudokuGameButtons[i][j];
        int valueSolution = solution[i][j];
        if (button.isEmpty() || button.getValue() == valueSolution) {
            return;
        }
        button.undo();

        for (SudokuGame spectator : spectators)
            spectator.undoCell(i, j);
    }

    public void undoCell() {
        undoCell(selectedCell.getRow(), selectedCell.getCol());
    }

    @Override
    public synchronized void setCell(int i, int j, int number) {
        SudokuGameButton buttonToModify = sudokuGameButtons[i][j];
        int solutionValue = solution[i][j];
        if (buttonToModify.getValue() == number) {
            return;
        }

        buttonToModify.setValue(number);
        buttonToModify.setForeground(Color.black);

        if (solutionValue != buttonToModify.getValue()) {
            buttonToModify.setForeground(Color.red);
        }

        for (SudokuGame spectator : spectators)
            spectator.setCell(i, j, number);
    }

    public void fillCell(int number) {
        setCell(selectedCell.getRow(), selectedCell.getCol(), number);
    }

    public void addNote(int number) {
        selectedCell.addNote(number);
    }

    private int[][] getBoardCopy() {
        int size = BOARD_WIDTH.getValue();
        int[][] boardCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            boardCopy[i] = new int[size];
            for (int j = 0; j < size; j++) {
                boardCopy[i][j] = sudokuGameButtons[i][j].getValue();
            }
        }
        return boardCopy;
    }

    @Override
    public void subscribe(SudokuGame game) {
        spectators.add(game);
    }

    @Override
    public void unSubscribe(SudokuGame game) {
        spectators.remove(game);
    }

    @Override
    public void notifyFullUpdate() {
        for (SudokuGame spectator : spectators) {
            spectator.setAllCells(getBoardCopy());
        }
    }

    @Override
    public void notifySolution() {
        for (SudokuGame spectator : spectators) {
            spectator.setSolution(solution);
        }
    }

}