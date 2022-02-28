package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Users;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    TextView usernameTV;
    RecyclerView packageRecyclerView;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usernameTV = findViewById(R.id.user_profile_username);
        packageRecyclerView = findViewById(R.id.user_profile_mySharedPackagesRecyclerView);
        packageRecyclerView.setLayoutManager(new LinearLayoutManager(packageRecyclerView.getContext()));

        intent = getIntent();
        String usernameStr = intent.getStringExtra("argUsername");
        usernameTV.setText(usernameStr);

        UserProfileActivity userProfileActivity = this;
        if(intent.getStringExtra("navigatedFrom").equals("PackageItemActivity") || intent.getStringExtra("navigatedFrom").equals("profile")) {
            userProfileActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                    Database db = new Database();
                    db.getPackageListSharedByUserID(intent.getIntExtra("argUserID", -1), userProfileActivity, a);
                }
            });
        } else {
            userProfileActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                    Database db = new Database();
                    db.getPackageListSharedByUserID(ST_Users.getCurrentUserID(), userProfileActivity, a);
                }
            });
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(usernameStr);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void refreshRecyclerView(ArrayList<PackageItems> list) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                packageRecyclerView.setAdapter(new RecyclerSharedAlarmListAdapter("sharedAlarmsFragment", packageRecyclerView.getContext(), list));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
