package game.utils;

import static game.SudokuSettings.*;

import java.util.*;

public class SudokuGenerator {
    private static final int PAIR = 2;
    private static int[][] cellsAvailable;
    private static int cellsAvailableSize = CELLS_AMOUNT.getValue();

    static {
        int size = BOARD_WIDTH.getValue();
        cellsAvailable = new int[CELLS_AMOUNT.getValue()][PAIR];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cellsAvailable[i * size + j][0] = i;
                cellsAvailable[i * size + j][1] = j;
            }
        }
    }

    public static int[][] generateSudokuAndShuffleCells() {
        int size = BOARD_WIDTH.getValue();
        int[][] allowed = new int[size][size];
        int[][] newSudoku = new int[size][size];

        for (int i = 0; i < allowed.length; i++) {
            int[] arr = new int[size];
            Arrays.fill(arr, 511);
            allowed[i] = arr;
        }

        while (cellsAvailableSize > 0) {
            int cellPicker = (int) (Math.random() * cellsAvailableSize);
            int[] cell = cellsAvailable[cellPicker];
            int x = cell[0];
            int y = cell[1];
            int randomNumber;
            int mask;
            do {
                randomNumber = (int) (Math.random() * 9) + 1;
                mask = SudokuSolver.BIT_REPRE[randomNumber];
            } while ((allowed[x][y] & mask) <= 0);

            newSudoku[x][y] = randomNumber;
            if (!SudokuSolver.solveSudoku(newSudoku).isEmpty()) {
                SudokuSolver.placeNumber(newSudoku, allowed, x, y, randomNumber);
                removeCell(cellPicker);
            } else {
                newSudoku[x][y] = 0;
            }
        }
        cellsAvailableSize = CELLS_AMOUNT.getValue();
        return newSudoku;
    }

    public static int[][] generateUniqueSudoku() {
        int[][] newSudoku = generateSudokuAndShuffleCells();
        int head = 0;
        while (head < cellsAvailableSize) {
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

    private static void removeCell(int cellPos) {
        int[] cell = cellsAvailable[cellPos];
        cellsAvailable[cellPos] = cellsAvailable[cellsAvailableSize - 1];
        cellsAvailable[cellsAvailableSize - 1] = cell;
        cellsAvailableSize--;
    }
}
