package com.example.alarm.ui.SharedAlarms;

import android.os.Build;
import android.os.Bundle;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.AlarmListDatabaseResponseHandler;
import com.example.alarm.Database;
import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.R;
import com.example.alarm.RecyclerSharedAlarmListAdapter;
import com.example.alarm.databinding.FragmentSharedAlarmsBinding;
import com.example.alarm.model.PackageItems;

import java.util.ArrayList;
import java.util.List;

public class SharedAlarmsFragment extends Fragment {

    private FragmentSharedAlarmsBinding binding;
    String navigatedFromStr;
    SearchView searchView;
    ListView searchResultListView;

    RecyclerView recyclerView;
    // icons
    int likeIcon[] = {R.drawable.heart_favourite_love_like_icon_159300,R.drawable.heart_favourite_love_like_icon_159300,R.drawable.heart_favourite_love_like_icon_159300,R.drawable.heart_favourite_love_like_icon_159300,R.drawable.heart_favourite_love_like_icon_159300};
    int shareIcon[] = {R.drawable.share_icon,R.drawable.share_icon,R.drawable.share_icon,R.drawable.share_icon,R.drawable.share_icon};

    SharedAlarmsFragment maf = this;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                Database db = new Database();
                db.getPopularPackages(maf, a);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shared_alarms, container, false);

        // Create RecyclerView
        recyclerView = view.findViewById(R.id.sharedAlarmRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        searchResultListView = view.findViewById(R.id.searchResultListView);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //TODO:- get navigatedFrom passed by intent if available
        navigatedFromStr = "sharedAlarmsFragment";

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                        Database db = new Database();
                        db.getSearchedPackageList(query, maf, a);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<1) {

                }
                return false;
            }
        });
        return view;
    }

    public void refreshRecyclerView(ArrayList<PackageItems> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RecyclerSharedAlarmListAdapter("sharedAlarmsFragment", getContext(), list));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}