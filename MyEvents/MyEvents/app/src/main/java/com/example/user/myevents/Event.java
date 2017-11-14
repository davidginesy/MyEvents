package com.example.user.myevents;


import java.util.List;

public class Event {
    public String eventKey;
    public String name;
    public String theme;
    public String address;
    public String date;
    public String time;
    //public String guests;
    public boolean isPublic;
    public String description;
    public double longitude;
    public double latitude;

    public Event(){
        //Default constructor
    }
    public Event(String eventKey,String name,String theme,String address,String date,String time,boolean isPublic,String description, double longitude, double latitude){
        this.eventKey=eventKey;
        this.name=name;
        this.theme=theme;
        this.address=address;
        this.date=date;
        this.time=time;
        this.isPublic=isPublic;
        this.description=description;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    public String toString() {
        return("Event name: "+name+"\nEvent Date: "+date+"\nEvent Hour: "+time);
    }
}
