package game.connection;

import java.io.IOException;
import java.io.OutputStream;

public abstract class UpdateSender {
    OutputStream outputStream;
    UpdateSender() {}

    UpdateSender(OutputStream out) {
       outputStream = out;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendUpdate(int i, int j, int number) {
        sendUpdate(new byte[]{(byte)i,(byte)j,(byte)number});
    }

    public void sendUpdate(byte[] bufferToSend) {
        if(outputStream == null)
            return;
        try {
            outputStream.write(bufferToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
