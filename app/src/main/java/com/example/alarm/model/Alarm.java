package com.example.alarm.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Alarm implements Serializable {
    int alarm_id;
    String alarm_description;
    int alarm_package_id;
    String time;
    boolean snooze;
    String repeat;
    String sound;
    boolean vibrate;
    private boolean expandable;
    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Alarm(int a_id, String a_desc, int p_id, String t, boolean snz, String rpt, String s, boolean v) {
        alarm_id = a_id;
        alarm_description = a_desc;
        alarm_package_id = p_id;
        time = t;
        snooze = snz;
        repeat = rpt;
        sound = s;
        vibrate = v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Alarm() {
        alarm_id = 1;
        alarm_description = "alarm description";
        alarm_package_id = 1;
        time = "7:00";
        snooze = true;
        repeat = "11111__"; // weekdays
        sound = "Sound1";
        vibrate = true;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public String getAlarm_description() {
        return alarm_description;
    }

    public int getAlarm_package_id() {
        return alarm_package_id;
    }

    public String getTimeFullString() {
        return time;
    }

    public String getTimeString() {
        String timesStr = time;
        String[] timeStrArray = timesStr.split("\\s+");
        return timeStrArray[0];
    }

    public String getTimePeriodString() {
        String timesStr = time;
        String[] timeStrArray = timesStr.split("\\s+");
        return timeStrArray[1];
    }

    public boolean isSnooze() {
        return snooze;
    }

    public String getRepeatString() {
        return repeat;
    }

    public String getSound() {
        return sound;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public String dateToString(Date time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(time);
    }

    public String repeatToDays() {
        String repeatConvertedStr = "";
        Boolean[] isRepeated = new Boolean[repeat.length()];
        for(int j=0; j<repeat.length(); j++) {
            isRepeated[j] = false;
            if (repeat.charAt(j) == '1') {
                switch (j) {
                    case 0:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Sun ";
                        break;
                    case 1:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Mon ";
                        break;
                    case 2:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Tue ";
                        break;
                    case 3:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Wed ";
                        break;
                    case 4:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Thu ";
                        break;
                    case 5:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Fri ";
                        break;
                    case 6:
                        isRepeated[j] = true;
                        repeatConvertedStr += "Sat ";
                        break;
                }
            }
        }
        if(isRepeated[1]&&isRepeated[2]&&isRepeated[3]&&isRepeated[4]&&isRepeated[5]&&!isRepeated[0]&&!isRepeated[6]) {
            repeatConvertedStr = "Weekdays";
        } else if(!isRepeated[1]&&!isRepeated[2]&&!isRepeated[3]&&!isRepeated[4]&&!isRepeated[5]&&isRepeated[0]&&isRepeated[6]) {
            repeatConvertedStr = "Weekend";
        } else if(isRepeated[1]&&isRepeated[2]&&isRepeated[3]&&isRepeated[4]&&isRepeated[5]&&isRepeated[0]&&isRepeated[6]) {
            repeatConvertedStr = "Everyday";
        } else if (!isRepeated[0]&&!isRepeated[1]&&!isRepeated[2]&&!isRepeated[3]&&!isRepeated[4]&&!isRepeated[5]&&!isRepeated[6]) {
            repeatConvertedStr = "Tomorrow";
        }
        return repeatConvertedStr;
    }

    public String convertTime(String argTimeStr) {
        String timeHourStr = argTimeStr.substring(0, 2);
        String timeMinuteStr = argTimeStr.substring(3,5);

        int timeHourStrToInt = Integer.parseInt(timeHourStr);
        if(timeHourStrToInt > 12) {
            timeHourStr = String.valueOf(timeHourStrToInt - 12);
        }
        return timeHourStr + ":" + timeMinuteStr;
    }

    public String updateTimePeriod(String argTimeStr) {
        if(Integer.parseInt(argTimeStr.substring(0, 2)) >= 12) {
            return "PM";
        } else {
            return "AM";
        }
    }

    public void setAlarm_package_id(int id) {
        this.alarm_package_id = id;
    }

}
