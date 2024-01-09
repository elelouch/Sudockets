package sudoku;

import java.util.*;

public class SudokuGenerator {
    private static final int PAIR = 2;
    private static final int SIZE = SudokuSolver.SIZE;
    private int CELLS_AMOUNT = 81;
    private int[][] cellsAvailable;
    private int cellsAvailableSize = CELLS_AMOUNT;

    public SudokuGenerator() {
        cellsAvailable = new int[CELLS_AMOUNT][PAIR];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cellsAvailable[i * 9 + j][0] = i;
                cellsAvailable[i * 9 + j][1] = j;
            }
        }
    }

    public int[][] generateSudokuAndShuffleCells() {
        int[][] allowed = new int[SIZE][SIZE];
        int[][] newSudoku = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE ; i++) {
            Arrays.fill(allowed[i], 511);
        }
        while(cellsAvailableSize > 0) {
            int cellPicker = (int)(Math.random() * cellsAvailableSize);
            int[] cell = cellsAvailable[cellPicker];
            int x = cell[0];
            int y = cell[1];
            int randomNumber;
            int mask;
            do {
                randomNumber = (int)(Math.random() * 9) + 1;
                mask = SudokuSolver.BIT_REPRE[randomNumber];
            }while ((allowed[x][y] & mask) <= 0);

            newSudoku[x][y] = randomNumber;
            if(!SudokuSolver.solveSudoku(newSudoku).isEmpty()) {
                SudokuSolver.placeNumber(newSudoku, allowed, x, y, randomNumber);
                removeCell(cellPicker);
            } else {
                newSudoku[x][y] = 0;
            }
        }
        cellsAvailableSize = CELLS_AMOUNT;
        return newSudoku;
    }

    public int[][] generateSolvableSudoku(){
        int[][] newSudoku = generateSudokuAndShuffleCells();
        int head = 0;
        while(head < CELLS_AMOUNT) {
            int[] cell = cellsAvailable[head++];
            int x = cell[0];
            int y = cell[1];
            int prevValue = newSudoku[x][y];
            newSudoku[x][y] = 0;
            if (SudokuSolver.solveSudoku(newSudoku).size() > 1) {
                newSudoku[x][y] = prevValue;
            }
        }
        return newSudoku;
    }

    private void removeCell(int cellPos) {
        int[] cell = cellsAvailable[cellPos];
        cellsAvailable[cellPos] = cellsAvailable[cellsAvailableSize - 1];
        cellsAvailable[cellsAvailableSize - 1] = cell;
        cellsAvailableSize--;
    }
}
