package edu.ifma.ifmadrone.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "missions")
public class MissionModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(defaultValue = "5.0")
    private float auto_flight_speed;

    @ColumnInfo(defaultValue = "10.0")
    private float max_flight_speed;

    @ColumnInfo(defaultValue = "0")
    private boolean exit_on_signal_lost;

    @ColumnInfo(defaultValue = "1")
    private int finished_action;

    @ColumnInfo(defaultValue = "0")
    private int flight_path_mode;

    @ColumnInfo(defaultValue = "0")
    private int goto_first_waypoint_mode;

    private String POI;

    @ColumnInfo(defaultValue = "4")
    private int heading_mode;

    @ColumnInfo(defaultValue = "1")
    private boolean gimbal_pitch_rotation_enabled;

    @ColumnInfo(defaultValue = "1")
    private int repeat_times;

    @Ignore
    private List<WaypointModel> waypointModels;

    public MissionModel(String POI){
        this.POI = POI;
    }

    public List<WaypointModel> getWaypointModels() {
        return waypointModels;
    }

    public void setWaypointModels(List<WaypointModel> waypointModels) {
        this.waypointModels = waypointModels;
    }

    public int getId() {
        return id;
    }

    public float getAuto_flight_speed() {
        return auto_flight_speed;
    }

    public float getMax_flight_speed() {
        return max_flight_speed;
    }

    public boolean isExit_on_signal_lost() {
        return exit_on_signal_lost;
    }

    public int getFinished_action() {
        return finished_action;
    }

    public int getFlight_path_mode() {
        return flight_path_mode;
    }

    public int getGoto_first_waypoint_mode() {
        return goto_first_waypoint_mode;
    }

    public String getPOI() {
        return POI;
    }

    public int getHeading_mode() {
        return heading_mode;
    }

    public boolean isGimbal_pitch_rotation_enabled() {
        return gimbal_pitch_rotation_enabled;
    }

    public int getRepeat_times() {
        return repeat_times;
    }

    public void setAuto_flight_speed(float auto_flight_speed) {
        this.auto_flight_speed = auto_flight_speed;
    }

    public void setMax_flight_speed(float max_flight_speed) {
        this.max_flight_speed = max_flight_speed;
    }

    public void setExit_on_signal_lost(boolean exit_on_signal_lost) {
        this.exit_on_signal_lost = exit_on_signal_lost;
    }

    public void setFinished_action(int finished_action) {
        this.finished_action = finished_action;
    }

    public void setFlight_path_mode(int flight_path_mode) {
        this.flight_path_mode = flight_path_mode;
    }

    public void setGoto_first_waypoint_mode(int goto_first_waypoint_mode) {
        this.goto_first_waypoint_mode = goto_first_waypoint_mode;
    }

    public void setPOI(String POI) {
        this.POI = POI;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHeading_mode(int heading_mode) {
        this.heading_mode = heading_mode;
    }

    public void setGimbal_pitch_rotation_enabled(boolean gimbal_pitch_rotation_enabled) {
        this.gimbal_pitch_rotation_enabled = gimbal_pitch_rotation_enabled;
    }

    public void setRepeat_times(int repeat_times) {
        this.repeat_times = repeat_times;
    }
}
