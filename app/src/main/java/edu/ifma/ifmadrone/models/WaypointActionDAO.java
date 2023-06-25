package edu.ifma.ifmadrone.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WaypointActionDAO {
    @Insert
    long insert(WaypointActionModel waypointAction);

    @Query("SELECT * from waypoint_actions WHERE waypoint_id=:waypoint_id")
    List<WaypointActionModel> getWaypointActions(int waypoint_id);
}
