package game.ui;

import javax.swing.*;
import java.awt.*;

public class SudokuCell extends JButton {
    private static final int EMPTY = 0;

    private int row;
    private int col;
    private int value;
    private boolean modifiable;

    public SudokuCell(int newRow, int newCol) {
        row = newRow;
        col = newCol;
        modifiable = true;
        setFont(new Font("monospaced", Font.BOLD, 30));
        setFocusPainted(true);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(Color.white);
    }

    public void setUnmodifiable() {
        modifiable = false;
    }

    public void setModifiable() {
        modifiable = true;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void undo() {
        if(!modifiable)
            return;

        setText("");
        value = 0;
    }

    public void addNote(int number) throws NullPointerException {
        if (!modifiable)
            return;

        addNotesIfNotAdded();

        Component desiredLabel = getComponent(number-1);
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

        setLayout(new GridLayout(3,3));
        for (int i = 0; i < 9; i++) {
            Font italicFont = new Font("monospaced", Font.ITALIC, 15);
            JLabel newLabel = new JLabel();
            newLabel.setFont(italicFont);
            add(newLabel);
        }
    }

    public void setValue(int val) {
        if(1 <= val && val <= 9 && modifiable) {
            removeAll();
            setUnmodifiable();
            value = val;
            setText(val + "");
        }
    }
    public int getValue() {
        return value;
    }
}
