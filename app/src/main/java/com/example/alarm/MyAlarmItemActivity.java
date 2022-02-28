package com.example.alarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.databinding.ActivityMainBinding;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Alarms;
import com.example.alarm.model.ST_Packages;
import com.example.alarm.model.ST_Users;
import com.example.alarm.ui.MyAlarms.MyAlarmsByAlarmFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyAlarmItemActivity extends AppCompatActivity implements PackageNameInputDialog.PackageNameInputDialogListener{

    Button timeSelectBtn, packageAddBtn;
    Button[] repeatDaysBtn = new Button[7];
    EditText alarmDescTextInput;
    boolean[] repeatSelected = new boolean[7];
    ToggleButton vibrateTB, snoozeTB;
    String ampm;

    String sounds[] = {"Sound1", "Sound2", "Sound3"};
    String packageDescriptionListString[];
    List packageDescriptionList = new ArrayList<String>();
    AutoCompleteTextView soundAutoCompleteTV, packageAutoCompleteTV;
    ArrayAdapter<String> adapterSoundItems, adapterPackageItems;

    Intent intent;

    private ActivityMainBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;

    private String repeatDaysInBinaryForm = ""; // ex) 11111__ (weekdays)
    private StringBuilder stringBuilderForRepeatDays;
    private String soundStr = "";
    private boolean isVibrateSet = false;
    private boolean isSnoozeSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alarm_item);
        packageDescriptionList.add("No Package");
        ST_Packages.getInstance().getPackageItems();

        alarmDescTextInput = findViewById(R.id.alarmItemViewAddOrEdit_AlarmDescTextField);
        soundAutoCompleteTV = findViewById(R.id.soundAutoCompleteTextView);
        packageAutoCompleteTV = findViewById(R.id.packageAutoCompleteTextView);
        timeSelectBtn = findViewById(R.id.timeBtn);
        packageAddBtn = findViewById(R.id.alarmItemViewAddOrEdit_AddPackageBtn);
        vibrateTB = findViewById(R.id.vibrateTB);
        snoozeTB = findViewById(R.id.snoozeTB);

        intent = getIntent();

        adapterSoundItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, sounds);
        soundAutoCompleteTV.setAdapter(adapterSoundItems);

        soundAutoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String soundItems = parent.getItemAtPosition(position).toString();
            }
        });

        String alarmDescriptionInputStr = alarmDescTextInput.getText().toString();
        alarmDescTextInput.setText(alarmDescriptionInputStr);
        alarmDescTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });


        for(int i=0; i<ST_Packages.getInstance().getPackageItems().size(); i++) {
            packageDescriptionList.add(ST_Packages.getInstance().getPackageItems().get(i).getPackage_description());
        }

        adapterPackageItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, packageDescriptionList);
        packageAutoCompleteTV.setAdapter(adapterPackageItems);

        packageAutoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packageItems = parent.getItemAtPosition(position).toString();
            }
        });

        // use of a back btn
        ActionBar actionBar = getSupportActionBar();

        for(int i = 0; i< repeatDaysBtn.length; i++) {
            switch (i) {
                case 0: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatSunBtn); break;
                case 1: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatMonBtn); break;
                case 2: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatTueBtn); break;
                case 3: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatWedBtn); break;
                case 4: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatThuBtn); break;
                case 5: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatFriBtn); break;
                case 6: repeatDaysBtn[i] = findViewById(R.id.alarmItemViewAddOrEdit_RepeatSatBtn); break;
            }
        }

        repeatDaysInBinaryForm = "_______";
        stringBuilderForRepeatDays = new StringBuilder(repeatDaysInBinaryForm);
        for(int i = 0; i< repeatDaysBtn.length; i++) {
            int repeatDaysBtnIdx = i;
            repeatDaysBtn[repeatDaysBtnIdx].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repeatSelected[repeatDaysBtnIdx] = !repeatSelected[repeatDaysBtnIdx];
                    if(repeatSelected[repeatDaysBtnIdx]) {
                        // change background color of btn
                        repeatDaysBtn[repeatDaysBtnIdx].setBackgroundColor(getResources().getColor(R.color.blue));
                        stringBuilderForRepeatDays.setCharAt(repeatDaysBtnIdx, '1');
                    } else {
                        repeatDaysBtn[repeatDaysBtnIdx].setBackgroundColor(getResources().getColor(R.color.light_blue));
                        stringBuilderForRepeatDays.setCharAt(repeatDaysBtnIdx, '_');
                    }
                }
            });
        }

        packageAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(); // popup window to get the user input
            }
        });

        String timeStr = intent.getStringExtra("timeData");
        if(timeStr != null) { // AddBtn clicked. Add a new alarm
            timeSelectBtn.setText(timeStr);
        }

        if(intent.getStringExtra("argAlarmDesc") == null) {
            actionBar.setTitle("New Alarm");
        } else {
            actionBar.setTitle(intent.getStringExtra("argAlarmDesc"));
        }

        createNotificationChannel();
        timeSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        vibrateTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if(isChecked) {
                    isVibrateSet = true;
                } else {
                    isVibrateSet = false;
                }
            }
        });

        snoozeTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if(isChecked) {
                    isSnoozeSet = true;
                } else {
                    isSnoozeSet = false;
                }
            }
        });

        /*
            argAlarmTime
            argAlarmDesc
            argPackageDesc
            argAlarmRepeat
            argAlarmSound
            argAlarmVibrate
            argAlarmSnooze
         */

        if(!intent.getStringExtra("navigatedFrom").equals("MyAlarmsFragment")) {
            timeSelectBtn.setText(intent.getStringExtra("argAlarmTime"));
            alarmDescTextInput.setText(intent.getStringExtra("argAlarmDesc"), TextView.BufferType.EDITABLE);
            packageAutoCompleteTV.setText(intent.getStringExtra("argPackageDesc"));

            // set background color of repeatDaysBtn accordingly
            for (int i = 0; i < repeatDaysBtn.length; i++) {
                if (intent.getStringExtra("argAlarmRepeat").charAt(i) == '1') {
                    repeatDaysBtn[i].setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }

            soundAutoCompleteTV.setText(intent.getStringExtra("argAlarmSound")); //TODO:- LIST ALL OPTIONS

            if (intent.getBooleanExtra("argAlarmVibrate", false)) { //TODO:- MODIFY ARGUMENT TO PASS BOOLEAN - DONE
                vibrateTB.setChecked(true);
            } else {
                vibrateTB.setChecked(false);
            }

            if (intent.getBooleanExtra("argAlarmSnooze", false)) {
                snoozeTB.setChecked(true);
            } else {
                snoozeTB.setChecked(false);
            }
        }

    }

    public void openDialog() {
        PackageNameInputDialog inputDialog = new PackageNameInputDialog();
        inputDialog.show(getSupportFragmentManager(), "Add a new package.");
    }

    // return to the previous tab
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu); // add "ADD" btn in action bar
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        /*
            int alarm_id;
            String alarm_description;
            int alarm_package_id;
            String time;
            boolean snooze;
            String repeat;
            String sound;
            boolean vibrate;
            private boolean expandable;
         */
        //TODO:- SEPARATE CASES - ADD(DONE) OR EDIT
        int newAlarmID = ST_Alarms.getInstance().getAlarmCount() - 1;
        if(id == R.id.doneBtn) {
            // add a new alarm
            String alarmDesc = alarmDescTextInput.getText().toString();
            String timesStr = timeSelectBtn.getText().toString();
            String[] timeStrArray = timesStr.split("  "); // split between time and period(AM/PM mode)
            String time = timePicker.getHour()+":"+ String.format("%02d", timePicker.getMinute()) + " " + timeStrArray[1];

            String time2 = timeSelectBtn.getText().toString();
            String sound = soundAutoCompleteTV.getText().toString();
            int packageID = -1;
            if(packageAutoCompleteTV.getText().toString().equals("No Package") || packageAutoCompleteTV.getText().toString().equals("Select a Package")) {
                // new!!
                // add Alarm
                Alarm newAlarm = new Alarm(newAlarmID, alarmDesc, packageID, time2, isSnoozeSet, stringBuilderForRepeatDays.toString(), sound, isVibrateSet);
                // TODO:- FIX A_ID - ING
                ST_Alarms.addAlarm(newAlarm);
            } else {
                // else, package existing!!
                // add to Alarm
                // add to packageItems

                boolean isPackageExisting = false;
                if(ST_Packages.getInstance().getPackageCount()>0) { // at least a package exists to check with
                    for(int i=0; i<ST_Packages.getInstance().getPackageCount(); i++) {
                        // existing package selected. add and associate newAlarm to it.
                        if (ST_Packages.getInstance().getPackageItems().get(i).getPackage_description().equals(packageAutoCompleteTV.getText().toString())) {
                            packageID = ST_Packages.getInstance().getPackageItems().get(i).getPackage_id();
                            Alarm newAlarm = new Alarm(newAlarmID, alarmDesc, packageID, time2, isSnoozeSet, stringBuilderForRepeatDays.toString(), sound, isVibrateSet);
                            ST_Alarms.addAlarm(newAlarm);
                            ST_Packages.getInstance().getPackageItems().get(i).getAlarmList().add(newAlarm);
                            //TODO:- ADD NEW ALARM ASSOCIATING TO THE PACKAGE TO DB - DONE
                            MyAlarmItemActivity myAlarmItemActivity = this;
                            Alarm finalNewAlarm = newAlarm;
                            myAlarmItemActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                                    Database db = new Database();
                                    try {
                                        db.addNewAlarmToPackageID(finalNewAlarm, a);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            isPackageExisting = true;
                            break;
                        }
                    }
                    if(!isPackageExisting) { // At least one package exists but new package created. add new package and associate newAlarm to it.
                        packageID = ST_Packages.getInstance().getPackageCount()+1;
                        List<Alarm> newAlarmList = new ArrayList<>();
                        Alarm newAlarm = new Alarm(newAlarmID, alarmDesc, packageID, time2, isSnoozeSet, stringBuilderForRepeatDays.toString(), sound, isVibrateSet);
                        newAlarmList.add(newAlarm);

                        ST_Alarms.addAlarm(newAlarm);
                        PackageItems newPackage = new PackageItems(packageID, packageAutoCompleteTV.getText().toString(), 0, 0, ST_Users.getCurrentUserID(), ST_Users.getCurrentUsername(), newAlarmList);

                        //TODO:- ADD NEW PACKAGE AND ALARM ASSOCIATING TO THE PACKAGE TO DB - DONE
                        MyAlarmItemActivity myAlarmItemActivity = this;
                        myAlarmItemActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                                Database db = new Database();
                                try {
                                    db.addNewPackage(newPackage, newAlarm, a);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }

                } else { // no package exists. first package -> add new a package and associate newAlarm to it
                    packageID = ST_Packages.getInstance().getPackageCount()+1;
                    List<Alarm> newAlarmList = new ArrayList<>();

                    Alarm newAlarm = new Alarm(newAlarmID, alarmDesc, packageID, time2, isSnoozeSet, stringBuilderForRepeatDays.toString(), sound, isVibrateSet);
                    newAlarmList.add(newAlarm);
                    ST_Alarms.addAlarm(newAlarm); // add newAlarm to myAlarmList
                    PackageItems newPackage = new PackageItems(packageID, packageAutoCompleteTV.getText().toString(), 0, 0, ST_Users.getCurrentUserID(), ST_Users.getCurrentUsername(), newAlarmList);

                    //TODO:- ADD NEW PACKAGE AND ALARM ASSOCIATING TO THE PACKAGE TO DB
                    MyAlarmItemActivity myAlarmItemActivity = this;
                    myAlarmItemActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                            Database db = new Database();

                            try {
                                db.addNewPackage(newPackage, newAlarm, a);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }

            Toast.makeText(getBaseContext(), "Alarm is added/edited.", Toast.LENGTH_LONG).show();
            // return to previous tab //TODO:- UPDATE UI WITH UPDATED LIST
//            MyAlarmsByAlarmFragment myAlarmsByAlarmFragment = new MyAlarmsByAlarmFragment();
//            myAlarmsByAlarmFragment.refreshRecyclerView(ST_Alarms.getInstance().getAlarms());
//            Intent newIntent = new Intent(this, MyAlarmsByAlarmFragment.class);
//            newIntent.putExtra("navigatedFrom", "MyAlarmItemActivity");
//            startActivity(newIntent);
            onSupportNavigateUp();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyInputText(String packageName) {
        packageAutoCompleteTV.setText(packageName);

    }

    private void showTimePicker() {
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .build();

        timePicker.show(getSupportFragmentManager(), "pick");

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:- SHORTEN THE METHOD OF CONVERTING TIME IN AM/PM
                if (timePicker.getHour() > 12) { // PM
                    timeSelectBtn.setText( // integer, left padded with zeros up to 2 digits
                            timePicker.getHour()-12 + ":" + String.format("%02d", timePicker.getMinute()) + "  PM"
                    );
                    ampm = "PM";

                } else { // hour <= 12
                    if(timePicker.getMinute()>0) { // 12:?? PM
                        timeSelectBtn.setText(
                                timePicker.getHour() + ":" + String.format("%02d", timePicker.getMinute()) + "  AM"
                        );
                        ampm = "PM";
                    } else { // AM
                        timeSelectBtn.setText(
                                timePicker.getHour() + ":" + String.format("%02d", timePicker.getMinute()) + "  AM"
                        );
                        ampm = "AM";
                    }
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "reminderChannel";
            String description = "Chanel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channelID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}