package com.example.user.myevents;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
                    inviteAccepted(event.eventKey,view);
                }
            });
            Button declineButton=findViewById(R.id.eventDeclineBtn);
            declineButton.setVisibility(View.VISIBLE);
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inviteDeclined(event.eventKey,view);
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

    private void inviteAccepted(final String eventID, View view){
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
        builder.setTitle("Lift options")
                .setMessage("Will take your car ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasAcceptedInvitation").setValue("accepted");
                        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasCar").setValue("true");
                        finish();

                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasAcceptedInvitation").setValue("accepted");
                        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasCar").setValue("false");
                        finish();
                    }
                })
                .setIcon(R.drawable.car)
                .show();
    }
    private void inviteDeclined(final String eventID, View view){
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
        builder.setTitle("Confirm decline")
                .setMessage("Are you sure you want to decline the invitation? \n THIS IS DEFINITIVE")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasAcceptedInvitation").setValue("declined");
                        db.child("eventGuestList").child(eventID).child(currentUser.getUid()).child("hasCar").setValue("false");
                        db.child("userInvited").child(currentUser.getUid()).removeValue();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

