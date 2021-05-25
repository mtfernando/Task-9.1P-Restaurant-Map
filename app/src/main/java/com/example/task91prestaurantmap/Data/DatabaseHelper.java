package com.example.task91prestaurantmap.Data;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.task91prestaurantmap.Util.Util;
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

        long result = db.insert(Util.PLACE_TABLE_NAME, null, cv);

        return result;
    }

    public List<String> getAllPlaceID(){
        SQLiteDatabase db = this.getReadableDatabase();



        //SQL for getting all PLACE_IDs
        final String GET_ALL_PLACES = "SELECT " + Util.PLACE_ID + " FROM " + Util.PLACE_TABLE_NAME;

        //Cursor object with all place IDs
        Cursor c = db.rawQuery(GET_ALL_PLACES, null);

        final int placeIDIndex = c.getColumnIndex(Util.PLACE_ID);

        try{
            if(!c.moveToFirst()){
                return new ArrayList<>();
            }

            List<String> placeIDList = new ArrayList<>();

            //do-while moves through each result of the cursor and adds them to the list to be returned later
            do{
                final String placeID = c.getString(placeIDIndex);
                placeIDList.add(placeID);
            } while(c.moveToNext());

            return placeIDList;

        } finally { //finally close the cursor and db
            c.close();
            db.close();
        }
    }
}
