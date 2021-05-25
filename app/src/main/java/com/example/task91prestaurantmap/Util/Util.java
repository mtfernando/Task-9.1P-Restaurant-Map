package com.example.task91prestaurantmap.Util;

import com.google.android.libraries.places.api.model.Place;

public class Util {
    public static final String PLACES_API_KEY = "AIzaSyAn-mWG1W9Rj_CwO3D5wKOvLA4UHVnPY78";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "places_db";
    public static final String PLACE_TABLE_NAME = "places";

    public static final String ROW_ID = "row_id";
    public static final String PLACE_ID = "place_id";

    //Creating the DATABASE
    public static final String CREATE_PLACES_TABLE = "CREATE TABLE " + PLACE_TABLE_NAME + "("
            + ROW_ID + " INTEGER AUTO INCREMENT PRIMARY KEY," + PLACE_ID + " TEXT)";

}
