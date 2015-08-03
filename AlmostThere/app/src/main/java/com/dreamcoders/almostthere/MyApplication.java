package com.dreamcoders.almostthere;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


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

    }
}
