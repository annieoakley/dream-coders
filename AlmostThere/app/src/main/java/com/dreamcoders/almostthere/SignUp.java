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
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please");
                if (username.length() == 0) {
                    validationError = true;
                    validationErrorMessage.append(" enter a username ");
                }
                if (password.length() == 0) {
                    if (validationError) {
                        validationErrorMessage.append("and");
                    }
                    validationError = true;
                    validationErrorMessage.append(" enter a password ");
                }

                if (!email.contains("@google.com") && !email.contains("@gmail.com")) {
                    if (validationError) {
                        validationErrorMessage.append("and");
                    }
                    validationError = true;
                    validationErrorMessage.append(" use google or gmail domain ");
                }
                

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(SignUp.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                mProgressBar.setVisibility(View.VISIBLE);
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
