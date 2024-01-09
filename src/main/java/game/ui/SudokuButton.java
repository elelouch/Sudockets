package game.ui;

import javax.swing.*;
import java.awt.*;

public class SudokuButton extends JButton {
    private int row;
    private int col;
    private boolean modifiable;
    public SudokuButton(int newRow, int newCol) {
        row = newRow;
        col = newCol;
        modifiable = true;

        Font buttonFont = new Font("monospaced", Font.BOLD, 30);
        setFont(buttonFont);
        setBackground(Color.black);
        setFocusPainted(true);
        setContentAreaFilled(false);
    }

    public void setUnmodifiable() {
        modifiable = false;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
