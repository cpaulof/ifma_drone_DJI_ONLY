package edu.ifma.detection;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Client {
    private ConcurrentLinkedDeque<DetectionResult> detectionResults;
    private Socket client;
    private DataOutputStream sender;
    private DataInputStream receiver;

    private boolean ready = false;
    private boolean stop = false;

    public Client(String ipAddress, int port) {
        detectionResults = new ConcurrentLinkedDeque<>();
        try {
            Log.v("[Client]", "initializing socket");
            client = new Socket(ipAddress, port);
            sender = new DataOutputStream(client.getOutputStream());
            receiver = new DataInputStream(client.getInputStream());
            Log.v("[Client]", "handshaking");
            byte[] msg = "<<hs>>".getBytes();
            sender.writeInt(6);
            sender.write(msg, 0, 6);
            Log.v("[Client]", "receiving confirmation");
            int size = receiver.readInt();
            Log.v("[Client]", "Confirmation: "+size);

            if (size == 9) {
                byte[] data = new byte[9];
                receiver.read(data, 0, 9);

                if (compareBytes("<<ready>>".getBytes(), data, 0, 9)) {
                    ready = true;
                    Log.v("[Client]", "CONNECTED!!!");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                recv();
                            } catch (IOException ex) {
                            }
                            ;
                        }
                    }).start();
                }
            }

        } catch (IOException ex) {
            // fail
        }
    }

    public void stopRecv() {
        stop = true;
    }

    private void recv() throws IOException {
        byte[] r;
        int idx, cls;
        Log.v("[Client]", "receiving loop");
        while (ready && !stop) {
            r = new byte[2];
            receiver.read(r, 0, 2);
            idx = (int) r[1];
            cls = (int) r[0];
            DetectionResult result = new DetectionResult();
            result.index = idx;
            result._class = cls;
            Log.v("[Client]", "GOT NEW RESULT: "+idx+" cls: "+cls);
            detectionResults.add(result);
        }
    }

    public void sendImgArray(byte[] data, int width, int height) {
        try {
            //int size = width * height * 3;
            sender.writeInt(width);
            sender.writeInt(height);
            sender.writeInt(data.length);
            sender.write(data, 0, data.length);
        } catch (IOException ex) {
        }
    }

    public DetectionResult getResult() {
        DetectionResult r = new DetectionResult();
        if (detectionResults.isEmpty()) {
            return r;
        }
        return detectionResults.pop();
    }

    private boolean compareBytes(byte[] a, byte[] b, int off, int len) {
        for (int i = off; i < len; i++) {
            if (a[i] != b[i])
                return false;
        }
        return true;
    }
}

