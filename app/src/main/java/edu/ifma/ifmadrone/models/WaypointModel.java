package edu.ifma.ifmadrone.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

import dji.common.mission.waypoint.WaypointAction;

@Entity(tableName = "waypoints",
        foreignKeys = {@ForeignKey(entity = MissionModel.class, parentColumns = {"id"}, childColumns = {"mission_id"})})
public class WaypointModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo
    private int mission_id;
    @ColumnInfo
    private double latitude;

    @ColumnInfo
    private double longitude;

    @ColumnInfo
    private float altitude;

    @ColumnInfo(defaultValue = "0")
    private int turn_mode;

    @Ignore
    private List<WaypointActionModel> waypointActionModels;

    public WaypointModel(double latitude, double longitude, float altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public List<WaypointActionModel> getWaypointActionModels() {
        return waypointActionModels;
    }

    public void setWaypointActionModels(List<WaypointActionModel> waypointActionModels) {
        this.waypointActionModels = waypointActionModels;
    }

    public int getId() {
        return id;
    }

    public int getMission_id() {
        return mission_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public int getTurn_mode() {
        return turn_mode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public void setTurn_mode(int turn_mode) {
        this.turn_mode = turn_mode;
    }
}
