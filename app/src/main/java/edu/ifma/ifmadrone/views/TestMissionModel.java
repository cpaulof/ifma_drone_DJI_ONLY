package edu.ifma.ifmadrone.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

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
import edu.ifma.ifmadrone.R;
import edu.ifma.ifmadrone.controllers.MissionManager;
import edu.ifma.ifmadrone.models.MissionModel;
import edu.ifma.ifmadrone.models.WaypointModel;

public class TestMissionModel extends AppCompatActivity {
    MissionManager missionManager;

    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mission_model);
        text = findViewById(R.id.textView3);

        missionManager = new MissionManager(this.getApplicationContext());

        // TEST saving from DJI mission builder to SQLITE
        WaypointMission.Builder missionBuilder = new WaypointMission.Builder();
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

        Waypoint waypoint1 = new Waypoint(-2.44, 44.4, 30f);
        waypoint1.turnMode = WaypointTurnMode.CLOCKWISE;
        waypoint1.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
        missionBuilder.addWaypoint(waypoint1);

        Waypoint waypoint2 = new Waypoint(-2.43, 44.3, 30f);
        waypoint2.turnMode = WaypointTurnMode.CLOCKWISE;
        waypoint2.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
        missionBuilder.addWaypoint(waypoint2);

        Waypoint waypoint3 = new Waypoint(-2.42, 44.2, 30f);
        waypoint3.turnMode = WaypointTurnMode.CLOCKWISE;
        waypoint3.addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
        missionBuilder.addWaypoint(waypoint3);

        WaypointMission mission = missionBuilder.build();
        missionManager.saveMission(mission);
    }

    public void btnClick(View view){
       // ((Button) view).setText("");
        List<MissionModel> missions = missionManager.getAllMissions();
        String s = "Total missions: "+missions.size();
        for(MissionModel mission: missions){
            s+="\n  mission: "+mission.getId();
            List<WaypointModel> waypoints = missionManager.getWaypoints((mission.getId()));
            for(WaypointModel w: waypoints){
                s+="\n    Waypoint: "+w.getId();
            }
            s+="\n";
        }
        text.setText(s);
    }
}