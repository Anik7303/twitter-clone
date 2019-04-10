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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetsActivity extends AppCompatActivity {

    private ListView tweetListView;
    private ArrayList<Map<String, String>> tweetList;
    private SimpleAdapter adapter;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        setupVariables();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(TweetsActivity.this);
        inflater.inflate(R.menu.user_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch(item.getItemId()) {
            case R.id.tweet:
                final EditText tweetEditText = new EditText(TweetsActivity.this);
                new AlertDialog.Builder(TweetsActivity.this)
                        .setTitle("Send a tweet")
                        .setView(tweetEditText)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseObject tweetObject = new ParseObject("Tweets");
                                tweetObject.put("username", currentUsername);
                                tweetObject.put("tweet", tweetEditText.getText().toString());
                                tweetObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        String message = "";
                                        if(e == null) {
                                            message = "Tweet sent";
                                            populateTweetList();
                                        }else {
                                            message = "Error: tweet not sent";
                                            handleException(e, "Sending Tweet");
                                        }
                                        Toast.makeText(TweetsActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Tweet", "tweet Canceled");
                                dialog.cancel();
                            }
                        })
                        .show();

                break;
            case R.id.user_feed:
                intent = new Intent(TweetsActivity.this, UserFeedActivity.class);
                startActivity(intent);
                break;
            case R.id.following:
                intent = new Intent(TweetsActivity.this, FollowListActivity.class);
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

    private void setupVariables() {
        tweetListView = findViewById(R.id.tweetListView);
        currentUsername = ParseUser.getCurrentUser().getUsername();
        tweetList = new ArrayList<>();
        adapter = new SimpleAdapter(TweetsActivity.this, tweetList, android.R.layout.simple_list_item_2, new String[] {"tweet", "username"}, new int[]{android.R.id.text1, android.R.id.text2});
        tweetListView.setAdapter(adapter);
        populateTweetList();
    }

    private void populateTweetList() {
        ParseQuery.getQuery("Tweets")
                .whereContainedIn("username", ParseUser.getCurrentUser().getList("following"))
                .orderByDescending("createdAt")
                .setLimit(20)
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null && objects != null && objects.size() > 0) {
                            tweetList.clear();
                            for (ParseObject object: objects) {
                                Map<String, String> temp = new HashMap<>();
                                temp.put("username", object.getString("username"));
                                temp.put("tweet", object.getString("tweet"));
                                tweetList.add(temp);
                            }
                            adapter.notifyDataSetChanged();
                        }else if(e != null) {
                            Toast.makeText(TweetsActivity.this, String.format("%s has no tweets", currentUsername), Toast.LENGTH_SHORT).show();
                            handleException(e, "ParseQuery UserTweets");
                        }
                        Log.i("tweetList.size()", Integer.toString(tweetList.size()));
                    }
                });
    }

    private void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ParseUser.getCurrentUser().getList("following").isEmpty()) {
            tweetList.clear();
            adapter.notifyDataSetChanged();
        }else {
            populateTweetList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
