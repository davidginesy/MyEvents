package com.example.user.myevents;


import java.util.List;

public class Event {
    public String name;
    public String theme;
    public String address;
    public String date;
    public String time;
    public String guests; // Changer String en List<User> avec fonction liste de contacts
    public boolean isPublic;
    public String description;
    public String ownerID;

    public Event(){
        //Default constructor
    }
    public Event(String name,String theme,String address,String date,String time,String guests,boolean isPublic,String description,String ownerID){
        this.name=name;
        this.address=address;
        this.date=date;
        this.time=time;
        this.guests=guests;
        this.isPublic=isPublic;
        this.description=description;
        this.ownerID=ownerID;
    }

    @Override
    public String toString() {
        return("Event name: "+name+"\nEvent Date: "+date+"\nEvent Hour:"+time);
    }
}
