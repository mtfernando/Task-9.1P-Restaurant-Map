package com.example.task91prestaurantmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.task91prestaurantmap.Util.Util;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

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
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddPlaceActivity.this, new String[] {ACCESS_FINE_LOCATION} ,1);
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

        Places.initialize(getApplicationContext(), Util.PLACES_API_KEY);
    }
}