package com.anikmohammad.twitterclone;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UserFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(UserFeedActivity.this);
        inflater.inflate(R.menu.user_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch(item.getItemId()) {
            case R.id.tweet:
                final EditText tweetEditText = new EditText(UserFeedActivity.this);
                new AlertDialog.Builder(UserFeedActivity.this)
                        .setTitle("Send a tweet")
                        .setView(tweetEditText)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseObject tweetObject = new ParseObject("Tweets");
                                tweetObject.put("usename", ParseUser.getCurrentUser().getUsername());
                                tweetObject.put("tweet", tweetEditText.getText().toString());
                                tweetObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null) {
                                            Toast.makeText(UserFeedActivity.this, "Tweet sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;
            case R.id.user_feed:
                intent = new Intent(UserFeedActivity.this, TweetsActivity.class);
                startActivity(intent);
                break;
            case R.id.following:
                intent = new Intent(UserFeedActivity.this, FollowListActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                ParseUser.logOut();
                finish();
                MainActivity.setEditTexts("","");
                break;
            default:
                return false;
        }
        return true;
    }

    private void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }
}
