package game.ui.sudoku;

import game.ui.sudoku.exceptions.UnsolvableSudokuException;
import game.utils.SudokuSolver;

import java.util.List;

public class SolutionHolder {
    int[][] solution;
    public SolutionHolder(int[][] sudokuBoard) {
        List<int[][]> solutions = SudokuSolver.solveSudoku(sudokuBoard);
        if(solutions.isEmpty()) {
            throw new UnsolvableSudokuException("Board must be at least solvable to have a solution");
        }
        solution = solutions.get(0);
    }

    public boolean isSolution(int i, int j, int number) {
        return solution[i][j] == number;
    }
}