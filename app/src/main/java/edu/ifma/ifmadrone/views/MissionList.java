package edu.ifma.ifmadrone.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.ifma.ifmadrone.R;
import edu.ifma.ifmadrone.controllers.MissionManager;
import edu.ifma.ifmadrone.models.MissionModel;
import edu.ifma.ifmadrone.views.components.MissionAdapter;
import edu.ifma.ifmadrone.views.components.MissionDetail;

public class MissionList extends AppCompatActivity {

    private ListView missionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_list);

        missionListView = findViewById(R.id.missionListView);

        MissionManager missionManager = new MissionManager(getApplicationContext());
        List<MissionModel> missions = missionManager.getAllMissions();

        MissionAdapter missionAdapter = new MissionAdapter(getApplicationContext(), missions);

        missionListView.setAdapter(missionAdapter);

        missionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MissionModel mission = (MissionModel) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplicationContext(), MissionDetail.class);
                intent.putExtra("MISSION", mission);
                startActivity(intent);
            }
        });
    }
}