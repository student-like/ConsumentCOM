package com.aswipe_menu.student_like.simpletabtutorial.DayTime;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetDayTime extends Activity{

    String LOG_TAG = GetDayTime.class.getSimpleName();

    public static final String MY_PREFS_NAME = "saveParams"; // save last day

    Activity activity;

    String dateTime;
    String date;

    public GetDayTime(Activity activity) {
        this.activity = activity;
    }

    // ====================== Day & DATETIME + DATE management =======================
    public String SaveLoadDayTime(int choice)
    {
        String temp_date = "0";

        if(choice == 0) { // LOAD
            SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            String restoredText = prefs.getString("lastDayTime", null);
            if (restoredText != null) {
                temp_date = prefs.getString("lastDayTime", "No name defined");//"No name defined" is the default value.
            }
            else{
                temp_date = "0";
            }

            Log.i(LOG_TAG, "infos: -> reading " + temp_date + " from shared preferences...");
        }
        else // SAVE
        {
            Log.i(LOG_TAG, "infos: <- writing " + date + " to shared preferences...");
            SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString("lastDayTime", date);
            editor.apply();
        }
        return temp_date;
    }

    public String getDayTime(int choice)
    {
        // time and date definitions
        SimpleDateFormat name = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        // convert SimpleDateFormat -> String
        String conv_date = name.format(new Date());

        String[] parts = conv_date.split(" ");
        date = parts[0];
        //date = "2016-03-08";

        dateTime = conv_date;
        //dateTime = "2016-03-08 10:20:19.945";

        if (choice == 0) {
            return date;
        }
        else {
            return dateTime;
        }

    }

}