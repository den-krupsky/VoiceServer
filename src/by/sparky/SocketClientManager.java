package by.sparky;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class SocketClientManager {


    private static final Map<String, InetAddress> clients = new HashMap<>();
    private InetAddress activeClient;
    private WMSASocketListener wmcaSocket;

    private int port = 8888;
    private DatagramSocket socket;
    private DatagramPacket packet;

    SocketClientManager() throws SocketException {
    }


    public boolean isRegistered(String host) {
        return clients.containsKey(host);
    }

    public void register(InetAddress inetAddress) {
        clients.put(inetAddress.getHostAddress(), inetAddress);
    }

    public void setClient(InetAddress clientAddress) {
        this.activeClient = clientAddress;
    }

    public boolean checkClient(InetAddress inetAddress) {
        if (activeClient == null) {
            return false;
        }
        return activeClient == inetAddress;
    }

    public void deleteClient() {
        this.activeClient = null;
    }



    public void sendMessage(byte[] msg, InetAddress inetAddress) {
        try {
            DatagramPacket packet = new DatagramPacket(msg, msg.length, inetAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            //do nothing
        }
    }
}
