package com.example.task91prestaurantmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.task91prestaurantmap.Data.DatabaseHelper;
import com.example.task91prestaurantmap.Util.Util;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
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
    Place currentPlace;
    DatabaseHelper db;

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

        db = new DatabaseHelper(this);

        //Initializing Places API
        Places.initialize(getApplicationContext(), Util.PLACES_API_KEY);
        PlacesClient placesClient = Places.createClient(getApplicationContext());

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = new ArrayList<>();
        placeFields.add(Place.Field.NAME);
        placeFields.add(Place.Field.ADDRESS);
        placeFields.add(Place.Field.ID);
        placeFields.add(Place.Field.LAT_LNG);
        placeFields.add(Place.Field.TYPES);

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
            ActivityCompat.requestPermissions(AddPlaceActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

        //Get current location and update EditTexts
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the builder to create a FindCurrentPlaceRequest.
                FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

                // Call findCurrentPlace and handle the response (first check that the user has granted permission).
                if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);

                    //Upon receiving a response
                    placeResponse.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FindCurrentPlaceResponse response = task.getResult();
                            System.out.println(response.getPlaceLikelihoods().get(0).getPlace().getAddress());

                            //Place object of the current location
                            currentPlace = response.getPlaceLikelihoods().get(0).getPlace();

                            //Set EditTexts accordingly
                            setEditText(currentPlace);
                        } else {
                            Exception exception = task.getException();

                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                            }

                            Toast.makeText(AddPlaceActivity.this, "No Location Available!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(AddPlaceActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
                }
            }
        });

        savePlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Insert place into DB
                long result = db.createPlace(currentPlace);

                //Check if DB insertion was successful
                if (result != -1)
                    Toast.makeText(AddPlaceActivity.this, "Place saved!", Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(AddPlaceActivity.this, "Erro! Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields).build(AddPlaceActivity.this);
                startActivityForResult(intent, 100);
            }
        });

    }

    //Set the two edit texts of Place name and Location with details of the given Place object.
    public void setEditText(Place place) {
        placeNameTextView.setText(place.getName());
        locationTextView.setText(place.getAddress());

        System.out.println("Edit Texts Updated. Name: " + place.getName() + ", Address: " + place.getAddress());
    }

    //Result from Places Autocomplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            currentPlace = Autocomplete.getPlaceFromIntent(data);
            setEditText(currentPlace);
        }
    }
}