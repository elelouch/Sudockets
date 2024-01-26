package game.ui;

import game.connection.UpdateSender;
import game.sudoku.SudokuSolver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import static game.SudokuSettings.*;

public class SudokuBoard extends JPanel {
    private static final int MAX_COLORED_CELLS = 21;
    private static final GridLayout SUDOKU_LAYOUT = new GridLayout(BOX_WIDTH.value, BOX_WIDTH.value);
    private static final int BORDER_PIXELS = 1;
    private static final Border DEFAULT_BORDER = LineBorder.createBlackLineBorder();
    private static final Border THICKER_BORDER = BorderFactory.createLineBorder(Color.black, BORDER_PIXELS + 2);

    private final ArrayDeque<SudokuCell> coloredCells;
    private SudokuCell selectedCell;
    private final JPanel[][] boxes;
    private final SudokuCell[][] cells;
    private int[][] boardSolution;
    private int[][] board;
    private UpdateSender updateSender;

    public SudokuBoard() {
        boxes = new JPanel[BOX_WIDTH.value][BOX_WIDTH.value];
        cells = new SudokuCell[BOARD_WIDTH.value][BOARD_WIDTH.value];
        coloredCells = new ArrayDeque<>(MAX_COLORED_CELLS);
        setBoxes();
        selectedCell = null;
        setLayout(SUDOKU_LAYOUT);
        setCells();
        setVisible(true);
    }

    public void setCells() {
        for (int i = 0; i < BOARD_WIDTH.value; i++) {
            cells[i] = new SudokuCell[BOARD_WIDTH.value];
            for (int j = 0; j < BOARD_WIDTH.value; j++) {
                SudokuCell newCell = generateNewCell(i, j);
                cells[i][j] = newCell;
                boxes[i / BOX_WIDTH.value][j / BOX_WIDTH.value].add(newCell);
            }
        }
    }

    public int[][] getBoardCopy() {
        int[][] boardCopy = new int[BOARD_WIDTH.value][BOARD_WIDTH.value];
        for (int i = 0; i < BOARD_WIDTH.value; i++) {
            boardCopy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return boardCopy;
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

    private void unColorCells() {
        while (!coloredCells.isEmpty()) {
            coloredCells.removeLast().setBackground(Color.white);
        }
    }

    private void colorBasedOnSelectedCell() {
        int i = selectedCell.getRow();
        int j = selectedCell.getCol();
        int boxi = i / BOX_WIDTH.value;
        int boxj = j / BOX_WIDTH.value;
        JPanel box = boxes[boxi][boxj];
        Component[] boxCells = box.getComponents();
        for (int k = 0; k < BOARD_WIDTH.value; k++) {
            colorAndAddCellToStack((SudokuCell) boxCells[k]);
            if (k / BOX_WIDTH.value != boxj) {
                colorAndAddCellToStack(cells[i][k]);
            }
            if (k / BOX_WIDTH.value != boxi) {
                colorAndAddCellToStack(cells[k][j]);
            }
        }
    }

    private void colorAndAddCellToStack(SudokuCell cell) {
        cell.setBackground(Color.lightGray);
        coloredCells.addLast(cell);
    }

    private SudokuCell generateNewCell(int i, int j) {
        SudokuCell newCell = new SudokuCell(i, j);
        newCell.setBorder(DEFAULT_BORDER);
        newCell.addActionListener(e -> {
            if (selectedCell != null) {
                selectedCell.setBorder(DEFAULT_BORDER);
            }
            selectedCell = (SudokuCell) e.getSource();
            selectedCell.setBorder(THICKER_BORDER);
            unColorCells();
            colorBasedOnSelectedCell();
        });
        return newCell;
    }

    public void startNewBoard(int[][] newBoard) throws UnsolvableSudokuException {
        List<int[][]> solutions = SudokuSolver.solveSudoku(newBoard);
        if (solutions.isEmpty()) {
            throw new UnsolvableSudokuException("Sudoku must be solvable to start a new board!");
        }
        board = newBoard;
        boardSolution = solutions.get(0);

        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard.length; j++) {
                int value = newBoard[i][j];
                undoCell(i, j);
                if (value != EMPTY_CELL.value) {
                    fillCell(i, j, value);
                }
            }
        }
    }

    public synchronized void undoCell(int i, int j) {
        try {
            SudokuCell cell = cells[i][j];
            if (cell.getValue() == boardSolution[i][j])
                return;
            cell.setModifiable();
            cell.undo();
            board[i][j] = EMPTY_CELL.value;
            if (updateSender != null) {
                updateSender.sendUndo(i, j);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void undoSelectedCell() {
        undoCell(selectedCell.getRow(), selectedCell.getCol());
    }

    public synchronized void fillCell(int i, int j, int number) {
        try {
            SudokuCell cellToModify = cells[i][j];
            cellToModify.setValue(number);
            cellToModify.setForeground(Color.black);
            board[i][j] = number;
            if (number != boardSolution[i][j]) {
                cellToModify.setForeground(Color.red);
            }

            if (updateSender != null) {
                updateSender.sendUpdate(i, j, number);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUpdateSender(UpdateSender sender) {
        updateSender = sender;
    }

    public void fillSelectedCell(int number) {
        fillCell(selectedCell.getRow(), selectedCell.getCol(), number);
    }

    public void addNoteSelectedCell(int number) {
        selectedCell.addNote(number);
    }
}
