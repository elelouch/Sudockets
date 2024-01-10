package sudoku.solver;

import org.junit.jupiter.api.Test;
import sudoku.SudokuGenerator;

public class SudokuGeneratorTest {
    @Test
    public void labanda(){
        SudokuGenerator morocha = new SudokuGenerator();
        int[][] sudoku = morocha.generateUniqueSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.println();
        }
    }
}
