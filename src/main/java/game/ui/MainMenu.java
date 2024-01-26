package game.ui;

import game.connection.Connecter;
import game.connection.SudokuClient;
import game.connection.SudokuServer;
import game.ui.sudoku.SudokuPanel;
import game.ui.sudoku.UnsolvableSudokuException;
import game.utils.SudokuGenerator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class MainMenu extends JPanel {
    private SudokuPanel board;
    private JButton startSessionButton;
    private JButton connectToSessionButton;
    private Connecter connecter;

    public MainMenu(SudokuPanel newBoard) {
        Pattern ipPattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");
        setLayout(new GridLayout(0, 1));
        board = newBoard;
        JTextArea ipAddressInput = new JTextArea("Insert IP session");

        ActionListener startSessionListener = e -> {
            JButton sessionButton = (JButton) e.getSource();
            sessionButton.setText("Waiting for a connection!");
            connecter = new SudokuServer(board);
            connecter.startConnection();
            sessionButton.setText("Connection established, play!");

            for (ActionListener listener : connectToSessionButton.getActionListeners()) {
                connectToSessionButton.removeActionListener(listener);
            }

        };

        ActionListener stopSessionListener = e -> {
            connecter.endConnection();
            startSessionButton.addActionListener(startSessionListener);
        };


        ActionListener connectToSessionListener = e -> {
            String clientInput = ipAddressInput.getText();
            if (ipPattern.matcher(clientInput).matches()) {
                connecter = new SudokuClient(clientInput, board);
                connectToSessionButton.setText("Connection established, play!");
                connecter.startConnection();
            } else {
                connectToSessionButton.setText("IP must be valid, try again with a valid IP");
            }

            removeListenersButton(startSessionButton);
        };

        ActionListener stopServerListener = e -> {
            connecter.endConnection();
            removeListenersButton(connectToSessionButton);
            connectToSessionButton.addActionListener(connectToSessionListener);
        };

        startSessionButton = new JButton("Start session");
        startSessionButton.addActionListener(startSessionListener);


        connectToSessionButton = new JButton("Connect session");
        connectToSessionButton.addActionListener(connectToSessionListener);

        JButton generateNewBoardButton = new JButton("Generate sudoku");
        generateNewBoardButton.addActionListener(e -> {
            try {
                board.startNewBoard(SudokuGenerator.generateUniqueSudoku());
            } catch (UnsolvableSudokuException notSolvableExc) {
                notSolvableExc.printStackTrace();
            }
        });

        ipAddressInput.setLineWrap(true);
        ipAddressInput.setBorder(LineBorder.createBlackLineBorder());
        add(generateNewBoardButton);
        add(ipAddressInput);
        add(connectToSessionButton);
        add(startSessionButton);
    }

    public static void removeListenersButton(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
}