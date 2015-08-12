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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


public class MyCarpools extends AppCompatActivity {

    private ParseQueryAdapter<ParseObject> currentCarpoolAdapter;
    private ListView carpoolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_carpools);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyCarpools.this, CurrentCarpools.class);
                startActivity(i);
            }
        });

        View mAddCarpool = findViewById(R.id.add_carpool);
        mAddCarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyCarpools.this, AddCarpool.class);
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
                return view;
            }
        };

        currentCarpoolAdapter.setAutoload(false);
        currentCarpoolAdapter.setPaginationEnabled(false);

        carpoolList = (ListView) findViewById(R.id.carpoolList);
        carpoolList.setAdapter(currentCarpoolAdapter);

        currentCarpoolAdapter.loadObjects();

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
                Intent intent = new Intent(MyCarpools.this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentCarpoolAdapter.loadObjects();
    }
}
