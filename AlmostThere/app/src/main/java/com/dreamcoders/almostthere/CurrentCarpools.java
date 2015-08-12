package com.dreamcoders.almostthere;

import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CurrentCarpools extends AppCompatActivity {

    private ParseQueryAdapter<ParseObject> currentCarpoolAdapter;
    private ListView carpoolList;
    private List<String> ObjectIdList = new ArrayList<String>();

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
                        query.whereEqualTo("driver", ParseUser.getCurrentUser());
                        return query;
                    }
                };


        currentCarpoolAdapter = new ParseQueryAdapter<ParseObject>(this, factory){
            @Override
            public View getItemView(ParseObject object, View view, ViewGroup parent) {
                if(view == null){
                    view = View.inflate(getContext(), R.layout.customer_row, null);
                }
                TextView bigContent = (TextView) view.findViewById(R.id.custom_row_large);
                TextView medContent = (TextView) view.findViewById(R.id.custom_row_med);


                bigContent.setText(object.getString("pickUpLocation"));
                medContent.setText(object.getString("destination"));
                ObjectIdList.add(object.getObjectId());
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


                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
//                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                if (cursor != null) {
//                    String locationSetting = Utility.getPreferredLocation(getActivity());
//                    ((Callback) getActivity())
//                            .onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
//                            ));
//                }
//                mPosition = position;
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

//    public Object getGlobalVariable() {
//        return globalVariable;
//    }
//
//    public void setGlobalVariable(Object g){
//        this.globalVariable = g;
//    }
}
