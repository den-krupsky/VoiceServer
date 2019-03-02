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
            changeState();
            System.out.println("Changed state to " + state.name());
        }
        catch (IOException e) {
            System.out.println("Error receive data");
        } finally {
            state = WMCAState.NONE;
        }
    }

    private void changeState() {
        for (WMCAState state : WMCAState.values()) {
            if (Arrays.equals(state.code(), codeState)) this.state = state;
        }
    }



    public synchronized WMCAState getState() {
        return state;
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
