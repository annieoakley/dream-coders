package com.dreamcoders.almostthere;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.w3c.dom.Text;


/**
 * Created by ginadomergue on 8/7/15.
 */
public class JoinCarpool extends Activity implements View.OnClickListener {

    //carpoolID is currently hardcoded in. Will need to change this


    protected Button joinCarpool;
    protected ParseObject newPassenger;
    //protected ProgressBar mProgressBar;
    protected Button mAddCarpool;
    private TextView driver;
    private TextView destination;
    private TextView pickupLocation;
    private TextView pickupTime;
    private TextView seats;
    private TextView notes;
    private String objectID;
//    private String testID = "ogT35JijfS";
    private String driverID;
    private int numSeats;
    private String carpoolObjectId;
    private ParseUser userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Object g = ((CurrentCarpools) this.getGlobalVariable()).getSomeVariable();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_carpool);

        joinCarpool = (Button) findViewById(R.id.JoinCarpool);
//        driver = (TextView) findViewById(R.id.myDriver);
        destination = (TextView) findViewById(R.id.myDestination);
        pickupLocation = (TextView) findViewById(R.id.myLocation);
        pickupTime = (TextView) findViewById(R.id.myTime);
        seats = (TextView) findViewById(R.id.mySeats);
        notes = (TextView) findViewById(R.id.myNotes);

        final DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:MM");

        objectID = getIntent().getExtras().getString("objectId");
//        Bundle extras;
//
//        if (savedInstanceState == null)
//        {
//            //fetching extra data passed with intents in a Bundle type variable
//
//            extras = getIntent().getExtras();
//
//            if (extras == null)
//            {
//                objectID = null;
//            } else{
//              /* fetching the string passed with intent using ‘extras’*/
//                objectID = extras.getString("objectId");
//            }
//        }

        final ParseQuery<ParseObject> carpoolQuery = ParseQuery.getQuery("Carpool");
        carpoolQuery.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
//                    // object will be your Carpool
                    carpoolObjectId = object.getObjectId();
                    driverID = object.getParseUser("driver").getObjectId();
                    userID = object.getParseUser("driver");
//                    driverID = object.getParseUser("driver").getObjectId();
                    destination.setText(object.getString("destination"));
                    pickupLocation.setText(object.getString("pickUpLocation"));
////                    pickupTime.DateObject.setText(object.getDate("pickUpTime"));
                    Date today = object.getDate("pickUpTime");
                    pickupTime.setText(df.format(today));
                    numSeats = object.getInt("seatsAvailable");
                    seats.setText(Integer.toString(numSeats));
                    notes.setText(object.getString("notes"));
//                    driver.setText(object.getParseUser("driver").getObjectId());
//                    driverID.getParseObject("User")
//                            .fetchIfNeededInBackground(new GetCallback<ParseObject>() {
//                                public void done(ParseObject user, ParseException e) {
//                                    driver.setText(user.getString("username"));
//                                    // Do something with your new title variable
//                                }
//                            });
//
                } else {
//                    destination.setText("something went wrong");
                    // something went wrong
                }
            }
        });
//
//        final ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("User");
//        userQuery.getInBackground(driverID, new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                     driver.setText(object.getString("username"));
//                }
//                else {
//                    // something went wrong
//                }
//            }
//        });


        Button button = (Button) findViewById(R.id.JoinCarpool);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numSeats <= 0)
                {
                    Toast.makeText(JoinCarpool.this,
                            "Sorry, there are no more seats available for this carpool.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    //delete a seat from the current carpool you're on
                    numSeats--;
                    ParseQuery<ParseObject> queryCarpool = ParseQuery.getQuery("Carpool");
                    // Retrieve the object by id
                    queryCarpool.getInBackground(carpoolObjectId, new GetCallback<ParseObject>() {
                        public void done(ParseObject carpoolObject, ParseException e) {
                            if (e == null) {
                                // Now let's update it with some new data. In this case, only cheatMode and score
                                // will get sent to the Parse Cloud. playerName hasn't changed.

                                carpoolObject.put("seatsAvailable", numSeats);
                                carpoolObject.saveInBackground();
                                //Add the passenger to Parse
                                newPassenger = new ParseObject("Passenger");
                                newPassenger.put("carpoolID", carpoolObjectId);
                                newPassenger.put("userID", userID);
                                newPassenger.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            //error
                                            Toast.makeText(JoinCarpool.this, "Join carpool failure", Toast.LENGTH_LONG).show();
                                        } else {
                                            //success

                                            Toast.makeText(JoinCarpool.this, "Carpool joined!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(
                                                    JoinCarpool.this,
                                                    MyCarpools.class));
                                        }
                                    }
                                });

                            }
                        }
                    });
                }

//
            }
        });

//        final ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("User");
////        String test = driverID.getUsername();
//        userQuery.getInBackground(driverID, new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
////                    // object will be your Carpool
//                    driver.setText(object.getString("username"));
////
//                } else {
//                    // something went wrong
//
//                }
//            }
//        });

    }

    @Override
    public void onClick(View v)
    {

    }


    //
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_current_carpools, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutButton:
                ParseUser.logOut();
                Intent intent = new Intent(JoinCarpool.this, LogIn.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
