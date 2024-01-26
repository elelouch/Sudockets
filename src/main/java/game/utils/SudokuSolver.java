package game.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static game.SudokuSettings.*;


public class SudokuSolver {
    protected static final int[] BIT_REPRE = {0, 1, 2, 4, 8, 16, 32, 64, 128, 256};

    private static int countBits(int n) {
        int count = 0;
        while (n > 0) {
            count += n & 1;
            n >>= 1;
        }
        return count;
    }

    private static int[][] deepCopy(int[][] arr) {
        return new int[][]{
                Arrays.copyOf(arr[0], BOARD_WIDTH.value),
                Arrays.copyOf(arr[1], BOARD_WIDTH.value),
                Arrays.copyOf(arr[2], BOARD_WIDTH.value),
                Arrays.copyOf(arr[3], BOARD_WIDTH.value),
                Arrays.copyOf(arr[4], BOARD_WIDTH.value),
                Arrays.copyOf(arr[5], BOARD_WIDTH.value),
                Arrays.copyOf(arr[6], BOARD_WIDTH.value),
                Arrays.copyOf(arr[7], BOARD_WIDTH.value),
                Arrays.copyOf(arr[8], BOARD_WIDTH.value),
        };
    }

    protected static void placeNumber(int[][] sudoku, int[][] allowed, int x, int y, int val) {
        int mask = ~BIT_REPRE[val];
        sudoku[x][y] = val;
        allowed[x][y] = 0;
        for (int i = 0; i < BOARD_WIDTH.value; i++) {
            allowed[x][i] &= mask;
            allowed[i][y] &= mask;
        }
        for (int i = 0; i < BOX_WIDTH.value; i++) {
            for (int j = 0; j < BOX_WIDTH.value; j++) {
                allowed[x / 3 * 3 + i][y / 3 * 3 + j] &= mask;
            }
        }
    }

    public static final List<int[][]> solveSudoku(int[][] sudoku) {
        int[][] gameCopy = deepCopy(sudoku);
        int[][] allowedCopy = new int[BOARD_WIDTH.value][BOARD_WIDTH.value];

        for (int[] allowed : allowedCopy) {
            Arrays.fill(allowed, 511);
        }

        int count = 0;
        for (int i = 0; i < gameCopy.length; i++) {
            for (int j = 0; j < gameCopy[i].length; j++) {
                if (sudoku[i][j] != EMPTY_CELL.value) {
                    placeNumber(gameCopy, allowedCopy, i, j, sudoku[i][j]);
                    count++;
                }
            }
        }

        return solveBoard(gameCopy, allowedCopy, count);
    }

    private static int trivialMoves(int[][] sudoku, int[][] allowed) {
        int count = 0;
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++) {
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

    private static int rowAndColumnSingles(int[][] sudoku, int[][] allowed) {
        int count = 0;
        for (int val = 1; val <= sudoku.length; val++) {
            int bitVal = BIT_REPRE[val];
            for (int i = 0; i < sudoku.length; i++) {
                int col = -1;
                for (int j = 0; j < sudoku.length; j++) {
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
            for (int i = 0; i < sudoku.length; i++) {
                int row = -1;
                for (int j = 0; j < sudoku.length; j++) {
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

    private static int boxSingles(int[][] sudoku, int[][] allowed) {
        int count = 0;
        for (int i = 0; i < sudoku.length; i += 3) {
            for (int j = 0; j < sudoku.length; j += 3) {
                for (int k = 1; k <= sudoku.length; k++) {
                    int[] pair = findSingle(allowed, i, j, k);
                    if (pair[0] >= 0) {
                        placeNumber(sudoku, allowed, pair[0], pair[1], k);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static int[] findSingle(int[][] allowed, int x, int y, int val) {
        int[] pair = new int[]{-1, -1};
        int bitval = BIT_REPRE[val];
        for (int i = 0; i < BOX_WIDTH.value; i++) {
            for (int j = 0; j < BOX_WIDTH.value; j++) {
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

    private static List<int[][]> bruteForceBoard(int[][] sudoku, int[][] allowed, int placed) {
        List<int[][]> solutions = new ArrayList<>();
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++) {
                if (sudoku[i][j] == EMPTY_CELL.value) {
                    for (int val = 1; val <= sudoku.length; val++) {
                        int bitval = BIT_REPRE[val];
                        if ((allowed[i][j] & bitval) > 0) {
                            int[][] gameCopy = deepCopy(sudoku);
                            int[][] allowedCopy = deepCopy(allowed);
                            placeNumber(gameCopy, allowedCopy, i, j, val);
                            solutions.addAll(solveBoard(gameCopy, allowedCopy, placed + 1));
                            if (solutions.size() >= 2) {
                                return solutions;
                            }
                        }
                    }
                    return solutions;
                }
            }
        }
        return solutions;
    }

    private static List<int[][]> solveBoard(int[][] sudoku, int[][] allowed, int placed) {
        List<int[][]> solutions = new ArrayList<>();
        placed += fillEasyValues(sudoku, allowed);

        if (placed < CELLS_AMOUNT.value) {
            solutions.addAll(bruteForceBoard(sudoku, allowed, placed));
        }

        if (placed == CELLS_AMOUNT.value) {
            solutions.add(sudoku);
        }

        return solutions;
    }

    private static int fillEasyValues(int[][] sudoku, int[][] allowed) {
        int before = -1;
        int placed = 0;
        while ((placed - before) > 0) {
            before = placed;
            placed += trivialMoves(sudoku, allowed);
            placed += boxSingles(sudoku, allowed);
            placed += rowAndColumnSingles(sudoku, allowed);
        }
        return placed;
    }
}
