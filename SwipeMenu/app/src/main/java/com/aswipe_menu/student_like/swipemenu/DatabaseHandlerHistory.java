package com.aswipe_menu.student_like.swipemenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerHistory extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "consumentCOM_history";

    // Contacts table name
    private static final String TABLE_CONSUMPTION_HISTORY = "consumption_history";

    // Contacts Table Columns names
    private static final String ID = "_id";
    private static final String DAY_TIME = "dayTime";
    private static final String PRODUCT = "last_consumed";

    public DatabaseHandlerHistory(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONSUMPTION_HISTORY + "("
                + ID + " TEXT, "
                + DAY_TIME + " TEXT, "
                + PRODUCT + " TEXT " + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMPTION_HISTORY);

        // Create tables again
        onCreate(db);
    }

    // Updating single contact
    public void addConsumption(int id_hist, String dayTime, int cons_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out.println("INFOs:: + consumption for day " +dayTime + " consuming type: " + cons_type +"  ... -> HISTORY");

        values.put(ID, id_hist);
        values.put(DAY_TIME, dayTime); // Add row with last day consumed
        values.put(PRODUCT, Integer.toString(cons_type)); // Add row with last PRODUCT consumed

        // Inserting Row
        db.insert(TABLE_CONSUMPTION_HISTORY, null, values);
        db.close(); // Closing database connection
    }

    public int deleteContact ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONSUMPTION_HISTORY,
                DAY_TIME + "= ? ",
                new String[]{Integer.toString(1)});
    }

    // load one specific column value of today
    public int loadConsumedProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        int valueTemp = 0;

        Cursor cursor = db.rawQuery("SELECT "+ PRODUCT +" FROM "+ TABLE_CONSUMPTION_HISTORY +" WHERE _id = ?",new String[] { Integer.toString(id) } ,null);

        // if cursor returns to be 0 ?
        if (cursor.moveToFirst()) {
            valueTemp = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT)));
        }
        cursor.close();

        //System.out.println("INFOs:: consumed product is " + valueTemp +  "... -> HISTORY");

        return valueTemp;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_CONSUMPTION_HISTORY);

        return numRows;
    }
}