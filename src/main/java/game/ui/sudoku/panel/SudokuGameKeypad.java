package game.ui.sudoku.panel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class SudokuGameKeypad extends JPanel {
    private static final int SIZE = 9;
    private static final int BORDER_WIDTH = 4;

    private SudokuGameUI sudokuGame;
    private boolean notesMode;
    private final static Border NOTES_OFF = BorderFactory.createLineBorder(Color.red, BORDER_WIDTH);
    private final static Border NOTES_ON = BorderFactory.createLineBorder(Color.green, BORDER_WIDTH);
    private final JButton notesButton;
    private final JButton undoButton;

    public SudokuGameKeypad() {
        setLayout(new FlowLayout());
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> sudokuGame.undoCell());
        add(undoButton);
        notesButton = new JButton("Notes");
        notesButton.setBorder(NOTES_OFF);
        notesButton.addActionListener(e -> {
            notesMode = !notesMode;
            if(notesMode) {
                notesButton.setBorder(NOTES_ON);
            } else {
                notesButton.setBorder(NOTES_OFF);
            }
        });

        add(notesButton);
        for (int i = 1; i <= SIZE; i++) {
            JButton newButton = new JButton(i + "");
            add(newButton);
        }
    }

    public SudokuGameKeypad(SudokuGameUI sudokuGame) {
        this();
        setSudokuGame(sudokuGame);
    }


    public void setSudokuGame(SudokuGameUI sudokuGame) {
        if(sudokuGame == null)
           return;

        ActionListener buttonListener = e -> {
            JButton src = (JButton) e.getSource();
            int valueSelected = Integer.parseInt(src.getText());
            if (notesMode) {
                sudokuGame.addNote(valueSelected);
            } else {
                sudokuGame.fillCell(valueSelected);
            }
        };

        Component[] buttons = getComponents();
        for (Component button : buttons) {
            JButton castedButton = (JButton) button;
            if(castedButton.getText().matches("^[1-9]$")) {
                castedButton.addActionListener(buttonListener);
            }
        }

        this.sudokuGame = sudokuGame;
    }
}
