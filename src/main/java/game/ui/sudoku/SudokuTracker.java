package game.ui.sudoku;

import game.utils.SudokuSolver;

import java.util.Arrays;
import java.util.List;

import static game.SudokuSettings.BOARD_WIDTH;
import static game.SudokuSettings.EMPTY_CELL;

public class SudokuTracker implements SudokuGame {
    private int[][] boardSolution;
    private int[][] board;

    public SudokuTracker(int[][] newBoard) throws UnsolvableSudokuException {
        board = newBoard;
        List<int[][]> solutions = SudokuSolver.solveSudoku(newBoard);
        if (solutions.isEmpty()){
            throw new UnsolvableSudokuException("Sudoku must be solvable");
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
