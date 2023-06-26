package edu.ifma.ifmadrone.controllers;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
import edu.ifma.ifmadrone.models.MissionDAO;
import edu.ifma.ifmadrone.models.MissionDatabase;
import edu.ifma.ifmadrone.models.MissionModel;
import edu.ifma.ifmadrone.models.WaypointActionDAO;
import edu.ifma.ifmadrone.models.WaypointActionModel;
import edu.ifma.ifmadrone.models.WaypointDAO;
import edu.ifma.ifmadrone.models.WaypointModel;

public class MissionManager {

    private MissionDAO missionDAO;
    private WaypointDAO waypointDAO;
    private WaypointActionDAO waypointActionDAO;

    private MissionDatabase db;

    public MissionManager(Context context){
        db = Room.databaseBuilder(context, MissionDatabase.class, "mission-control").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        missionDAO = db.getMissionDAO();
        waypointDAO = db.getWaypointDAO();
        waypointActionDAO = db.getWaypointActionDAO();

    }

    public MissionModel getMissionModel(int mission_id){
        MissionModel mission = missionDAO.getMission(mission_id);
        if(mission!=null){
            List<WaypointModel> waypoints = waypointDAO.getWaypoints(mission_id);
            for(WaypointModel w: waypoints){
                List<WaypointActionModel> actions = waypointActionDAO.getWaypointActions(w.getId());
                w.setWaypointActionModels(actions);
            }
            mission.setWaypointModels(waypoints);
        }
        return mission;
    }

    public WaypointMission fromMissionModel2DJIMission(MissionModel missionModel){
        WaypointMission.Builder missionBuilder = new WaypointMission.Builder();
        missionBuilder = new WaypointMission.Builder();
        missionBuilder.autoFlightSpeed(missionModel.getAuto_flight_speed());
        missionBuilder.maxFlightSpeed(missionModel.getMax_flight_speed());
        missionBuilder.setExitMissionOnRCSignalLostEnabled(missionModel.isExit_on_signal_lost());
        missionBuilder.finishedAction(WaypointMissionFinishedAction.find(missionModel.getFinished_action()));
        missionBuilder.flightPathMode(WaypointMissionFlightPathMode.find(missionModel.getFlight_path_mode()));
        missionBuilder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.find(missionModel.getGoto_first_waypoint_mode()));

        String[] poi = missionModel.getPOI().split(":");
        double v = Double.parseDouble(poi[0]);
        double v1 = Double.parseDouble(poi[1]);
        missionBuilder.setPointOfInterest(new LocationCoordinate2D(v, v1));

        missionBuilder.headingMode(WaypointMissionHeadingMode.find(missionModel.getHeading_mode()));
        missionBuilder.setGimbalPitchRotationEnabled(missionModel.isGimbal_pitch_rotation_enabled());
        missionBuilder.repeatTimes(missionModel.getRepeat_times());

