package game.ui.menu;

import game.connection.SudokuServer;
import game.ui.sudoku.panel.GameUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SessionListeners implements ListenersFactory{
    private ActionListener closer;
    private ActionListener opener;

    SessionListeners(GameUI gameUI) {
        opener = e -> {
            JButton button = (JButton) e.getSource();
            button.setText("Waiting for connection");
            SudokuServer.initializeServer(gameUI);
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
