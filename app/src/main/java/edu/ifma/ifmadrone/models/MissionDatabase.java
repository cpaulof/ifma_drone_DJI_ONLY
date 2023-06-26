package edu.ifma.ifmadrone.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { MissionModel.class, WaypointModel.class, WaypointActionModel.class },
        version = 4)
public abstract class MissionDatabase extends RoomDatabase {

    public abstract MissionDAO getMissionDAO();
    public abstract WaypointDAO getWaypointDAO();
    public abstract WaypointActionDAO getWaypointActionDAO();

}
