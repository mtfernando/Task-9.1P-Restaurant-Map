package com.example.task91prestaurantmap.Util;

import com.google.android.libraries.places.api.model.Place;

public class Util {
    public static final String PLACES_API_KEY = "AIzaSyAuuYM85VQZh7Fs1Yw6mcwd2CmjFH8VVf4";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "places_db";
    public static final String PLACE_TABLE_NAME = "places";

    public static final String ROW_ID = "row_id";
    public static final String PLACE_ID = "place_id";
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    //Creating the DATABASE
    //All Places.Fields stored for MapsActivity to set Markers properly.
    public static final String CREATE_PLACES_TABLE = "CREATE TABLE " + PLACE_TABLE_NAME + "("
            + ROW_ID + " INTEGER AUTO INCREMENT PRIMARY KEY," + PLACE_ID + " TEXT," + ADDRESS + " TEXT," +
            NAME + " TEXT," + LATITUDE + " REAL," + LONGITUDE + " REAL)";

}
