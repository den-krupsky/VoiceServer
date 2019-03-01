package by.sparky;

import static by.sparky.ClientConnectionState.WAIT;
import static by.sparky.ClientConnectionState.ACCEPT;
import static by.sparky.ClientConnectionState.CONNECT;
import static by.sparky.ClientConnectionState.DISCONNECT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//WSMA Recceiver
public class ServerThread extends Thread {


    private static final String DEFAULT_NAME = "Sparky server";
    private static final int DEFAULT_PORT = 8888;

    private int controlledPort = DEFAULT_PORT;
    private ClientConnectionState state = WAIT;
    private DatagramSocket serverSocket;
    private boolean portListed = false;

    private Map<ClientConnectionState, ClientRequestHandler> requestHandlerMap = new HashMap<>();

    ServerThread() {
        this(-1, null);
    }

    ServerThread(String name) {
        this(-1, name);
    }

    ServerThread(int controlledPort, String name) {
        if (controlledPort == -1)
            controlledPort = DEFAULT_PORT;
        if (name == null)
            name = DEFAULT_NAME;

        attachServer(controlledPort);
        setName(name);
        bindMap();
    }

    private void bindMap() {
        ClientRequestHandler scan = (inetAddress) -> {
            StringBuilder packetBuilder = new StringBuilder();
            packetBuilder.append(getServerName());
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



        requestHandlerMap.put(ClientConnectionState.WAIT, scan);
        requestHandlerMap.put(ClientConnectionState.ACCEPT, accept);
        requestHandlerMap.put(ClientConnectionState.CONNECT, connect);
        requestHandlerMap.put(ClientConnectionState.DISCONNECT, disconnect);
    }

    private void attachServer(int port) { //не понятно что делать, если не удалось всё-таки занять порт
        System.out.println("Starting attach server on " + port + " port");
        try {
            this.serverSocket = new DatagramSocket(port);
            this.portListed = true;
        } catch (SocketException ex) {
            System.out.println("Failed attach to controlledPort " + port + ". " + ex.getMessage()); // not attached
            serverSocket = null;
        } finally {
            if(serverSocket == null) System.out.println("Server not set up");
        }
    }

    //1 step
    private DatagramPacket waitClient() {



        byte[] buffer = new byte[2];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//        serverSocket.receive(packet);
//
//        for (ClientStatus : ClientConnectionState.values()) {
//
//        }
//

        return packet;
    }

    private void serviceClient(InetAddress clientAdress) {
        while(true) {

            byte[] status = new byte[2];
            byte[] clientData = waitClient().getData();
            status[0] = clientData[0];
            status[1] = clientData[1];

            for (ClientConnectionState clientConnectionState : ClientConnectionState.values()) {
                if (Arrays.equals(clientConnectionState.code(), status)) {

                    System.out.println("Client service: request for " + clientConnectionState.name());
                    ClientRequestHandler handler = requestHandlerMap.get(clientConnectionState);
                    handler.handle(clientAdress);
                }
            }


        }

    }

    @Override
    public void run() {

        while(true) {
            DatagramPacket packet = waitClient();
            packet.getData();


        }





//        InetAddress clientAdress = packet.getAddress();

//        serviceClient(clientAdress);
    }



    public void waitConnection() throws IOException {
        //data 1 1

        String serverName = getServerName();

        DatagramSocket socket = new DatagramSocket();

        StringBuilder packetBuilder = new StringBuilder();
        packetBuilder.append(getServerName());
        packetBuilder.append(":");
        packetBuilder.append("receiver controlledPort"); //TODO

        DatagramPacket packet = new DatagramPacket(packetBuilder.toString().getBytes(), packetBuilder.length()); //TODO other parameters
        socket.send(packet);
        socket.close();
    }


    public void sendPacket(DatagramPacket packet) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
        } catch (IOException ex) {
            System.out.println("Error sending packet. " + ex.getMessage());
        }
    }







    public void setServerName(String name) {
        setName(name);
    }

    public String getServerName() {
        return getName();
    }




}
