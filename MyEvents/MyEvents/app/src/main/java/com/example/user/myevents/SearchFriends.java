package com.example.user.myevents;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFriends extends AppCompatActivity {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    List<String> photoURL = new ArrayList<>();
    List<String> friends = new ArrayList<>();
    String friendsAdded = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        final AutoCompleteTextView simpleAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, friends);

        simpleAutoCompleteTextView.setAdapter(adapter);
        simpleAutoCompleteTextView.setThreshold(1);//start searching from 1 character

        Button btn = findViewById(R.id.add_btn);
        Button btn_finish= findViewById(R.id.btn_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = simpleAutoCompleteTextView.getText().toString();
                simpleAutoCompleteTextView.setText("");
                Toast.makeText(getApplicationContext(), name+" added",
                        Toast.LENGTH_LONG).show();
                friendsAdded+=name +" ";

            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",friendsAdded);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        mDatabase.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    friends.add(postSnapshot.child("username").getValue().toString());
                    photoURL.add(postSnapshot.child("photoURL").getValue().toString());
                    Log.d("aaaaaaa", photoURL.get(0).toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("aaaaaaa", "Failed to read app title value.", error.toException());
            }
        });

        }
}

