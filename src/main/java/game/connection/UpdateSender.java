package game.connection;

import java.io.IOException;
import java.io.OutputStream;

public abstract class UpdateSender {
    private static final byte UPDATE = 0;
    private static final byte UNDO = 1;
    private static final byte FULL_UPDATE = 2;
    private static final byte BUFFER_SIZE = 81;
    OutputStream outputStream;

    UpdateSender() { }

    UpdateSender(OutputStream out) {
        outputStream = out;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendUpdate(int i, int j, int number) {
        sendBuffer(new byte[]{UPDATE, (byte) i, (byte) j, (byte) number});
    }

    public void sendUndo(int i, int j) {
        sendBuffer(new byte[]{UNDO,(byte) i, (byte) j});
    }

    private static byte[] generateFlatBoardWithoutOption(int[][] board) {
        byte[] buffer = new byte[BUFFER_SIZE + 1];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                buffer[i * board.length + j] = (byte)board[i][j];
            }
        }

        return buffer;
    }

    public void sendFullUpdate(int[][] sudoku) {
        byte[] buffer = generateFlatBoardWithoutOption(sudoku);
        buffer[0] = FULL_UPDATE;
        sendBuffer(buffer);
    }


    public void sendBuffer(byte[] bufferToSend) {
        if (outputStream == null)
            return;
        try {
            outputStream.write(bufferToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
