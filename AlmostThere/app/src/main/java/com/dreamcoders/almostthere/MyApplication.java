package com.dreamcoders.almostthere;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.http.ParseException;

/**
 * Created by zoetiet on 7/29/15.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Pa2HnrjL7zTzwjDdUzlBOma2nnBCWwigukbpn8wy", "eWr7V9IkIvpxMh6CeTZIXiqiUE82Zv93107TkyR2");

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {

            }

            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
