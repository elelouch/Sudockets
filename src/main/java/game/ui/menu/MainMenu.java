package game.ui.menu;

import game.ui.sudoku.panel.GameUI;
import game.utils.SudokuGenerator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainMenu extends JPanel {

    public MainMenu(GameUI newBoard) {
        JButton startSessionButton = new JButton("Start session");
        JButton connectToSessionButton = new JButton("Start connection");
        JTextField ipAddressInput = new JTextField("Insert session IP");
        ipAddressInput.setBorder(LineBorder.createBlackLineBorder());

        setLayout(new GridLayout(0, 1));

        SessionListeners sessionListeners = new SessionListeners(newBoard);
        startSessionButton.addActionListener(sessionListeners.createOpenListener());
        startSessionButton.addActionListener(sessionListeners.createCloseListener());

        ConnectionListeners connectionListeners =
                new ConnectionListeners(ipAddressInput, newBoard);
        connectToSessionButton.addActionListener(connectionListeners.createOpenListener());
        connectToSessionButton.addActionListener(connectionListeners.createCloseListener());

        connectToSessionButton.addActionListener(e -> ListenersFactory.removeButtonListeners(startSessionButton));
        startSessionButton.addActionListener(e -> ListenersFactory.removeButtonListeners(connectToSessionButton));

        JButton generateNewBoardButton = new JButton("Generate new sudoku");
        generateNewBoardButton.addActionListener(e -> newBoard.setAllCells(SudokuGenerator.generateUniqueSudoku()));

        add(generateNewBoardButton);
        add(ipAddressInput);
        add(connectToSessionButton);
        add(startSessionButton);
    }


}