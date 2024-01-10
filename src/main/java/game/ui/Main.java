package game.ui;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    Main() {
        Container pane = getContentPane();
        SudokuBoard game = new SudokuBoard();
        pane.add(game);
        pane.add(new SudokuKeypad(game), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);
    }
    public static void main(String[] args) {
        Main mainUI = new Main();
    }
}
