package com.example.user.myevents;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable{
    public String eventKey;
    public String name;
    public String theme;
    public String address;
    public String date;
    public String time;
    public String owner;
    public String ownerKey;
    public boolean isPublic;
    public String description;
    public double longitude;
    public double latitude;
    public float rating;
    public int nbVote;
    private ArrayList<User> guestList=new ArrayList<>();
    public Event(){
        //Default constructor
    }
    public Event(String eventKey,String name,String theme,String address,String date,String time,String owner,String ownerKey,boolean isPublic,
                 String description, double longitude, double latitude){
        this.eventKey=eventKey;
        this.name=name;
        this.theme=theme;
        this.address=address;
        this.date=date;
        this.time=time;
        this.owner=owner;
        this.ownerKey=ownerKey;
        this.isPublic=isPublic;
        this.description=description;
        this.latitude=latitude;
        this.longitude=longitude;
        this.rating=0;
        this.nbVote=0;
    }

    public Event(Parcel in){
        this.name=in.readString();
        this.theme=in.readString();
        this.address=in.readString();
        this.date=in.readString();
        this.time=in.readString();
        this.owner=in.readString();
        this.ownerKey=in.readString();
        this.description=in.readString();
        this.eventKey=in.readString();
        this.rating=in.readFloat();
        this.nbVote=in.readInt();
        in.readTypedList(this.guestList,User.CREATOR);
    }
    public void setGuestList(ArrayList<User> guestList){
        this.guestList=guestList;
    }


    public ArrayList<User> getGuestList(){
        return guestList;
    }


    @Override
    public String toString() {
        return("Event name: "+name+"\nEvent Date: "+date+"\nEvent Hour: "+time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(theme);
        parcel.writeString(address);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(owner);
        parcel.writeString(ownerKey);
        parcel.writeString(description);
        parcel.writeString(eventKey);
        parcel.writeFloat(rating);
        parcel.writeTypedList(guestList);
        parcel.writeInt(nbVote);
    }
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {

        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
