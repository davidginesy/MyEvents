package com.example.user.myevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


public class EventFragment extends Fragment {
    List<Event> myEvents=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.event_view, parent, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btn_create_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateEventActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        final DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        final EventAdapter eventAdapter=new EventAdapter(getActivity(),myEvents);
        ListView listView=(ListView) view.findViewById(R.id.eventViewList);
        listView.setAdapter(eventAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event=(Event)parent.getItemAtPosition(position);
                String eventID=event.eventID;
                String userID=auth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("/events/" + eventID+ "/", null);
                map.put("/users/"+userID+"/eventList/"+eventID,null);
                rootRef.updateChildren(map);
                myEvents.remove(position);
                return true;
            }
        });
        Query eventListQuery=rootRef.child("users").child(auth.getCurrentUser().getUid()).child("eventList");
        eventListQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Query eventInfoQuery=rootRef.child("events").child(dataSnapshot.getKey());
                //Log.d("eventInfoKey QUERY====",eventId);
                eventInfoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myEvents.add(dataSnapshot.getValue(Event.class));
                        eventAdapter.notifyDataSetChanged();
                        //Log.d("eventInfo QUERY====", dataSnapshot.getValue(Event.class).toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
                                             }
        );


        return view;
    }
}
