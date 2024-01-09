package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Keypad extends JPanel {
    private static final int SIZE = 9;
    private SudokuBoard game;
    private boolean notesMode;
    private JButton notesButton;
    private JButton undoButton;

    public Keypad(SudokuBoard newGame) {
        notesMode = false;
        game = newGame;

        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {game.undoSelectedCell();});
        add(undoButton);

        notesButton = new JButton("Notes");
        notesButton.addActionListener(e -> {notesMode = !notesMode;});
        add(notesButton);

        setLayout(new FlowLayout());
        ActionListener buttonListener = e -> {
            JButton src = (JButton) e.getSource();
            int valueSelected = Integer.parseInt(src.getText());
            if (notesMode) {
                game.addNoteSelectedCell(valueSelected);
            } else {
                game.fillSelectedCell(valueSelected);
            }
        };

        for (int i = 1; i <= SIZE; i++) {
            JButton newButton = new JButton(i + "");
            newButton.addActionListener(buttonListener);
            add(newButton);
        }

    }
}
