package com.anikmohammad.twitterclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // sign up a user named 'root'
//        ParseUser user = new ParseUser();
//        user.setUsername("root");
//        user.setPassword("root");
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null) {
//                    Toast.makeText(MainActivity.this, "root successfully signed up", Toast.LENGTH_SHORT).show();
//                } else {
//                    handleException(e, "ParseUser SignUp");
//                }
//            }
//        });
    }

    protected void testServer(View view) {
        if(ParseUser.getCurrentUser() == null) {
            final String username = "root";
            final String password = "root";
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e == null && user != null) {
                        Toast.makeText(MainActivity.this, String.format("%s successfully logged in", username), Toast.LENGTH_SHORT).show();
                    } else if(e == null) {
                        Toast.makeText(MainActivity.this, "user == null", Toast.LENGTH_SHORT).show();
                    } else {
                        handleException(e, "ParseUser Login");
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, String.format("username: %s", ParseUser.getCurrentUser().getUsername()), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }
}
