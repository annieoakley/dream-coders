package com.dreamcoders.almostthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;


public class MainActivity extends Activity {

    protected Button mLoginButton;
    protected Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = (Button) findViewById(R.id.logInButton);
        mSignupButton = (Button) findViewById(R.id.signUpButton);

		/*
		 * Check for cached user using ParseUser.getCurrentUser()
        */
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, CurrentCarpools.class));
            finish();
        } else {
            // show the login screen

            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LogIn.class);
                    startActivity(intent);
                }
            });

            //show sign up screen
            mSignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SignUp.class);
                    startActivity(intent);
                }
            });
        }

    }
    
}
