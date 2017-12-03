package com.example.user.myevents;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import static java.nio.file.Paths.get;


public class InviteNotificationService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        /*if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }*/

        FirebaseAuth auth;
        DatabaseReference rootRef;
        auth =FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        //Intent intent = new Intent(this,EventDetailsActivity.class);
       // final Event event = new Event();

        /*intent.putExtra("event",event);
        intent.putExtra("isInvitation",true);
        startActivity(intent);*/

        //Log.e("dataChat",remoteMessage.getData().toString());
        /*
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON_OBJECT", object.toString());
        */
        //String eventID = remoteMessage.getData().get("eventId");
        //Log.d("eventID", eventID);


        /*
        rootRef.child("event").child(eventID).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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

                                                                             }
                                                                             */


        //Log.d("test", remoteMessage.getData().get("eventInfo"));
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
       // Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        //Log.d("test", remoteMessage.toString());
        //Toast.makeText(getBaseContext(),remoteMessage.getData().get("eventInfo") , Toast.LENGTH_LONG).show();
    }
}
