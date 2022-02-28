package com.example.alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Interface.ItemClickListener;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Packages;

import java.io.Serializable;
import java.util.List;

public class RecyclerMyAlarmListByPackageAdapter extends RecyclerView.Adapter<RecyclerMyAlarmByPackageViewHolder> {
    String navigatedFrom;
    List<PackageItems> myAllPackageList;
    Context context;
    public RecyclerMyAlarmListByPackageAdapter(String nf, Context ct, List<PackageItems> packageList) {
        navigatedFrom = nf;
        context = ct;
        myAllPackageList = packageList;
    }
    @NonNull
    @Override
    public RecyclerMyAlarmByPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_packages_row, parent, false);
        return new RecyclerMyAlarmByPackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMyAlarmByPackageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pDesc.setText(myAllPackageList.get(position).getPackage_description());
        holder.username.setText(String.valueOf(ST_Packages.getInstance().getPackageItems().get(position).getUsername()));
        holder.numOfAlarms.setText(String.valueOf(myAllPackageList.get(position).getAlarmList().size()) + " alarms"); // size of alarmList

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String argPackageDescription = myAllPackageList.get(position).getPackage_description();
                String argUsername = String.valueOf(myAllPackageList.get(position).getUserID());
                List<PackageItems> argPackageItemList = myAllPackageList;
                List<Alarm> argAlarmsForPackageID = myAllPackageList.get(position).getAlarmList();
                Intent intent = new Intent(context, PackageItemActivity.class);
                intent.putExtra("navigatedFrom", "myAlarmsByPackageFragment");

                // pass arguments
                intent.putExtra("argPackageDescription", argPackageDescription); // get data and put in intent
                intent.putExtra("argUsername", argUsername);
                intent.putExtra("argLikedCnt", myAllPackageList.get(position).getLikedCnt());
                intent.putExtra("argSharedCnt", myAllPackageList.get(position).getSharedCnt());
                intent.putExtra("argPackageID", myAllPackageList.get(position).getPackage_id());
                intent.putExtra("argPackageItemList", (Serializable) argPackageItemList.get(position).getAlarmList()); // PASS ALARM LIST
                intent.putExtra("argAlarmsForPackageID", (Serializable) argAlarmsForPackageID);
                context.startActivity(intent); // redirect to PackageItemActivity (package list-header only)
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("navigatedFrom", "profile");
                intent.putExtra("argUserID", myAllPackageList.get(position).getUserID());
                intent.putExtra("argUsername", myAllPackageList.get(position).getUsername());
                //TODO:- In UserProfileActivity, set actionBar title to username(get it by the passed ID 'argUserID'); - DONE
                v.getContext().startActivity(intent); // redirect to UserProfileActivity
            }
        });
    }

    @Override
    public int getItemCount() {
        return myAllPackageList.size();
    }
}
