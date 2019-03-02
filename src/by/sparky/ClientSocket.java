package by.sparky;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ClientSocket {
    private InetAddress clientAddress;

    ClientSocket(InetAddress inetAddress) {
        this.clientAddress = inetAddress;
    }

    public void sendData(DatagramSocket socket, byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, 8888);
        try {
            System.out.println("send data + " + Arrays.toString(packet.getData()));
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(DatagramSocket socket, String str) {
        byte[] data = str.getBytes();
        sendData(socket, data);
    }
}
