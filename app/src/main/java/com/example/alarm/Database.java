package com.example.alarm;

import android.content.res.Resources;
import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Users;
import com.example.alarm.ui.Login.SignInFragment;
import com.example.alarm.ui.Login.SignUpFragment;
import com.example.alarm.ui.Profile.ProfileFragment;
import com.example.alarm.ui.SharedAlarms.SharedAlarmsFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;



public class Database {
    private final OkHttpClient client = new OkHttpClient();
    private final String web_API_address = "http://whodiez.dynns.com:7770";
    private final String local = "http://10.0.2.2:7770";
    public void getAlarmListForSelectedPackage(int pID, int uID, PackageItemActivity act, DatabaseResponseHandler responseCallback) {
        final String urlStr = web_API_address + "/alarm/" + pID + "/" + uID;
        Request request = new Request.Builder()
                .url(urlStr)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String result = responseBody.string();
                    ArrayList<Alarm> alarmList = responseCallback.performGetAlarmListForSelectedPackage(result);
                    responseCallback.updateAlarmUserInterface(act, alarmList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getPopularPackages(SharedAlarmsFragment fragment, DatabaseResponseHandler responseCallback) {
        Request request = new Request.Builder()
                .url(web_API_address + "/popularPackages")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String result = responseBody.string();
                    ArrayList<PackageItems> popularPackageList = responseCallback.performGetPopularPackageList(result);
                    responseCallback.updatePackageUserInterface(fragment, popularPackageList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getSearchedPackageList(String keyword, SharedAlarmsFragment fragment, DatabaseResponseHandler responseCallback) {
        String urlStr = web_API_address + "/search/" + keyword;
        Request request = new Request.Builder()
                .url(urlStr)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String result = responseBody.string();
                    ArrayList<PackageItems> searchedPackageList = responseCallback.performGetPopularPackageList(result);
                    responseCallback.updatePackageUserInterface(fragment, searchedPackageList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getPackageListSharedByCurrentUser(ProfileFragment fragment, DatabaseResponseHandler responseCallback) {
        String urlStr = web_API_address + "/packageSharedByUser/" + ST_Users.getCurrentUserID();
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String result = responseBody.string();
                    ArrayList<PackageItems> packageListSharedByUser = responseCallback.performGetPackageListSharedByUser(ST_Users.getCurrentUserID(), result);

                    responseCallback.updateProfileUserInterface(fragment, packageListSharedByUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)  {
                e.printStackTrace();
            }
        });
    }

    public void getPackageListSharedByUserID(int uID, UserProfileActivity activity, DatabaseResponseHandler responseCallback) {
        String urlStr = web_API_address + "/packageSharedByUser/" + uID; // TODO:- ACCESS TO ST_USER (CURRENT USER)
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String result = responseBody.string();

                    ArrayList<PackageItems> packageListSharedByUser = responseCallback.performGetPackageListSharedByUser(uID, result);
                    responseCallback.updateUserProfileUserInterface(activity, packageListSharedByUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)  {
                e.printStackTrace();
            }
        });
    }

    public void addNewUser(SignUpFragment fragment, String firebaseUID, String joinedDate, String username, DatabaseResponseHandler responseCallback) throws IOException {
        String urlStr = web_API_address + "/userRegister";

        RequestBody requestBody = new FormBody.Builder()
                .add("firebaseUID", firebaseUID)
                .add("joinedDate", joinedDate)
                .add("username", username)
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        /*
         Issue: A NetworkOnMainThreadException is thrown when an app attempts to perform networking operations on the main thread.
         Solution: Completely wrap any task that attempts to perform these actions into a separate async function.
         The async function creates a new thread, and therefore, the process does not run on the UI thread.
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            else {
                responseCallback.updateFragmentForAddingNewUser(fragment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewAlarmToPackageID(Alarm newAlarm, DatabaseResponseHandler responseCallback) throws IOException {
        String urlStr = web_API_address + "/alarmAddNew";

        String alarmDesc = newAlarm.getAlarm_description();
        String repeat = newAlarm.getRepeatString();
        String sound = newAlarm.getSound();
        int vibrate = newAlarm.isVibrate() ? 1 : 0;
        int snooze = newAlarm.isSnooze() ? 1 : 0;
        int pID = newAlarm.getAlarm_package_id();
        int uID = ST_Users.getCurrentUserID();

        String time = "";
        String timeHM = newAlarm.getTimeString();
        String timeH = "";
        String timeM = "";
        String separator1 = ":";
        int sepPos = timeHM.lastIndexOf(separator1);
        timeH = timeHM.substring(0, sepPos);
        timeM = timeHM.substring(sepPos+1);

        String timePeriod = newAlarm.getTimePeriodString();
        if(timePeriod.equals("AM")) {
            time = timeH + ":" + timeM + ":00";
        } else { // PM
            int timeHToInt = Integer.parseInt(timeH);
            timeHToInt += 12;
            if(timeHToInt == 24) {
                time = "00:" + timeM + ":00";
            } else {
                time = timeHToInt + ":" + timeM + ":00";
            }
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("time", time)
                .add("alarmDesc", alarmDesc)
                .add("repeat", repeat)
                .add("sound", sound)
                .add("vibrate", String.valueOf(vibrate))
                .add("snooze", String.valueOf(snooze))
                .add("pID", String.valueOf(pID))
                .add("uID", String.valueOf(uID))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            else {
                System.out.println("New alarm successfully added to Package ID (" + pID +").");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // actionIndicator: indicate up or down
    public void updateLikeCount(String actionIndicator, int pID, int uID) {
        String urlStr = "";
        int packageID = pID;
        int userID = uID;

        if(actionIndicator.equals("up")) {
            urlStr = web_API_address + "/liked";
        } else { // down
            urlStr = web_API_address + "/likeCanceled";
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("packageID", String.valueOf(packageID))
                .add("userID", String.valueOf(userID))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            else {
                System.out.println("Update Like Count Success: " + actionIndicator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateShareCount(int pID, int uID) {
        String urlStr = web_API_address + "/share";
        int packageID = pID;
        int userID = uID;

        RequestBody requestBody = new FormBody.Builder()
                .add("packageID", String.valueOf(packageID))
                .add("userID", String.valueOf(userID))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            else {
                System.out.println("ShareCount updated.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewPackage(PackageItems newPackage, Alarm newAlarm, DatabaseResponseHandler responseCallback) throws IOException {
        String urlStr = web_API_address + "/packageAddNew";

        String pDesc = newPackage.getPackage_description();
        int uid = newPackage.getUserID(); // OR ST_USERS.getCurrentUserID

        String time = "";
        String timeHM = newAlarm.getTimeString();
        String timeH = "";
        String timeM = "";
        String separator1 = ":";
        int sepPos = timeHM.lastIndexOf(separator1);
        timeH = timeHM.substring(0, sepPos);
        timeM = timeHM.substring(sepPos+1);

        String timePeriod = newAlarm.getTimePeriodString();
        if(timePeriod.equals("AM")) {
            time = timeH + ":" + timeM + ":00";
        } else { // PM
            int timeHToInt = Integer.parseInt(timeH);
            timeHToInt += 12;
            if(timeHToInt == 24) {
                time = "00:" + timeM + ":00";
            } else {
                time = timeHToInt + ":" + timeM + ":00";
            }
        }

        String alarmDesc = newAlarm.getAlarm_description();
        boolean snooze = newAlarm.isSnooze();
        String repeat = newAlarm.getRepeatString();
        String sound = newAlarm.getSound();
        boolean vibrate = newAlarm.isVibrate();

        RequestBody requestBody = new FormBody.Builder()
                .add("pDesc", pDesc)
                .add("uid", String.valueOf(uid))
                .add("time", time)
                .add("alarmDesc", alarmDesc)
                .add("snooze", String.valueOf(snooze ? 1 : 0))
                .add("repeat", repeat)
                .add("sound", sound)
                .add("vibrate", String.valueOf(vibrate ? 1 : 0))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            else {
                System.out.println("New package added.");
                String result = response.body().string();
                responseCallback.updatePackageIDAfterAddToDB(newPackage, newAlarm, result);

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void addNewAlarmAssociatedToPackageID(int pID, int uID, Alarm alarmToAdd) {
        String urlStr = web_API_address + "/alarmAddNew";
        //Alarm(int a_id, String a_desc, int p_id, String t, boolean snz, String rpt, String s, boolean v)
        int packageID = pID;
        int userID = uID;
        String time = alarmToAdd.getTimeFullString(); //TODO:- CHECK FORMAT STORED ON DB
        String alarmDesc = alarmToAdd.getAlarm_description();
        boolean snooze = alarmToAdd.isSnooze();
        String repeat = alarmToAdd.getRepeatString();
        String sound = alarmToAdd.getSound();
        boolean vibrate = alarmToAdd.isVibrate();

        RequestBody requestBody = new FormBody.Builder()
                .add("packageID", String.valueOf(packageID))
                .add("userID", String.valueOf(userID))
                .add("time", time)
                .add("alarmDesc", alarmDesc)
                .add("snooze", String.valueOf(snooze ? 1 : 0))
                .add("repeat", repeat)
                .add("sound", sound)
                .add("vibrate", String.valueOf(vibrate ? 1 : 0))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            else {
                System.out.println("Alarm added to PackageID = " + packageID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(String firebaseID, DatabaseResponseHandler responseCallback) throws IOException {
        String urlStr = web_API_address + "/getUserByFUID/" + firebaseID; // TODO:- ACCESS TO ST_USER (CURRENT USER)
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String result = responseBody.string();
                    responseCallback.setCurrentUser(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)  {
                e.printStackTrace();
            }
        });
    }
}
