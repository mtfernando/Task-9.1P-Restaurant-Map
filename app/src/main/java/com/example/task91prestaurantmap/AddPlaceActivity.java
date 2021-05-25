package com.example.task91prestaurantmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddPlaceActivity extends AppCompatActivity {
    Button savePlaceButton, getLocationButton, showPlaceButton;
    TextView placeNameTextView, locationTextView;
    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;
    List<Address> addresses;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        placeNameTextView = findViewById(R.id.placeNameTextView);
        locationTextView = findViewById(R.id.locationTextView);

        getLocationButton = findViewById(R.id.getLocationButton);
        savePlaceButton = findViewById(R.id.saveButton);
        showPlaceButton = findViewById(R.id.showOnMapButton);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("PRINTING LAT AND LONG NOW!");
                System.out.println(latitude);
                System.out.println(longitude);
            }
        };

        //Checking permissions before requesting location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddPlaceActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION} ,1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    System.out.println(addresses.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}