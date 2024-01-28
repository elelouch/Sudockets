package game.ui.menu;

import game.connection.server.SudokuServer;
import game.ui.sudoku.panel.SudokuGameUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SessionListeners implements ListenersFactory{
    private ActionListener closer;
    private ActionListener opener;

    SessionListeners(SudokuGameUI sudokuGameUI) {
        opener = e -> {
            JButton button = (JButton) e.getSource();
            button.setText("Waiting for connection");
            SudokuServer.initializeServer(sudokuGameUI);
            button.setText("Close server!");
            ListenersFactory.removeButtonListeners(button);
            button.addActionListener(closer);
        };

        closer = e -> {
            JButton button = (JButton) e.getSource();
            SudokuServer.finalizeServer();
            ListenersFactory.removeButtonListeners(button);
            button.setText("Start server");
            button.addActionListener(opener);
        };
    }

    @Override
    public ActionListener createCloseListener() {
        return closer;
    }

    @Override
    public ActionListener createOpenListener() {
        return opener;
    }

}
