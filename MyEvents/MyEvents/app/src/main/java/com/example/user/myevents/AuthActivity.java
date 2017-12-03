package com.example.user.myevents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    public static final String user_mail = "com.example.user.myevents.MESSAGE";
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                startActivity(new Intent(this, MainActivity.class));
                String photoURL=null;
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                /*if (!auth.getCurrentUser().getProviders().get(0).equals("password")){
                    photoURL=auth.getCurrentUser().getPhotoUrl().toString();
                }*/
                //photoURL=auth.getCurrentUser().getPhotoUrl().toString();
                final List<String> registeredUsers=new ArrayList<>();
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot users:dataSnapshot.getChildren()){
                            registeredUsers.add(users.getKey());
                        }
                        if(!registeredUsers.contains(auth.getCurrentUser().getUid())){
                            String token = FirebaseInstanceId.getInstance().getToken();
                            User user = new User(auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getEmail(), auth.getCurrentUser().getProviders().get(0), auth.getCurrentUser().getPhotoUrl().toString(),auth.getCurrentUser().getUid(),token);
                            mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
                            String myNotification="notification_"+auth.getCurrentUser().getUid();
                            FirebaseMessaging.getInstance()
                                    .subscribeToTopic(myNotification);
                            finish();
                        }
                        else{
                            finish();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

        }
    }


}
