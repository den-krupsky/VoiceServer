package by.sparky;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ClientSocket {

    private int port = 8888;
    private InetAddress clientAddress;
    private DatagramSocket socket = null;
    private DatagramPacket packet;


    ClientSocket(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
        try {
            this.socket = new DatagramSocket(0);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] msg) {
        try {
            DatagramPacket packet = new DatagramPacket(msg, msg.length, clientAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            //do nothing
        }
    }

}
