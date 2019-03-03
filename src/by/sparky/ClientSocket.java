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
            System.out.println("Response: " + Arrays.toString(packet.getData()));
            System.out.println("From address: " + socket.getLocalAddress().toString());
            System.out.println("On address: " + packet.getAddress().toString());
            System.out.println("From local Socket: " + socket.getLocalSocketAddress().toString());
            System.out.println("On Socket: " + packet.getSocketAddress().toString());
            System.out.println();
        } catch (UnknownHostException e) {
            System.out.println("Sender| error send message. Cause: error host by name ");
        } catch (IOException e) {
            System.out.println("Sender| error send message");
        }
    }

}
