package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Interface.ItemClickListener;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.ST_Alarms;
import com.example.alarm.model.ST_Packages;

import java.util.List;

public class RecyclerMyAlarmListAdapter extends RecyclerView.Adapter<RecyclerMyAlarmViewHolder> {
    String navigatedFrom;
    List<Alarm> myAlarmList;
    Context context;
    public RecyclerMyAlarmListAdapter(String nf, Context ct, List<Alarm> alarmList) {
        navigatedFrom = nf;
        context = ct;
        myAlarmList = alarmList;
    }
    @NonNull
    @Override
    public RecyclerMyAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_alarms_row, parent, false);
        return new RecyclerMyAlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMyAlarmViewHolder holder, int position) {
        //TODO:- Substring timeStr into HH:MM and AM/PM - DONE
        holder.time.setText(myAlarmList.get(position).getTimeString());
        holder.period.setText(myAlarmList.get(position).getTimePeriodString());

        if(myAlarmList.get(position).getAlarm_package_id() < 0) { // No package
            holder.pDesc.setText(""); // empty string
        } else {
            for(int i=0; i<ST_Packages.getInstance().getPackageCount(); i++) {
                if(ST_Alarms.getInstance().getAlarms().get(position).getAlarm_package_id() == ST_Packages.getInstance().getPackageItems().get(i).getPackage_id()) {
                    holder.pDesc.setText(ST_Packages.getInstance().getPackageItems().get(i).getPackage_description() + ", ");
                }
            }
        }
        holder.aDesc.setText(myAlarmList.get(position).getAlarm_description());
        holder.repeat.setText(myAlarmList.get(position).repeatToDays());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String argTime = myAlarmList.get(position).getTimeFullString();
                String argAlarmDescription = myAlarmList.get(position).getAlarm_description();
                String argPackageDescription = "";
                if(myAlarmList.get(position).getAlarm_package_id() == -1) {
                    argPackageDescription = "No Package";
                } else {
                    argPackageDescription = String.valueOf(myAlarmList.get(position).getAlarm_package_id()); //TODO:- Get description by the pID
                }
                String argRepeatString = myAlarmList.get(position).getRepeatString();
                String argSound = myAlarmList.get(position).getSound();
                boolean argVibrate = myAlarmList.get(position).isVibrate();
                boolean argSnooze = myAlarmList.get(position).isSnooze();
                // pass argument data through intent
                Intent intent = new Intent(context, MyAlarmItemActivity.class);
                intent.putExtra("navigatedFrom", "myAlarmsFragment");
                intent.putExtra("argAlarmTime", argTime); // get data and put in intent
                intent.putExtra("argAlarmDesc", argAlarmDescription);
                intent.putExtra("argPackageDesc", argPackageDescription);
                intent.putExtra("argAlarmRepeat", argRepeatString);
                intent.putExtra("argAlarmSound", argSound);
                intent.putExtra("argAlarmVibrate", argVibrate);
                intent.putExtra("argAlarmSnooze", argSnooze);
                context.startActivity(intent); //redirect to MyAlarmItemActivity(Edit Ver.)
            }
        });
    }

    @Override
    public int getItemCount() { // # of my alarms (by alarms)
        return myAlarmList.size();
    }
}
