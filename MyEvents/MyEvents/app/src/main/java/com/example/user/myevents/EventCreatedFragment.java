package com.example.user.myevents;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexandre on 02/12/2017.
 */

public class EventCreatedFragment extends Fragment {
    RecyclerView eventView;
    FirebaseRecyclerAdapter eventAdapter;
    FirebaseAuth auth;
    DatabaseReference rootRef;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.event_created_view, parent, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btn_create_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateEventActivity.class);
                startActivity(intent);
            }
        });
        eventView=(RecyclerView) view.findViewById(R.id.eventViewList);
        eventView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        eventView.setHasFixedSize(false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        //Requete pour recuperer les events d'un utilisateur
        auth =FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        final String userId=auth.getCurrentUser().getUid();
        Query myEventsQuery=rootRef.child("eventList").child(userId);
        final FirebaseRecyclerOptions<Event> myEvents =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(myEventsQuery, Event.class)
                        .build();

        eventAdapter=new FirebaseRecyclerAdapter<Event,EventHolder>(myEvents) {
            @Override
            public void onBindViewHolder(EventHolder holder, final int position, final Event event) {
                holder.txtName.setText(event.name);
                //holder.txtTheme.setText(event.theme);
                holder.txtAddress.setText("At "+event.address);
                holder.txtDate.setText("On "+event.date);
                holder.txtTime.setText("at "+event.time);
                //if(event.description!=null)holder.txtDescription.setText("Description: "+event.description);
                final String eventID=myEvents.getSnapshots().get(position).eventKey;
                final boolean isPublic = myEvents.getSnapshots().get(position).isPublic;
                event.setGuestList(getGuestListFromDB(eventID));
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d("LONG CLICK EVENT", "EVENT LONG CLICK");
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
                        builder.setTitle("Confirm delete")
                                .setMessage("Are you sure you want to cancel?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Map<String,Object> deleteQuery=new HashMap<>();
                                        deleteQuery.put("/events/"+eventID,null);
                                        deleteQuery.put("/eventList/"+userId+"/"+eventID,null);
                                        deleteQuery.put("/eventGuestList/"+eventID,null);
                                        if (isPublic){
                                            deleteQuery.put("/eventPublic/"+eventID, null);
                                        }
                                        for (int i=0; i<event.getGuestList().size();i++){
                                            deleteQuery.put("/userInvited/"+event.getGuestList().get(i).UID+"/"+eventID, null);
                                        }
                                        rootRef.updateChildren(deleteQuery);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return true;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),EventDetailsActivity.class);
                        intent.putExtra("event",event);
                        intent.putExtra("isInvitation",false);
                        startActivity(intent);
                    }
                });




            }
            @Override
            public EventHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_item, parent, false);
                return new EventHolder(view);
            }
            @Override
            public void onError(DatabaseError e) {
                Log.d("ERROR BDD ","error bdd");
            }
        };
        eventView.setAdapter(eventAdapter);
        eventAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        eventAdapter.stopListening();
    }
    private ArrayList<User> getGuestListFromDB(String eventID){
        final ArrayList<User> guestList=new ArrayList<>();
        rootRef.child("eventGuestList").child(eventID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                guestList.add(dataSnapshot.getValue(User.class));
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
        return guestList;
    }

    private class EventHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtTheme;
        public TextView txtAddress;
        public TextView txtDate;
        public TextView txtTime;
        public TextView txtDescription;
        public View itemView;

        public EventHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            txtName = (TextView) itemView.findViewById(R.id.txtEventName);
            //txtTheme = (TextView) itemView.findViewById(R.id.txtEventTheme);
            txtAddress = (TextView) itemView.findViewById(R.id.txtEventAddress);
            txtDate = (TextView) itemView.findViewById(R.id.txtEventDate);
            txtTime = (TextView) itemView.findViewById(R.id.txtEventTime);
            //txtDescription = (TextView) itemView.findViewById(R.id.txtEventDescription);

        }
    }
}
