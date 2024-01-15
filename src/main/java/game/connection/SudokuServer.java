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
        newBoard.setUpdateSender(this);
        setOutputStream(client.getOutputStream());

        UpdateListener listener = new UpdateListener(newBoard);
        listener.setSharingStream(client.getInputStream());

        sendFullUpdate(newBoard.getBoardCopy());

        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }


}
