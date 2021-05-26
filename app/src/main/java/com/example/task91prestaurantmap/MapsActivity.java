package com.example.task91prestaurantmap;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.example.task91prestaurantmap.Data.DatabaseHelper;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper db;
    String placeID = null;
    double placeLat, placeLong;
    Location location;
    LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        //placeID will be null if all places are to be displayed.
        placeID = intent.getStringExtra("placeID");
        placeLat = intent.getDoubleExtra("placeLat", -1);
        placeLong = intent.getDoubleExtra("placeLong", -1);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //If a place ID is provided, only one location will be shown.
        //Else, all saved locations will be shown.
        if(placeID != null){
            //Adding Marker with LatLng and Title from DB given the Place ID.
            mMap.addMarker(new MarkerOptions().position(new LatLng(placeLat, placeLong)).title(db.getName(placeID)));
            System.out.println(db.getName(placeID));
            //Move the camera to the user's location and zoom in!
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeLat, placeLong), 12.0f));
        }

        //TODO: Implement Method to show all saved location markers
        else{
            //Loop through every placeID in the DB
            for(String currentPlaceID: db.getAllPlaceID()){
                mMap.addMarker(new MarkerOptions().position(db.getLatLng(currentPlaceID)).title(db.getName(currentPlaceID)));
            }

            //Checking permissions before requesting location updates
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
            }

            //Set the camera to the first location in the DB
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(db.getLatLng(db.getAllPlaceID()[0]), 12.0f));
        }
    }
}