package game.ui.menu;

import game.connection.Connecter;
import game.ui.sudoku.panel.GameUI;
import game.utils.SudokuGenerator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private GameUI board;
    private JButton startSessionButton;
    private JButton connectToSessionButton;
    private Connecter connecter;

    public MainMenu(GameUI newBoard) {
        setLayout(new GridLayout(0, 1));
        board = newBoard;
        JTextArea ipAddressInput = new JTextArea("Insert IP session");
        ipAddressInput.setLineWrap(true);
        ipAddressInput.setBorder(LineBorder.createBlackLineBorder());
        startSessionButton = new JButton("Start session");
        SessionButtonListeners sessionListeners = new SessionButtonListeners(board);
        startSessionButton.addActionListener(sessionListeners.createOpenListener());
        startSessionButton.addActionListener(sessionListeners.createCloseListener());
        // CUIDADO QUE ESTO PUEDE NO FUNCIONAR
        ConnectionButtonListeners connectionListeners =
                new ConnectionButtonListeners(ipAddressInput, board);
        connectToSessionButton = new JButton("Connect session");
        connectToSessionButton.addActionListener(connectionListeners.createOpenListener());
        connectToSessionButton.addActionListener(connectionListeners.createCloseListener());

        JButton generateNewBoardButton = new JButton("Generate new sudoku");
        generateNewBoardButton.addActionListener(e -> {
            board.setAllCells(SudokuGenerator.generateUniqueSudoku());
        });

        add(generateNewBoardButton);
        add(ipAddressInput);
        add(connectToSessionButton);
        add(startSessionButton);
    }
}