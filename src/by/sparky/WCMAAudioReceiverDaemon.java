package by.sparky;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class WCMAAudioReceiverDaemon extends Thread {

    private static final int AUDIO_PORT = 0;
    private static final int TIMEOUT = 3000;
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
        this.socket = new DatagramSocket(AUDIO_PORT);
        socket.setSoTimeout(TIMEOUT);
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
        this.isNewIncoming = true;
        listen();
    }

    private void listen() {
        while(true) {
            byte[] receiveData = new byte[4096];
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
            try {
                socket.receive(packet);
                this.audioPacket = packet;
                isNewIncoming = true;
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    public byte[] getAudio() {
        if (this.isNewIncoming) {
            isNewIncoming = false;
        }
        return audioPacket.getData();
    }

    public boolean isIncoming() {
        return this.isNewIncoming;
    }
}
