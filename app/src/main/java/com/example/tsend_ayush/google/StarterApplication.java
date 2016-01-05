package com.example.tsend_ayush.google;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Tsend-Ayush on 11/15/2015.
 */
public class StarterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this,"pe9QDUKA8hN3ba8oVCWBNYDXJ65FhAHFW5qXQswp","X4CkK9HzHB2gHCCLdaZQZMgE4WnFzpaLuyt8X4Hf");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();


        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        // ParseACL.setDefaultACL(defaultACL, true);
    }
}

