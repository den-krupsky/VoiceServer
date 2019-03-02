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
        wmcaState.run();
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

        while(true) {
            switch (wmcaState.getState()) {
                case WAIT:
                    client.sendData(serviceSocket, "MyServer:8887");
                    break;
                case ACCEPT:
                    client.sendData(serviceSocket, WMCAState.ACCEPT.code());
                    break;
                case CONNECT:
                    client.sendData(serviceSocket, WMCAState.CONNECT.code());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case DISCONNECT:
                    System.out.println("Client directly disconnected");
                    break;
            }

//
        }
    }
}
