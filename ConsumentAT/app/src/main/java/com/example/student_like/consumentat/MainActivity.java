package com.example.student_like.consumentat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

// database includes
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity
{
    // save when turning app off
    public static final String MY_PREFS_NAME = "saveParams";

    double bierL;
    double consArray[] = new double [6];
    String articles[] = {"bier", "wein", "ofen", "ziga", "shot"};
    //enum articles {BIER, WEIN, OFEN, ZIGA, SHOT};

    String lastDayTime;
    String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////// INITIAL STATEMENTS //////////////
        getDayTime(); // dateTime = date of today

        ButtonClick();
    }

    void ButtonClick()
    {
        // create new instance of Database
        final DatabaseHandler db = new DatabaseHandler(this);

        // instantiate textView class
        final TextView myTextView = (TextView) findViewById(R.id.textView);
        myTextView.setTextColor(Color.BLACK);

        Button Bier05 = (Button) findViewById(R.id.button2);
        Button Bier33 = (Button) findViewById(R.id.button3);
        Button Wein18 = (Button) findViewById(R.id.button4);
        Button Zigare = (Button) findViewById(R.id.button5);

        ImageButton bierButton05 = (ImageButton) findViewById(R.id.bierButton05);
        ImageButton bierButton033 = (ImageButton) findViewById(R.id.bierButton033);

        // check if values for curr. day exist
        //bierL = db.getVal(dateTime, "bier");
        //myTextView.setText(String.valueOf("Letzter Eintrag e.g. Bier: " + bierL));

        // check if today already exist, else create new row with curr. day
        final String readTemp = SaveLoadDayTime(0);

        if (!readTemp.equals(dateTime)) {
            db.addDay(dateTime); // create new table for today

            SaveLoadDayTime(1); // set today as last day used
            System.out.println("INFO: new row created for today...");
        }
        else {
            System.out.println("INFO: a table for today already exists loading prev. vals...");

            for (int i=1;i<6;i++) {
                consArray[i] = db.loadVal(dateTime, articles[i-1]); // fill consArray with pre-defined vals from today
            }
            SaveLoadDayTime(1); // set today as last day used
        }

        // increase beer +0.5
        bierButton05.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                consArray[1] = consArray[1] + 0.5;
                myTextView.setText("Day: " + readTemp + String.valueOf(" Bier: " + consArray[1]));

                db.updateContact(dateTime, consArray[1], articles[0]);
            }
        });
        // increase beer +0.33
        bierButton033.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get curr. dayTime
                consArray[1] = consArray[1] + 0.33;
                myTextView.setText("Day: " + readTemp + String.valueOf(" Bier: " + consArray[1]));

                db.updateContact(dateTime, consArray[1], articles[0]);
            }
        });
        // increase Wein + 0.125
        Wein18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get curr. dayTime
                consArray[2] = consArray[2] + 0.125;
                myTextView.setText("Day: " + readTemp + String.valueOf(" Wein: " + consArray[2]));

                db.updateContact(dateTime, consArray[2], articles[1]);
            }
        });
    }

    // 1 = save DayTime, 0 = load DayTime
    public String SaveLoadDayTime(int choice)
    {
        if(choice == 0) { // LOAD
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("lastDayTime", null);
            if (restoredText != null) {
                lastDayTime = prefs.getString("lastDayTime", "No name defined");//"No name defined" is the default value.
            }
            else{
                lastDayTime = "0";
            }

            System.out.println("INFO: -> reading " + lastDayTime + " from shared preferences...");
        }
        else // SAVE
        {
            //dateTime = getDayTime();

            System.out.println("INFO: <- writing " + dateTime + " to shared preferences...");
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("lastDayTime", dateTime);
            editor.commit();
        }
        return lastDayTime;
    }

    public String getDayTime()
    {
        // time and date definitions
        Date now = new Date();
        String name = new SimpleDateFormat("ddMMyyyy").format(now);

        dateTime = "23022016";

        System.out.println("INFO: today is day " + dateTime);

        return name;
    }
}
