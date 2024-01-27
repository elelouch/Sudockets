package game.ui.menu;

import game.ui.sudoku.panel.GameUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SessionButtonListeners extends ListenersFactory{
    private ActionListener closer;
    private ActionListener opener;

    SessionButtonListeners(GameUI gameUI) {
        super(gameUI);
        opener = e -> {
            JButton button = (JButton) e.getSource();
            button.setText("Waiting for connection");
            startConnection();
            button.setText("Connection ready, click again to stop connection");
            removeButtonListeners(button);
            button.addActionListener(closer);
        };

        closer = e -> {
            endConnection();
            JButton button = (JButton) e.getSource();
            removeButtonListeners(button);
            button.setText("Start server");
            button.addActionListener(opener);
        };
    }

    @Override
    ActionListener createCloseListener() {
        return closer;
    }

    @Override
    ActionListener createOpenListener() {
        return opener;
    }

}
