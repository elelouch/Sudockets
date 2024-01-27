package game.ui.sudoku.tracker;

import game.ui.sudoku.cell.SudokuCell;
import game.ui.sudoku.game.SudokuGame;
import game.ui.sudoku.exceptions.UnsolvableSudokuException;
import game.utils.SudokuSolver;

import java.util.List;

import static game.SudokuSettings.BOARD_WIDTH;

public class SudokuGameUpdater implements SudokuGame {
    private final int[][] boardSolution;
    private final CellTracker[][] board;

    static class CellTracker implements SudokuCell {
        private int value;

        private CellTracker(int value) {
            this.value = value;
        }

        @Override
        public void setValue(int value) {
            if (value >= 1 && value <= 9) {
                this.value = value;
            }
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public void undo() {
            value = 0;
        }
    }

    public SudokuGameUpdater(int[][] newBoard) throws UnsolvableSudokuException {
        int boardSize = BOARD_WIDTH.getValue();
        List<int[][]> solutions = SudokuSolver.solveSudoku(newBoard);
        if (solutions.isEmpty()) {
            throw new UnsolvableSudokuException("Sudoku must be solvable");
        }
        board = new CellTracker[boardSize][boardSize];
        boardSolution = solutions.get(0);

        for (int i = 0; i < boardSize; i++) {
            board[i] = new CellTracker[boardSize];
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new CellTracker(boardSolution[i][j]);
            }
        }
    }

    public int[][] getBoardCopy() {
        int[][] boardCopy = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            boardCopy[i] = new int[board.length];
            for (int j = 0; j < board.length; j++) {
                boardCopy[i][j] = board[i][j].getValue();
            }
        }
        return boardCopy;
    }

    @Override
    public void setCell(int i, int j, int num) {
        board[i][j].setValue(num);
    }

    @Override
    public void undoCell(int i, int j) {
        board[i][j].undo();
    }

    @Override
    public void setAllCells(int[][] board) {
    }

}
