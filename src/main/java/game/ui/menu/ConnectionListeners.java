package game.ui.menu;

import game.connection.SudokuClient;
import game.ui.sudoku.panel.GameUI;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ConnectionListeners implements ListenersFactory {
    private static final Pattern ipPattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");
    private ActionListener closer;
    private ActionListener opener;

    ConnectionListeners(JTextField userInput, GameUI gameUI) {
        opener = e ->{
            String address = userInput.getText();
            JButton button = (JButton) e.getSource();
            if(ipPattern.matcher(address).matches()) {
                SudokuClient.initializeClient(gameUI,address);
                button.setText("Stop connection");
                ListenersFactory.removeButtonListeners(button);
                button.addActionListener(closer);
                return;
            }
            button.setText("Address must be valid");
        };

        closer = e -> {
            JButton button = (JButton) e.getSource();
            SudokuClient.finalizeClient();
            button.setText("Start connection");
            ListenersFactory.removeButtonListeners(button);
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
