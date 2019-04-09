package com.anikmohammad.twitterclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ParseServer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(ParseServer.this);

        Parse.initialize(new Parse.Configuration.Builder(ParseServer.this)
                .applicationId(getString(R.string.app_id))
                .clientKey(getString(R.string.master_key))
                .server(getString(R.string.server_url))
                .build()
        );

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
