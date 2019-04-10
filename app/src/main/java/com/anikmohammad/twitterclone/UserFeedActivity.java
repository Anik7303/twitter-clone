package com.anikmohammad.twitterclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFeedActivity extends AppCompatActivity {

    private ListView tweetListView;
    private ArrayList<Map<String, String>> tweetList;
    private SimpleAdapter adapter;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        setupVariables();
    }

    private void setupVariables() {
        tweetListView = findViewById(R.id.userTweetListView);
        currentUsername = ParseUser.getCurrentUser().getUsername();
        tweetList = new ArrayList<>();
        adapter = new SimpleAdapter(UserFeedActivity.this, tweetList, android.R.layout.simple_list_item_2, new String[] {"tweet", "username"}, new int[]{android.R.id.text1, android.R.id.text2});
        tweetListView.setAdapter(adapter);
        populateTweetList();
    }

    private void populateTweetList() {
        ParseQuery.getQuery("Tweets")
                .whereEqualTo("username", currentUsername)
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
                            Toast.makeText(UserFeedActivity.this, String.format("%s has no tweets", currentUsername), Toast.LENGTH_SHORT).show();
                            handleException(e, "ParseQuery UserTweets");
                        }
                        Log.i("tweetList.size()", Integer.toString(tweetList.size()));
                    }
                });
    }

    private static void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
