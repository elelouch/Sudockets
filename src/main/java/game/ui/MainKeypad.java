package game.ui;

import game.connection.SudokuClient;
import game.connection.SudokuServer;
import game.connection.UpdateSender;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainKeypad extends JPanel {
    private SudokuBoard board;
    private JButton startSession;
    private JButton connectToSession;

    public MainKeypad(SudokuBoard sudokuBoard) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        board = sudokuBoard;
        startSession = new JButton("Start session");
        startSession.addActionListener(e -> {
            try{
                board.setUpdateSender(new SudokuServer(board));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        connectToSession = new JButton("Connect session");
        add(startSession);
        add(connectToSession);
        JTextArea textInput = new JTextArea("Inserte aca la ip");
        add(textInput);
        connectToSession.addActionListener(e -> {
            try {
                board.setUpdateSender(new SudokuClient(textInput.getText(), board));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }


}
