package com.example.user.myevents;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Alexandre on 28/11/2017.
 */

public class InviteInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDService";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(firebaseUser.getUid())
                    .child("token")
                    .setValue(refreshedToken);
        sendRegistrationToServer(refreshedToken);

        String myNotification="notification_"+firebaseUser.getUid();
        FirebaseMessaging.getInstance()
                .subscribeToTopic(myNotification);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
