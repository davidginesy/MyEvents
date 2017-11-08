package com.example.user.myevents;


import java.util.List;

public class User {
    public String username;
    public String email;
    public String provider;
    public String photoURL;
    public List<Event> event;
    public List<User> friends;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String provider, String photoURL, List<Event> event, List<User> friends) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.photoURL = photoURL;
        this.event = event;
        this.friends = friends;
    }

    public String toString() {
        String res = "name : "+username+"/n"+"email : "+email+"/n"+"provider : "+provider+"/n"+"photoURL : "+photoURL+"/n"+"events : "+event.toString()+"/n"+"friends : "+friends.toString();
        return res;
    }
}
