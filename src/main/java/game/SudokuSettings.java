package game;

public enum SudokuSettings {
    BOARD_WIDTH(9),
    BOX_WIDTH(3),
    EMPTY_CELL(0),
    CELLS_AMOUNT(81);

    public int value;
    SudokuSettings(int val) {
        value = val;
    }
}
