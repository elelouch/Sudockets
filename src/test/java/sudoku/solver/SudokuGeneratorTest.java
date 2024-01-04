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
        int[][] sudoku = morocha.generateSudokuAndShuffleCells();
    }
}
