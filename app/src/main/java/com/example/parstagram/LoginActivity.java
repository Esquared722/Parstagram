package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.parstagram.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                signUpUser(username, password);
            }
        });
    }

    private void signUpUser(String username, String password) {
        Log.i(TAG, "Attempting to sign up user " + username);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with sign up", e);
                    String error_message = "";
                    switch(e.getCode()) {
                        case 202:
                            error_message += String.format(getString(R.string.signup_error_username_taken), username);
                            break;
                        default:
                            error_message += getString(R.string.parse_unknown_error);
                    }
                    if (!error_message.isEmpty()) {
                        Snackbar.make(binding.btnSignUp, error_message, Snackbar.LENGTH_LONG).show();
                    }
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success in signup!\nLogging in new user...", Toast.LENGTH_SHORT).show();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with login", e);
                    String error_message = "";
                    switch(e.getCode()) {
                        case 101:
                            error_message += getString(R.string.login_error_invalid);
                            break;
                        case 107:
                            error_message += getString(R.string.login_error_network);
                            break;
                        case 200:
                            error_message += getString(R.string.login_error_username);
                            break;
                        case 201:
                            error_message += getString(R.string.login_error_password);
                    }
                    Snackbar.make(binding.btnLogin, error_message, Snackbar.LENGTH_LONG).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}