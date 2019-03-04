package by.sparky;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class WMSAService extends Thread {

    private static final int DEFAULT_PORT = 8888;
    private String serviceName = "Sparky Server";
    private WMSASocketListener socketListener;
    private WMSAAudioSocketListener audioSocketListener;
    private SocketClientManager clientManager;

    WMSAService() throws SocketException {
        this.audioSocketListener = new WMSAAudioSocketListener();
        this.socketListener = new WMSASocketListener(DEFAULT_PORT);
        audioSocketListener.start();
        //socketListener.start();
        this.clientManager = new SocketClientManager();
    }

    @Override
    public void run() {
        System.out.println("Service is running!");
        while(true) {
            DatagramPacket packet = socketListener.getPacket();
            handleRequest(packet);
        }

    }

    private void handleRequest(DatagramPacket packet) {
        InetAddress clientAddress = packet.getAddress();

        for (WMSAState request : WMSAState.values()) {
            if (Arrays.equals(request.code(), packet.getData())) {
                System.out.println("Get packet from " + packet.getSocketAddress().toString() + " with data " + Arrays.toString(packet.getData()));
                switch (request) {
                    case WAIT:
                        if (!clientManager.isRegistered(clientAddress.getHostAddress())) {
                            clientManager.register(clientAddress);
                            String msg = serviceName + ":" + audioSocketListener.getReceiverPort();
                            clientManager.sendMessage(msg.getBytes(), clientAddress);
                        }
                        break;
                    case CONNECT:
                        if(clientManager.isRegistered(clientAddress.getHostAddress())) {
                           clientManager.sendMessage(WMSAState.CONNECT.code(), clientAddress);
                           clientManager.setClient(clientAddress);
                        }
                        break;
                    case ACCEPT:
                        if (clientManager.checkClient(clientAddress)) {
                            clientManager.sendMessage(WMSAState.ACCEPT.code(), clientAddress);
                        } else {
                            clientManager.sendMessage(WMSAState.NONE.code(), clientAddress);
                        }
                        break;
                    case DISCONNECT:
                        if (clientManager.checkClient(clientAddress)) {
                            clientManager.deleteClient();
                        }
                        break;
                }
            }
        }

    }

}

