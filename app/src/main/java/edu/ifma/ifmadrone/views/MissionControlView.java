package edu.ifma.ifmadrone.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dji.common.error.DJIError;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.util.CommonCallbacks;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.sdkmanager.DJISDKManager;
import edu.ifma.ifmadrone.R;
import edu.ifma.ifmadrone.controllers.MissionManager;
import edu.ifma.ifmadrone.models.MissionModel;

public class MissionControlView extends AppCompatActivity {

    private TextureView videoFeedView;
    DJICodecManager codecManager;
    WaypointMission mission;
    MissionModel missionModel;
    VideoFeeder.VideoDataListener videoDataListener;
    TextView missionState;
    Button btnLoadMission;
    WaypointMissionOperator operator = null;

    MissionManager missionManager;



    WaypointMissionOperator getOperator(){
        if(operator==null){
            operator = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
        }
        return operator;
    }

    private void updateCurrentStateLabel(){
        missionState.setText(operator.getCurrentState().getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control_view);

        videoFeedView = findViewById(R.id.textureView2);
        missionState = findViewById(R.id.missionState);
        // btnLoadMission = findViewById(R.id.btnMCLoadMission);

        missionManager = new MissionManager(getApplicationContext());
        missionModel = (MissionModel) getIntent().getSerializableExtra("MISSION");
        mission = missionManager.fromMissionModel2DJIMission(missionModel);

        init();
        operator = getOperator();
        updateCurrentStateLabel();


        operator.addListener(new WaypointMissionOperatorListener() {
            @Override
            public void onDownloadUpdate(@NonNull WaypointMissionDownloadEvent waypointMissionDownloadEvent) {
                Log.v("[WaypointMissionOperator]", "Download update index: "+waypointMissionDownloadEvent.getProgress().downloadedWaypointIndex);
            }

            @Override
            public void onUploadUpdate(@NonNull WaypointMissionUploadEvent waypointMissionUploadEvent) {
                Log.v("[WaypointMissionOperator]", "Upload current: "+waypointMissionUploadEvent.getCurrentState());
            }

            @Override
            public void onExecutionUpdate(@NonNull WaypointMissionExecutionEvent waypointMissionExecutionEvent) {
                boolean reached = waypointMissionExecutionEvent.getProgress().isWaypointReached;
                String state = waypointMissionExecutionEvent.getProgress().executeState.name();
                Log.v("[WaypointMissionOperator]", "Execution Update: "+state+" Reached:"+reached);
            }

            @Override
            public void onExecutionStart() {
                Log.v("[WaypointMissionOperator]", "Execution STARTED");
            }

            @Override
            public void onExecutionFinish(@Nullable DJIError djiError) {
                Log.v("[WaypointMissionOperator]", "Execution FINISHED");
            }
        });
    }

    private void updateButton(){
        operator = getOperator();

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

    public void loadMission(View view){
        operator = getOperator();
        DJIError error = operator.loadMission(mission);
        String msg = "Error, ";
        if(error==null){ // success
            updateCurrentStateLabel();
            msg = "MISSION LOADED";

        }else{
            msg+=error.getDescription();
        }
        Log.v("MISSION CONTROL VIEW", msg);
    }

    public void uploadMission(View view){
        operator = getOperator();
        operator.uploadMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if(djiError == null) Log.v("MISSION CONTROL VIEW", "Upload mission, error: null");
                else Log.v("MISSION CONTROL VIEW", djiError.getDescription());
            }
        });
    }

    public void pauseMission(View view){
        operator = getOperator();
        operator.pauseMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if(djiError == null) Log.v("MISSION CONTROL VIEW", "Pause mission, error: null");
                else Log.v("MISSION CONTROL VIEW", djiError.getDescription());
            }
        });
    }

    public void stopMission(View view){
        operator = getOperator();
        operator.stopMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if(djiError == null) Log.v("MISSION CONTROL VIEW", "Stop mission, error: null");
                else Log.v("MISSION CONTROL VIEW", djiError.getDescription());
            }
        });
    }

    public void startMission(View view){
        operator = getOperator();
        operator.startMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if(djiError == null) Log.v("MISSION CONTROL VIEW", "Start mission, error: null");
                else Log.v("MISSION CONTROL VIEW", djiError.getDescription());
            }
        });
    }


}