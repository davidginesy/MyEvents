package com.example.user.myevents;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CreateEventActivity extends AppCompatActivity {
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String friendsAdded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Calendar calendar = Calendar.getInstance();
        final EditText eventDate= (EditText) findViewById(R.id.eventDate);
        final Button btn_addGuest= (Button) findViewById(R.id.contactButton);
        btn_addGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), SearchFriends.class),1);
            }});
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                eventDate.setText(new SimpleDateFormat("dd/MM/yy").format(calendar.getTime()));
            }
            };
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this,R.style.datePickerDialogTheme, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final EditText eventTime = (EditText) findViewById(R.id.eventTime);
        eventTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this,R.style.timePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute<10){
                            eventTime.setText( selectedHour + ":0" + selectedMinute);
                        }
                        else eventTime.setText( selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, true);
                mTimePicker.show();

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                friendsAdded=result;
                final EditText eventGuest = (EditText) findViewById(R.id.eventGuest);
                eventGuest.setText(friendsAdded);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
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

    public void createEvent(View v) throws IOException {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Récupération du formulaire
        final EditText eventName = (EditText) findViewById(R.id.eventName);
        String name = eventName.getText().toString();
        final EditText eventTheme = (EditText) findViewById(R.id.eventTheme);
        String theme = eventTheme.getText().toString();


        final EditText eventLocation = (EditText) findViewById(R.id.eventLocation);


        String address = eventLocation.getText().toString();
        final EditText eventDate = (EditText) findViewById(R.id.eventDate);
        String date = eventDate.getText().toString();
        final EditText eventTime = (EditText) findViewById(R.id.eventTime);
        String time = eventTime.getText().toString();
        // A améliorer avec les contacts
        final EditText eventGuest = (EditText) findViewById(R.id.eventGuest);
        String guests = eventGuest.getText().toString();
        final EditText eventDescription = (EditText) findViewById(R.id.eventDescription);
        String description = eventDescription.getText().toString();
        final Switch eventPublicSwitch = (Switch) findViewById(R.id.eventPublicSwitch);
        boolean isPublic = eventPublicSwitch.isChecked();
        // Transormation de l'adresse en coordonnées GPS
        double longitude = 0;
        double latitude = 0;
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        addresses = geocoder.getFromLocationName(address, 1);
        if(addresses.size() > 0) {
            latitude= addresses.get(0).getLatitude();
            longitude= addresses.get(0).getLongitude();
        }
        final String eventID=mDatabase.child("events").push().getKey();
        Event eventCreated= new Event(eventID,name,theme,address,date,time,guests,isPublic,description,auth.getCurrentUser().getUid(),latitude,longitude);
        mDatabase.child("events").child(eventID).setValue(eventCreated);
        mDatabase.child("users").child(auth.getCurrentUser().getUid()).child("eventList").child(eventID).setValue(true);

        Toast.makeText(this, "Event created!",
                Toast.LENGTH_LONG).show();
        finish();
    }

    public void cancelEvent(View v){
        Toast.makeText(this, "Event creation canceled :(",
                Toast.LENGTH_LONG).show();
        finish();
    }
}
