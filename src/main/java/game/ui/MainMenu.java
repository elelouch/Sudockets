package game.ui;

import game.connection.SudokuClient;
import game.connection.SudokuServer;
import game.sudoku.SudokuGenerator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends JPanel {
    private SudokuBoard board;
    private JButton startSession;
    private JButton connectToSession;

    public MainMenu(SudokuBoard newBoard) {
        Pattern ipPattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");
        setLayout(new GridLayout(0, 1));
        board = newBoard;

        startSession = new JButton("Start session");
        startSession.addActionListener(e -> {
            try {
                JButton sessionButton = (JButton) e.getSource();
                sessionButton.setText("Waiting for a connection!");
                board.setUpdateSender(new SudokuServer(board));
                sessionButton.setText("Connection established, play!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JTextArea ipAddressInput = new JTextArea("Insert IP session");
        ipAddressInput.setLineWrap(true);
        ipAddressInput.setBorder(LineBorder.createBlackLineBorder());

        connectToSession = new JButton("Connect session");
        connectToSession.addActionListener(e -> {
            try {
                String clientInput = ipAddressInput.getText();
                if (ipPattern.matcher(clientInput).matches()) {
                    board.setUpdateSender(new SudokuClient(clientInput, board));
                    connectToSession.setText("Connection established, play!");
                } else {
                    connectToSession.setText("IP must be valid, try again with a valid IP");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        JButton generateNewBoardButton = new JButton("Generate sudoku");
        generateNewBoardButton.addActionListener(e -> {
            newBoard.startNewBoard(SudokuGenerator.generateUniqueSudoku());
        });

        add(generateNewBoardButton);
        add(ipAddressInput);
        add(connectToSession);
        add(startSession);
    }
}
