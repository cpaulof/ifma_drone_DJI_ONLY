package edu.ifma.ifmadrone.controllers;

import android.widget.TextView;

import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointTurnMode;
import dji.common.model.LocationCoordinate2D;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import edu.ifma.ifmadrone.models.MissionModel;

public class MissionControlManager {
    WaypointMission mission;
    WaypointMission.Builder missionBuilder;
    WaypointMissionOperator operator;
    public MissionControlManager(WaypointMission mission){
        this.mission = mission;
        operator = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
    }

    public void initMission(){
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

    public void clear(){
        initMission();
    }

    public void addWaypoint(Waypoint waypoint){
        if(missionBuilder!=null){
            missionBuilder.addWaypoint(waypoint);
        }
    }

    public void createShotWaypoint(double lat, double log, float alt){
        Waypoint waypoint = new Waypoint(lat, log, alt);
        waypoint.turnMode = WaypointTurnMode.CLOCKWISE;
        waypoint.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
       addWaypoint(waypoint);
    }

    public void addCreateWaypointFromCurrentLocation(){
        Aircraft aircraft =  (Aircraft) DJISDKManager.getInstance().getProduct();
        LocationCoordinate3D loc = aircraft.getFlightController().getState().getAircraftLocation();
        createShotWaypoint(loc.getLatitude(), loc.getLongitude(), loc.getAltitude());
    }


    public WaypointMission loadMission(){
        if(missionBuilder == null) return null;
        mission = missionBuilder.build();
        operator.loadMission(mission);
        return mission;
    }
}
