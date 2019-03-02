package by.sparky;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PortListener extends Thread {

    private DatagramSocket socket;

    PortListener(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        try {
            DatagramPacket packet = new DatagramPacket();
            socket.receive(packet);
        } catch (IOException e) {
            //do nothing
        }
    }

    public DatagramPacket packetArrived() {

    }
}
