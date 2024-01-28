package game.connection;

public enum Updates {
    UPDATE(0),
    UNDO(1),
    FULL_UPDATE(2),
    END_CONNECTION(3);

    private final int value;

    Updates(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }
}
