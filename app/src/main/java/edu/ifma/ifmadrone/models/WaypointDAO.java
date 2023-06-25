package edu.ifma.ifmadrone.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WaypointDAO {

    @Insert
    long insert(WaypointModel waypoint);

    @Query("SELECT * from waypoints WHERE mission_id=:mission_id")
    List<WaypointModel> getWaypoints(int mission_id);
}
