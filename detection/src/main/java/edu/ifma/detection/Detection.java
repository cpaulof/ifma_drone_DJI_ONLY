package edu.ifma.detection;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

public class Detection {
    private Client client;
    private Overlay overlay;

    public boolean overlayRunning;
    private Activity app;

    public Detection(ImageView img, String serverAddr, int port, Activity app){
        overlayRunning = true;
        this.app = app;
        Log.v("[DETECTION]", "Initialzing Overlay");
        overlay = new Overlay(img);
        Log.v("[DETECTION ACTIVITY]", "Initialzing Client");
        client = new Client(serverAddr, port);
        Log.v("[DETECTION ACTIVITY]", "Initialzing RECV THREAD");
        Thread t = new DetectionThread(this);
        t.start();
    }

    public void startOverlay(){
        overlayRunning = true;
    }

    public void stopOverlay(){
        overlayRunning = false;
    }

    public void sendImgData(byte[] data, int width, int height){
        client.sendImgArray(data, width, height);
    }

    public void detection(){
        DetectionResult result = client.getResult();
        Log.v("DETECTION", "result class: "+result._class+" index: "+result.index);
        if(result.index>=0){
            if(result._class == 0)
                overlay.drawNegative(0.95f);
            else
                overlay.drawPositive(0.84f);

            overlay.updateView(app);
        }
    }
}

class DetectionThread extends Thread{
    Detection detection;
    public DetectionThread(Detection det){

        detection = det;
        Log.v("[DETECTION]", "Creating new Detection Thread");
    }
    @Override
    public void run(){
        while(true){
            try{
                if(detection.overlayRunning) detection.detection();
                sleep(200);
            } catch(InterruptedException e){
                Log.v("[DETECTION]", e.getMessage());
                detection.stopOverlay();
                return;
            }
        }
    }
}
