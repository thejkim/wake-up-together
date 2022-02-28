package com.example.alarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.StateSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Alarms;
import com.example.alarm.model.ST_Packages;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class PackageItemActivity extends AppCompatActivity {

    /*
        argPackageDescription
        argUsername
        argPackageItem
        argAlarmsForPackageID
     */

    RecyclerView recyclerView; // alarm list for packageID
    List<PackageItems> packageItemsList;
    private List<Alarm> alarmList;
    TextView packageDescriptionTV, usernameTV, likedCntTV, sharedTV;
    String navigatedFromStr;
    LinearLayout likedLayout;
    boolean isLiked, isShared;
    Intent intent;
    private Toast toast;

    String time[], aDesc[], repeat[], soundOnOff[], vibrateOnOff[], snoozeOnOff[];
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_item);

        recyclerView = findViewById(R.id.packageItem_alarmListRecyclerView);
        packageDescriptionTV = findViewById(R.id.packageItem_packageDesc);
        usernameTV = findViewById(R.id.packageItem_username);
        likedCntTV = findViewById(R.id.liked_txt);
        sharedTV = findViewById(R.id.shared_txt);
        likedLayout = findViewById(R.id.packageItem_likedLayout);

        // get data from intent in which we put data
        intent = getIntent();

        navigatedFromStr = intent.getStringExtra("navigatedFrom");

        initData();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        ArrayList selectedAlarmList = new ArrayList<Alarm>();

        if(intent.getStringExtra("navigatedFrom").equals("profileFragment")) {

            //TODO:- FIX IT!!!
            // get alarm list associated to the selected packageID
            // - FIXED.
            for (int i = 0; i < ST_Alarms.getInstance().getAlarmCount(); i++) {
                if (ST_Alarms.getInstance().getAlarms().get(i).getAlarm_package_id() == intent.getIntExtra("argPackageID", -1)) {
                    Alarm selectedAlarm = ST_Alarms.getInstance().getAlarms().get(i);
                    selectedAlarmList.add(selectedAlarm);
                }
            }
            recyclerView.setAdapter(new PackageItemAdapter(navigatedFromStr, recyclerView.getContext(), selectedAlarmList));

        } else if (intent.getStringExtra("navigatedFrom").equals("sharedAlarmsFragment")) {
            PackageItemActivity packageItemActivity = this;
            packageItemActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                    Database db = new Database();
                    db.getAlarmListForSelectedPackage(intent.getIntExtra("argPackageID",-1), intent.getIntExtra("argUserID",-1), packageItemActivity, a);
                }
            });
        } else {
            for (int i = 0; i < ST_Alarms.getInstance().getAlarmCount(); i++) {
                if (ST_Alarms.getInstance().getAlarms().get(i).getAlarm_package_id() == intent.getIntExtra("argPackageID", -1)) {
                    Alarm selectedAlarm = ST_Alarms.getInstance().getAlarms().get(i);
                    selectedAlarmList.add(selectedAlarm);
                    recyclerView.setAdapter(new PackageItemAdapter(navigatedFromStr, recyclerView.getContext(), selectedAlarmList));
                }
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intent.getStringExtra("argPackageDescription"));

        if(navigatedFromStr.equals("sharedAlarmsFragment")) {
            usernameTV.setText(intent.getStringExtra("argUsername"));
        }

        // to remove(navigated from ProfileFragment) or reveal(navigated from SharedAlarmsFragment) usernameTV
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) packageDescriptionTV.getLayoutParams();

        if(!navigatedFromStr.equals("sharedAlarmsFragment")) { // previous fragment was myAlarmsByPackageFragment or profileFragment => remove usernameTV
            usernameTV.setHeight(0);
            usernameTV.setPadding(16, 0, 16, 0);
            layoutParams.topMargin = 45;
        } else { // username should be visible and clickable
            usernameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int argUserID = intent.getIntExtra("argUserID", -1);
                    String argUsername = intent.getStringExtra("argUsername");
                    Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                    intent.putExtra("navigatedFrom", "profile"); //TODO:- NEED IT?

                    intent.putExtra("argUserID", argUserID);
                    intent.putExtra("argUsername", argUsername);

                    v.getContext().startActivity(intent);
                }
            });
        }

        // get the passed argument
        String argPackageDescription = intent.getStringExtra("argPackageDescription");
        actionBar.setTitle(argPackageDescription); // which text we get from previous activity that will set in the action bar
        // set data in view, which we get in previous activity
        packageDescriptionTV.setText(argPackageDescription);

        // Like click listener
        likedLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                int updatedLikedCnt = 0;

                DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                Database db = new Database();

                isLiked = !isLiked;
                if(isLiked) { //liked
                    // bold the font
                    likedCntTV.setTypeface(null, Typeface.BOLD);
                    // increase by 1
                    updatedLikedCnt = intent.getIntExtra("argLikedCnt", -1) + 1;
                    db.updateLikeCount("up", intent.getIntExtra("argPackageID", -1), intent.getIntExtra("argUserID", -1));

                    toast = Toast.makeText(getApplicationContext(), "You liked the package.", Toast.LENGTH_LONG);
                    toast.show();
                } else { // like canceled
                    // decrease by 1
                    updatedLikedCnt = intent.getIntExtra("argLikedCnt", -1);
                    likedCntTV.setTypeface(null, Typeface.NORMAL);
                    db.updateLikeCount("down", intent.getIntExtra("argPackageID", -1), intent.getIntExtra("argUserID", -1));

                    toast = Toast.makeText(getApplicationContext(), "You canceled to like the package.", Toast.LENGTH_LONG);
                    toast.show();
                }
                likedCntTV.setText(String.valueOf(updatedLikedCnt));
            }
        });
    }

    public void refreshRecyclerView(ArrayList<Alarm> list) {
        PackageItemActivity packageItemActivity = this;
        packageItemActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alarmList = list;
                recyclerView.setAdapter(new PackageItemAdapter(navigatedFromStr, recyclerView.getContext(), list));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() {
        if(navigatedFromStr.equals("myAlarmsByPackageFragment")) {
            // get data from intent
            alarmList = (List<Alarm>) intent.getSerializableExtra("argPackageItemList");
            likedCntTV.setText(String.valueOf(intent.getIntExtra("argLikedCnt", 0)));
            sharedTV.setText(String.valueOf(intent.getIntExtra("argSharedCnt", 0)));
        } else if(navigatedFromStr.equals("sharedAlarmsFragment")) { // Fetch packages
            alarmList = (List<Alarm>) intent.getSerializableExtra("argPackageItemList");
            likedCntTV.setText(String.valueOf(intent.getIntExtra("argLikedCnt", 0)));
            sharedTV.setText(String.valueOf(intent.getIntExtra("argSharedCnt", 0)));
        } else if(navigatedFromStr.equals("profileFragment")) {
            alarmList = (List<Alarm>) intent.getSerializableExtra("argPackageItemList");
            likedCntTV.setText(String.valueOf(intent.getIntExtra("argLikedCnt", 0)));
            sharedTV.setText(String.valueOf(intent.getIntExtra("argSharedCnt", 0)));
        } else if(navigatedFromStr.equals("userProfileActivity")){
            alarmList = (List<Alarm>) intent.getSerializableExtra("argPackageItemList");
            likedCntTV.setText(String.valueOf(intent.getIntExtra("argLikedCnt", 0)));
            sharedTV.setText(String.valueOf(intent.getIntExtra("argSharedCnt", 0)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(navigatedFromStr.equals("sharedAlarmsFragment") || navigatedFromStr.equals("profileFragment")) {
            //TODO:- DISABLE ADD BTN AFTER ADDED. -DONE
            boolean isCreatedByCurrentUser = false;
            for(int i=0; i<ST_Packages.getInstance().getPackageCount(); i++) {
                if (ST_Packages.getInstance().getPackageItems().get(i).getPackage_id() == intent.getIntExtra("argPackageID", -1)) {
                    isCreatedByCurrentUser = true;
                }
            }
            if(!isCreatedByCurrentUser) {
                getMenuInflater().inflate(R.menu.add_menu, menu);
            }
            return super.onCreateOptionsMenu(menu);
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int updatedSharedCnt = 0;

        if(id == R.id.addBtn) {
            // add package
            if(navigatedFromStr.equals("sharedAlarmsFragment")) {
                isShared = !isShared;
                if(isShared == true) {
                    sharedTV.setTypeface(null, Typeface.BOLD);

                    updatedSharedCnt = intent.getIntExtra("argSharedCnt", -1) + 1;

                    String newPDesc = intent.getStringExtra("argPackageDescription");
                    int newPID = intent.getIntExtra("argPackageID", -1);
                    int newPSharedCnt = intent.getIntExtra("argSharedCnt", 0);
                    int newPLikedCnt = intent.getIntExtra("argLikedCnt", 0);
                    int newPUID = intent.getIntExtra("argUserID", -1);
                    String newPUsername = intent.getStringExtra("argUsername");

                    // add alarm associated to the package added to ST_Alarms
                    for(int i=0; i<alarmList.size(); i++) {
                        Alarm alarmToAddToPackage = new Alarm(alarmList.get(i).getAlarm_id(), alarmList.get(i).getAlarm_description(), alarmList.get(i).getAlarm_package_id(),
                                                                alarmList.get(i).getTimeFullString(), alarmList.get(i).isSnooze(), alarmList.get(i).getRepeatString(),
                                                                alarmList.get(i).getSound(), alarmList.get(i).isVibrate());
                        ST_Alarms.addAlarm(alarmToAddToPackage);
                    }

                    // add package added to ST_Packages
                    PackageItems packageToAdd = new PackageItems(newPID, newPDesc, newPLikedCnt, newPSharedCnt, newPUID, newPUsername, alarmList);
                    ST_Packages.addPackage(packageToAdd);

                    DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                    Database db = new Database();
                    db.updateShareCount(intent.getIntExtra("argPackageID", -1), intent.getIntExtra("argUserID", -1));

                    Toast.makeText(getApplicationContext(), "Package is added.", Toast.LENGTH_LONG).show();
                } else {
                    sharedTV.setTypeface(null, Typeface.NORMAL);
                    // decrease by 1
                    updatedSharedCnt = intent.getIntExtra("argSharedCnt", -1);
                }
                sharedTV.setText(String.valueOf(updatedSharedCnt));

            } else {
                Intent intent = new Intent(getApplicationContext(), MyAlarmItemActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
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