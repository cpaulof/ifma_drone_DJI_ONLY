package edu.ifma.ifmadrone.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

import dji.common.mission.waypoint.WaypointAction;


@Entity(tableName = "waypoint_actions",
        foreignKeys = @ForeignKey(
        entity=WaypointModel.class,
        parentColumns = "id",
        childColumns = "waypoint_id",
        onDelete = ForeignKey.CASCADE
))
public class WaypointActionModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo
    private int type;
    @ColumnInfo
    private int param;
    private int waypoint_id;
    public WaypointActionModel(int type, int param){
        this.type = type;
        this.param = param;
    }

    public static WaypointActionModel fromWaypointAction(WaypointAction action){
        return new WaypointActionModel(action.actionType.value(),action.actionParam);
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public void setWaypoint_id(int waypoint_id) {
        this.waypoint_id = waypoint_id;
    }

    public int getParam() {
        return param;
    }

    public int getWaypoint_id() {
        return waypoint_id;
    }
}
