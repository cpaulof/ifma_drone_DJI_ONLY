package edu.ifma.ifmadrone.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import edu.ifma.ifmadrone.R;
import edu.ifma.ifmadrone.views.components.VideoFeedView;

import edu.ifma.detection.Detection;

public class DetectionActivity extends AppCompatActivity {
    private VideoFeedView videoFeedView;
    ByteArrayOutputStream outStream;
    Detection detection;
    ImageView imgViewOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        videoFeedView = findViewById(R.id.videoFeedView);
        imgViewOverlay = findViewById(R.id.imgViewOverlay);
        Log.v("[DETECTION ACTIVITY]", "Initialzing detection");
        detection = new Detection(imgViewOverlay, "192.168.10.11", 3535, this);

        videoFeedView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

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
                Bitmap bitmap = videoFeedView.getBitmap();
                outStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                byte[] data = outStream.toByteArray();
                int width = bitmap.getWidth();;
                int height = bitmap.getHeight();
                detection.sendImgData(data, width, height);
            }
        });

    }
}