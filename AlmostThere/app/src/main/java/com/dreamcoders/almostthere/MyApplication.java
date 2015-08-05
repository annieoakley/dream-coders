package com.dreamcoders.almostthere;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Pa2HnrjL7zTzwjDdUzlBOma2nnBCWwigukbpn8wy", "eWr7V9IkIvpxMh6CeTZIXiqiUE82Zv93107TkyR2");


        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Testing creating new carpool
//        ParseObject newCarpool = new ParseObject("Carpool");
//        if(currentUser != null){
//            newCarpool.put("driver", ParseUser.getCurrentUser());
//        }
//        newCarpool.put("destination", "Home");
//        newCarpool.put("pickUpLocation", "Home");
//        newCarpool.put("pickUpTime", new Date());
//        newCarpool.put("seatsAvailable", 2);
//        newCarpool.put("notes", "Hello world!");
//        newCarpool.saveInBackground();


    }
}
