package com.dreamcoders.almostthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LogIn extends Activity {


    protected EditText mUsernameText;
    protected EditText mPwdText;
    protected ProgressBar mProgressBar;
    protected Button mloginButton;
    protected TextView mToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mUsernameText = (EditText) findViewById(R.id.usernameText);
        mPwdText = (EditText) findViewById(R.id.pwdText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mloginButton = (Button) findViewById(R.id.loginPageButton);
        mToSignUp = (TextView) findViewById(R.id.signUp_login);

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String username = mUsernameText.getText().toString();
                String password = mPwdText.getText().toString();

                /**
				* Login using ParseUser
				*/
                ParseUser.logInInBackground(username, password,
                        new LogInCallback() {
                            public void done(ParseUser user,
                                             ParseException e) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                if (user != null ) {
                                    if(user.getBoolean("emailVerified")){
                                        // Hooray! The user is logged in.
                                        startActivity(new Intent(
                                                LogIn.this,
                                                CurrentCarpools.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LogIn.this,
                                                "Please verify your email first!",
                                                Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    // Login failed. Look at the
                                    // ParseException to see what happened.
                                    Toast.makeText(
                                            LogIn.this,
                                            "Login failed! Try again.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            
        });

    }


    public void onClick(View view){
        Intent intent = new Intent(LogIn.this, SignUp.class);
        startActivity(intent);
    }


}
