package com.example.alarm.ui.MyAlarms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAlarmsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyAlarmsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}