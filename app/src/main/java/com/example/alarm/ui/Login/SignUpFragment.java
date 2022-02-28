package com.example.alarm.ui.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.alarm.AlarmListDatabaseResponseHandler;
import com.example.alarm.Database;
import com.example.alarm.Interface.DatabaseResponseHandler;
import com.example.alarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpFragment extends Fragment {
    TextInputLayout signUp_emailAddressTIL, signUp_usernameTIL, signUp_passwordTIL;
    TextInputEditText signUp_emailAddressET, signUp_usernameET, signUp_passwordET;
    Button signUpBtn, googleBtn, signInBtn;
    Toast toast;

    FirebaseAuth firebaseAuth;
    SignUpFragment maf = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUp_emailAddressTIL = view.findViewById(R.id.signUp_emailAddressTIL);
        signUp_emailAddressET = view.findViewById(R.id.signUp_emailAddressET);
        signUp_usernameTIL = view.findViewById(R.id.signUp_usernameTIL);
        signUp_usernameET = view.findViewById(R.id.signUp_usernameET);
        signUp_passwordTIL = view.findViewById(R.id.signUp_passwordTIL);
        signUp_passwordET = view.findViewById(R.id.signUp_passwordET);
        signUpBtn = view.findViewById(R.id.signUp_signUpBtn);
        googleBtn = view.findViewById(R.id.signUp_googleBtn);
        signInBtn = view.findViewById(R.id.signUp_signInBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Sign Up");

        signUp_emailAddressET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        signUp_emailAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String emailInput = signUp_emailAddressET.getText().toString();
                    signUp_emailAddressET.setText(emailInput);
                }
                return false;
            }
        });

        signUp_usernameTIL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });

        signUp_usernameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String usernameInput = signUp_usernameET.getText().toString();
                    signUp_usernameET.setText(usernameInput);
                }
                return false;
            }
        });

        signUp_passwordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        signUp_passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String passwordInput = signUp_passwordET.getText().toString();
                    signUp_passwordET.setText(passwordInput);
                }
                return false;
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply
                toast = Toast.makeText(getContext(), "Signing Up..", Toast.LENGTH_LONG);
                toast.show();
                // stay. direct to login

                String email = signUp_emailAddressET.getText().toString().trim();
                String password = signUp_passwordET.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String firebaseUID = user.getUid();
                            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date(System.currentTimeMillis());
                            Date joinedDate =  date;
                            String dateStr = formatter.format(joinedDate);
                            String username = signUp_usernameET.getText().toString();

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                                    Database db = new Database();
                                    try {
                                        db.addNewUser(maf, firebaseUID, dateStr, username, a);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            toast = Toast.makeText(getContext(), "Signed up. Welcome!", Toast.LENGTH_LONG);
                            toast.show();

                            // direct to SignInFragment
//                            LoginContentActivity loginContentActivity = new LoginContentActivity();
//                            loginContentActivity.setContentViewToSignInFragment();

                        } else {
                            toast = Toast.makeText(getContext(), "Pleast try again.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });


            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(getContext(), "Signing up with google account..", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to Login Activity
                Intent loginIntent = new Intent(getContext(), LoginContentActivity.class);
                loginIntent.putExtra("navigateTo", "signInFragment");
                startActivity(loginIntent);
            }
        });

        return view;
    }

    public void redirectToSignInFragment() {
        LoginContentActivity loginContentActivity = new LoginContentActivity();
        loginContentActivity.setContentViewToSignInFragment();
    }
}
