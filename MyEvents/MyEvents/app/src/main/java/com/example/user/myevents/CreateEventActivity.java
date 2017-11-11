package com.example.user.myevents;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Calendar calendar = Calendar.getInstance();
        final EditText eventDate= (EditText) findViewById(R.id.eventDate);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    public void createEvent(View v){
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
        Event eventCreated= new Event(name,theme,address,date,time,guests,isPublic,description,auth.getUid());
        final String eventID=mDatabase.child("events").push().getKey();
        mDatabase.child("events").child(eventID).setValue(eventCreated);
        mDatabase.child("users").child("eventList").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                List<Object> eventList=dataSnapshot.getValue(ArrayList.class);
                eventList.add("eventID");
                Map<String,Object> taskMap=new HashMap<>();
                taskMap.put("eventList",eventList);
                mDatabase.child("users").child(auth.getUid()).updateChildren(taskMap);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
