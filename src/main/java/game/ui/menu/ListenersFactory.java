package game.ui.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface ListenersFactory {

    ActionListener createCloseListener();

    ActionListener createOpenListener();

    static void removeButtonListeners(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
}