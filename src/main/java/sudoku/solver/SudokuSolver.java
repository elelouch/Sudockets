package sudoku.solver;

import java.util.Arrays;

public class SudokuSolver {
    private static final int[] BIT_REPRE = {0, 1, 2, 4, 8, 16, 32, 64, 128, 256};
    private static final int SIZE = 9;
    private static final int THIRD = 3;
    private static final int EMPTY = 0;
    private static final int SUDOKU_FILLED = 81;

    public static int countBits(int n) {
        int count = 0;
        while (n > 0) {
            count += n & 1;
            n >>= 1;
        }
        return count;
    }

    public static int[][] deepCopy(int[][] arr) {
        return new int[][]{
                Arrays.copyOf(arr[0], SIZE),
                Arrays.copyOf(arr[1], SIZE),
                Arrays.copyOf(arr[2], SIZE),
                Arrays.copyOf(arr[3], SIZE),
                Arrays.copyOf(arr[4], SIZE),
                Arrays.copyOf(arr[5], SIZE),
                Arrays.copyOf(arr[6], SIZE),
                Arrays.copyOf(arr[7], SIZE),
                Arrays.copyOf(arr[8], SIZE),
        };
    }

    public static void placeNumber(int[][] sudoku, int[][] allowed, int x, int y, int val) {
        int mask = ~BIT_REPRE[val];
        sudoku[x][y] = val;
        allowed[x][y] = 0;
        for (int i = 0; i < SIZE; i++) {
            allowed[x][i] &= mask;
            allowed[i][y] &= mask;
        }
        for (int i = 0; i < THIRD; i++) {
            for (int j = 0; j < THIRD; j++) {
                allowed[x / 3 * 3 + i][y / 3 * 3 + j] &= mask;
            }
        }
    }

    public static int[][] solveSudoku(int[][] sudoku) {
        int[][] gameCopy = deepCopy(sudoku);
        int[][] allowedCopy = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(allowedCopy[i], 511);
        }

        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (sudoku[i][j] != EMPTY) {
                    placeNumber(gameCopy, allowedCopy, i, j, sudoku[i][j]);
                    count++;
                }
            }
        }
        if (solveBoard(gameCopy, allowedCopy, count) == SUDOKU_FILLED ){
            return gameCopy;
        }
        return null;
    }

    public static int trivialMoves(int sudoku[][], int[][] allowed) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (countBits(allowed[i][j]) == 1) {
                    int num = allowed[i][j];
                    int val = 0;
                    while (num > 0) {
                        num >>= 1;
                        val++;
                    }
                    placeNumber(sudoku, allowed, i, j, val);
                    count++;
                }
            }
        }
        return count;
    }

    public static int rowAndColumnSingles(int[][] sudoku, int[][] allowed) {
        int count = 0;
        for (int val = 1; val <= SIZE; val++) {

            int bitVal = BIT_REPRE[val];
            for (int i = 0; i < SIZE; i++) {
                int col = -1;
                for (int j = 0; j < SIZE; j++) {
                    if ((allowed[i][j] & bitVal) > 0) {
                        if (col < 0) {
                            col = j;
                        } else {
                            col = -1;
                            break;
                        }
                    }
                }
                if (col >= 0) {
                    placeNumber(sudoku, allowed, i, col, val);
                    count++;
                }
            }

            for (int i = 0; i < SIZE; i++) {
                int row = -1;
                for (int j = 0; j < SIZE; j++) {
                    if ((allowed[j][i] & bitVal) > 0) {
                        if (row < 0) {
                            row = j;
                        } else {
                            row = -1;
                            break;
                        }
                    }
                }
                if (row >= 0) {
                    placeNumber(sudoku, allowed, row, i, val);
                    count++;
                }
            }
        }
        return count;
    }

    public static int boxSingles(int[][] sudoku, int[][] allowed) {
        int count = 0;
        for (int i = 0; i < SIZE; i += 3) {
            for (int j = 0; j < SIZE; j += 3) {
                for (int k = 1; k <= SIZE; k++) {
                    int[] pair = single(sudoku, allowed, i, j, k);
                    if (pair[0] >= 0) {
                        placeNumber(sudoku, allowed, pair[0], pair[1], k);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static int[] single(int[][] sudoku, int[][] allowed, int x, int y, int val) {
        int[] pair = new int[]{-1, -1};
        int bitval = BIT_REPRE[val];
        for (int i = 0; i < THIRD; i++) {
            for (int j = 0; j < THIRD; j++) {
                if ((allowed[i + x][j + y] & bitval) > 0) {
                    if (pair[0] < 0) {
                        pair[0] = i + x;
                        pair[1] = j + y;
                    } else {
                        pair[0] = -1;
                        return pair;
                    }
                }
            }
        }
        return pair;
    }

    public static int[][] bruteForceBoard(int[][] sudoku, int[][] allowed, int placed) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (sudoku[i][j] == EMPTY) {
                    for (int val = 1; val <= SIZE; val++) {
                        int bitval = BIT_REPRE[val];
                        if ((allowed[i][j] & bitval) > 0) {
                            int[][] gameCopy = deepCopy(sudoku);
                            int[][] allowedCopy = deepCopy(allowed);
                            placeNumber(gameCopy, allowedCopy, i, j, val);
                            int placedNumbers = solveBoard(gameCopy, allowedCopy, placed + 1);
                            if (placedNumbers == SUDOKU_FILLED) {
                                return gameCopy;
                            }
                        }
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public static int solveBoard(int[][] sudoku, int[][] allowed, int placed) {
        int before = -1;
        while ((placed - before) > 0) {
            before = placed;
            placed += trivialMoves(sudoku, allowed);
            placed += boxSingles(sudoku, allowed);
            placed += rowAndColumnSingles(sudoku, allowed);
        }
        if (placed < SUDOKU_FILLED) {
            int solution[][] = bruteForceBoard(sudoku, allowed, placed);
            if (solution != null) {
                placed = 0;
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        if(solution[i][j] != EMPTY) {
                            sudoku[i][j] = solution[i][j];
                            placed++;
                        }
                    }
                }
            }
        }
        return placed;
    }
}