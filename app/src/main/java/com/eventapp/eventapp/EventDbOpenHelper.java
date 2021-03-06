package com.eventapp.eventapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A database helper for saving interesting events, and also deleting them
 */

public class EventDbOpenHelper extends SQLiteOpenHelper {

    // database columns
    final static String ACT_ID = "id";
    final static String TABLE_NAME = "saved_events";
    final static String ACT_TITLE = "title";
    final static String ACT_IMG_URL = "img_url";
    final static String ACT_DATE = "date";
    final static String END_TIME = "end_time";
    final static String ACT_DETAILS = "details";
    final static String ACT_VENUE = "venue";
    final static String ACT_URL = "url";
    final static String ACT_NAME = "act_name";
    final static String VENUE_ADDRESS = "venue_address";
    final static String ALL_DAY = "all_day";
    final static String LAT = "lat";
    final static String LNG = "lng";
    final static String [] columns = { ACT_TITLE, ACT_IMG_URL, ACT_ID, ACT_DATE, END_TIME, ACT_DETAILS, ACT_VENUE, ACT_URL, ACT_NAME, VENUE_ADDRESS, ALL_DAY, LAT, LNG };

    // creates the database
    final private static String CREATE_CMD =

        "CREATE TABLE saved_events ("
            + ACT_ID + " TEXT NOT NULL, "
            + ACT_TITLE + " TEXT NOT NULL, "
            + ACT_IMG_URL + " TEXT, "
            + ACT_DATE + " TEXT, "
            + END_TIME + " TEXT, "
            + ACT_DETAILS + " TEXT, "
            + ACT_VENUE + " TEXT, "
            + ACT_URL + " TEXT NOT NULL, "
            + ACT_NAME + " TEXT NOT NULL, "
            + VENUE_ADDRESS + " TEXT, "
            + ALL_DAY + " TEXT, "
            + LAT + " TEXT, "
            + LNG + " TEXT)";

    // db setup
    final private static String NAME = "event_db";
    final private static Integer VERSION = 1;
    final private Context mContext;

    public EventDbOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_CMD); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EventDbOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // add events to the db
    public void addEvent(String id, String title, String img_url, String date, String end_time, String details, String venue, String url, String name, String venue_address, boolean all_day, Double lat, Double lng) {
        Log.e("addEventID", id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(ACT_ID, id);
        value.put(ACT_TITLE, title);
        value.put(ACT_IMG_URL, img_url);
        value.put(ACT_DATE, date);
        value.put(END_TIME, end_time);
        value.put(ACT_DETAILS, details);
        value.put(ACT_VENUE, venue);
        value.put(ACT_URL, url);
        value.put(ACT_NAME, name);
        value.put(VENUE_ADDRESS, venue_address);
        value.put(ALL_DAY, all_day);
        value.put(LAT, lat);
        value.put(LNG, lng);

        db.insert(TABLE_NAME, null, value);

        db.close();
    }

    // get all saved events from db
    public ArrayList<EventListing> getAllSavedEvents() {

        ArrayList<EventListing> list = new ArrayList<EventListing>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                EventListing e = new EventListing();
                e.setId(cursor.getString(0));
                e.setTitle(cursor.getString(1));
                e.setImgUrl(cursor.getString(2));
                e.setDate(cursor.getString(3));
                e.setEndTimes(cursor.getString(4));
                e.setDetails(cursor.getString(5));
                e.setVenueName(cursor.getString(6));
                e.setUrl(cursor.getString(7));
                e.setArtistName(cursor.getString(8));
                e.setVenueAddress(cursor.getString(9));
                e.setAllDay(cursor.getString(10));
                e.setLat(cursor.getString(11));
                e.setLng(cursor.getString(12));
                list.add(e);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.e("getAllSavedEvents()", list.toString());

        return list;
    }

    // get all the ids of saved events, used to check if an event has already been saved to the db
    public List<String> getAllSavedEventsIDS() {
        List<String> savedEventIDS = new LinkedList<String>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String id = null;
        if (cursor.moveToFirst()) {
            do {
                id = new String(cursor.getString(0));
                savedEventIDS.add(id);
            } while (cursor.moveToNext());
        }
        Log.e("getAllSavedEventIDS()", savedEventIDS.toString());

        cursor.close();
        db.close();
        return savedEventIDS;
    }

    public void deleteEventById(String id) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ACT_ID + "='" + id + "'", null);

        db.close();
    }

    // deletes the database
    public void deleteDatabase() { mContext.deleteDatabase(NAME); }
}
