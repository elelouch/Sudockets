package game.ui;

import game.connection.ConnectionManager;
import game.connection.SudokuServer;

import javax.swing.*;
import java.awt.*;

public class MainKeypad extends JPanel {
    private SudokuBoard board;
    private JButton startSession;
    private JButton connectToSession;

    public MainKeypad(SudokuBoard sudokuBoard) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        board = sudokuBoard;
        startSession = new JButton("Start session");
        startSession.addActionListener(e -> new SudokuServer());
        connectToSession = new JButton("Connect session");
        add(startSession);
        add(connectToSession);
        JTextArea textInput = new JTextArea("Inserte aca la ip");
        add(textInput);
        connectToSession.addActionListener(e -> {
            connectionManager.startAsClient(textInput.getText());
        });
    }


}