        for(WaypointModel w: missionModel.getWaypointModels()){
            Waypoint waypoint = fromWaypointModel2DJIWaypoint(w);
            for(WaypointActionModel wa: w.getWaypointActionModels()){
                WaypointAction action = fromWaypointActionModel2DJIWaypointAction(wa);
                waypoint.addAction(action);
            }
            missionBuilder.addWaypoint(waypoint);
        }
        return missionBuilder.build();
    }
    private String convertDJILoc2D(LocationCoordinate2D loc){
        String poi = loc.getLatitude()+":"+loc.getLongitude();
        return poi;
    }
    public Waypoint fromWaypointModel2DJIWaypoint(WaypointModel w){
        Waypoint waypoint = new Waypoint(w.getLatitude(), w.getLongitude(), w.getAltitude());
        waypoint.turnMode = WaypointTurnMode.find( w.getTurn_mode());
        return waypoint;
    }
    public WaypointModel fromDJIWaypoint2WaypointModel(Waypoint w){
        WaypointModel waypoint = new WaypointModel(w.coordinate.getLatitude(), w.coordinate.getLatitude(), w.altitude);
        waypoint.setTurn_mode(w.turnMode.value());
        return waypoint;
    }

    public WaypointAction fromWaypointActionModel2DJIWaypointAction(WaypointActionModel wa){
        WaypointAction waypointAction = new WaypointAction(WaypointActionType.find(wa.getType()), wa.getParam());
        return waypointAction;
    }

    public WaypointActionModel fromDJIWaypointAction2WaypointActionModel(WaypointAction wa){
        WaypointActionModel waypointAction = new WaypointActionModel(wa.actionType.value(), wa.actionParam);
        return waypointAction;
    }

    public MissionModel fromDJIMission2MissionModel(WaypointMission mission){
        LocationCoordinate2D loc = mission.getPointOfInterest();

        MissionModel missionModel = new MissionModel(convertDJILoc2D(loc));
        missionModel.setAuto_flight_speed(mission.getAutoFlightSpeed());
        missionModel.setExit_on_signal_lost(mission.isExitMissionOnRCSignalLostEnabled());
        missionModel.setMax_flight_speed(mission.getMaxFlightSpeed());
        missionModel.setFinished_action(mission.getFinishedAction().value());
        missionModel.setFlight_path_mode(mission.getFlightPathMode().value());
        missionModel.setGoto_first_waypoint_mode(mission.getGotoFirstWaypointMode().value());
        missionModel.setHeading_mode(mission.getHeadingMode().value());
        missionModel.setGimbal_pitch_rotation_enabled(mission.isGimbalPitchRotationEnabled());
        missionModel.setRepeat_times(mission.getRepeatTimes());

        List<WaypointModel> waypointModels = new ArrayList<WaypointModel>();

        for(Waypoint waypoint: mission.getWaypointList()){
            WaypointModel wModel = fromDJIWaypoint2WaypointModel(waypoint);
            Log.v("MISSION CONTROL", " mission id:"+missionModel.getId());
            //wModel.setMission_id(missionModel.getId());
            waypointModels.add(wModel);
            List<WaypointActionModel> actionModels = new ArrayList<WaypointActionModel>();
            for(WaypointAction action: waypoint.waypointActions){
                WaypointActionModel actionModel = fromDJIWaypointAction2WaypointActionModel(action);
                //actionModel.setWaypoint_id(wModel.getId());
                actionModels.add(actionModel);
            }
            wModel.setWaypointActionModels(actionModels);
        }
        missionModel.setWaypointModels(waypointModels);

        return missionModel;
    }

    public void saveMission(WaypointMission mission){
        db.runInTransaction(new Runnable() {
            @Override
            public void run() {
                MissionModel missionModel = fromDJIMission2MissionModel(mission);
                Log.v("MISSION CONTROL", "inserting mission");
                long mission_id = missionDAO.insert(missionModel);
                Log.v("MISSION CONTROL", "_ mission - "+mission_id);
                int i = 0;
                for(WaypointModel waypoint: missionModel.getWaypointModels()){
                    waypoint.setMission_id((int)mission_id);
                    Log.v("MISSION CONTROL", "inserting waypoint: "+i);
                    Log.v("MISSION CONTROL", "Mission ids: "+missionModel.getId()+ ", "+waypoint.getMission_id());
                    Log.v("MISSION CONTROL", "Mission id from waypoint: "+missionModel.getId()+ ", "+waypoint.getMission_id());
                    long waypoint_id = waypointDAO.insert(waypoint);
                    i+=1;

                    for(WaypointActionModel action: waypoint.getWaypointActionModels()){
                        Log.v("MISSION CONTROL", "inserting waypoint action: "+i);
                        action.setWaypoint_id((int)waypoint_id);
                        waypointActionDAO.insert(action);
                    }
                }
            }
        });
    }

    public List<MissionModel> getAllMissions(){
        return missionDAO.getAll();
    }
    public List<WaypointModel> getWaypoints(int mission_id){
        return waypointDAO.getWaypoints(mission_id);
    }
}
