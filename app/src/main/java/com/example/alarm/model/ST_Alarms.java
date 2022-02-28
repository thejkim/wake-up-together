package com.example.alarm.model;

import java.util.ArrayList;
import java.util.List;

public class ST_Alarms {
    private static ST_Alarms instance = null;

    private static List<Alarm> alarms;
    private ST_Alarms() {
        alarms = new ArrayList<Alarm>();
    }

    public static ST_Alarms getInstance() {
        if(instance == null) {
            instance = new ST_Alarms();
        }
        return instance;
    }

    public static void addAlarm(Alarm alarm) {
        alarms.add(alarms.size(), alarm);
    }

    public void resetAlarms() {
        alarms = new ArrayList<>();
    }

    public List<Alarm> getAlarms() {
        return ST_Alarms.alarms;
    }

    public int getAlarmCount() {
        return getAlarms().size();
    }

}
