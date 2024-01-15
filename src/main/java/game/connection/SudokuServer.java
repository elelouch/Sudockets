package game.connection;

import game.ui.SudokuBoard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SudokuServer extends UpdateSender {
    private static final int SIZE = 9;
    ServerSocket server;
    Socket client;

    public SudokuServer(SudokuBoard newBoard) throws IOException {
        server = new ServerSocket(6789);
        client = server.accept();
        setOutputStream(client.getOutputStream());

        UpdateListener listener = new UpdateListener(newBoard);
        listener.setSharingStream(client.getInputStream());

        sendUpdate(flatBoard(newBoard.getBoardCopy()));

        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    private static byte[] flatBoard(int[][] board) {
        byte[] buffer = new byte[81];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buffer[i * SIZE + j] = (byte)board[i][j];
            }
        }
        return buffer;
    }

}
