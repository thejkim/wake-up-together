package com.example.alarm.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PackageItems implements Serializable {
    int package_id;
    String package_description;
    int likedCnt;
    int sharedCnt;
    int userID;
    String username;
    List<Alarm> alarmList = new ArrayList<Alarm>();
    private boolean expandable;

    public PackageItems(int package_id, String package_description, int likedCnt, int sharedCnt, int userID, String username, List<Alarm> alarmList) {
        this.package_id = package_id;
        this.package_description = package_description;
        this.likedCnt = likedCnt;
        this.sharedCnt = sharedCnt;
        this.userID = userID;
        this.username = username;
        this.alarmList = alarmList;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public int getPackage_id() {
        return package_id;
    }

    public String getPackage_description() {
        return package_description;
    }

    public int getLikedCnt() {
        return likedCnt;
    }

    public int getSharedCnt() {
        return sharedCnt;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() { return username; }

    public List<Alarm> getAlarmList() {
        return alarmList;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public void setPackage_description(String package_description) {
        this.package_description = package_description;
    }

    public void setLikedCnt(int likedCnt) {
        this.likedCnt = likedCnt;
    }

    public void setSharedCnt(int sharedCnt) {
        this.sharedCnt = sharedCnt;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) { this.username = username; }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

}
