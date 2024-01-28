package game.connection.updaters;

public enum SudokuUpdate {
    UPDATE(0),
    UNDO(1),
    FULL_UPDATE(2),
    SOLUTION(3),
    END_CONNECTION(4);

    private final int value;

    SudokuUpdate(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }
}
