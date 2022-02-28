package com.example.alarm.ui.MyAlarms;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.alarm.AlarmListDatabaseResponseHandler;
import com.example.alarm.Database;
import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.MyAlarmItemActivity;
import com.example.alarm.R;
import com.example.alarm.RecyclerMyAlarmListAdapter;
import com.example.alarm.databinding.FragmentMyAlarmsBinding;
import com.example.alarm.model.Alarm;
import com.example.alarm.model.ST_Alarms;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MyAlarmsFragment extends Fragment {

    private MyAlarmsViewModel myAlarmsViewModel;
    private FragmentMyAlarmsBinding binding;

    TabLayout tabLayout;
    MyAlarmsViewPager viewPager;

    RecyclerMyAlarmListAdapter adapt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_alarms, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new MyAlarmsByAlarmFragment(), "By Alarm");
        viewPagerAdapter.addFragment(new MyAlarmsByPackageFragment(), "By Package");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setAllowedSwipeDirection(MyAlarmsViewPager.SwipeDirection.NONE);
        tabLayout.setupWithViewPager(viewPager);

        Button addNewAlarmBtn = view.findViewById(R.id.addBtn);
        addNewAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyAlarmItemActivity.class);
                intent.putExtra("navigatedFrom", "MyAlarmsFragment");
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}