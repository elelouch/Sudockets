package game.ui;

import game.connection.ConnectionManager;
import sudoku.SudokuGenerator;
import sudoku.SudokuSolver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;

public class SudokuBoard extends JPanel {
    private static final int SIZE = 9;
    private static final int MAX_COLORED_CELLS = 21;
    private static final int BOX_SIDE = 3;
    private static final int FIRST = 0;
    private static final int MAX_TRIES = 3;
    private static final int EMPTY = 0;
    private static final int BORDER_PIXELS = 1;
    private static final Border DEFAULT_BORDER = LineBorder.createBlackLineBorder();
    private static final Border THICKER_BORDER = BorderFactory.createLineBorder(Color.black, BORDER_PIXELS + 2);
    private static final String nullCellMessage = "Must select a cell before placing a number";

    private final ArrayDeque<SudokuCell> coloredCells;
    private SudokuCell selectedCell;
    private final JPanel[][] boxes;
    private final SudokuCell[][] cells;
    private int[][] boardSolution;
    private int[][] board;
    private int tries;
    private int settedCells;
    private ConnectionManager connectionManager;

    public SudokuBoard() {
        tries = MAX_TRIES;
        boxes = new JPanel[BOX_SIDE][BOX_SIDE];
        cells = new SudokuCell[SIZE][SIZE];
        coloredCells = new ArrayDeque<>(MAX_COLORED_CELLS);

        setLayout(generateGridSquaredLayout(BOX_SIDE));
        setBoxes();
        startNewBoard(SudokuGenerator.generateUniqueSudoku());
        setVisible(true);
    }

    private static GridLayout generateGridSquaredLayout(int side) {
        return new GridLayout(side, side);
    }

    public int[][] getBoardCopy() {
        int[][] boardCopy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            boardCopy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return boardCopy;
    }
    private void setBoxes() {
        for (int i = 0; i < BOX_SIDE; i++) {
            boxes[i] = new JPanel[BOX_SIDE];
            for (int j = 0; j < BOX_SIDE; j++) {
                boxes[i][j] = new JPanel();
                boxes[i][j].setLayout(generateGridSquaredLayout(BOX_SIDE));
                boxes[i][j].setBorder(THICKER_BORDER);
                add(boxes[i][j]);
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
        int boxi = i / BOX_SIDE;
        int boxj = j / BOX_SIDE;
        JPanel box = boxes[boxi][boxj];
        Component[] boxCellsToBeSelected = box.getComponents();
        for (int k = 0; k < SIZE; k++) {
            colorAndAddCellToStack((SudokuCell) boxCellsToBeSelected[k]);
            if (k / BOX_SIDE != boxj) {
                colorAndAddCellToStack(cells[i][k]);
            }
            if (k / BOX_SIDE != boxi) {
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
        newCell.setOpaque(true);
        newCell.setBackground(Color.white);
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

    public void startNewBoard(int[][] newBoard) {
        board = newBoard;
        boardSolution = SudokuSolver.solveSudoku(newBoard).get(FIRST);
        selectedCell = null;
        settedCells = 0;
        for (int i = 0; i < SIZE; i++) {
            cells[i] = new SudokuCell[SIZE];
            for (int j = 0; j < SIZE; j++) {
                SudokuCell newCell = generateNewCell(i, j);
                cells[i][j] = newCell;
                boxes[i / BOX_SIDE][j / BOX_SIDE].add(newCell);
                if (board[i][j] != EMPTY) {
                    newCell.setText(board[i][j] + "");
                    newCell.setUnmodifiable();
                }
            }
        }
    }

    public void undoSelectedCell() {
        if (selectedCell.isModifiable())
            selectedCell.setText("");
    }

    public synchronized void fillCell(int i, int j, int number) throws NullPointerException {
        SudokuCell cellToModify = cells[i][j];
        if (validNumber(number) && cellToModify.isModifiable()) {
            if (number == boardSolution[i][j]) {
                cellToModify.setUnmodifiable();
            } else {
                tries--;
            }
            cellToModify.removeAll();
            cellToModify.setText(String.valueOf(number));
            board[i][j] = number;
            if (connectionManager != null) {
                connectionManager.sendUpdate(i,j,number);
            }
        }
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void fillSelectedCell(int number) throws NullPointerException {
        throwIfCellNull();
        fillCell(selectedCell.getRow(), selectedCell.getCol(), number);
    }

    private static boolean validNumber(int number) {
        return number >= 1 && number <= 9;
    }

    private boolean selectedCellHasNotes() {
        return selectedCell.getComponents().length > 0;
    }

    private void throwIfCellNull() throws NullPointerException {
        if (selectedCell == null) {
            throw new NullPointerException(nullCellMessage);
        }
    }

    public void addNoteSelectedCell(int number) throws NullPointerException {
        throwIfCellNull();
        if (!(validNumber(number) && selectedCell.isModifiable()))
            return;
        addNotesIfNotAdded();

        Component desiredLabel = selectedCell.getComponents()[number - 1];
        JLabel label = (JLabel) desiredLabel;
        if (label.getText().isEmpty()) {
            label.setText(number + "");
        } else {
            label.setText("");
        }
    }

    private void addNotesIfNotAdded() {
        if (selectedCellHasNotes())
            return;

        selectedCell.setLayout(generateGridSquaredLayout(BOX_SIDE));
        for (int i = 0; i < SIZE; i++) {
            Font italicFont = new Font("monospaced", Font.ITALIC, 15);
            JLabel newLabel = new JLabel();
            newLabel.setFont(italicFont);
            selectedCell.add(newLabel);
        }
    }
}