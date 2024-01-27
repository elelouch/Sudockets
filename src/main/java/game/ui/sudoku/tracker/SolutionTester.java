package game.ui.sudoku.tracker;

import game.ui.sudoku.exceptions.UnsolvableSudokuException;
import game.utils.SudokuSolver;

import java.util.List;

public class SolutionTester {
    int[][] solution;
    public SolutionTester(int[][] sudokuBoard) {
        List<int[][]> solutions = SudokuSolver.solveSudoku(sudokuBoard);
        if(solutions.isEmpty()) {
            throw new UnsolvableSudokuException("Board must be at least solvable to start a solution tester");
        }
        solution = solutions.get(0);
    }

    public boolean isSolution(int i, int j, int number) {
        return solution[i][j] == number;
    }
}