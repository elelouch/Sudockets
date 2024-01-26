package game.ui.sudoku.tracker;

import game.ui.sudoku.cell.SudokuCell;
import game.ui.sudoku.game.SudokuGame;
import game.ui.sudoku.exceptions.UnsolvableSudokuException;
import game.utils.SudokuSolver;

import java.util.Arrays;
import java.util.List;

import static game.SudokuSettings.BOARD_WIDTH;
import static game.SudokuSettings.EMPTY_CELL;

public class SudokuTracker implements SudokuGame {
    private CellTracker[][] boardSolution;
    private CellTracker[][] board;

    private class CellTracker implements SudokuCell {
        int value;

        @Override
        public void setValue(int value) {
           this.value = value;
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



    public SudokuTracker(int[][] newBoard) throws UnsolvableSudokuException {
        List<int[][]> solutions = SudokuSolver.solveSudoku(newBoard);
        board = new CellTracker[BOARD_WIDTH.value][BOARD_WIDTH.value];
        boardSolution = new CellTracker[BOARD_WIDTH.value][BOARD_WIDTH.value];

        if (solutions.isEmpty()){
            throw new UnsolvableSudokuException("Sudoku must be solvable");
        }

        int[][] solution = solutions.get(0);

        for(int[] row : solution) {

        }
    }

    public int[][] getBoardCopy() {
        int[][] boardCopy = new int[BOARD_WIDTH.value][BOARD_WIDTH.value];
        for (int i = 0; i < BOARD_WIDTH.value; i++) {
            boardCopy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return boardCopy;
    }

    @Override
    public void setCell(int i, int j, int num) {
        board[i][j] = num;
    }

    @Override
    public void undoCell(int i, int j) {
        board[i][j] = EMPTY_CELL.value;
    }

    public boolean isSolution(int i, int j, int number) {
        return boardSolution[i][j] == number;
    }
}
