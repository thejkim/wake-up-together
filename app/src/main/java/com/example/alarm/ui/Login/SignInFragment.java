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
import com.example.alarm.MainActivity;
import com.example.alarm.R;
import com.example.alarm.model.ST_Users;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class SignInFragment extends Fragment {
    TextInputLayout emailAddressTIL, passwordTIL;
    TextInputEditText emailAddressET, passwordET;
    TextView forgotPasswordTV;
    Button signInBtn, signUpBtn;
    Toast toast;

    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailAddressTIL = view.findViewById(R.id.emailAddressTIL);
        emailAddressET = view.findViewById(R.id.emailAddressET);
        passwordTIL = view.findViewById(R.id.passwordTIL);
        passwordET = view.findViewById(R.id.passwordET);
        forgotPasswordTV = view.findViewById(R.id.forgotPasswordTV);
        signInBtn = view.findViewById(R.id.signInBtn);
        signUpBtn = view.findViewById(R.id.signUpBtn);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Sign In");

        // AUTO LOGIN FOR TESTING. TODO:- ***COMMENT OUT AFTER TEST or May 2022
        // Open for others to test around the app themselves until May 2022.
        emailAddressET.setText("testuser7@gmail.com");
        passwordET.setText("testuser7");

        /* Firebase Auth*/
        firebaseAuth = FirebaseAuth.getInstance();

        emailAddressET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        emailAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String emailInput = emailAddressET.getText().toString();
                    emailAddressET.setText(emailInput);
                }
                return false;
            }
        });

        passwordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String passwordInput = passwordET.getText().toString();
                    passwordET.setText(passwordInput);
                }
                return false;
            }
        });


        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast = Toast.makeText(getContext(), "Finding password..", Toast.LENGTH_LONG);
                toast.show();
                // redirect to FindPassword Activity
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verify email, password
                // if successful,
                //      emailAddressTIL.setError(null);
                //      passwordTIL.setError(null);
                //      redirect to MyAlarmsFragment
                toast = Toast.makeText(getContext(), "Signed in.", Toast.LENGTH_LONG);
                toast.show();

                // if not
                //      emailAddressTIL.setError("Incorrect email or password.");
                //      passwordTIL.setError("Incorrect email or password.");
                //      passwordET.setText("");

                String email = emailAddressET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       Intent mainActivity = new Intent(getContext(), MainActivity.class);

                       FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                       String firebaseUserID = currentFirebaseUser.getUid();

                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               DatabaseResponseHandler a = new AlarmListDatabaseResponseHandler();
                               Database db = new Database();
                               try {
                                   db.setCurrentUser(firebaseUserID, a);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
                       ST_Users.setCurrentUserEmail(emailAddressET.getText().toString());

                       startActivity(mainActivity);
                   } else {
                       toast = Toast.makeText(getContext(), "Please try again.", Toast.LENGTH_LONG);
                       toast.show();
                   }
                });
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to SignUp Activity
                Intent signUpIntent = new Intent(getContext(), LoginContentActivity.class);
                signUpIntent.putExtra("navigateTo", "signUpFragment");
                startActivity(signUpIntent);
            }
        });
        return view;
    }
}