package com.example.user.myevents;


import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String email;
    public String provider;
    public String photoURL;
    public String UID;


    public User() {

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String provider, String photoURL,String UID) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.photoURL = photoURL;
        this.UID=UID;
    }

    public String toString() {
        String res = "name : "+username+"/n"+"email : "+email+"/n"+"provider : "+provider+"/n"+"photoURL : "+photoURL;
        return res;
    }
}
