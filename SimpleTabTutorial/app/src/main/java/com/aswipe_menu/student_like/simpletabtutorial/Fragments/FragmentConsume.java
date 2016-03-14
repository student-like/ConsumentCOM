package com.aswipe_menu.student_like.simpletabtutorial.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aswipe_menu.student_like.simpletabtutorial.Database.DatabaseHandler;
import com.aswipe_menu.student_like.simpletabtutorial.Database.DatabaseHandlerHistory;
import com.aswipe_menu.student_like.simpletabtutorial.Product;
import com.aswipe_menu.student_like.simpletabtutorial.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentConsume extends Fragment {

    // ====================== LOG output DEFINITIONS =======================
    String LOG_TAG = FragmentConsume.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    // ====================== DATA-BASE DEFINITIONS =======================
    // save when turning app off
    public static final String MY_PREFS_NAME = "saveParams"; // save last day

    double amount;
    String choice;
    double prevCons[] = new double [6];
    double consAmount[] = new double [] {0.5, 0.33, 0.25, 1, 1, 0.125};
    String articles[] = {"Bier", "Bier", "Wein", "Ofen", "Ziga", "Shot"};

    String dateTime;
    String date;

    // ----------------------- HISTORY DATA-BASE -------------------------
    int id_hist; // only for HISTORY

    public FragmentConsume() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.i(LOG_TAG, "infos: ---------- START --------- CONSUME ----------");

        final DatabaseHandler db = new DatabaseHandler(this.getContext());
        final DatabaseHandlerHistory db_hist = new DatabaseHandlerHistory(this.getContext());

        getDayTime(); // dateTime = date of today

        // --------------  create basic DATABASE --------------
        Log.i(LOG_TAG, "infos: basic array definition...");

        // check if today already exist, else create new row with curr. day
        final String readTemp = SaveLoadDayTime(0);

        if (!readTemp.equals(date)) {
            Log.i(LOG_TAG, "infos: new row created for today...");

            db.addDay(date); // create new table for today
            SaveLoadDayTime(1); // set today as last day used
        }
        else {
            Log.i(LOG_TAG, "infos: a table for today already exists loading prev. vals...");

            for (int i=1;i<6;i++) {
                prevCons[i] = db.loadVal(date, articles[i-1]); // fill consArray with pre-defined vals from today
            }
            SaveLoadDayTime(1); // set today as last day used
        }

        // get product ARRAY from Product.java
        Product product = new Product();
        List <String> productList = product.getProductList();

        // --------------  ADAPTER  STUFF --------------
        final ArrayAdapter <String> productAdapter =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.fragment_consume,// ID der XML-Layout Datei
                        R.id.fragment_consume_text_view, // ID des TextViews
                        productList); // Beispieldaten in einer ArrayList


        View rootView = inflater.inflate(R.layout.fragment_consume, container, false);

        ListView productListView = (ListView) rootView.findViewById(R.id.list);
        productListView.setAdapter(productAdapter);

        // -------------- ON ITEM CLICK  --------------
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // update current date-Time + milliseconds and all
                getDayTime();

                // ======================== adding a product ==========================
                amount = consAmount[position]; // year is amount
                choice = articles[position];

                // ------------------- adding infromation to CONSUMPTION table ---------------------
                prevCons[position] = db.loadVal(date, articles[position]);
                db.updateContact(date, prevCons[position] + amount, articles[position]);

                // ------------------ adding infromation to CONS_HISTORY table ---------------------
                // update id_hist -> length of db_hist table
                id_hist = db_hist.numberOfRows();
                db_hist.addConsumption(dateTime, position); // incl time saved

                // ------------------ update FRAGMENT-HISTORY -----------------------------
                if (mListener != null) {
                    mListener.onFragmentInteraction(articles[position] + " " + amount + " L + " + position + " with id, " + dateTime);
                }
            }
        });

        return rootView;
    }

    // ====================== Day & DATETIME + DATE management =======================
    public String SaveLoadDayTime(int choice)
    {
        String temp_date = "0";

        if(choice == 0) { // LOAD
            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            String restoredText = prefs.getString("lastDayTime", null);
            if (restoredText != null) {
                temp_date = prefs.getString("lastDayTime", "No name defined");//"No name defined" is the default value.
            }
            else{
                temp_date = "0";
            }

            System.out.println("INFOs:: -> reading " + temp_date + " from shared preferences...");
        }
        else // SAVE
        {
            System.out.println("INFOs:: <- writing " + date + " to shared preferences...");
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString("lastDayTime", date);
            editor.apply();
        }
        return temp_date;
    }

    public String getDayTime()
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

        //System.out.println("INFOs:: updating dateTime " + date);

        return conv_date;
    }

    // ======================= FRAGMENT COMMUNICATION ======================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String userContent);
    }


}