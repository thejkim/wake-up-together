package com.example.alarm;

import android.app.Activity;

import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Alarms;
import com.example.alarm.model.ST_Packages;
import com.example.alarm.model.ST_Users;
import com.example.alarm.ui.Login.SignUpFragment;
import com.example.alarm.ui.Profile.ProfileFragment;
import com.example.alarm.ui.SharedAlarms.SharedAlarmsFragment;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlarmListDatabaseResponseHandler implements DatabaseResponseHandler {
    @Override
    public ArrayList<Alarm> performGetAlarmListForSelectedPackage(String result) throws JSONException {
        JSONArray alarmArr = new JSONArray(result);

        ArrayList<Alarm> alarmList = new ArrayList<>();
        for(int i=0 ; i < alarmArr.length() ; i++) {
            JSONObject obj = alarmArr.getJSONObject(i);
            Alarm alarm = new Alarm(obj.getInt("AlarmID"),
                                    obj.getString("Description"),
                                    obj.getInt("PackageID"),
                                    obj.getString("Time"),
                                    obj.getInt("Snooze") == 1 ? true : false,
                                    obj.getString("RepeatDay"),
                                    obj.getString("Sound"),
                                    obj.getInt("Vibrate") == 1 ? true : false);
            alarmList.add(alarm);
        }
        return alarmList;
    }

    public ArrayList<PackageItems> performGetPopularPackageList(String result) throws JSONException {
        JSONArray packageArr = new JSONArray(result);

        ArrayList<PackageItems> packageItemsArrayList = new ArrayList<>();

        int alarmCount = 0;
        for(int i=0 ; i < packageArr.length() ; i++) {
            JSONObject obj = packageArr.getJSONObject(i);
            alarmCount = obj.getInt("AlarmCount");
            PackageItems packageItem = new PackageItems(obj.getInt("PackageID"),
                    obj.getString("Description"),
                    obj.getInt("LikeCount"),
                    obj.getInt("ShareCount"),
                    obj.getInt("UserID"),
                    obj.getString("Username"),
                    Arrays.asList(new Alarm[alarmCount]) );
            packageItemsArrayList.add(packageItem);
        }
        return packageItemsArrayList;
    }

    public ArrayList<PackageItems> performGetSearchedPackageList(String result) throws JSONException {
        JSONArray packageArr = new JSONArray(result);
        ArrayList<PackageItems> searchedPackageList = new ArrayList<>();
        int alarmCount = 0;
        for(int i=0 ; i < packageArr.length() ; i++) {
            JSONObject obj = packageArr.getJSONObject(i);
            alarmCount = obj.getInt("AlarmCount");
            PackageItems packageItem = new PackageItems(obj.getInt("PackageID"),
                    obj.getString("Description"),
                    obj.getInt("LikeCount"),
                    obj.getInt("ShareCount"),
                    obj.getInt("UserID"),
                    obj.getString("Username"),
                    Arrays.asList(new Alarm[alarmCount]) );
            searchedPackageList.add(packageItem);
        }
        return searchedPackageList;
    }

    public ArrayList<PackageItems> performGetPackageListSharedByUser(int uID, String result) throws JSONException {
        JSONArray packageArr = new JSONArray(result);

        ArrayList<PackageItems> packageItemsArrayList = new ArrayList<>();
        int alarmCount = 0;
        for(int i=0 ; i < packageArr.length() ; i++) {
            JSONObject obj = packageArr.getJSONObject(i);
            alarmCount = obj.getInt("AlarmCount");
            PackageItems packageItem = new PackageItems(obj.getInt("PackageID"),
                    obj.getString("Description"),
                    obj.getInt("LikeCount"),
                    obj.getInt("ShareCount"),
                    uID, //TODO:- ACCESS ST_USERS (CURRENT USER)
                    obj.getString("Username"),
                    Arrays.asList(new Alarm[alarmCount]));
            packageItemsArrayList.add(packageItem);

        }
        return packageItemsArrayList;
    }

    public void updateFragmentForAddingNewUser(SignUpFragment fragment) {
        fragment.redirectToSignInFragment();
    }

    public void updatePackageUserInterface(SharedAlarmsFragment fragment, ArrayList<PackageItems> list) {
        fragment.refreshRecyclerView(list);
    }

    public void updateAlarmUserInterface(PackageItemActivity act, ArrayList<Alarm> list) {
        act.refreshRecyclerView(list);
    }

    public void updateProfileUserInterface(ProfileFragment fragment, ArrayList<PackageItems> list) {
        fragment.refreshRecyclerView(list);
    }

    public void updateUserProfileUserInterface(UserProfileActivity act, ArrayList<PackageItems> list) {
        act.refreshRecyclerView(list);
    }

    public void updatePackageIDAfterAddToDB(PackageItems newPackage, Alarm newAlarm, String result) throws JSONException {
        PackageItems updatedPackage = newPackage;
        JSONArray resultArray = new JSONArray(result);
        int updatedPackageID = 0;
        for(int i=0 ; i < resultArray.length() ; i++) {
            JSONObject obj = resultArray.getJSONObject(i);
            updatedPackageID = obj.getInt("PID");
        }
        updatedPackage.setPackage_id(updatedPackageID);
        ST_Packages.addPackage(updatedPackage);

        //TODO:- PACKAGEID FOR ALARM SHOULD BE UPDATED TOO - DONE.
        ST_Alarms.getInstance().getAlarms().get(ST_Alarms.getInstance().getAlarms().size() - 1).setAlarm_package_id(updatedPackageID); // last alarm
        // OR search for max(packageID) in the alarmList
    }

    public void setCurrentUser(String result) throws JSONException {
        JSONArray packageArr = new JSONArray(result);
        for(int i=0 ; i < packageArr.length() ; i++) {
            JSONObject obj = packageArr.getJSONObject(i);
            ST_Users.setCurrentUserID(obj.getInt("UserID"));
            ST_Users.setCurrentUsername(obj.getString("Username"));
        }
    }
}
