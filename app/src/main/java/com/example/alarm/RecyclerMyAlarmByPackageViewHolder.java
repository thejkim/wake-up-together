package com.example.alarm;

import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;


import androidx.recyclerview.widget.RecyclerView;


import com.example.alarm.Interface.ItemClickListener;

public class RecyclerMyAlarmByPackageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView pDesc, username, numOfAlarms;
    private ItemClickListener itemClickListener;

    public RecyclerMyAlarmByPackageViewHolder(@NonNull View itemView){
        super(itemView);
        pDesc = itemView.findViewById(R.id.myAlarms_byPackage_packageDesc_txt);
        username = itemView.findViewById(R.id.creatorUsername);
        numOfAlarms = itemView.findViewById(R.id.myAlarms_byPackage_numberOfAlarms);

        itemView.setOnClickListener(this);

        ToggleButton packageOnOffToggleBtn = itemView.findViewById(R.id.packageOnOffToggleButton);
        packageOnOffToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}
