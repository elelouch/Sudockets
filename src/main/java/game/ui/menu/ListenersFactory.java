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
    protected void setAsServer() {
        connecter = new SudokuServer(gameUi);
    }
    protected void setAsClient(String address) {
        connecter = new SudokuClient(address,gameUi);
    }

    protected Connecter getConnecter() {
        return connecter;
    }

    protected void removeButtonListeners(JButton button) {
        for(ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
}
