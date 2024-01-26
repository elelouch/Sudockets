package game.ui;

import game.ui.sudoku.panel.SudokuPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class SudokuKeypad extends JPanel {
    private static final int SIZE = 9;

    private SudokuPanel sudokuGame;
    private boolean notesMode;
    private Border notesModeOnBorder;
    private Border notesModeOffBorder;
    private JButton notesButton;
    private JButton undoButton;

    public SudokuKeypad() {
        notesMode = false;
        notesModeOffBorder = BorderFactory.createLineBorder(Color.red, 5);
        notesModeOnBorder = BorderFactory.createLineBorder(Color.green, 5);
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> sudokuGame.undoCell());
        add(undoButton);
        notesButton = new JButton("Notes");
        notesButton.setBorder(notesModeOffBorder);
        notesButton.addActionListener(e -> {
            notesMode = !notesMode;
            if(notesMode) {
                notesButton.setBorder(notesModeOnBorder);
            } else {
                notesButton.setBorder(notesModeOffBorder);
            }
        });
        add(notesButton);
        setLayout(new FlowLayout());
        for (int i = 1; i <= SIZE; i++) {
            JButton newButton = new JButton(i + "");
            add(newButton);
        }
    }

    public SudokuKeypad(SudokuPanel sudokuGame) {
        this();
        setSudokuGame(sudokuGame);
    }


    public void setSudokuGame(SudokuPanel sudokuGame) {
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
        for (int i = 0; i < buttons.length; i++) {
            JButton button = (JButton) buttons[i];
            if(button.getText().matches("^[1-9]$")) {
                button.addActionListener(buttonListener);
            }
        }
        this.sudokuGame = sudokuGame;
    }
}
