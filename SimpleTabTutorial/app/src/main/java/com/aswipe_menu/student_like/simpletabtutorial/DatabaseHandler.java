package com.aswipe_menu.student_like.simpletabtutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "consumentCOM";

    // Contacts table name
    private static final String TABLE_CONSUMPTION = "consumption";

    // Contacts Table Columns names
    private static final String DAY_TIME = "dayTime";
    // CONSUMPTION columns
    private static final String BIER = "bier";
    private static final String WEIN = "wein";
    private static final String OFEN = "ofen";
    private static final String ZIGA = "ziga";
    private static final String SHOT = "shot";

    // ====================== LOG output DEFINITIONS =======================
    String LOG_TAG = DatabaseHandler.class.getSimpleName();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONSUMPTION + "("
                + DAY_TIME + " TEXT, "
                + BIER + " TEXT,"
                + WEIN + " TEXT,"
                + OFEN + " TEXT,"
                + ZIGA + " TEXT,"
                + SHOT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMPTION);

        // Create tables again
        onCreate(db);
    }

    // Adding new Day
    public void addDay(String dayTime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.i(LOG_TAG, "infos: creating new Day " + dayTime + "...");

        values.put(DAY_TIME, dayTime);//consumption.newDay());
        values.put(BIER, 0); // Contact Name
        values.put(WEIN, 0); // Contact Phone
        values.put(OFEN, 0); // Contact Phone
        values.put(ZIGA, 0); // Contact Phone
        values.put(SHOT, 0); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONSUMPTION, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int updateContact(String id,  double AddedVal, String choice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.i(LOG_TAG, "infos:  updating " + id + ", " + AddedVal + ", " + choice + " ...");

        values.put(choice, AddedVal);

        // updating row
        return db.update(TABLE_CONSUMPTION, values, DAY_TIME + " = ?",
                new String[] {id});
    }

    public int deleteContact ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONSUMPTION,
                DAY_TIME + "= ? ",
                new String[]{Integer.toString(1)});
    }

    // load one specific column value of today
    public Double loadVal(String dayTime, String choice) {
        SQLiteDatabase db = this.getReadableDatabase();

        double valueTemp = 0;

        Cursor cursor = db.rawQuery("SELECT " + choice + " FROM consumption WHERE dayTime = ?",new String[] { dayTime } ,null);

        // if cursor returns to be 0 ?
        if (cursor.moveToFirst()) {
            valueTemp = Double.parseDouble(cursor.getString(cursor.getColumnIndex(choice)));
        }
        cursor.close();

        //System.out.println("INFOs:: last " + choice + " value in day " + dayTime + " is " + valueTemp + "...");

        return valueTemp;
    }

}