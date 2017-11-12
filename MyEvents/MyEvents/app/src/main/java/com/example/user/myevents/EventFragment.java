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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EventFragment extends Fragment {
    List<Event> myEvents;
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
        FirebaseAuth auth=FirebaseAuth.getInstance();
        final DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        Query eventListQuery=rootRef.child("users").child(auth.getUid()).child("eventList");
            final Set<String> eventListId=new HashSet<>();

           eventListQuery.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   final long[] pendingLoadCount = { dataSnapshot.getChildrenCount() };
                   for (DataSnapshot child : dataSnapshot.getChildren())
                   {
                       eventListId.add(child.getValue().toString());
                   }
                   myEvents=new ArrayList<>();
                   for(String eventId: eventListId){
                       Query eventInfoQuery=rootRef.child("events").child(eventId);
                       Log.d("eventInfoKey QUERY====",eventId);
                       eventInfoQuery.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               myEvents.add(dataSnapshot.getValue(Event.class));
                               Log.d("eventInfo QUERY====", dataSnapshot.getValue(Event.class).toString());
                               pendingLoadCount[0]-=1;

                               if(pendingLoadCount[0]==0){
                                   EventAdapter eventAdapter=new EventAdapter(getActivity(),myEvents);
                                   ListView listView=(ListView) view.findViewById(R.id.eventViewList);
                                   listView.setAdapter(eventAdapter);
                               }
                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                   }

               }
               @Override
               public void onCancelled(DatabaseError databaseError) {
               }
           }
           );


        return view;
    }







}
