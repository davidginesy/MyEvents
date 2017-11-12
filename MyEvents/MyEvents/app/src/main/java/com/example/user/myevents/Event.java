package com.example.user.myevents;


import java.util.List;

public class Event {
    public String eventID;
    public String name;
    public String theme;
    public String address;
    public String date;
    public String time;
    public String guests; // Changer String en List<User> avec fonction liste de contacts
    public boolean isPublic;
    public String description;
    public String ownerID;
    public double longitude;
    public double latitude;

    public Event(){
        //Default constructor
    }
    public Event(String eventID,String name,String theme,String address,String date,String time,String guests,boolean isPublic,String description,String ownerID, double longitude, double latitude){
        this.eventID=eventID;
        this.name=name;
        this.theme=theme;
        this.address=address;
        this.date=date;
        this.time=time;
        this.guests=guests;
        this.isPublic=isPublic;
        this.description=description;
        this.ownerID=ownerID;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    public String toString() {
        return("Event name: "+name+"\nEvent Date: "+date+"\nEvent Hour: "+time);
    }
}
