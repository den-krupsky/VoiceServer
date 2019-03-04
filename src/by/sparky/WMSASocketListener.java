package by.sparky;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class WMSASocketListener extends Thread {

    //for receive data
    private byte[] codeState = new byte[2];
    private DatagramPacket packet = new DatagramPacket(codeState, codeState.length);

    private DatagramSocket listedSocket;
    private volatile boolean isRequestAvailable = false;
    private ReentrantLock lock = new ReentrantLock();

    {
        //init daemon thread
        this.setName("WMSARequestListener");
        this.setDaemon(true);
    }

    WMSASocketListener(int port) throws SocketException {
        this.listedSocket = new DatagramSocket(port);
    }

    private void waitPacket() {
        try {
            listedSocket.receive(packet);
            if (validRequest()) {
                isRequestAvailable = true;
            }
        } catch (IOException e) {
            System.out.println("Error receive data");
        }
    }

    private boolean validRequest() {
        if (packet.getLength() == 2) {
            for (WMCAState state : WMCAState.values()) {
                if (Arrays.equals(state.code(), codeState)) {
                    return true;
                }
            }
        }
        return false;
    }

    public DatagramPacket getPacket() {
        if (isRequestAvailable) {
            isRequestAvailable = false;
            lock.newCondition().signal();
            return packet;
        }

        lock.newCondition().signal();
        return null;
    }

    public synchronized boolean isRequestAvailable() {
        return isRequestAvailable;
    }

    @Override
    public void run() {
        while (true) {
            waitPacket();
            try {
                lock.newCondition().await();
            } catch (InterruptedException e) {
                // need log this
            }
        }
    }


}
