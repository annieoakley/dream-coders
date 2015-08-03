package com.dreamcoders.almostthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends Activity {

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mSignUp;
    protected ProgressBar mProgressBar;
    protected ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUsername = (EditText) findViewById(R.id.username_SignUp);
        mEmail = (EditText) findViewById(R.id.email_SignUp);
        mPassword = (EditText) findViewById(R.id.password_SignUp);
        mSignUp = (Button) findViewById(R.id.button_SignUp);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_SignUp);


        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();



					/*
                     * Sign up using ParseUser
					 */
                user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);

                user.signUpInBackground(new SignUpCallback() {

                    public void done(ParseException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);

                        if (e == null) {
                                // Hooray! Let them use the app now.
                                startActivity(new Intent(
                                        SignUp.this,
                                        LogIn.class));
                        } else {
                            // Sign up didn't succeed. Look at the
                            // ParseException to figure out what went wrong
                            Toast.makeText(SignUp.this,
                                    "Sign up failed! Try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
