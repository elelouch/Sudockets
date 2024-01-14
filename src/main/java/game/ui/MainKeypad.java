package game.ui;

import game.connection.ConnectionManager;

import javax.swing.*;

public class MainKeypad extends JPanel {
    private SudokuBoard board;
    private ConnectionManager connectionManager;
    private JButton startSession;
    private JButton connectToSession;

    public MainKeypad(SudokuBoard sudokuBoard) {
        board = sudokuBoard;
        connectionManager = new ConnectionManager(sudokuBoard);
        startSession = new JButton("Start session");
        connectToSession = new JButton("Connect session");
        startSession.addActionListener(e -> {
            connectionManager.startAsServer();
        });
        add(startSession);
        add(connectToSession);
    }


}
