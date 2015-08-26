package com.dreamcoders.almostthere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by ginadomergue on 7/30/15.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        final String loggedInSession = prefs.getString("login", null);

        Thread timerThread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(loggedInSession != null){
                        ParseUser.becomeInBackground(loggedInSession, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                startActivity(new Intent(
                                        SplashScreen.this,
                                        CurrentCarpools.class));
                                finish();
                            }
                        });
                    }else {
                        Intent i = new Intent("com.dreamcoders.almostthere.LogIn");
                        startActivity(i);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
