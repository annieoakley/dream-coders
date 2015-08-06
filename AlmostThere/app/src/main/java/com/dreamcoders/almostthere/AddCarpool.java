package com.dreamcoders.almostthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by zoetiet on 8/5/15.
 */
public class AddCarpool extends ActionBarActivity {

    protected ParseUser mCurrentUser;
    protected EditText mDestination;
    protected EditText mPickUpLocation;
    protected EditText mPickUpTime;
    protected NumberPicker mSeats;
    protected EditText mNotes;
    protected Button mCreateCarpool;
    protected ProgressBar mProgressBar;
    protected ParseObject newCarpool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carpool);

        mCurrentUser = ParseUser.getCurrentUser();
        mDestination = (EditText) findViewById(R.id.editDestination);
        mPickUpLocation = (EditText) findViewById(R.id.editPickupLocation);
        mPickUpTime = (EditText) findViewById(R.id.editPickUpTime);
        mSeats = (NumberPicker) findViewById(R.id.numberPicker);
        mNotes = (EditText) findViewById(R.id.editNotes);
        mCreateCarpool = (Button) findViewById(R.id.button_SignUp);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mSeats.setMinValue(1);
        mSeats.setMaxValue(20);

        Button button = (Button) findViewById(R.id.createCarpoolButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String destination = mDestination.getText().toString();
                String pickUpLocation = mPickUpLocation.getText().toString();
                String pickUpTime = mPickUpTime.getText().toString();
                int seats = mSeats.getValue();
                String notes = mNotes.getText().toString();

                newCarpool = new ParseObject("Carpool");
                if(mCurrentUser != null) {
                    newCarpool.put("driver", mCurrentUser);
                }
                newCarpool.put("destination", destination);
                newCarpool.put("pickUpLocation", pickUpLocation);
                newCarpool.put("pickUpTime", new Date());
                newCarpool.put("seatsAvailable", seats);
                newCarpool.put("notes", notes);
                newCarpool.saveInBackground(new SaveCallback() {

                    public void done(ParseException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);

                        if (e != null) {
                            //error
                            Toast.makeText(AddCarpool.this, "Create carpool failure", Toast.LENGTH_LONG).show();
                        } else {
                            //success
                            Toast.makeText(AddCarpool.this, "Carpool created!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(
                                    AddCarpool.this,
                                    CurrentCarpools.class));
                        }
                    }
                });

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_carpool, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutButton:
                ParseUser.logOut();
                Intent intent = new Intent(AddCarpool.this, LogIn.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
