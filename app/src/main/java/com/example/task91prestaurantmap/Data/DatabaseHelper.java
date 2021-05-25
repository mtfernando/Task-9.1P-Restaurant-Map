package com.example.task91prestaurantmap.Data;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.task91prestaurantmap.Util.Util;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Executing SQL from Util class
        db.execSQL(Util.CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_PLACE_TABLE = "DROP TABLE IF EXISTS";
        db.execSQL(DROP_PLACE_TABLE, new String[] {Util.PLACE_TABLE_NAME});

        onCreate(db);
    }

    public long createPlace(Place place){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(Util.PLACE_ID, place.getId());
        cv.put(Util.ADDRESS, place.getAddress());
        cv.put(Util.NAME, place.getName());
        cv.put(Util.LATITUDE, place.getLatLng().latitude);
        cv.put(Util.LONGITUDE, place.getLatLng().longitude);

        long result = db.insert(Util.PLACE_TABLE_NAME, null, cv);

        return result;
    }

    //Return LatLng object for a given placeID
    public LatLng getLatLng(String placeID){
        double latitude, longitude;
        SQLiteDatabase db = this.getReadableDatabase();

        //Query for getting row with given placeID.
        final String GET_PLACE = "SELECT * FROM " + Util.PLACE_TABLE_NAME + " WHERE " + Util.PLACE_ID + " = \"" + placeID + "\"";

        Cursor c = db.rawQuery(GET_PLACE, null);

        //Cursor Indices for Latitude and Longitude
        final int latIndex = c.getColumnIndex(Util.LATITUDE);
        final int longIndex = c.getColumnIndex(Util.LONGITUDE);

        //Set Latitude and Longitude if Cursor is not empty
        if(c.moveToFirst()){
            latitude = c.getDouble(latIndex);
            longitude = c.getDouble(longIndex);
        }
        else{
            return new LatLng(-1, -1);
        }

        //Return LatLng object
        return new LatLng(latitude, longitude);
    }

    //Return Name  for a given placeID
    public String getName(String placeID){
        String name;
        SQLiteDatabase db = this.getReadableDatabase();

        //Query for getting row with given placeID.
        final String GET_PLACE = "SELECT * FROM " + Util.PLACE_TABLE_NAME + " WHERE " + Util.PLACE_ID + " = \"" + placeID + "\"";

        Cursor c = db.rawQuery(GET_PLACE, null);

        //Cursor Indices for Latitude and Longitude
        final int nameIndex = c.getColumnIndex(Util.NAME);

        //Set Latitude and Longitude if Cursor is not empty
        if(c.moveToFirst()){
            name = c.getString(nameIndex);
        }
        else{
            return null;
        }

        //Return LatLng object
        return name;
    }

    public String[] getAllPlaceID(){
        SQLiteDatabase db = this.getReadableDatabase();



        //Query for getting all PLACE_IDs
        final String GET_ALL_PLACES = "SELECT " + Util.PLACE_ID + " FROM " + Util.PLACE_TABLE_NAME;

        //Cursor object with all place IDs
        Cursor c = db.rawQuery(GET_ALL_PLACES, null);

        final int placeIDIndex = c.getColumnIndex(Util.PLACE_ID);

        try{
            if(!c.moveToFirst()){
                return new String[]{null};
            }

            String[] placeIDList = new String[(int) DatabaseUtils.queryNumEntries(db, Util.PLACE_TABLE_NAME)];


            int insertIndex = 0;
            //do-while moves through each result of the cursor and adds them to the list to be returned later
            do{
                final String placeID = c.getString(placeIDIndex);
                placeIDList[insertIndex] = placeID;
                insertIndex++;
            } while(c.moveToNext());

            return placeIDList;

        } finally { //finally close the cursor and db
            c.close();
            db.close();
        }
    }
}
