package edu.ifma.ifmadrone.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MissionDAO {

    @Insert
    long insert(MissionModel mission);

    @Query("SELECT * from missions where id=:mission_id")
    MissionModel getMission(int mission_id);

    @Query("SELECT * from missions")
    List<MissionModel> getAll();

}
