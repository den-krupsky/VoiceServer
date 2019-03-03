package by.sparky;

import java.net.DatagramSocket;

public class Main {

    public static void main(String[] args) throws Exception {
	    new Main().start();
    }

    private void start() throws Exception {
        DatagramSocket datagramSocket = new DatagramSocket(8888);
        SocketClientService clientService = new SocketClientService(datagramSocket);
        clientService.start();
    }
}
