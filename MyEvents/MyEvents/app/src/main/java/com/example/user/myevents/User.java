package com.example.user.myevents;


import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String email;
    public String provider;
    public String photoURL;
    public List<String> eventList;
    public List<Object> friendsList;


    public User() {

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String provider, String photoURL) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.photoURL = photoURL;
    }

    public String toString() {
        String res = "name : "+username+"/n"+"email : "+email+"/n"+"provider : "+provider+"/n"+"photoURL : "+photoURL+"/n"+"events : "+eventList.toString()+"/n"+"friends : "+friendsList.toString();
        return res;
    }

    public List<String> getEvents(){
        return this.eventList;
    }
}
