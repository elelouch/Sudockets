package game.ui;

import game.ui.menu.SudokuMainMenu;
import game.ui.sudoku.panel.SudokuGameKeypad;
import game.ui.sudoku.panel.SudokuGameUI;
import game.utils.SudokuGenerator;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    SudokuGameUI board;
    SudokuGameKeypad mainKeypad;
    SudokuMainMenu sudokuMainMenu;

    MainUI() {
        Container pane = getContentPane();
        board = new SudokuGameUI(SudokuGenerator.generateUniqueSudoku());
        System.out.println("Game successfully started");
        mainKeypad = new SudokuGameKeypad(board);
        sudokuMainMenu = new SudokuMainMenu(board);

        pane.add(mainKeypad, BorderLayout.SOUTH);
        pane.add(sudokuMainMenu, BorderLayout.EAST);
        pane.add(board, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainUI mainUI = new MainUI();
    }

}