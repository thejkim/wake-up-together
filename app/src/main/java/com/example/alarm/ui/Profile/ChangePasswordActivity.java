package com.example.alarm.ui.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {
    TextView emailAddressTV;
    TextInputLayout currentPasswordTIL, newPasswordTIL;
    TextInputEditText currentPasswordET, newPasswordET;
    Button applyBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        emailAddressTV = findViewById(R.id.emailAddressTV);
        currentPasswordTIL = findViewById(R.id.currentPasswordTIL);
        currentPasswordET = findViewById(R.id.currentPasswordET);
        newPasswordTIL = findViewById(R.id.passwordTIL);
        newPasswordET = findViewById(R.id.passwordET);
        applyBtn = findViewById(R.id.applyBtn);

        Intent intent = getIntent();
        emailAddressTV.setText(intent.getStringExtra("argEmailAddress"));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");

        currentPasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });

        currentPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String currentPasswordInput = currentPasswordET.getText().toString();
                    currentPasswordET.setText(currentPasswordInput);
                    if (currentPasswordInput.equals("asdf1234")) { // TODO:- REFACTOR
                        currentPasswordTIL.setEndIconDrawable(android.R.drawable.checkbox_on_background);
                        currentPasswordTIL.setError(null);
                        currentPasswordTIL.setHelperText("Correct password."); //TODO:- HINT? HELPER?
                        newPasswordTIL.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        currentPasswordTIL.setError("Incorrect password.");
                        currentPasswordET.setText("");
                        return false;
                    }
                }
                return false;
            }
        });

        newPasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        newPasswordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newPasswordInput = newPasswordET.getText().toString();
                    currentPasswordET.setText(newPasswordInput);
                    int minLenOfPW = 6;
                    if (newPasswordInput.matches(".*[a-zA-Z].*") && newPasswordInput.matches(".*[0-9].*") && newPasswordInput.length()>=minLenOfPW) {
                        newPasswordTIL.setHelperText("Valid password");
                        newPasswordTIL.setError(null);
                        applyBtn.setVisibility(View.VISIBLE);
                    } else if (newPasswordInput.equals("oldPassword")) { // TODO:- PREVENT FROM THE SAME PW

                    } else {
                        newPasswordTIL.setError("Invalid password. Please have at least 6 characters with at least one letter and number.");
                        newPasswordET.setText("");
                    }

                }
                return false;
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply
                // Redirect to Login Activity.
                Toast.makeText(getBaseContext(), "Password is changed.", Toast.LENGTH_LONG).show();
                finish(); // just for now, navigate up
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cancel_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.cancel) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
