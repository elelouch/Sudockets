package game.ui.sudoku;

public interface SudokuGame {
    void setCell(int i, int j, int num);
    void undoCell(int i, int j);
}
