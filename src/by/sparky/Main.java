package by.sparky;

import java.io.IOException;
import java.net.DatagramSocket;

public class Main {

    public static void main(String[] args) {
	    new Main().start();
    }

    private void start() {
        try {
            DatagramSocket socket = new DatagramSocket(8888);
            SocketClientService server = new SocketClientService(socket);
            server.start();
        } catch (IOException e) {
            System.out.println("Server fail start");
        }
    }
}
