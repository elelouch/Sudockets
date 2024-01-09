package game.ui;

import sudoku.SudokuSolver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SudokuBoard extends JPanel {
    private static final int SIZE = 9;
    private static final int MAX_COLORED_CELLS = 21;
    private static final int THIRD = 3;
    private static final int FIRST = 0;
    private static final int MAX_TRIES = 3;
    private static final int EMPTY = 0;
    private static final int BORDER_PIXELS = 1;
    private static final int BUTTONS_GAP = 10;
    private static final String nullCellMessage = "Must select a cell before placing a number";

    SudokuButton[] borderedCells;
    SudokuButton selectedCell;
    JPanel[][] boxes;
    SudokuButton[][] cellsButtons;
    int[][] solution;
    Border border;
    int tries;
    int settedCells;

    SudokuBoard(int[][] sudoku) {
        selectedCell = null;
        settedCells = 0;
        solution = SudokuSolver.solveSudoku(sudoku).get(FIRST);
        tries = MAX_TRIES;
        boxes = new JPanel[THIRD][THIRD];
        cellsButtons = new SudokuButton[SIZE][SIZE];
        borderedCells = new SudokuButton[MAX_COLORED_CELLS];
        border = LineBorder.createBlackLineBorder();

        setLayout(generateSquaredGappedLayout(0, 3));
        fillAndAddBoxes();
        fillCellsButtons(sudoku);
        setVisible(true);
    }

    private static GridLayout generateSquaredGappedLayout(int gap, int side) {
        GridLayout gapFixedLayout = new GridLayout(side, side);
        gapFixedLayout.setHgap(gap);
        gapFixedLayout.setVgap(gap);
        return gapFixedLayout;
    }

    private void fillAndAddBoxes() {
        for (int i = 0; i < THIRD; i++) {
            boxes[i] = new JPanel[THIRD];
            for (int j = 0; j < THIRD; j++) {
                boxes[i][j] = new JPanel();
                boxes[i][j].setLayout(generateSquaredGappedLayout(0, 3));
                boxes[i][j].setBorder(border);
                add(boxes[i][j]);
            }
        }
    }

    private void unsetHelperBorders() {
        for (int i = 0; i < borderedCells.length ; i++) {
            if(borderedCells[i] != null) {
                borderedCells[i].setBorder(border);
            }
        }
    }

    private void setHelperBorders() {
        int i = selectedCell.getRow();
        int j = selectedCell.getCol();
        int boxi = i / THIRD;
        int boxj = j / THIRD;
        JPanel box = boxes[boxi][boxj];
        Component[] cells = box.getComponents();
        int count = 0;

        for (int k = 0; k < cells.length; k++) {
            SudokuButton cell = (SudokuButton) cells[k];
            borderedCells[count++] = cell;
        }

        for (int k = 0; k < SIZE ; k++) {
            SudokuButton aux;
            int lineNumber = k / THIRD;
            if(lineNumber != boxi) {
                aux = cellsButtons[i][k];
                borderedCells[count++] = aux;
            }

            if(lineNumber != boxj) {
                aux = cellsButtons[k][j];
                borderedCells[count++] = aux;
            }
        }
    }

    private void fillCellsButtons(int[][] sudokuBoard) {
        for (int i = 0; i < SIZE; i++) {
            cellsButtons[i] = new SudokuButton[SIZE];
            for (int j = 0; j < SIZE; j++) {
                SudokuButton newButton = new SudokuButton(i, j);
                newButton.setOpaque(true);
                newButton.setBackground(Color.white);
                newButton.setBorder(border);
                cellsButtons[i][j] = newButton;
                boxes[i / THIRD][j / THIRD].add(newButton);

                newButton.addActionListener(e -> {
                    selectedCell = (SudokuButton) e.getSource();
                    unsetHelperBorders();
                    setHelperBorders();
                });

                if (sudokuBoard[i][j] != EMPTY) {
                    newButton.setText(sudokuBoard[i][j] + "");
                    newButton.setUnmodifiable();
                }
            }
        }
    }

    public boolean lost() {
        return tries <= 0;
    }

    public void undoSelectedCell() {
        if (selectedCell.isModifiable())
            selectedCell.setText("");
    }

    public void fillSelectedCell(int number) throws NullPointerException {
        throwIfCellNull();

        if (validNumber(number) && selectedCell.isModifiable()) {
            int i = selectedCell.getRow();
            int j = selectedCell.getCol();

            if(number == solution[i][j]) {
                selectedCell.setUnmodifiable();
            } else {
                tries--;
            }

            selectedCell.removeAll();
            selectedCell.setText(String.valueOf(number));
        }
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

        if (!(validNumber(number) && selectedCell.isModifiable())) {
            return;
        }

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
        if (!selectedCellHasNotes()) {
            selectedCell.setLayout(generateSquaredGappedLayout(0, 3));
            for (int i = 0; i < SIZE; i++) {
                Font italicFont = new Font("monospaced", Font.ITALIC, 15);
                JLabel newLabel = new JLabel();
                newLabel.setFont(italicFont);
                selectedCell.add(newLabel);
            }
        }
    }
}
