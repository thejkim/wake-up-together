package com.example.alarm.ui.Profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.alarm.databinding.FragmentProfileBinding;
import com.example.alarm.model.PackageItems;
import com.example.alarm.model.ST_Users;
import com.example.alarm.ui.Login.LoginContentActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    RecyclerView recyclerView; // the current user's packages that the user created and shared
    TextView changePasswordTV, logoutTV, deleteAccountTV, usernameTV, emailAddressTV;
    private Toast toast;
    ProfileFragment maf = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                Database db = new Database();
                db.getPackageListSharedByCurrentUser(maf, a);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTV = view.findViewById(R.id.profile_username);
        emailAddressTV = view.findViewById(R.id.profile_email);

        // Create RecyclerView
        recyclerView = view.findViewById(R.id.profile_mySharedPackagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        usernameTV.setText(ST_Users.getCurrentUsername());
        emailAddressTV.setText(ST_Users.getEmailAddress());

        changePasswordTV = view.findViewById(R.id.profile_changePW);
        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change password
                Intent changePasswordIntent = new Intent(getContext(), ChangePasswordActivity.class);
                changePasswordIntent.putExtra("argEmailAddress", emailAddressTV.getText().toString());
                startActivity(changePasswordIntent);
            }
        });
        logoutTV = view.findViewById(R.id.profile_logout);
        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log out
                toast = Toast.makeText(getContext(), "Logged out..", Toast.LENGTH_LONG);
                toast.show();

                Intent loginIntent = new Intent(getContext(), LoginContentActivity.class);
                loginIntent.putExtra("navigateTo", "signInFragment");
                startActivity(loginIntent);

            }
        });
        deleteAccountTV = view.findViewById(R.id.deleteAccount);
        deleteAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete account
                toast = Toast.makeText(getContext(), "Account is deleted.......", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return view;
    }

    public void refreshRecyclerView(ArrayList<PackageItems> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RecyclerSharedAlarmListAdapter("profileFragment", getContext(), list));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}