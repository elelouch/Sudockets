package game.connection;

public enum ConnectionOptions {
    UPDATE((byte)0),
    UNDO((byte)1),
    FULL_UPDATE((byte)2);

    byte value;

    ConnectionOptions(byte val) {
        value = val;
    }
}
