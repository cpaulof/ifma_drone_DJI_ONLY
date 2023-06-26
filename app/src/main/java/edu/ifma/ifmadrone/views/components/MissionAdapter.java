package edu.ifma.ifmadrone.views.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.ifma.ifmadrone.R;
import edu.ifma.ifmadrone.models.MissionModel;

public class MissionAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<MissionModel> missions;
    Context context;

    public MissionAdapter(Context context, List<MissionModel> missions){
        this.context = context;
        this.missions = missions;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return missions.size();
    }

    @Override
    public Object getItem(int i) {
        return missions.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(missions.get(i)!=null) return missions.get(i).getId();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_mission_list_view, null);
        TextView missionText = (TextView) view.findViewById(R.id.missionId);
        MissionModel mission = (MissionModel) getItem(i);
        missionText.setText("Mission:"+ mission.getId());
        return view;
    }
}
