package game.ui;

import game.ui.menu.MainMenu;
import game.ui.sudoku.panel.GameUI;
import game.utils.SudokuGenerator;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    GameUI board;
    SudokuKeypad mainKeypad;
    MainMenu mainMenu;

    Main() {
        Container pane = getContentPane();
        board = new GameUI(SudokuGenerator.generateUniqueSudoku());
        mainKeypad = new SudokuKeypad(board);
        mainMenu = new MainMenu(board);

        pane.add(mainKeypad, BorderLayout.SOUTH);
        pane.add(mainMenu, BorderLayout.EAST);
        pane.add(board, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);
    }

    public static void main(String[] args) {
        Main mainUI = new Main();
    }

}