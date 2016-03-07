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

    public int deleteContact (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //System.out.println("INFOs:: deleting rowID " + (id) + "...");

        return db.delete(TABLE_CONSUMPTION_HISTORY, "rowid" + "=" + (id), null);
    }

    public void doTheVacuum()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("VACUUM");
    }
    // load one specific column value of today
    public int loadConsumedProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int valueTemp = 0;

        Cursor cursor = db.rawQuery("SELECT "+ PRODUCT +" FROM "+ TABLE_CONSUMPTION_HISTORY +" WHERE rowid = ?",new String[] { Integer.toString(id) } ,null);

        if (cursor.moveToFirst()) {
            valueTemp = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT)));
        }

        cursor.close();

        return valueTemp;
    }

    public long getNrOfRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        long valueTemp = 0;

        Cursor cursor = db.rawQuery("SELECT ROWID from " +TABLE_CONSUMPTION_HISTORY+ " order by ROWID DESC limit 1", null);

        if (cursor.moveToFirst()) {
            valueTemp = cursor.getLong(0);
        }

        return valueTemp;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_CONSUMPTION_HISTORY);

        return numRows;
    }
}