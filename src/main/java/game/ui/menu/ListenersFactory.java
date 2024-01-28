package game.ui.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface ListenersFactory {

    ActionListener createListener();

    static void removeButtonListeners(JButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }
}