package com.example.alarm.ui.MyAlarms;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alarm.R;
import com.example.alarm.RecyclerMyAlarmListAdapter;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.ST_Alarms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAlarmsByAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAlarmsByAlarmFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    String time[], period[], pDesc[], aDesc[], repeat[];
    RecyclerMyAlarmListAdapter adapt;

    CardView cards[];

    List<Alarm> myAlarmList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int originalAlarmCount = 0;

    public MyAlarmsByAlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAlarmsByAlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAlarmsByAlarmFragment newInstance(String param1, String param2) {
        MyAlarmsByAlarmFragment fragment = new MyAlarmsByAlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ST_Alarms st_alarms = ST_Alarms.getInstance();
        myAlarmList = st_alarms.getAlarms();

        if(originalAlarmCount == st_alarms.getAlarmCount()) { // alarm has not been added
            if(readAlarmsFromFile()) { // file exists
            } else { // file not exist
                writeAlarmsToFile();
            }
        } else { // alarm has been added
            // write new alarm objects to existing file (update)
            updateFile(); //TODO:- EDIT ALARM ALSO NEEDS TO UPDATE FILE!!!
        }

    }

    //TODO:- *IMPLEMENT DELETE ON SWIPE
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    myAlarmList.remove(position);
                    adapt.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };

    public void refreshRecyclerView() {
        ST_Alarms sta = ST_Alarms.getInstance();
        myAlarmList = sta.getAlarms();
        recyclerView.setAdapter(new RecyclerMyAlarmListAdapter("myAlarmsByAlarmFragment", getContext(), myAlarmList));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_alarms_by_alarm, container, false);

        // Create RecyclerView
        recyclerView = view.findViewById(R.id.myAlarmRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        this.refreshRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    public void writeAlarmsToFile() {
        ST_Alarms st_alarms = ST_Alarms.getInstance();
        String filename = getContext().getResources().getString(R.string.alarmListFilename);

        try {
            FileOutputStream file = null;
            file = getContext().openFileOutput(filename, Context.MODE_PRIVATE);

            ObjectOutputStream output = new ObjectOutputStream(file);
            for(int i=0; i<st_alarms.getAlarms().size(); i++) {
                output.writeObject(st_alarms.getAlarms().get(i));
            }
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void updateFile() {
        ST_Alarms st_alarms = ST_Alarms.getInstance();
        String filename = getContext().getResources().getString(R.string.alarmListFilename);

        try {
            FileOutputStream file = null;
            file = getContext().openFileOutput(filename, Context.MODE_PRIVATE);

            ObjectOutputStream output = new ObjectOutputStream(file);
            for(int i=0; i<st_alarms.getAlarms().size(); i++) {
                output.writeObject(st_alarms.getAlarms().get(i));
            }
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public boolean readAlarmsFromFile() {
        ST_Alarms st_alarms = ST_Alarms.getInstance();
        String filename = getContext().getResources().getString(R.string.alarmListFilename);
        int state = 0; // false
        int objCnt = 0;
        FileInputStream file = null;
        try {
            file = getContext().openFileInput(getContext().getResources().getString(R.string.alarmListFilename));
            ObjectInputStream input = new ObjectInputStream(file);

            boolean cont = true;
            while(cont) {
                Alarm obj = (Alarm) input.readObject(); //TODO:- EOF ERROR
                if(obj != null) {
                    objCnt++;
                    st_alarms.getAlarms().add(obj);
                    state = 1; // true
                } else {
                    cont = false;
                }
            }
            originalAlarmCount = objCnt;

            input.close();
            file.close();
            state = 1; // true
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            state = 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(state == 1 ) {
            return true;
        } else {
            return false;
        }
    }

}