package com.anikmohammad.twitterclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FollowListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> users;
    private ArrayAdapter<String> adapter;
    private String columnName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        setupVariables();
    }

    private void setupVariables() {
        listView = findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        users = new ArrayList<>();
        populateUsersList();
        adapter = new ArrayAdapter<>(FollowListActivity.this, android.R.layout.simple_list_item_checked, users);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        columnName = "following";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()) {
            ParseUser.getCurrentUser().add(columnName, users.get(position));
        }else {
            List<String> tempUsers = ParseUser.getCurrentUser().getList(columnName);
            tempUsers.remove(users.get(position));
            ParseUser.getCurrentUser().remove(columnName);
            ParseUser.getCurrentUser().put(columnName, tempUsers);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    handleException(e, "ParseUser following");
                }
            }
        });
    }

    private void populateUsersList() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects != null && objects.size() > 0) {
                    users.clear();
                    for(ParseUser user : objects) {
                        users.add(user.getUsername());
                    }
                    adapter.notifyDataSetChanged();

                    List<String> followingUsers = ParseUser.getCurrentUser().getList(columnName);
                    for(String username: followingUsers) {
                        listView.setItemChecked(users.indexOf(username), true);
                    }
                }else if(e == null) {
                    Toast.makeText(FollowListActivity.this, "There is no user except you", Toast.LENGTH_SHORT).show();
                }else {
                    handleException(e, "ParseQuery User");
                }
            }
        });
    }

    private void handleException(Exception exception, String title) {
        Log.i(String.format("Error - %s", title), exception.getMessage());
    }
}
