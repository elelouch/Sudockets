package game.ui;

import game.connection.SudokuClient;
import game.connection.SudokuServer;
import game.connection.UpdateSender;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class MainKeypad extends JPanel {
    private SudokuBoard board;
    private JButton startSession;
    private JButton connectToSession;
    private Main main;

    public MainKeypad(Main mainPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        startSession = new JButton("Start session");
        main = mainPanel;
        startSession.addActionListener(e -> {
            try {
                JButton sessionButton = (JButton) e.getSource();
                sessionButton.setText("Waiting for a connection");
                board.setUpdateSender(new SudokuServer(board));
                sessionButton.setText("Connection established, play!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        connectToSession = new JButton("Connect session");
        add(startSession);
        add(connectToSession);
        JTextArea textInput = new JTextArea("Insert IP session");
        textInput.setLineWrap(true);
        add(textInput);
        textInput.setBorder(LineBorder.createBlackLineBorder());
        connectToSession.addActionListener(e -> {
            try {
                board.setUpdateSender(new SudokuClient(textInput.getText(), board));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        JButton generatorButton = new JButton("Generate new game");
        generatorButton.addActionListener(e -> {
            main.generateSudokuBoard();
        });
        add(generatorButton);
    }


}
