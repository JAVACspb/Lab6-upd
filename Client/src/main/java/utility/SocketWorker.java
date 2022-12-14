package utility;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Class to work with client's socket
 */
public class SocketWorker {

    private final SocketAddress socketAddress;
    private DatagramChannel datagramChannel;
    private final ResponseHandler responseHandler;

    public SocketWorker(SocketAddress aSocketAddress) {
        responseHandler = ResponseHandler.getInstance();
        socketAddress = aSocketAddress;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            datagramChannel.connect(socketAddress);
        } catch (IOException e) {
            Console.getInstance().print("Some problem's with network!");
        }
    }

    public String sendRequest(byte[] dataToSend) {

        try {
            ByteBuffer buf = ByteBuffer.wrap(dataToSend);
            do {
                datagramChannel.write(buf);
            } while (buf.hasRemaining());
            return responseHandler.receive(receiveAnswer());
        } catch (IOException exception) {
            RequestHandler.getInstance().setSocketStatus(false);
            return TextFormatting.getRedText("\tCommand didn't send, try again!\n");
        }
    }

    public String receiveAnswer() {

        long timeStart = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true) {
            if ((System.currentTimeMillis() - timeStart) < 5000) {
                try {
                    datagramChannel.receive(buffer);
                    if (buffer.position() != 0) {
                        return ResponseHandler.getInstance().receive(buffer);
                    }
                } catch (IOException ignored) {
                }
            } else {
                RequestHandler.getInstance().setSocketStatus(false);
                return TextFormatting.getRedText("\n\tServer isn't available at the moment! " +
                        "Please, select another remote host!\n");
            }
        }
    }
}