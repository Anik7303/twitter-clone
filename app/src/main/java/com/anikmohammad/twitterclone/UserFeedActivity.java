package com.anikmohammad.twitterclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.parse.ParseUser;

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
            case R.id.others_tweets:
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
