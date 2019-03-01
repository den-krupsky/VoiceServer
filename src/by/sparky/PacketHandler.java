package by.sparky;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PacketHandler implements Runnable {

    DatagramSocket serverSocket;
    Map<ClientConnectionState, ClientRequestHandler> handlerMap = new HashMap<>();

    PacketHandler(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private void init() {
        ClientRequestHandler scan = (inetAddress) -> {
            StringBuilder packetBuilder = new StringBuilder();
            packetBuilder.append("Sparky receiver");
            packetBuilder.append(":");
            packetBuilder.append("8887"); //TODO

            byte [] buffer = packetBuilder.toString().getBytes();
            sendPacket(new DatagramPacket(buffer, buffer.length, inetAddress, 8888));
        };

        ClientRequestHandler accept = (inetAddress) -> {
            byte [] buffer = ClientConnectionState.ACCEPT.code();
            sendPacket(new DatagramPacket(buffer, buffer.length, inetAddress, 8888));
        };

        ClientRequestHandler connect = (inetAddress) -> {
            byte [] buffer = ClientConnectionState.CONNECT.code();
            sendPacket(new DatagramPacket(buffer, buffer.length, inetAddress, 8888));
        };
        ClientRequestHandler disconnect = (inetAddress) -> {
            System.out.println("Client " + inetAddress.getCanonicalHostName() + " has disconnected");
        };
    }

    @Override
    public void run() {
        DatagramPacket packet = getPacket();
        byte[] request = packet.getData();
        handle(request);
    }

    private DatagramPacket getPacket() {
        byte[] buffer = new byte[2];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            serverSocket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packet;
    }

    private void sendPacket() {

    }

    private void handle(byte[] request) {
        ClientConnectionState state = validate(request);
        if (state != null) {

        }
    }

    private ClientConnectionState validate(byte[] code) {
        byte[] request = new byte[2];
        code = Arrays.copyOf(code, code.length);

        for (ClientConnectionState state : ClientConnectionState.values()) {
            if (Arrays.equals(state.code(), request))
                return state;
        }
        return null;
    }

}
