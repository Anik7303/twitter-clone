package com.anikmohammad.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private boolean loginState;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView alternatePrefixTextView;
    private TextView alternateTextView;
    private Button actionButton;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupVariables();

        if(ParseUser.getCurrentUser() != null) {
            redirectToFollowListActivity();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void setupVariables() {
        loginState = true;
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        alternatePrefixTextView = findViewById(R.id.alternatePrefixTextView);
        alternateTextView = findViewById(R.id.alternateTextVew);
        actionButton = findViewById(R.id.actionButton);
    }

    protected void actionFunction(View view) {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if(!username.isEmpty() && !password.isEmpty()) {
            if(loginState) {
                login(username, password);
            }else {
                signup(username, password);
            }
        }else {
            Toast.makeText(MainActivity.this, "Please fill username and password correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private void login(String username, String password) {
        logout();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                String message = "";
                if(e == null && user != null) {
                    message = "Login successful";
                }else if(e == null) {
                    message = "There was a problem while loggin in";
                }else {
                    message = e.getMessage();
                    handleException(e, "ParseUser Login");
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                logout();
            }
        });
    }

    private void signup(final String username, final String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(MainActivity.this, "signup successful", Toast.LENGTH_SHORT).show();
                    login(username, password);
                    logout();
                }else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    handleException(e, "ParseUser SignUp");
                }
            }
        });
    }

    private void logout() {
        if(ParseUser.getCurrentUser() != null) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    protected void alternateActionFunction(View view) {
        if(loginState) {
            actionButton.setText(R.string.signup);
            alternatePrefixTextView.setText(R.string.login_prefix);
            alternateTextView.setText(R.string.login);
        }else {
            actionButton.setText(R.string.login);
            alternatePrefixTextView.setText(R.string.signup_prefix);
            alternateTextView.setText(R.string.signup);
        }
        loginState = !loginState;
    }

    private void redirectToFollowListActivity() {
        Intent intent = new Intent(getApplicationContext(), FollowListActivity.class);
        startActivity(intent);
    }

    private void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }
}
