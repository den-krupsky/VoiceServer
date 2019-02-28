package by.sparky;

import javax.sound.sampled.AudioFormat;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Date;

public class MicListener {
    DatagramSocket serverSocket;
    DatagramSocket receiverSocket;
    File tempFile;
    DataOutputStream dataOutputStream;
    AudioFormat audioFormat;

    private void init() throws IOException {
        this.serverSocket = new DatagramSocket(0);
        receiverSocket.setSoTimeout(3000);
        this.tempFile = new File(new Date().toString() + "_temp.wav");
        this.tempFile.deleteOnExit();
        //this.dataOutputStream = new DataOutputStream(new FileOutputStream());
        this.audioFormat = new AudioFormat(44100.0F, 16, 1, true, false);

    }
}
