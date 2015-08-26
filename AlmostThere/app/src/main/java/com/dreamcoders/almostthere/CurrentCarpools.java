package com.dreamcoders.almostthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CurrentCarpools extends AppCompatActivity {

    private ParseQueryAdapter<ParseObject> currentCarpoolAdapter;
    private ListView carpoolList;
    private List<String> ObjectIdList = new ArrayList<String>();
    final DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:MM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_carpools);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CurrentCarpools.this, MapsActivity.class);
                startActivity(i);
            }
        });

        View mAddCarpool = findViewById(R.id.add_carpool);
        mAddCarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CurrentCarpools.this, AddCarpool.class);
                startActivity(i);
            }
        });

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Carpool");
                        query.whereNotEqualTo("driver", ParseUser.getCurrentUser());
                        return query;
                    }
                };


        currentCarpoolAdapter = new ParseQueryAdapter<ParseObject>(this, factory){
            @Override
            public View getItemView(ParseObject object, View view, ViewGroup parent) {
                if(view == null){
                    view = View.inflate(getContext(), R.layout.customer_row, null);
                }
                Date today = object.getDate("pickUpTime");
                int numSeats = object.getInt("seatsAvailable");

                TextView bigContent = (TextView) view.findViewById(R.id.custom_row_large);
                TextView medContent = (TextView) view.findViewById(R.id.custom_row_med);

                boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if(tabletSize) {

                    TextView bigContent2 = (TextView) view.findViewById(R.id.custom_row_large2);
                    TextView medContent2 = (TextView) view.findViewById(R.id.custom_row_med2);

                    ObjectIdList.add(object.getObjectId());

                    bigContent.setText("To: " + object.getString("destination"));
                    medContent.setText("From: " + object.getString("pickUpLocation"));
                    bigContent2.setText("Pick up on: " + df.format(today));
                    if (numSeats > 1) {
                        medContent2.setText(Integer.toString(numSeats) + " seats available");
                    } else {
                        medContent2.setText(Integer.toString(numSeats) + " seat available");
                    }
                } else {
                    ObjectIdList.add(object.getObjectId());

                    bigContent.setText("To: " + object.getString("destination"));
                    medContent.setText("From: " + object.getString("pickUpLocation"));
                }

                return view;

            }
        };

        currentCarpoolAdapter.setAutoload(false);
        currentCarpoolAdapter.setPaginationEnabled(false);

        carpoolList = (ListView) findViewById(R.id.carpoolList);
        carpoolList.setAdapter(currentCarpoolAdapter);



        carpoolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                //globalVariable = carpoolList.getItemAtPosition(position);
                Intent i = new Intent(CurrentCarpools.this, JoinCarpool.class);
                i.putExtra("objectId", ObjectIdList.get(position));
                startActivity(i);




            }
        });

        currentCarpoolAdapter.loadObjects();

    }

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
                Intent intent = new Intent(CurrentCarpools.this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.myCarpools:
                Intent i = new Intent(CurrentCarpools.this, MyCarpools.class);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        currentCarpoolAdapter.loadObjects();
    }
}
