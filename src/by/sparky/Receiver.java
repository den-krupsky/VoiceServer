package by.sparky;

import java.net.DatagramSocket;

public class Receiver extends Thread {
    private DatagramSocket receiverSocket;

//    Receiver(int port) {
//        receiverSocket = new DatagramSocket();
//    }

    public int getReceiverPort() {
        return receiverSocket.getLocalPort();
    }
}
