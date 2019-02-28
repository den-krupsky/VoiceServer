package by.sparky;

import java.net.DatagramSocket;

public class Receiver {
    private DatagramSocket datagramSocket;

    public int getReceiverPort() {
        return datagramSocket.getLocalPort();
    }
}
