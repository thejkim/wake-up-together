package com.example.alarm.ui.SharedAlarms;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedAlarmsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SharedAlarmsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}