package com.example.alarm;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Interface.ItemClickListener;

public class RecyclerMyAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView time, period, pDesc, aDesc, repeat;
    private ItemClickListener itemClickListener;
    RecyclerMyAlarmViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.myAlarms_byPackage_packageDesc_txt);
        period = itemView.findViewById(R.id.clock_period_txt);
        pDesc = itemView.findViewById(R.id.createdBy);
        aDesc = itemView.findViewById(R.id.alarmDesc_txt);
        repeat = itemView.findViewById(R.id.myAlarms_byPackage_numberOfAlarms);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}
