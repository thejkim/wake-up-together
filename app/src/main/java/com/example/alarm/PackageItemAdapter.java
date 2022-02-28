package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;

import java.util.List;

public class PackageItemAdapter extends RecyclerView.Adapter<PackageItemAdapter.PackageItemViewHolder>{

    List<Alarm> alarmList;
    Context context;
    String navigatedFrom;

    public PackageItemAdapter(String nf, Context ct, List<Alarm> alarmList) {
        navigatedFrom = nf;
        context = ct;
        this.alarmList = alarmList;
    }


    @NonNull
    @Override
    public PackageItemAdapter.PackageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.packageitem_alarms_row, parent, false);
        return new PackageItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageItemAdapter.PackageItemViewHolder holder, int position) {
        holder.time.setText(alarmList.get(position).getTimeString() + " " + alarmList.get(position).getTimePeriodString());
        holder.aDesc.setText(alarmList.get(position).getAlarm_description());
        holder.repeat.setText(alarmList.get(position).repeatToDays());
        holder.sound.setText(alarmList.get(position).getSound());
        if(alarmList.get(position).isVibrate()) {
            holder.vibrate.setText("ON");
        } else {
            holder.vibrate.setText("OFF");
        }
        if(alarmList.get(position).isSnooze()) {
            holder.snooze.setText("ON");
        } else {
            holder.snooze.setText("OFF");
        }
        boolean isExpandable = alarmList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class PackageItemViewHolder extends RecyclerView.ViewHolder {
        TextView time, aDesc, repeat, sound, vibrate, snooze;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public PackageItemViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.packageItem_alarm_time);
            aDesc = itemView.findViewById(R.id.packageItem_alarm_desc);
            repeat = itemView.findViewById(R.id.packageItem_alarm_repeat);
            sound = itemView.findViewById(R.id.packageItem_alarm_sound_onOff);
            vibrate = itemView.findViewById(R.id.packageItem_alarm_vibrate_onOff);
            snooze = itemView.findViewById(R.id.packageItem_alarm_snooze_onOff);

            linearLayout = itemView.findViewById(R.id.packageitem_linearLayout);
            expandableLayout = itemView.findViewById(R.id.packageitem_expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alarm alarm = alarmList.get(getAbsoluteAdapterPosition());
                    alarm.setExpandable(!alarm.isExpandable());
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });

            TextView alarmEditBtn = (TextView) itemView.findViewById(R.id.packageItem_alarm_edit);
            if(navigatedFrom.equals("sharedAlarmsFragment")) {
                alarmEditBtn.setHeight(0);
            } else {
                //TODO:- IF SELECTED PACKAGE IS CREATED BY THE CURRENT USER, REVEAL THE EDIT BTN
                //      ELSE, HIDE IT.
            }
            alarmEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyAlarmItemActivity.class);
                    intent.putExtra("navigatedFrom", navigatedFrom);
                    intent.putExtra("argAlarmTime", alarmList.get(getAbsoluteAdapterPosition()).getTimeString());
                    intent.putExtra("argAlarmDesc", alarmList.get(getAbsoluteAdapterPosition()).getAlarm_description());
                    intent.putExtra("argPackageDesc", intent.getStringExtra("argPackageDescription"));
                    intent.putExtra("argAlarmRepeat", alarmList.get(getAbsoluteAdapterPosition()).getRepeatString());
                    intent.putExtra("argAlarmSound", alarmList.get(getAbsoluteAdapterPosition()).getSound());
                    intent.putExtra("argAlarmVibrate", alarmList.get(getAbsoluteAdapterPosition()).isVibrate());
                    intent.putExtra("argAlarmSnooze", alarmList.get(getAbsoluteAdapterPosition()).isSnooze());
                    context.startActivity(intent);
                }
            });
        }
    }

}
