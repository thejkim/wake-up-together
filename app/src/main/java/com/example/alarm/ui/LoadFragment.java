package com.example.alarm.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoadFragment {
    FragmentManager fragmentManager;
    public LoadFragment(FragmentManager fm) {
        this.fragmentManager = fm;
    }

    protected void initializeFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fra)
    }
}
