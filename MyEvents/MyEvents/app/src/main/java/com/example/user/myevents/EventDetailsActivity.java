package com.example.user.myevents;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Alexandre on 30/11/2017.
 */

public class EventDetailsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Event event=(Event) getIntent().getParcelableExtra("event");
        this.setTitle(event.name);
        TextView detailDescription =findViewById(R.id.detailDescription);
        detailDescription.setText(event.description);
        TextView detailDate=findViewById(R.id.detailDate);
        detailDate.setText(event.date);
        TextView detailTime=findViewById(R.id.detailTime);
        detailTime.setText(event.time);
        TextView detailLocation=findViewById(R.id.detailLocation);
        detailLocation.setText(event.address);
        TextView detailOwner=findViewById(R.id.detailOwner);
        detailOwner.setText("Event created by "+event.owner);
        TextView detailTheme=findViewById(R.id.detailTheme);
        detailTheme.setText("This event theme is "+event.theme);

        RecyclerView detailGuestRecyclerView=findViewById(R.id.detailGuestList);
        detailGuestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GuestAdapter guestAdapter = new GuestAdapter(event.getGuestList(),this);
        detailGuestRecyclerView.setAdapter(guestAdapter);
        guestAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}

