package game.ui.sudoku.panel;

import game.ui.painter.SudokuCellsPainter;
import game.ui.sudoku.game.SudokuGame;
import game.ui.sudoku.game.SudokuGameObserver;
import game.ui.sudoku.SolutionHolder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.SudokuSettings.*;

public class GameUI extends JPanel implements SudokuGame, SudokuGameObserver {
    private static final GridLayout SUDOKU_LAYOUT = new GridLayout(BOX_WIDTH.getValue(),
            BOX_WIDTH.getValue());
    private static final int BORDER_PIXELS = 1;
    private static final Border DEFAULT_BORDER = LineBorder.createBlackLineBorder();
    private static final Border THICKER_BORDER = BorderFactory.createLineBorder(Color.black,
            BORDER_PIXELS + 2);

    private final List<SudokuGame> spectators;
    private final JPanel[][] boxes;
    private final GameButton[][] gameButtons;
    private final SudokuCellsPainter painter;
    private GameButton selectedCell;
    private SolutionHolder solutionHolder;

    public GameUI(int[][] newBoard) {
        spectators = new ArrayList<>();
        boxes = new JPanel[BOX_WIDTH.getValue()][BOX_WIDTH.getValue()];
        gameButtons = new GameButton[BOARD_WIDTH.getValue()][BOARD_WIDTH.getValue()];
        painter = new SudokuCellsPainter(gameButtons);
        setBoxes();
        setLayout(SUDOKU_LAYOUT);
        setCells();
        setAllCells(newBoard);
        setVisible(true);
    }

    private void setCells() {
        for (int i = 0; i < BOARD_WIDTH.getValue(); i++) {
            gameButtons[i] = new GameButton[BOARD_WIDTH.getValue()];
            for (int j = 0; j < BOARD_WIDTH.getValue(); j++) {
                GameButton newCell = generateNewButton(i, j);
                gameButtons[i][j] = newCell;
                boxes[i / BOX_WIDTH.getValue()][j / BOX_WIDTH.getValue()].add(newCell);
            }
        }
    }

    private void setBoxes() {
        for (int i = 0; i < BOX_WIDTH.getValue(); i++) {
            boxes[i] = new JPanel[BOX_WIDTH.getValue()];
            for (int j = 0; j < boxes.length; j++) {
                JPanel newBox = new JPanel();
                boxes[i][j] = newBox;
                newBox.setLayout(SUDOKU_LAYOUT);
                newBox.setBorder(THICKER_BORDER);
                add(newBox);
            }
        }
    }

    private GameButton generateNewButton(int i, int j) {
        GameButton newButton = new GameButton(i, j);
        newButton.setBorder(DEFAULT_BORDER);
        newButton.addActionListener(e -> {
            if (selectedCell != null) {
                selectedCell.setBorder(DEFAULT_BORDER);
            }
            selectedCell = (GameButton) e.getSource();
            selectedCell.setBorder(THICKER_BORDER);

            painter.setSelectedCell(selectedCell.getRow(), selectedCell.getCol());
            painter.paintCells();
        });
        return newButton;
    }

    @Override
    public synchronized void setAllCells(int[][] newBoard) {
        solutionHolder = new SolutionHolder(newBoard);
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                int value = newBoard[i][j];
                undoCell(i, j);
                if (value != EMPTY_CELL.getValue()) {
                    setCell(i, j, value);
                }
            }
        }
    }

    @Override
    public synchronized void undoCell(int i, int j) {
        GameButton button = gameButtons[i][j];
        if (button.isEmpty() || solutionHolder.isSolution(i, j, button.getValue())) {
            return;
        }
        button.setModifiable();
        button.undo();

        for (SudokuGame spectator : spectators)
            spectator.undoCell(i, j);
    }

    public void undoCell() {
        undoCell(selectedCell.getRow(), selectedCell.getCol());
    }

    @Override
    public synchronized void setCell(int i, int j, int number) {
        GameButton buttonToModify = gameButtons[i][j];
        if (buttonToModify.getValue() == number) {
            return;
        }

        buttonToModify.setValue(number);
        buttonToModify.setForeground(Color.black);

        if (!solutionHolder.isSolution(i, j, number)) {
            buttonToModify.setForeground(Color.red);
        }

        for (SudokuGame spectator : spectators)
            spectator.setCell(i, j, number);
    }

    public void fillCell(int number) {
        setCell(selectedCell.getRow(), selectedCell.getCol(), number);
    }

    public void addNote(int number) {
        selectedCell.addNote(number);
    }

    private int[][] getBoardCopy() {
        int size = BOARD_WIDTH.getValue();
        int[][] boardCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            boardCopy[i] = new int[size];
            for (int j = 0; j < size; j++) {
                boardCopy[i][j] = gameButtons[i][j].getValue();
            }
        }
        return boardCopy;
    }

    @Override
    public void subscribe(SudokuGame game) {
        spectators.add(game);
    }

    @Override
    public void unSubscribe(SudokuGame game) {
        spectators.remove(game);
    }

    @Override
    public void notifyFullUpdate() {
        for (SudokuGame spectator : spectators) {
            spectator.setAllCells(getBoardCopy());
        }
    }
}