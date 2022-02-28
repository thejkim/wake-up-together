package com.example.alarm.ui.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alarm.ui.MyAlarms.MyAlarmsFragment;

public class LoginContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent.getStringExtra("navigateTo") == null) {
            setContentViewToSignInFragment();
        } else if(intent.getStringExtra("navigateTo").equals("signInFragment")) {
            setContentViewToSignInFragment();
        } else if (intent.getStringExtra("navigateTo").equals("signUpFragment")) {
            setContentViewToSignUpFragment();
        } else if(intent.getStringExtra("navigateTo").equals("myAlarmsFragment")) {
            setContentViewToMyAlarmsFragment();
        }
    }

    public void setContentViewToSignInFragment() {
        SignInFragment signInFragment = new SignInFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, signInFragment);
        fragmentTransaction.commit();
    }

    public void setContentViewToSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, signUpFragment);
        fragmentTransaction.commit();
    }

    public void setContentViewToMyAlarmsFragment() {
        MyAlarmsFragment myAlarmsFragment = new MyAlarmsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager(); // TODO:- CHANGE IMPORT TO ANDROID.APP.FRAGMENT INSTEAD FOR ALL FRAGMENT
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, myAlarmsFragment);
        fragmentTransaction.commit();
    }

}
