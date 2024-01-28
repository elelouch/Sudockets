package game.ui.sudoku.panel;

import game.ui.sudoku.cell.SudokuCell;

import javax.swing.*;
import java.awt.*;

public class SudokuGameButton extends JButton implements SudokuCell {
    private final int row;
    private final int col;
    private int value;

    public SudokuGameButton(int newRow, int newCol) {
        row = newRow;
        col = newCol;
        setFont(new Font("monospaced", Font.BOLD, 30));
        setFocusPainted(true);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(Color.white);
    }

    @Override
    public boolean isEmpty() {
        return value == 0 && getText().isEmpty();
    }

    @Override
    public void setValue(int val) {
        if (isEmpty() && 1 <= val && val <= 9) {
            removeAll();
            value = val;
            setText(val + "");
        }
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void undo() {
        setText("");
        value = 0;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void addNote(int number) {
        if (!isEmpty())
            return;

        addNotesIfNotAdded();

        Component desiredLabel = getComponent(number - 1);
        JLabel label = (JLabel) desiredLabel;
        if (label.getText().isEmpty()) {
            label.setText(number + "");
        } else {
            label.setText("");
        }
    }

    private boolean hasNotes() {
        return getComponents().length > 0;
    }

    private void addNotesIfNotAdded() {
        if (hasNotes())
            return;

        setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            Font italicFont = new Font("monospaced", Font.ITALIC, 15);
            JLabel newLabel = new JLabel();
            newLabel.setFont(italicFont);
            add(newLabel);
        }
    }

}
