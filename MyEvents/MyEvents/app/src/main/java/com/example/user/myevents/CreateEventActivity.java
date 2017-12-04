package com.example.user.myevents;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateEventActivity extends AppCompatActivity {
    FirebaseAuth auth=FirebaseAuth.getInstance();
    List<User> guestList;
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
                Intent intent=new Intent(CreateEventActivity.this,InviteFriendsActivity.class);
                startActivityForResult(intent,1);
            }});
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                eventDate.setText(new SimpleDateFormat("yy/MM/dd").format(calendar.getTime()));
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
                final Spinner eventGuest = (Spinner) findViewById(R.id.eventGuest);
                guestList=data.getParcelableArrayListExtra("guests");
                eventGuest.setAdapter(new GuestSpinnerAdapter(CreateEventActivity.this,R.layout.guest_spinner_item,guestList));
            }
            if (resultCode == Activity.RESULT_CANCELED) {

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
        if(TextUtils.isEmpty(name)) {
            eventName.setError("This field cannot be empty!");
            return;
        }
        final EditText eventTheme = (EditText) findViewById(R.id.eventTheme);
        String theme = eventTheme.getText().toString();
        if(TextUtils.isEmpty(theme)) {
            eventTheme.setError("This field cannot be empty!");
            return;
        }
        final EditText eventLocation = (EditText) findViewById(R.id.eventLocation);
        String address = eventLocation.getText().toString();
        if(TextUtils.isEmpty(address)) {
            eventLocation.setError("This field cannot be empty!");
            return;
        }
        final EditText eventDate = (EditText) findViewById(R.id.eventDate);
        String date = eventDate.getText().toString();
        if(TextUtils.isEmpty(date)) {
            eventDate.setError("This field cannot be empty!");
            return;
        }
        final EditText eventTime = (EditText) findViewById(R.id.eventTime);
        String time = eventTime.getText().toString();
        if(TextUtils.isEmpty(time)) {
            eventTime.setError("This field cannot be empty!");
            return;
        }

        final EditText eventDescription = (EditText) findViewById(R.id.eventDescription);
        String description = eventDescription.getText().toString();
        if(TextUtils.isEmpty(description)) {
            eventDescription.setError("This field cannot be empty!");
            return;
        }


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


        // Ajout du nouvel event dans la bdd
        final String eventID=mDatabase.child("events").push().getKey();
        final String userID=auth.getCurrentUser().getUid();
        Event eventInfo= new Event(eventID,name,theme,address,date,time,auth.getCurrentUser().getDisplayName(),auth.getCurrentUser().getUid(),isPublic,description,longitude,latitude);
        Map<String,Object> updateQuery=new HashMap<>();
        updateQuery.put("/events/"+eventID,eventInfo);
        updateQuery.put("/eventList/"+userID+"/"+eventID,eventInfo);
        if(guestList != null){
            for(User guest:guestList){
                updateQuery.put("/eventGuestList/"+eventID+"/"+guest.UID,guest);
                updateQuery.put("/userInvited/"+guest.UID+"/"+eventID, eventInfo);
            }
        }
        if (isPublic) {
            updateQuery.put("/eventPublic/"+eventID+"/",eventInfo);
        }

        mDatabase.updateChildren(updateQuery);
        /*Toast.makeText(this, "Event created!",
                Toast.LENGTH_LONG).show();*/
        finish();
    }

    public void cancelEvent(View v){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
        builder.setTitle("Confirm cancel")
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
