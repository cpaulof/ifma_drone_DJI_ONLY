package edu.ifma.ifmadrone.views.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;

import dji.midware.usb.P3.UsbAccessoryService;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import edu.ifma.ifmadrone.R;

/**
 * TODO: document your custom view class.
 */
public class VideoFeedView extends TextureView {
    DJICodecManager codecManager;
    VideoFeeder.VideoDataListener videoDataListener;
    public VideoFeedView(@NonNull Context context, AttributeSet attrs){
        super(context, attrs);
        Log.v("[VIDEO FEED VIEW", "initiating 2");
        //setup(context);
    }

    public void setup(Context context){
        this.setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                if(codecManager== null){
                    codecManager = new DJICodecManager(
                            context,
                            surfaceTexture,
                            i,
                            i1,
                            UsbAccessoryService.VideoStreamSource.Camera);

//                    codecManager.getBitmap(new DJICodecManager.OnGetBitmapListener() {
//                        @Override
//                        public void onGetBitmap(Bitmap bitmap) {
//                            Log.v("[VIDEO FEED VIEW]", "WIDTH: "+bitmap.getWidth()+" HEIGHT:"+bitmap.getHeight()+" Size:"+bitmap.getByteCount());
//                        }
//                    });

                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {


            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        });

        videoDataListener = new VideoFeeder.VideoDataListener() {
            @Override
            public void onReceive(byte[] videoBuffer, int size) {
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
    public VideoFeedView(@NonNull Context context) {
        super(context);
        Log.v("[VIDEO FEED VIEW", "initiating");
        setup(context);



    }


}