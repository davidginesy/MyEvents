package com.example.user.myevents;


import android.content.pm.PackageManager;
//import android.location.*;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.Manifest;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    Double latitude;
    Double longitude;
    boolean ispublic;
    String name;
    String horaire;
    String adresse;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String userID=auth.getCurrentUser().getUid();
    private FusedLocationProviderClient mFusedLocationClient;
    DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yy HH:mm");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.maps_view, parent, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Test obtenir position de l'user
        /*
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
        }
        else {
            mMap.setMyLocationEnabled(false);
            // Show rationale and request permission.
        }*/


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //display public events
                for (DataSnapshot child : dataSnapshot.child("eventPublic").getChildren()) {
                    horaire = child.child("date").getValue(String.class) + " " + child.child("time").getValue(String.class);
                    Date date = null;
                    try {
                        date = sourceFormat.parse(horaire);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal =Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.HOUR_OF_DAY,3);
                    date = cal.getTime();
                    Date currDate = new Date();
                    if (currDate.before(date)){
                        latitude = child.child("latitude").getValue(Double.class);
                        longitude = child.child("longitude").getValue(Double.class);
                        name = child.child("name").getValue(String.class);
                        adresse = child.child("adress").getValue(String.class);
                        Log.d("ok", latitude.toString());
                        Log.d("ok", longitude.toString());
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .snippet(horaire));
                    }
                }

                //display events invited
                for (DataSnapshot child : dataSnapshot.child("userInvited").child(userID).getChildren()){
                    horaire = child.child("date").getValue(String.class) + " " + child.child("time").getValue(String.class);
                    Date date = null;
                    try {
                        date = sourceFormat.parse(horaire);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal =Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.HOUR_OF_DAY,3);
                    date = cal.getTime();
                    Date currDate = new Date();
                    if (currDate.before(date)){
                        latitude = child.child("latitude").getValue(Double.class);
                        longitude = child.child("longitude").getValue(Double.class);
                        name = child.child("name").getValue(String.class);
                        adresse = child.child("adress").getValue(String.class);
                        Log.d("ok", latitude.toString());
                        Log.d("ok", longitude.toString());
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .snippet(horaire));
                    }
                }

                //display events created
                for (DataSnapshot child : dataSnapshot.child("eventList").child(userID).getChildren()){
                    horaire = child.child("date").getValue(String.class) + " " + child.child("time").getValue(String.class);
                    Date date = null;
                    try {
                        date = sourceFormat.parse(horaire);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal =Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.HOUR_OF_DAY,3);
                    date = cal.getTime();
                    Date currDate = new Date();
                    if (currDate.before(date)){
                        latitude = child.child("latitude").getValue(Double.class);
                        longitude = child.child("longitude").getValue(Double.class);
                        name = child.child("name").getValue(String.class);
                        adresse = child.child("adress").getValue(String.class);
                        Log.d("ok", latitude.toString());
                        Log.d("ok", longitude.toString());
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .snippet(horaire));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //mMap.addMarker(new MarkerOptions().position(/*some location*/));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.3347178,-71.3825182), 6));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

