package com.example.user.myevents;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class EventPastFragment extends Fragment {
        RecyclerView eventView;
        RecyclerView eventView2;
        FirebaseRecyclerAdapter eventAdapter;
        FirebaseRecyclerAdapter eventAdapter2;
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
            eventView2=(RecyclerView) view.findViewById(R.id.eventPastViewList2);
            eventView2.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
            eventView2.setHasFixedSize(false);
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
            final String currentDate=new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance().getTime());
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
                    holder.ratingBar.setRating(event.rating);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog ratingDialog = new Dialog(getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
                            ratingDialog.setContentView(R.layout.rating_dialog);
                            ratingDialog.setCancelable(true);
                            final RatingBar ratingBar=(RatingBar) ratingDialog.findViewById(R.id.dialog_ratingbar);
                            Button rateButton = (Button) ratingDialog.findViewById(R.id.rate_dialog_button);
                            rateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Float myRating=ratingBar.getRating();
                                    rootRef.child("userInvited").child(auth.getCurrentUser().getUid()).child(event.eventKey).child("rating").setValue(myRating);
                                    rootRef.child("eventList").child(event.ownerKey).child(event.eventKey).runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            Event e=mutableData.getValue(Event.class);
                                            int nbVote=e.nbVote+1;
                                            float currentRate=e.rating;
                                            float myRating=ratingBar.getRating();
                                            float newRate=currentRate+(myRating-currentRate)/nbVote;
                                            e.nbVote++;
                                            e.rating=newRate;
                                            mutableData.setValue(e);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                        }
                                    });
                                    ratingDialog.dismiss();
                                }
                            });
                            ratingDialog.show();
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

            Query myEventsQuery2=rootRef.child("eventList").child(userId).orderByChild("date").startAt(lastWeek).endAt(currentDate);
            final FirebaseRecyclerOptions<Event> myEvents2 =
                    new FirebaseRecyclerOptions.Builder<Event>()
                            .setQuery(myEventsQuery2, Event.class)
                            .build();

            eventAdapter2=new FirebaseRecyclerAdapter<Event, EventPastHolder>(myEvents2) {
                @Override
                public void onBindViewHolder(EventPastHolder holder, final int position, final Event event) {
                    holder.txtName.setText(event.name);
                    holder.txtTime.setText("Was on "+event.date+" at "+event.time);
                    holder.ratingBar.setRating(event.rating);
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
            eventView2.setAdapter(eventAdapter2);
            eventAdapter2.startListening();
        }
        @Override
        public void onStop(){
            super.onStop();
            eventAdapter.stopListening();
            eventAdapter2.stopListening();
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
