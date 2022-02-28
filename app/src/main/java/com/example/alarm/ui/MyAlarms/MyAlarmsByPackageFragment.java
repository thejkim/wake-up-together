package com.example.alarm.ui.MyAlarms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alarm.AlarmListDatabaseResponseHandler;
import com.example.alarm.Database;
import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.R;
import com.example.alarm.RecyclerMyAlarmListAdapter;
import com.example.alarm.RecyclerMyAlarmListByPackageAdapter;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Alarms;
import com.example.alarm.model.ST_Packages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAlarmsByPackageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAlarmsByPackageFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    String pDesc[], creatorUsername[], numOfAlarms[];
    RecyclerMyAlarmListByPackageAdapter adapt;

    // package list created by the current user & shared by others (which the current user added)
    private List<PackageItems> myAllPackageList;
    private List<Alarm> alarmListForPackageID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int originalAlarmCount = 0;

    public MyAlarmsByPackageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAlarmsByPackage.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAlarmsByPackageFragment newInstance(String param1, String param2) {
        MyAlarmsByPackageFragment fragment = new MyAlarmsByPackageFragment();
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

        setHasOptionsMenu(true);

        ST_Packages st_packages = ST_Packages.getInstance();
        myAllPackageList = st_packages.getPackageItems();

        if(originalAlarmCount == st_packages.getPackageCount()) {
            if(readPackagesFromFile()) { // file exists
            } else { // file not exist
                writePackagesToFile();
            }
        } else { // alarm has been added
            // write new alarm objects to existing file (update)
            updatePackageFile(); //TODO:- EDIT ALARM ALSO NEEDS TO UPDATE FILE!!!
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_alarms_by_package, container, false);

        // Create RecyclerView
        recyclerView = view.findViewById(R.id.myAlarmsByPackageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        this.refreshRecyclerView();

        return view;
    }

    public void refreshRecyclerView( ) {
        ST_Packages st_packages = ST_Packages.getInstance();
        myAllPackageList = st_packages.getPackageItems();
        recyclerView.setAdapter(new RecyclerMyAlarmListByPackageAdapter("myAlarmsByPackageFragment", getContext(), myAllPackageList));
    }

    public void writePackagesToFile() {
        ST_Packages st_packages = ST_Packages.getInstance();
        String filename = getContext().getResources().getString(R.string.packageListFilename);

        try {
            FileOutputStream file = null;
            file = getContext().openFileOutput(filename, Context.MODE_PRIVATE);

            ObjectOutputStream output = new ObjectOutputStream(file);
            for(int i=0; i<st_packages.getPackageItems().size(); i++) {
                output.writeObject(st_packages.getPackageItems().get(i));
            }
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void updatePackageFile() {
        ST_Packages st_packages = ST_Packages.getInstance();
        String filename = getContext().getResources().getString(R.string.packageListFilename);

        try {
            FileOutputStream file = null;
            file = getContext().openFileOutput(filename, Context.MODE_PRIVATE);

            ObjectOutputStream output = new ObjectOutputStream(file);
            for(int i=0; i<st_packages.getPackageItems().size(); i++) {
                output.writeObject(st_packages.getPackageItems().get(i));
            }
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public boolean readPackagesFromFile() {
        ST_Packages st_packages = ST_Packages.getInstance();
        int state = 0; // false
        int objCnt = 0;
        FileInputStream file = null;
        try {
            file = getContext().openFileInput(getContext().getResources().getString(R.string.packageListFilename));
            ObjectInputStream input = new ObjectInputStream(file);

            boolean cont = true;
            while(cont) {
                PackageItems obj = (PackageItems) input.readObject();
                if(obj != null) {
                    objCnt++;
                    st_packages.getPackageItems().add(obj);
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