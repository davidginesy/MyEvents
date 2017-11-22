package com.example.user.myevents;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class InviteFriendsActivity extends AppCompatActivity{

    RecyclerView inviteRecyclerView;
    InviteAdapter inviteAdapter;
    List<User> inviteList=new ArrayList<>();
    FirebaseUser auth= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_guest);
        inviteRecyclerView=(RecyclerView)findViewById(R.id.inviteFriendView);
        inviteRecyclerView.setHasFixedSize(false);
        inviteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUserList(inviteList);
        inviteAdapter = new InviteAdapter(inviteList,this);
        inviteRecyclerView.setAdapter(inviteAdapter);
        Button inviteFriendValidateBtn=(Button) findViewById(R.id.inviteFriendValidateBtn);
        inviteFriendValidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
                builder.setTitle("Confirm invitations")
                        .setMessage("Invite the selected friends?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                List<User> invitedList=new ArrayList<>();
                                Integer size = inviteAdapter.inviteChecked.size();
                                Log.d("BOOLEAN SIZE",size.toString());
                                for(int i=0;i<size;i++){
                                    if(inviteAdapter.inviteChecked.get(i)){
                                        Log.d("BOOLEAN VALUE==","true");
                                        invitedList.add(inviteList.get(i));
                                    }
                                    else Log.d("BOOLEAN VALUE==","false");
                                }
                                Intent intent=new Intent();
                                intent.putParcelableArrayListExtra("guests",(ArrayList) invitedList);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }
    public void setUserList(final List<User> userList){

        mDatabase.child("FriendList").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    inviteList.add(dataSnapshot.getValue(User.class));
                    inviteAdapter.notifyDataSetChanged();

                //Log.d("AAAAAAA",dataSnapshot.getValue(User.class).toString());
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
    }
}
