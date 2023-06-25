package edu.ifma.ifmadrone.models;

import android.provider.BaseColumns;

public final class MissionContract {
    private MissionContract() {}
    public static class MissionEntry implements BaseColumns {

        public static final String TABLE_NAME = "mission";
        public static final String COLUMN_NAME_FLIGHT_SPEED = "auto_flight_speed";
        public static final String COLUMN_NAME_MAX_FLIGHT_SPEED = "max_flight_speed";
        public static final String COLUMN_NAME_EXIT_ON_SIGNAL_LOST = "exit_on_signal_lost";
        public static final String COLUMN_NAME_FINISHED_ACTION = "finished_action";
        public static final String COLUMN_NAME_FLIGHT_PATH_MODE = "flight_path_mode";
        public static final String COLUMN_NAME_GOTO_FIRST_WAYPOINT_MODE = "goto_first_waypoint_mode";
        public static final String COLUMN_NAME_POI = "POI";
        public static final String COLUMN_NAME_HEADING_MODE = "heading_mode";
        public static final String COLUMN_NAME_GIMBAL_PITCH_ROTATION_ENABLED = "gimbal_pitch_rotation_enabled";
        public static final String COLUMN_NAME_REPEAT_TIMES = "repeat_times";
    }

    public static class WaypointEntry implements BaseColumns {

        public static final String TABLE_NAME = "waypoint";
        public static final String COLUMN_NAME_MISSION = "mission_id";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ALTITUDE = "altitude";
        public static final String COLUMN_NAME_TURN_MODE = "turn_mode";
    }

    public static class WaypointActionEntry implements BaseColumns {

        public static final String TABLE_NAME = "waypoint_action";
        public static final String COLUMN_NAME_WAYPOINT = "waypoint_id";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_FLAG = "flag";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS "+MissionEntry.TABLE_NAME+" (" +
                    MissionEntry._ID + " INTEGER PRIMARY KEY," +
                    MissionEntry.COLUMN_NAME_FLIGHT_SPEED+" REAL," +
                    MissionEntry.COLUMN_NAME_EXIT_ON_SIGNAL_LOST+" INTEGER," +
                    MissionEntry.COLUMN_NAME_MAX_FLIGHT_SPEED+" REAL," +
                    MissionEntry.COLUMN_NAME_FINISHED_ACTION+" INTEGER," +
                    MissionEntry.COLUMN_NAME_FLIGHT_PATH_MODE+" INTEGER," +
                    MissionEntry.COLUMN_NAME_GOTO_FIRST_WAYPOINT_MODE+" INTEGER," +
                    MissionEntry.COLUMN_NAME_POI+" TEXT," +
                    MissionEntry.COLUMN_NAME_HEADING_MODE+"  INTEGER," +
                    MissionEntry.COLUMN_NAME_GIMBAL_PITCH_ROTATION_ENABLED+ "  INTEGER," +
                    MissionEntry.COLUMN_NAME_REPEAT_TIMES+"  INTEGER" +
                    ")" +
            "CREATE TABLE IF NOT EXISTS "+WaypointEntry.TABLE_NAME+ "(" +
                    WaypointEntry._ID+" INTEGER PRIMARY KEY," +
                    WaypointEntry.COLUMN_NAME_MISSION+" INTEGER," +
                    WaypointEntry.COLUMN_NAME_LATITUDE+" REAL," +
                    WaypointEntry.COLUMN_NAME_LONGITUDE+" REAL," +
                    WaypointEntry.COLUMN_NAME_ALTITUDE+" REAL," +
                    WaypointEntry.COLUMN_NAME_TURN_MODE+" INTEGER," +
                    "FOREIGN KEY("+WaypointEntry.COLUMN_NAME_MISSION+") REFERENCES "+MissionEntry.TABLE_NAME+"("+MissionEntry._ID+")" +
                    ")" +
            "CREATE TABLE IF NOT EXISTS "+WaypointActionEntry.TABLE_NAME+ "(" +
                    WaypointActionEntry._ID+" INTEGER PRIMARY KEY," +
                    WaypointActionEntry.COLUMN_NAME_WAYPOINT+" INTEGER," +
                    WaypointActionEntry.COLUMN_NAME_TYPE+" INTEGER," +
                    WaypointActionEntry.COLUMN_NAME_FLAG+" INTEGER," +
                    "FOREIGN KEY("+WaypointActionEntry.COLUMN_NAME_WAYPOINT+") REFERENCES "+WaypointEntry.TABLE_NAME+"("+WaypointEntry._ID+")" +
                    ")";

}


