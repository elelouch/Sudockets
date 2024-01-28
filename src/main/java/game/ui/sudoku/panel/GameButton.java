package game.ui.sudoku.panel;

import game.ui.sudoku.cell.SudokuCell;

import javax.swing.*;
import java.awt.*;

public class GameButton extends JButton implements SudokuCell {
    private final int row;
    private final int col;
    private int value;
    private boolean modifiable;

    public GameButton(int newRow, int newCol) {
        row = newRow;
        col = newCol;
        modifiable = true;
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
        if (1 <= val && val <= 9 && modifiable) {
            removeAll();
            setUnmodifiable();
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
        if (!modifiable)
            return;

        setText("");
        value = 0;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setUnmodifiable() {
        modifiable = false;
    }

    public void setModifiable() {
        modifiable = true;
    }


    public void addNote(int number) {
        if (!modifiable)
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
