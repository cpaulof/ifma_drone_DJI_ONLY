package edu.ifma.ifmadrone.views.components;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import edu.ifma.ifmadrone.R;
import edu.ifma.ifmadrone.models.MissionModel;

public class MissionDetail extends AppCompatActivity {
    MissionModel mission;

    TextView missionId;
    TextView missionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);
        mission = (MissionModel) getIntent().getSerializableExtra("MISSION");

        missionId = findViewById(R.id.detailMissionId);

        missionName = findViewById(R.id.detailMissionName);
        missionId.setText(""+mission.getId());
        missionName.setText("Campus Monte Castelo (main building)");


        ((TextView) findViewById(R.id.detailMissionWaypointsCount)).setText("Waypoints: "+mission.getWaypointModels().size());
        ((TextView) findViewById(R.id.detailMissionPOI)).setText(mission.getPOI());
        ((TextView) findViewById(R.id.detailMissionAFS)).setText(mission.getAuto_flight_speed()+"KM/h");
        ((TextView) findViewById(R.id.detailMissionMFS)).setText(mission.getMax_flight_speed()+"KM/h");
        ((TextView) findViewById(R.id.detailMissionEOSL)).setText(""+mission.isExit_on_signal_lost());
        ((TextView) findViewById(R.id.detailMissionFA)).setText(
                WaypointMissionFinishedAction.find(mission.getFinished_action()).name()
        );
        ((TextView) findViewById(R.id.detailMissionFPM)).setText(
                WaypointMissionFlightPathMode.find(mission.getFlight_path_mode()).name()
        );
        ((TextView) findViewById(R.id.detailMissionGOTOFWP)).setText(
                WaypointMissionGotoWaypointMode.find(mission.getGoto_first_waypoint_mode()).name()
        );
        ((TextView) findViewById(R.id.detailMissionHM)).setText(
                WaypointMissionHeadingMode.find(mission.getHeading_mode()).name()
        );
        ((TextView) findViewById(R.id.detailMissionGPRE)).setText(""+mission.isGimbal_pitch_rotation_enabled());
        ((TextView) findViewById(R.id.detailMissionRT)).setText(""+mission.getRepeat_times());
    }
}