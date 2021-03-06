package com.example.user.myevents;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable{
    public String username;
    public String email;
    public String provider;
    public String photoURL;
    public String UID;
    public String token;
    public String hasAcceptedInvitation="pending";
    public String hasCar="pending";

    public User() {
    }

    public User(String username, String email, String provider, String photoURL,String UID,String token) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.photoURL = photoURL;
        this.UID=UID;
        this.token=token;
    }

    public User(Parcel in){
        this.username=in.readString();
        this.email=in.readString();
        this.provider=in.readString();
        this.photoURL=in.readString();
        this.UID=in.readString();
        this.token=in.readString();
        this.hasAcceptedInvitation=in.readString();
        this.hasCar=in.readString();
    }
    public String toString() {
        String res = "name : "+username+"email : "+email+"provider : "+provider+"photoURL : "+photoURL+"\nhasAcceptedInvitation"+hasAcceptedInvitation+"\nhasCar"+hasCar;
        return res;
    }

    public String getUsername(){return username;}
    public String getPhotoURL(){return photoURL;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(provider);
        parcel.writeString(photoURL);
        parcel.writeString(UID);
        parcel.writeString(token);
        parcel.writeString(hasAcceptedInvitation);
        parcel.writeString(hasCar);
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
