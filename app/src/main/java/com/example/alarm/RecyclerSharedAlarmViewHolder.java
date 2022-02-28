package com.example.alarm;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Interface.ItemClickListener;

public class RecyclerSharedAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView pDesc, likedCnt, sharedCnt, alarmCnt;
    private ItemClickListener itemClickListener;

    public RecyclerSharedAlarmViewHolder(@NonNull View itemView) {
        super(itemView);
        pDesc = itemView.findViewById(R.id.package_name_txt);
        likedCnt = itemView.findViewById(R.id.liked_txt);
        sharedCnt = itemView.findViewById(R.id.shared_txt);
        alarmCnt = itemView.findViewById(R.id.alarm_count_txt);

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
