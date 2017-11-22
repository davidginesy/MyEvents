package com.example.user.myevents;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;


public class SearchFriendsActivity extends AppCompatActivity {
    RecyclerView userRecyclerView;
    public static EditText search;
    public UserAdapter userAdapter;
    public static String selectedUserID;
    public static User selectedUser;
    FirebaseUser auth=FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    List<User> userList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        search=(EditText)findViewById(R.id.searchText);
        userRecyclerView=(RecyclerView)findViewById(R.id.userListView);
        userRecyclerView.setHasFixedSize(false);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUserList(userList);
        userAdapter = new UserAdapter(userList,this);
        userRecyclerView.setAdapter(userAdapter);
        addTextListener();
        Button friendAddValidatedBtn=(Button) findViewById(R.id.friendAddValidateBtn);
        friendAddValidatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("FriendList").child(auth.getUid()).child(selectedUserID).setValue(selectedUser);
                search.setText("");
                userAdapter.notifyDataSetChanged();
                Toast.makeText(SearchFriendsActivity.this, "Friend Added !",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setUserList(final List<User> userList){

        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(!auth.getUid().equals(dataSnapshot.getKey())){
                userList.add(dataSnapshot.getValue(User.class));
                    userAdapter.notifyDataSetChanged();
                }

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

    public void addTextListener(){

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<User> filteredList = new ArrayList<>();

                for (User user : userList) {

                    final String filter = user.username.toLowerCase();
                    if (filter.contains(query)) {
                        filteredList.add(user);
                    }
                }

                userRecyclerView.setLayoutManager(new LinearLayoutManager(SearchFriendsActivity.this));
                userAdapter = new UserAdapter(filteredList, SearchFriendsActivity.this);
                userRecyclerView.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }




}

