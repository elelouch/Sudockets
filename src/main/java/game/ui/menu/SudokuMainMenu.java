package game.ui.menu;

import game.ui.sudoku.panel.SudokuGameUI;
import game.utils.SudokuGenerator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SudokuMainMenu extends JPanel {

    public SudokuMainMenu(SudokuGameUI newBoard) {
        JButton startSessionButton = new JButton("Start session");
        JButton connectToSessionButton = new JButton("Start connection");
        JTextField ipAddressInput = new JTextField("Insert session IP");
        ipAddressInput.setBorder(LineBorder.createBlackLineBorder());

        setLayout(new GridLayout(0, 1));

        SessionListeners sessionListeners = new SessionListeners(newBoard);
        startSessionButton.addActionListener(sessionListeners.createListener());

        ConnectionListeners connectionListeners = new ConnectionListeners(ipAddressInput, newBoard);
        connectToSessionButton.addActionListener(connectionListeners.createListener());

        JButton generateNewBoardButton = new JButton("Generate new sudoku");
        generateNewBoardButton.addActionListener(e -> {
            int[][] board = SudokuGenerator.generateUniqueSudoku();
            newBoard.setSolution(board);
            newBoard.setAllCells(board);
        });

        add(generateNewBoardButton);
        add(ipAddressInput);
        add(connectToSessionButton);
        add(startSessionButton);
    }

}
