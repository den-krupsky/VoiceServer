package by.sparky;

import javax.sound.sampled.SourceDataLine;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class SocketClientService {

    private List<ClientSocket> clients = new ArrayList<>();
    private WMCASocketDriver wmcaState;
    private DatagramSocket serviceSocket;
    private WCMAAudioReceiverDaemon receiverDaemon;

    SocketClientService(DatagramSocket serviceSocket) {
        this.serviceSocket = serviceSocket;
        this.wmcaState = new WMCASocketDriver(serviceSocket);
        System.out.println("Service init " + serviceSocket.getLocalAddress().getCanonicalHostName() + " on " + serviceSocket.getLocalPort());
    }

    private void attachOn(int port) throws SocketException {
        this.serviceSocket = new DatagramSocket(port);
    }

    public void start() {
        System.out.println("Service started!");
        Thread task = new Thread(wmcaState);
        task.start();
        InetAddress clientAddress = null;

        while(clientAddress == null) {
            clientAddress = wmcaState.getAddress();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ClientSocket client = new ClientSocket(clientAddress);

        runDaemon();
        while(true) {
            switch (wmcaState.getState()) {
                case WAIT:
                    client.sendMessage("MyServer:8888".getBytes());
                    break;
                case ACCEPT:
                    client.sendMessage(WMCAState.ACCEPT.code());
                    break;
                case CONNECT:
                    client.sendMessage(WMCAState.CONNECT.code());
                    break;
                case DISCONNECT:
                    System.out.println("Client has been disconnected");
                    break;
                 default:

            }
        }
    }

    private void runDaemon() {
        try {
            this.receiverDaemon = new WCMAAudioReceiverDaemon();
        } catch (SocketException e) {
            System.out.println("Error: Audio Recevier Daemon is not running");
        }
        this.receiverDaemon.start();
    }
}
