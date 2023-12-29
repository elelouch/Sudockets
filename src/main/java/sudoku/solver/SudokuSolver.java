package sudoku.solver;

import java.util.Arrays;

public class SudokuSolver {
    public static final int X = 0;
    public static final int Y = 1;
    public static final int SIZE = 9;
    public static final int EMPTY = 0;

    public int placedNumbers;
    public boolean[][][] possibilities;
    public int[][] solution;

    public SudokuSolver() {
        possibilities = null;
        solution = null;
    }

    public SudokuSolver(int[][] sudoku) {
        setNewBoard(sudoku);
    }

    public void setNewBoard(int[][] sudoku) {
        placedNumbers = 0;
        solution = new int[SIZE][SIZE];
        possibilities = new boolean[SIZE][SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                Arrays.fill(possibilities[i][j], true);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (sudoku[i][j] != EMPTY)
                    placeNumber(i, j, sudoku[i][j]);
            }
        }
    }

    public void placeNumber(int x, int y, int val) {
        solution[x][y] = val;
        placedNumbers++;
        for (int i = 0; i < SIZE; i++) {
            possibilities[x][i][val - 1] = false;
            possibilities[i][y][val - 1] = false;
        }
        for (int i = 0; i < SIZE / 3; i++) {
            for (int j = 0; j < SIZE / 3; j++) {
                possibilities[x / 3 * 3 + i][y / 3 * 3 + j][val - 1] = false;
            }
        }
    }

    public boolean canPlaceNumber(int x, int y, int val) {
        return possibilities[x][y][val - 1] && solution[x][y] == EMPTY;
    }

    public int countPossibilities(int x, int y) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            if (possibilities[x][y][i])
                count++;
        }
        return count;
    }

    public int[] cellLeastPossibilities() {
        int[] pair = new int[2];
        int min = SIZE;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int c = countPossibilities(i, j);
                if (0 < c && c <= min && solution[i][j] == EMPTY) {
                    min = c;
                    pair[X] = i;
                    pair[Y] = j;
                }
            }
        }
        return pair;
    }

    public void trivialMoves() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (isTrivial(i, j)) {
                        fillTrivial(i, j);
                        changed = true;
                    }
                }
            }
        }
    }

    public void fillTrivial(int x, int y) {
        for (int i = 0; i < SIZE; i++) {
            if (possibilities[x][y][i]) {
                placeNumber(x, y, i + 1);
                return;
            }
        }
    }

    public boolean isTrivial(int x, int y) {
        return solution[x][y] == EMPTY && countPossibilities(x, y) == 1;
    }

    public int[][] solve() {
        if (placedNumbers == 81) {
            return solution;
        }
        int[] cell = cellLeastPossibilities();
        int x = cell[X];
        int y = cell[Y];

        for (int i = 1; i <= SIZE; i++) {
            if (canPlaceNumber(x, y, i)) {
                int[][] solsCopy = deepCopySols();
                boolean[][][] possibsCopy = deepCopyPossibilities();
                int placedCopy = placedNumbers;

                placeNumber(x, y, i);
                trivialMoves();
                if (solve() != null) {
                    return solution;
                }

                placedNumbers = placedCopy;
                solution = solsCopy;
                possibilities = possibsCopy;
            }
        }
        return null;
    }

    public int[][] deepCopySols() {
        return new int[][]{
                Arrays.copyOf(solution[0], SIZE),
                Arrays.copyOf(solution[1], SIZE),
                Arrays.copyOf(solution[2], SIZE),
                Arrays.copyOf(solution[3], SIZE),
                Arrays.copyOf(solution[4], SIZE),
                Arrays.copyOf(solution[5], SIZE),
                Arrays.copyOf(solution[6], SIZE),
                Arrays.copyOf(solution[7], SIZE),
                Arrays.copyOf(solution[8], SIZE),
        };
    }

    public boolean[][][] deepCopyPossibilities() {
        boolean[][][] possibilitiesCopy = Arrays.copyOf(possibilities,SIZE);
        for (int i = 0; i < SIZE; i++) {
            possibilitiesCopy[i] = Arrays.copyOf(possibilities[i],SIZE);
            for (int j = 0; j < SIZE; j++) {
                possibilitiesCopy[i][j] = Arrays.copyOf(possibilities[i][j],SIZE);
            }
        }
        return possibilitiesCopy;
    }

}