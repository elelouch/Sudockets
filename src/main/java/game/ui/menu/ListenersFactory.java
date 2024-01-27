package game.ui.menu;

import game.connection.Connecter;
import game.connection.SudokuClient;
import game.connection.SudokuServer;
import game.ui.sudoku.panel.GameUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class ListenersFactory {
    Connecter connecter;
    GameUI gameUi;

    public ListenersFactory(GameUI ui) {
        gameUi = ui;
    }

    abstract ActionListener createCloseListener();

    abstract ActionListener createOpenListener();

    protected void startConnection() {
        connecter = new SudokuServer(gameUi);
        connecter.startConnection();
    }

    protected void startConnection(String address) {
        connecter = new SudokuClient(address, gameUi);
        connecter.startConnection();
    }

    public void endConnection() {
        if (connecter != null) {
            connecter.endConnection();
        }
    }

    protected void removeButtonListeners(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
}