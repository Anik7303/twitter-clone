package com.anikmohammad.twitterclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

            }else {

            }
        }else {
            Toast.makeText(MainActivity.this, "Please fill username and password correctly", Toast.LENGTH_SHORT).show();
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

    private void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }
}
