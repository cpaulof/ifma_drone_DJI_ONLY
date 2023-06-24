package edu.ifma.ifmadrone.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointTurnMode;
import dji.common.util.CommonCallbacks;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;

import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.model.LocationCoordinate2D;
import dji.keysdk.callback.KeyListener;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import edu.ifma.ifmadrone.R;

import static dji.keysdk.FlightControllerKey.AIRCRAFT_LOCATION;

public class CreateMission extends AppCompatActivity {

    private TextureView videoFeedView;
    private WaypointMission.Builder missionBuilder;
    DJICodecManager codecManager;
    VideoFeeder.VideoDataListener videoDataListener;

    private WaypointMissionOperator waypointMissionOperator = null;

    private double altitude  = .0;
    private double latitude  = .0;
    private double longitude = .0;

    private TextView positionLabel;
    private TextView waypointsLabel;
    private TextView loadMissionLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
        videoFeedView = findViewById(R.id.textureView);
        positionLabel = findViewById(R.id.posLabel);
        waypointsLabel = findViewById(R.id.addedWaypointsLabel);
        loadMissionLabel = findViewById(R.id.loadMissionLabel);

        init();

        initMissionBuilder();
        KeyManager.getInstance().addListener(FlightControllerKey.create(AIRCRAFT_LOCATION), new KeyListener() {
            @Override
            public void onValueChange(@Nullable Object o, @Nullable Object o1) {
                if(o1 != null){
                    LocationCoordinate3D loc = (LocationCoordinate3D) o1;
                    altitude = loc.getAltitude();
                    longitude = loc.getLongitude();
                    latitude = loc.getLatitude();
                    positionLabel.setText(getLocationString());
                }
                else if(o != null){
                    LocationCoordinate3D loc = (LocationCoordinate3D) o;
                    altitude = loc.getAltitude();
                    longitude = loc.getLongitude();
                    latitude = loc.getLatitude();
                    positionLabel.setText(getLocationString());
                }
            }
        });
    }
    private String getLocationString(){
        return "Lat:   "+latitude+"\nLong:  "+longitude+"\nAlt:   "+altitude;
    }
    private void initMissionBuilder(){
        // Inicia um novo Mission Builder. Adicionando parametros iniciais
        // OBS:clicar em Limpar tbm chama essa função, então todos os pontos
        // adicionando anteriomente serão perdidos
        missionBuilder = new WaypointMission.Builder();
        missionBuilder.autoFlightSpeed(5f);
        missionBuilder.maxFlightSpeed(10f);
        missionBuilder.setExitMissionOnRCSignalLostEnabled(false);
        missionBuilder.finishedAction(WaypointMissionFinishedAction.GO_HOME);
        missionBuilder.flightPathMode(WaypointMissionFlightPathMode.NORMAL);
        missionBuilder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY);
        missionBuilder.setPointOfInterest(new LocationCoordinate2D(15, 15));
        missionBuilder.headingMode(WaypointMissionHeadingMode.TOWARD_POINT_OF_INTEREST);
        missionBuilder.setGimbalPitchRotationEnabled(true);
        missionBuilder.repeatTimes(1);
    }

    // d
    public void onLocationClick(View view){
        Aircraft aircraft =  (Aircraft) DJISDKManager.getInstance().getProduct();
        LocationCoordinate3D loc = aircraft.getFlightController().getState().getAircraftLocation();
        TextView l = (TextView) view;
        l.setText(loc.toString());
    }

    public void onAddBtnClick(View view){
        Waypoint waypoint = new Waypoint(latitude, longitude, (float) altitude);
        waypoint.turnMode = WaypointTurnMode.CLOCKWISE;
        waypoint.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
        missionBuilder.addWaypoint(waypoint);
        int total = missionBuilder.getWaypointCount();
        waypointsLabel.setText("Total waypoints adicionados: "+total+"\n\nUltimo Waypoint:\n"+getLocationString());
    }

    public void onBtnClearClick(View view){
        missionBuilder = null;
        initMissionBuilder();
        int total = missionBuilder.getWaypointCount();
        waypointsLabel.setText("Total waypoints adicionados: "+total+"\n\n");
    }

    public void onBtnLoadClick(View view){
        waypointMissionOperator = getWaypointMissionOperator();
        DJIError error = waypointMissionOperator.loadMission(missionBuilder.build());
        String msg = "Error";
        if(error==null){ // success
            msg = "Mission Loaded!";
        }else{
            msg+=error.getDescription();
        }
        loadMissionLabel.setText(msg);

    }

    public void onBtnUploadClick(View view){
        waypointMissionOperator = getWaypointMissionOperator();
        if (WaypointMissionState.READY_TO_RETRY_UPLOAD.equals(waypointMissionOperator.getCurrentState()) || WaypointMissionState.READY_TO_UPLOAD.equals(waypointMissionOperator.getCurrentState())) {
            waypointMissionOperator.uploadMission(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    loadMissionLabel.setText("Mission Uploaded into the Aircraft!");
                }
            });
        }
        else{
            loadMissionLabel.setText("Mission is not loaded!");
        }
    }

    public void onBtnStartClick(View view){
        waypointMissionOperator = getWaypointMissionOperator();
        waypointMissionOperator.startMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if(djiError == null) loadMissionLabel.setText("Mission Started!");
                else loadMissionLabel.setText(djiError.getDescription());
            }
        });
    }

    private WaypointMissionOperator getWaypointMissionOperator() {
        if (null == waypointMissionOperator) {
            if (null != MissionControl.getInstance()) {
                return MissionControl.getInstance().getWaypointMissionOperator();
            }
        }
        return waypointMissionOperator;
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