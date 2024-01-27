package game.ui.menu;

import game.connection.Connecter;
import game.ui.sudoku.panel.GameUI;
import game.utils.SudokuGenerator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    public MainMenu(GameUI newBoard) {
        JButton startSessionButton = new JButton("Start session");
        JButton connectToSessionButton = new JButton("Connect to session");
        JTextField ipAddressInput = new JTextField("Insert IP session");
        setLayout(new GridLayout(0, 1));
        ipAddressInput.setBorder(LineBorder.createBlackLineBorder());
        SessionButtonListeners sessionListeners = new SessionButtonListeners(newBoard);
        startSessionButton.addActionListener(sessionListeners.createOpenListener());
        startSessionButton.addActionListener(sessionListeners.createCloseListener());
        ConnectionButtonListeners connectionListeners =
                new ConnectionButtonListeners(ipAddressInput, newBoard);
        connectToSessionButton.addActionListener(connectionListeners.createOpenListener());
        connectToSessionButton.addActionListener(connectionListeners.createCloseListener());

        JButton generateNewBoardButton = new JButton("Generate new sudoku");
        generateNewBoardButton.addActionListener(e -> newBoard.setAllCells(SudokuGenerator.generateUniqueSudoku()));

        add(generateNewBoardButton);
        add(ipAddressInput);
        add(connectToSessionButton);
        add(startSessionButton);
    }
}