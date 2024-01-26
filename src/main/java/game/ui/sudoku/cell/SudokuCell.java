package game.ui.sudoku.cell;

public interface SudokuCell {
    void setValue(int value);
    int getValue();
    void undo();
}
