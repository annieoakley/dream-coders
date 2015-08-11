package com.dreamcoders.almostthere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddCarpool extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected ParseUser mCurrentUser;
    protected AutoCompleteTextView mDestination;
    protected AutoCompleteTextView mPickUpLocation;
    protected EditText mPickUpDay;
    protected EditText mPickUpTime;
    protected NumberPicker mSeats;
    protected EditText mNotes;
    protected Button mCreateCarpool;
    protected ProgressBar mProgressBar;
    protected ParseObject newCarpool;
    protected ParseGeoPoint pickupGeo;
    protected ParseGeoPoint destinationGeo;
    protected ParseObject toDestinationGeo;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private SimpleDateFormat dateFormatter;

    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mDestinationAdapter;
    private PlaceAutocompleteAdapter mPickupAdapter;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(37.209952, -122.472618), new LatLng(37.897678, -121.691871));

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        setContentView(R.layout.activity_add_carpool);

        mCurrentUser = ParseUser.getCurrentUser();
        mDestination = (AutoCompleteTextView) findViewById(R.id.editDestination);
        mPickUpLocation = (AutoCompleteTextView) findViewById(R.id.editPickupLocation);

        //Date Picker
        mPickUpDay = (EditText) findViewById(R.id.datePicker);
        mPickUpDay.setInputType(InputType.TYPE_NULL);
        mPickUpDay.requestFocus();

        //Time Picker
        mPickUpTime = (EditText) findViewById(R.id.timePicker);
        mPickUpTime.setInputType(InputType.TYPE_NULL);

        mSeats = (NumberPicker) findViewById(R.id.numberPicker);
        mNotes = (EditText) findViewById(R.id.editNotes);
        mCreateCarpool = (Button) findViewById(R.id.button_SignUp);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mSeats.setMinValue(1);
        mSeats.setMaxValue(20);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        mDestination.setOnItemClickListener(mDestinationAutocompleteClickListener);
        mPickUpLocation.setOnItemClickListener(mPickupAutocompleteClickListener);



        //set up the adapter that will retrieve suggestions from the Places Geo Data API.
        mDestinationAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                    mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        mDestination.setAdapter(mDestinationAdapter);

        mPickupAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_SYDNEY,null);
        mPickUpLocation.setAdapter(mPickupAdapter);




        Button button = (Button) findViewById(R.id.createCarpoolButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String destination = mDestination.getText().toString();
                String pickUpLocation = mPickUpLocation.getText().toString();
               // int month = mPickUpDay.getMonth();
                //int day = ;
                int year;
                int hour;
                int min;
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
                newCarpool.put("pickupGeo", pickupGeo);
                newCarpool.put("toDestinationGeo", toDestinationGeo);
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

//    private void setDateTimeField() {
//        mPickUpDay.setOnClickListener(this);
//        mPickUpTime.setOnClickListener(this);
//
//        Calendar newCalendar = Calendar.getInstance();
//        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                mPickUpDay.setText(dateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//        timePickerDialog = new TimePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                mPickUpTime.setText(dateFormatter.format(newDate.getTime()));
//            }
//
//        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//    }

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
            case R.id.logoutAddCarpool:
                ParseUser.logOut();
                Intent intent = new Intent(AddCarpool.this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private AdapterView.OnItemClickListener mDestinationAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //retrieve the place ID of the selected item from the adapter. The adapter
            //stores each Place suggestion in a PlaceAutocomplete object from which we read the place ID.
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mDestinationAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            //issue a request to the Places GEo Data API to retrieve a Place object with additional details
            //about the place.
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mDestinationUpdatePlaceDetailsCallback);


        }
    };

    private AdapterView.OnItemClickListener mPickupAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //retrieve the place ID of the selected item from the adapter. The adapter
            //stores each Place suggestion in a PlaceAutocomplete object from which we read the place ID.
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mPickupAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            //issue a request to the Places GEo Data API to retrieve a Place object with additional details
            //about the place.
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mPickupUpdatePlaceDetailsCallback);


        }
    };



    //callback for results from a Place Geo Data API query that shows the first place result in the details view
    //on screen
    private ResultCallback<PlaceBuffer> mDestinationUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>(){

        @Override
        public void onResult(PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                //request is not complete successfully
                places.release();
                return;
            }

            //get the Place object from the buffer
            final Place place = places.get(0);

            //Format details of the place for display and show it in a Textview
            mDestination.setText(formatPlaceDetails(getResources(), place.getName()));
            destinationGeo = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            toDestinationGeo = new ParseObject("DestinationGeo");
            toDestinationGeo.put("destinationGeo", destinationGeo);

            places.release();
        }
    };

    //callback for results from a Place Geo Data API query that shows the first place result in the details view
    //on screen
    private ResultCallback<PlaceBuffer> mPickupUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>(){

        @Override
        public void onResult(PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                //request is not complete successfully
                places.release();
                return;
            }

            //get the Place object from the buffer
            final Place place = places.get(0);

            //Format details of the place for display and show it in a Textview
            mPickUpLocation.setText(formatPlaceDetails(getResources(), place.getName()));
            pickupGeo = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name){
        return Html.fromHtml(res.getString(R.string.place_details, name));
    }


    //called when the activity could not connect to Google Play services.
    //notify user that API is not available.

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public void onClick(View view) {
//        if(view == fromDateEtxt) {
//            fromDatePickerDialog.show();
//        } else if(view == toDateEtxt) {
//            toDatePickerDialog.show();
//        }
//    }

}
