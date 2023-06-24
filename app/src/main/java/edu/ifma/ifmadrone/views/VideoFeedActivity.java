package edu.ifma.ifmadrone.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import edu.ifma.ifmadrone.R;


public class VideoFeedActivity extends AppCompatActivity {
    private TextureView videoFeedView;
    DJICodecManager codecManager;
    VideoFeeder.VideoDataListener videoDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_feed);
        videoFeedView = findViewById(R.id.textureView);

        init();
    }

    private void init(){
        Context context = this;
        videoFeedView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener(){

            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                if(codecManager== null){
                    codecManager = new DJICodecManager(
                            context,
                            surfaceTexture,
                            i,
                            i1,
                            UsbAccessoryService.VideoStreamSource.Camera);

                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                // code
            }
            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
                //TensorImage tensorImage = TensorImage.fromBitmap(videoFeedView.getBitmap());
            }
        });
        videoDataListener = new VideoFeeder.VideoDataListener() {
            @Override
            public void onReceive(byte[] videoBuffer, int size) {
                //lastReceivedFrameTime.set(System.currentTimeMillis());

                if (codecManager != null) {
                    codecManager.sendDataToDecoder(videoBuffer,
                            size,
                            UsbAccessoryService.VideoStreamSource.Camera.getIndex());
                }
            }
        };
        VideoFeeder.VideoFeed videoFeed = VideoFeeder.getInstance().getPrimaryVideoFeed();
        videoFeed.addVideoDataListener(videoDataListener);
    }

}