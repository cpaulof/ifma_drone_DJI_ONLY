package edu.ifma.ifmadrone.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.ifma.ifmadrone.R;

public class MainPage extends AppCompatActivity {
    // Não usado no momento (futuro MENU, com stream, missoes, classficação de objetos, etc)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    public void handleBntClick(View view){
        Intent intent = null;

        if(view.getId() == R.id.btnOpenCreateMission)
            intent = new Intent(this, CreateMission.class);
        else if(view.getId() == R.id.btnOpenDetection1)
            intent = new Intent(this, DetectionActivity.class);
        else if(view.getId() == R.id.btnOpenMissionList)
            intent = new Intent(this, MissionList.class);
        else if(view.getId() == R.id.btnOpenVideoStream)
            intent = new Intent(this, VideoFeedActivity.class);

        if(intent != null) startActivity(intent);
    }
}