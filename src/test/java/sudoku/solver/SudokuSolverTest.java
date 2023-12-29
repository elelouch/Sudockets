package sudoku.solver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


public class SudokuSolverTest {

    int[][] sudoku3 = {
            {0, 1, 4, 0, 7, 0, 0, 9, 0},
            {0, 0, 8, 0, 0, 0, 0, 6, 1},
            {0, 5, 0, 3, 0, 2, 0, 0, 0},
            {0, 0, 6, 0, 9, 4, 0, 0, 0},
            {0, 0, 0, 8, 0, 6, 0, 0, 7},
            {0, 0, 0, 1, 3, 0, 6, 0, 0},
            {0, 0, 0, 2, 0, 7, 0, 3, 0},
            {5, 7, 0, 0, 0, 0, 9, 0, 0},
            {0, 6, 0, 0, 5, 0, 7, 8, 0}
    };

    int[][] sudoku0 = {
            {5, 0, 3, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 7, 6, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 8, 0, 0},
            {0, 0, 8, 3, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 6},
            {0, 0, 0, 0, 0, 0, 0, 7, 0},
            {1, 4, 0, 5, 0, 0, 2, 0, 0},
            {0, 6, 0, 2, 0, 0, 0, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    int[][] sudoku4 = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 9, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    int[][] sudoku6 = {
            {0, 0, 0, 0, 0, 0, 4, 0, 8},
            {0, 0, 1, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 9, 7, 0},
            {5, 0, 8, 9, 0, 1, 0, 0, 0},
            {3, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 5, 2, 8, 0, 3, 0},
            {8, 0, 7, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 4, 5, 0, 0},
            {0, 6, 2, 8, 9, 0, 0, 0, 0}
    };

    int[][] sudoku1 = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    int[][] sudoku2 = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 0, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };
    SudokuSolver solverTrivial = new SudokuSolver(sudoku2);

    @Test
    public void testSolver() {
        SudokuSolver solver = new SudokuSolver(sudoku6);
        long opa = System.nanoTime();
        int[][] epa = solver.solve();
        long upa = System.nanoTime();
        System.out.println("time: " + (upa - opa) );
        for (int[] row : epa) {
            for (int num : row ) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void testTrivialMoves() {
        solverTrivial.trivialMoves();
        Assertions.assertTrue(sudokuIsFilled(solverTrivial.solution));
    }

    @Test
    public void testIsTrivial() {
        Assertions.assertTrue(solverTrivial.isTrivial(1, 1));
    }

    @Test
    public void testCountPossibilities() {
        Assertions.assertEquals(solverTrivial.countPossibilities(1, 1), 1);
    }

    @Test
    public void testPossibility() {
        Assertions.assertEquals(solverTrivial.possibilities[1][1][6], true);
    }

    public boolean sudokuIsFilled(int[][] sudoku) {
        for (int[] row : sudoku) {
            for (int num : row) {
                if (num == 0)
                    return false;
            }
        }
        return true;
    }
}
