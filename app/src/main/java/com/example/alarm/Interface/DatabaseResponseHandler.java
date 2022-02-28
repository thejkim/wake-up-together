package com.example.alarm.Interface;

import android.app.Activity;

import com.example.alarm.MyAlarmItemActivity;
import com.example.alarm.PackageItemActivity;
import com.example.alarm.UserProfileActivity;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.ui.Login.SignUpFragment;
import com.example.alarm.ui.Profile.ProfileFragment;
import com.example.alarm.ui.SharedAlarms.SharedAlarmsFragment;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface DatabaseResponseHandler {
    public ArrayList<Alarm> performGetAlarmListForSelectedPackage(String result) throws JSONException;
    public ArrayList<PackageItems> performGetPopularPackageList(String result) throws JSONException;
    public ArrayList<PackageItems> performGetPackageListSharedByUser(int uID, String result) throws JSONException;
    public void updatePackageUserInterface(SharedAlarmsFragment fragment, ArrayList<PackageItems> list);
    public void updateAlarmUserInterface(PackageItemActivity act, ArrayList<Alarm> list);
    public void updateProfileUserInterface(ProfileFragment fragment, ArrayList<PackageItems> list);
    public void updateUserProfileUserInterface(UserProfileActivity act, ArrayList<PackageItems> list);
    public ArrayList<PackageItems> performGetSearchedPackageList(String result) throws JSONException;
    public void updateFragmentForAddingNewUser(SignUpFragment fragment);
    public void updatePackageIDAfterAddToDB(PackageItems newPackage, Alarm newAlarm, String result) throws JSONException;
    public void setCurrentUser(String result) throws JSONException;
}
