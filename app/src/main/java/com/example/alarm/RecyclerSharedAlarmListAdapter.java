package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Interface.ItemClickListener;
import com.example.alarm.model.PackageItems;

import java.io.Serializable;
import java.util.List;

public class RecyclerSharedAlarmListAdapter extends RecyclerView.Adapter<RecyclerSharedAlarmViewHolder> {
    String navigatedFrom;
    Context context;
    List<PackageItems> packageList;

    public RecyclerSharedAlarmListAdapter(String nf, Context ct, List<PackageItems> pList) {
        navigatedFrom = nf;
        context = ct;
        packageList = pList;
    }

    @NonNull
    @Override
    public RecyclerSharedAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_alarms_row, parent, false);
        return new RecyclerSharedAlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSharedAlarmViewHolder holder, int position) {
        holder.pDesc.setText(packageList.get(position).getPackage_description());
        holder.likedCnt.setText(String.valueOf(packageList.get(position).getLikedCnt()));
        holder.sharedCnt.setText(String.valueOf(packageList.get(position).getSharedCnt()));
        holder.alarmCnt.setText(String.valueOf(packageList.get(position).getAlarmList().size()) + " alarms");

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent(context, PackageItemActivity.class);

                switch (navigatedFrom) {
                    case "sharedAlarmsFragment":
                        intent.putExtra("navigatedFrom", "sharedAlarmsFragment");
                        intent.putExtra("argPackageID", packageList.get(position).getPackage_id());
                        intent.putExtra("argPackageDescription", packageList.get(position).getPackage_description());
                        intent.putExtra("argUserID", packageList.get(position).getUserID());
                        intent.putExtra("argUsername", packageList.get(position).getUsername());
                        intent.putExtra("argLikedCnt", packageList.get(position).getLikedCnt());
                        intent.putExtra("argSharedCnt", packageList.get(position).getSharedCnt());
                        intent.putExtra("argPackageItemList", (Serializable) packageList.get(position).getAlarmList()); // PASS ALARM LIST
                        break;
                    case "profileFragment":
                        intent.putExtra("navigatedFrom", "profileFragment");
                        intent.putExtra("argPackageID", packageList.get(position).getPackage_id());
                        intent.putExtra("argPackageItemList", (Serializable) packageList.get(position).getAlarmList()); // PASS ALARM LIST
                        intent.putExtra("argPackageDescription", packageList.get(position).getPackage_description());
                        intent.putExtra("argLikedCnt", packageList.get(position).getLikedCnt());
                        intent.putExtra("argSharedCnt", packageList.get(position).getSharedCnt());
                        break;
                    case "userProfileActivity":
                        intent.putExtra("navigatedFrom", "userProfileActivity");
                        intent.putExtra("argPackageID", packageList.get(position).getPackage_id());
                        intent.putExtra("argPackageDescription", packageList.get(position).getPackage_description());
                        intent.putExtra("argUserID", packageList.get(position).getUserID());
                        intent.putExtra("argUsername", packageList.get(position).getUsername());
                        intent.putExtra("argLikedCnt", packageList.get(position).getLikedCnt());
                        intent.putExtra("argSharedCnt", packageList.get(position).getSharedCnt());
                        break;
                }

                intent.putExtra("packageList", (Serializable) packageList); // get data and put in intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }
}
