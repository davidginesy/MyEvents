package com.example.user.myevents;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alexandre on 30/11/2017.
 */

public class EventDetailsActivity extends AppCompatActivity {
    DatabaseReference db;
    FirebaseUser currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Event event=(Event) getIntent().getParcelableExtra("event");
        Boolean isInvitation=getIntent().getExtras().getBoolean("isInvitation");
        if(isInvitation){
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
            Button acceptButton=findViewById(R.id.eventAcceptBtn);
            acceptButton.setVisibility(View.VISIBLE);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inviteAccepted(event.eventKey);
                }
            });
            Button declineButton=findViewById(R.id.eventDeclineBtn);
            declineButton.setVisibility(View.VISIBLE);
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inviteDeclined(event.eventKey);
                }
            });

            RecyclerView detailGuestRecyclerView=findViewById(R.id.detailGuestList);
            detailGuestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            GuestAdapter guestAdapter = new GuestAdapter(event.getGuestList(),this);
            detailGuestRecyclerView.setAdapter(guestAdapter);
            guestAdapter.notifyDataSetChanged();
        }
        else{
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

    private void inviteAccepted(String eventID){
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();
        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasAcceptedInvitation").setValue("accepted");
        finish();
    }
    private void inviteDeclined(String eventID){
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();
        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasAcceptedInvitation").setValue("declined");
        finish();
    }
}

