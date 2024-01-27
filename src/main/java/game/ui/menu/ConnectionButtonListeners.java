package game.ui.menu;

import game.ui.sudoku.panel.GameUI;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ConnectionButtonListeners extends ListenersFactory {
    private static final Pattern ipPattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");
    private ActionListener closer;
    private ActionListener opener;

    ConnectionButtonListeners(JTextField userInput, GameUI gameUI) {
        super(gameUI);
        opener = e ->{
            String address = userInput.getText();
            JButton button = (JButton) e.getSource();
            if(ipPattern.matcher(address).matches()) {
                startConnection(address);
                button.setText("Connection created!, click again to stop connection");
                removeButtonListeners(button);
                button.addActionListener(closer);
                return;
            }
            button.setText("Address must be valid");
        };

        closer = e -> {
            JButton button = (JButton) e.getSource();
            endConnection();
            button.setText("Connection finished, click again to start a connection");
            removeButtonListeners(button);
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
