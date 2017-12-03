package com.example.user.myevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class EventPastFragment extends Fragment {
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
            View view=inflater.inflate(R.layout.event_past_view, parent, false);
            eventView=(RecyclerView) view.findViewById(R.id.eventPastViewList);
            eventView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
            eventView.setHasFixedSize(false);
            return view;
        }
        public void onStart(){
            super.onStart();

            //Requete pour recuperer les events d'un utilisateur
            auth =FirebaseAuth.getInstance();
            rootRef= FirebaseDatabase.getInstance().getReference();
            final String userId=auth.getCurrentUser().getUid();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            String lastWeek=new SimpleDateFormat("yy/MM/dd").format(cal.getTime());
            Log.d("DATE",lastWeek);
            String currentDate=new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance().getTime());
            Query myEventsQuery=rootRef.child("userInvited").child(userId).orderByChild("date").startAt(lastWeek).endAt(currentDate);
            final FirebaseRecyclerOptions<Event> myEvents =
                    new FirebaseRecyclerOptions.Builder<Event>()
                            .setQuery(myEventsQuery, Event.class)
                            .build();

            eventAdapter=new FirebaseRecyclerAdapter<Event, EventPastHolder>(myEvents) {
                @Override
                public void onBindViewHolder(EventPastHolder holder, final int position, final Event event) {
                    holder.txtName.setText(event.name);
                    holder.txtTime.setText("Was on "+event.date+" at "+event.time);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });




                }
                @Override
                public EventPastHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.event_past_item, parent, false);
                    return new EventPastHolder(view);
                }
                @Override
                public void onError(DatabaseError e) {
                    Log.d("ERROR BDD ","error bdd");
                }
            };
            eventView.setAdapter(eventAdapter);
            eventAdapter.startListening();
        }
      /*  private ArrayList<User> getGuestListFromDB(String eventID){
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
        }*/

        @Override
        public void onStop(){
            super.onStop();
            eventAdapter.stopListening();
        }
        private class EventPastHolder extends RecyclerView.ViewHolder {
            public TextView txtName;
            public TextView txtTime;
            public RatingBar ratingBar;
            public View itemView;

            public EventPastHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                txtName = (TextView) itemView.findViewById(R.id.eventPastName);
                txtTime = (TextView) itemView.findViewById(R.id.eventPastTime);
                ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar);

            }
        }
}
