package sudoku.solver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sudoku.SudokuGenerator;
import sudoku.SudokuSolver;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuGeneratorTest {
    @Test
    public void labanda(){
        SudokuGenerator morocha = new SudokuGenerator();
        int[][] sudoku = morocha.generateSolvableSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }
}
