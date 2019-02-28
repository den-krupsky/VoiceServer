package by.sparky;


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
    private DatagramSocket serverSocket;
    private boolean portListed = false;

    private Map<ClientStatus, ClientRequestHandler> requestHandlerMap = new HashMap<>();

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
            byte [] buffer = ClientStatus.ACCEPT.getCode();
            sendPacket(new DatagramPacket(buffer, buffer.length, inetAddress, 8888));
        };

        ClientRequestHandler connect = (inetAddress) -> {
            byte [] buffer = ClientStatus.CONNECT.getCode();
            sendPacket(new DatagramPacket(buffer, buffer.length, inetAddress, 8888));
        };
        ClientRequestHandler disconnect = (inetAddress) -> {
            System.out.println("Client " + inetAddress.getCanonicalHostName() + " has disconnected");
        };

        requestHandlerMap.put(ClientStatus.SCAN, scan);
        requestHandlerMap.put(ClientStatus.ACCEPT, accept);
        requestHandlerMap.put(ClientStatus.CONNECT, connect);
        requestHandlerMap.put(ClientStatus.DISCONNECT, disconnect);
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
        byte[] status = new byte[2];
        DatagramPacket p = new DatagramPacket(status, status.length);
        try {
            serverSocket.receive(p);
        } catch (IOException e) {
            System.out.println("Error recieve client status. " + e.getMessage() + " data: " + Arrays.toString(p.getData()));
            e.printStackTrace();
        }
        System.out.println("Client pecked");
        System.out.println("Host Address: " + p.getAddress().getHostAddress());
        System.out.println("Port:" + p.getPort());
        System.out.println("Data: " + Arrays.toString(p.getData()));

        return p;
    }

    private void serviceClient(InetAddress clientAdress) {
        while(true) {
            byte[] status = new byte[2];
            byte[] clientData = waitClient().getData();
            status[0] = clientData[0];
            status[1] = clientData[1];

            for (ClientStatus clientStatus : ClientStatus.values()) {
                if (Arrays.equals(clientStatus.getCode(), status)) {

                    System.out.println("Client service: request for " + clientStatus.name());
                    ClientRequestHandler handler = requestHandlerMap.get(clientStatus);
                    handler.handle(clientAdress);
                }
            }


        }

    }

    @Override
    public void run() {
        DatagramPacket packet = waitClient();
        InetAddress clientAdress = packet.getAddress();

        serviceClient(clientAdress);
    }



    public void some() throws IOException {
        //getting connection state and sender inet address
        byte[] data = new byte[2];
        //data[0] = 4, data[0] = 4 is connection out
        //data[0] = 3, data[0] = 3 is status connected. set host adress and
        //data[0] = 2, data[0] = 2 is
        //data[0] = 1, data[0] = 1 is connection established. sender ready for authorization
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        serverSocket.receive(datagramPacket);
        InetAddress inetAddress = datagramPacket.getAddress();


        DatagramSocket socket;
        byte[] buffer;


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

    public void acceptConnection() throws IOException {
        //data 2 2
        DatagramSocket socket = new DatagramSocket();


        byte[] status = new byte[] {2, 2};
        DatagramPacket packet = new DatagramPacket(status, status.length); //TODO
        socket.send(packet);
        socket.close();
    }

    public void establishedConnection() throws IOException {
        //data 3 3
        DatagramSocket socket = new DatagramSocket();


        byte[] status = new byte[] {3, 3}; //TODO 3 - if ip == client adress else 0
        DatagramPacket packet = new DatagramPacket(status, status.length); //TODO
        socket.send(packet);
        socket.close();
        //TODO run connection waiter
    }

    public void clientWouldDisconnect() {
        //data 4 4

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
