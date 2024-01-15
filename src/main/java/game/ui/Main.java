package game.ui;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    SudokuBoard board;
    SudokuKeypad mainKeypad;
    MainKeypad mainMenu;

    public void generateSudokuBoard() {
        board = new SudokuBoard();
        getContentPane().add(board, BorderLayout.CENTER);
    }



    Main() {
        Container pane = getContentPane();
        mainKeypad = new SudokuKeypad();
        mainMenu = new MainKeypad(this);

        pane.add(mainKeypad, BorderLayout.SOUTH);
        pane.add(mainMenu, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);
    }
    public static void main(String[] args) {
        Main mainUI = new Main();
    }

}