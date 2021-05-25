package com.example.task91prestaurantmap;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.task91prestaurantmap.Data.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper db;
    String placeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        //placeID will be null if all places are to be displayed.
        placeID = intent.getStringExtra("placeID");



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //If a place ID is provided, only one location will be shown.
        //Else, all saved locations will be shown.
        if(!placeID.equals(null)){
            //Adding Marker with LatLng and Title from DB given the Place ID.
            mMap.addMarker(new MarkerOptions().position(db.getLatLng(placeID)).title(db.getName(placeID)));
            System.out.println(db.getName(placeID));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(db.getLatLng(placeID)));
        }

        //TODO: Implement Method to show all saved location markers
    }
}