package by.sparky;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class WMCASocketDriver implements Runnable {

    private volatile WMCAState state = WMCAState.NONE;
    private byte[] codeState = new byte[2];
    private DatagramPacket packet = new DatagramPacket(codeState, codeState.length);
    private DatagramSocket socket;

    WMCASocketDriver(DatagramSocket socket) {
        this.socket = socket;
    }

    private void waitState() {
        try {
            socket.receive(packet);
            System.out.println("Request: " + Arrays.toString(packet.getData()));
            System.out.println("Address: " + packet.getAddress().toString());
            System.out.println("My Address: " + socket.getLocalAddress().toString());
            System.out.println("Socket: " + packet.getSocketAddress().toString());
            System.out.println("Socket: " + socket.getLocalSocketAddress().toString());
            System.out.println();
            changeState();
            System.out.println("Changed state to " + state.name());
        }
        catch (IOException e) {
            state = WMCAState.NONE;
            System.out.println("Error receive data");
        }
    }

    private void changeState() {
        for (WMCAState state : WMCAState.values()) {
            if (Arrays.equals(state.code(), codeState)) this.state = state;
        }
    }



    public synchronized WMCAState getState() {
        WMCAState currentState = state;
        state = WMCAState.NONE;
        return currentState;
    }

    public synchronized InetAddress getAddress() {
        return packet.getAddress();
    }

    @Override
    public void run() {
        while(true) {
            waitState();
        }
    }
}
