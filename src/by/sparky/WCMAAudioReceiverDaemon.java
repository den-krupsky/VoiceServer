package by.sparky;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public class WCMAAudioReceiverDaemon extends Thread {



    private static final int TIMEOUT = 3000;
    private final int audioPort;
    private DatagramSocket socket;
    private AudioFormat audioFormat;
    private SourceDataLine sourceDataLine;

    //audio raw
    private byte[] audioBytes;
    private DatagramPacket audioPacket;

    private boolean isNewIncoming;

    {
        //init daemon thread
        this.setName("WCMAAudioReceiverDaemon");
        this.setDaemon(true);
    }

    WCMAAudioReceiverDaemon() throws SocketException {
        socket = new DatagramSocket(0);
        socket.setSoTimeout(TIMEOUT);
        audioPort = socket.getLocalPort();
    }

    //magic
    private void initAudio() {
        this.audioFormat = new AudioFormat(44100.0F, 16, 1, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            this.sourceDataLine.open(audioFormat);
            this.sourceDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initAudio();
        listen();
    }

    private void listen() {
        while(true) {
            byte[] receiveData = new byte[4096];
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
            try {
                socket.receive(packet);
                System.out.println("AudioPacket is getting by Daemon Thread");
                this.audioPacket = packet;

                sourceDataLine.write(packet.getData(), 0, packet.getLength());
                isNewIncoming = true;
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    public byte[] getAudio() {
        if (isNewIncoming) isNewIncoming = false;
        return audioPacket.getData();
    }

    public boolean isIncoming() {
        return isNewIncoming;
    }

    public int getReceiverPort() {
        return audioPort;
    }
}
