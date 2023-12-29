package sudoku.generator;

import sudoku.solver.SudokuSolver;

import java.util.*;

public class SudokuGenerator {
    private static final int PAIR = 2;
    private static final int SIZE = 9;
    private static final int X = 0;
    private static final int Y = 1;
    SudokuSolver solver = new SudokuSolver();
    int[][] sudoku;
    List<int[]> cells;
    public SudokuGenerator() {
        sudoku = new int[SIZE][SIZE];
        cells = new ArrayList<int[]>(SIZE ^ 2);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int [] pair = new int[PAIR];
                pair[X] = i;
                pair[Y] = j;
                cells.add(pair);
            }
        }
    }

    public int[][] generateFilledSudoku() {
        Collections.shuffle(cells);
        Queue<int[]> cellsQ = new LinkedList<>(cells);
        while(!cellsQ.isEmpty()) {
            int[] cell = cellsQ.poll();
            int randNum = (int)Math.random() * 10;

        }
        return new int[1][1];
    }

}
