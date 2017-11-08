package com.example.user.myevents;


import java.util.List;

public class User {
    private String username;
    private String email;
    private String provider;
    private String photoURL;
    private List<Event> event;
    private List<User> friends;


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

}
