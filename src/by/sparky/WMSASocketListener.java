package by.sparky;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class WMSASocketListener {

    //for receive data
    private byte[] codeState = new byte[2];
    private DatagramPacket packet = new DatagramPacket(codeState, codeState.length);

    private DatagramSocket listedSocket;

    WMSASocketListener(int port) throws SocketException {
        this.listedSocket = new DatagramSocket(port);
    }

//    private void waitPacket() {
//        try {
//            listedSocket.receive(packet);
//            if (validRequest()) {
//                isRequestAvailable = true;
//            }
//        } catch (IOException e) {
//            System.out.println("Error receive data");
//        }
//    }

    private boolean validRequest() {
        if (packet.getLength() == 2) {
            for (WMSAState state : WMSAState.values()) {
                if (Arrays.equals(state.code(), codeState)) {
                    return true;
                }
            }
        }
        return false;
    }

    public DatagramPacket getPacket() {
        try {
            do {
                listedSocket.receive(packet);
            } while(!validRequest());
        } catch (IOException e) {
            System.out.println("Error receive data");
        }
        return packet;
    }

//    public synchronized boolean isRequestAvailable() {
//        return isRequestAvailable;
//    }

}
