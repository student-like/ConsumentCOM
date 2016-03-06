package com.aswipe_menu.student_like.swipemenu.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aswipe_menu.student_like.swipemenu.DatabaseHandler;
import com.aswipe_menu.student_like.swipemenu.DatabaseHandlerHistory;
import com.aswipe_menu.student_like.swipemenu.ListView.AppController;
import com.aswipe_menu.student_like.swipemenu.ListView.CustomListAdapter;
import com.aswipe_menu.student_like.swipemenu.ListView.Movie;
import com.aswipe_menu.student_like.swipemenu.MainActivity;
import com.aswipe_menu.student_like.swipemenu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConsumerFragment extends Fragment {

    // ====================== DATA-BASE DEFINITIONS =======================
    // save when turning app off
    public static final String MY_PREFS_NAME = "saveParams"; // save last day

    double amount;
    int choice;
    int id_hist; // only for HISTORY
    double consArray[] = new double [6];
    String articles[] = {"bier", "wein", "ofen", "ziga", "shot"};

    String dateTime;
    String date;

    // ====================== LIST-VIEW DEFINITIONS =======================
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url2 = "http://www.json-generator.com/api/json/get/ceqmosPsde?indent=2";

    // ====================== END OF    DEFINITIONS =======================

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ConsumerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConsumerFragment newInstance(int sectionNumber) {
        ConsumerFragment fragment = new ConsumerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        System.out.println("INFOs:: ON-CREATE-VIEW Consume...");

        // ====================== LIST-VIEW INITIALISATION =======================
        final List<Movie> movieList = new ArrayList<Movie>();

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        final CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), movieList);
        listView.setAdapter(adapter);

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                       // Log.d("INFOs:: WORKS", response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                movie.setRating(obj.getInt("rating"));
                                movie.setYear(obj.getDouble("releaseYear"));

                                // Genre is json array
                                JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);

                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                VolleyLog.d("INFOs:: ERROR", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


        // create new instance of Database
        final DatabaseHandler db = new DatabaseHandler(this.getContext());
        final DatabaseHandlerHistory db_hist = new DatabaseHandlerHistory(this.getContext());

        // ====================== DATABASE INITIALISATION =======================
        getDayTime(); // dateTime = date of today

        // check if today already exist, else create new row with curr. day
        final String readTemp = SaveLoadDayTime(0);

        if (!readTemp.equals(date)) {
            System.out.println("INFOs:: new row created for today...");

            db.addDay(date); // create new table for today

            SaveLoadDayTime(1); // set today as last day used
        }
        else {
            System.out.println("INFOs:: a table for today already exists loading prev. vals...");

            for (int i=1;i<6;i++) {
                consArray[i] = db.loadVal(date, articles[i-1]); // fill consArray with pre-defined vals from today
            }
            SaveLoadDayTime(1); // set today as last day used
        }

        // ====================== LIST VIEW CLICK INITIALISATION =======================
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                // load before saved object from class movie
                Movie movie = movieList.get(position);

                // retreive information from Movie aka product
                amount = movie.getYear(); // year is amount
                choice = movie.getRating(); // rating = product position

                // String u = Integer.toString(choice);
                //Log.d("INFOs:: product", u);

                //String output = Double.toString(amount);
                //Log.d("INFOs:: amount2", output);

                // ------------------- adding infromation to CONSUMPTION table ---------------------
                consArray[choice + 1] = consArray[choice + 1] + amount;
                db.updateContact(date, consArray[choice + 1], articles[choice]);

                // ------------------ adding infromation to CONS_HISTORY table ---------------------
                // update id_hist -> length of db_hist table
                id_hist = db_hist.numberOfRows();
                // add +1 to id_hist to put new value in new row
                db_hist.addConsumption(id_hist + 1, dateTime, position); // incl time saved
            }
        });

        return rootView;
    }

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
        Date now = new Date();
        String name = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN).format(now);

        String[] parts = name.split(" ");
        date = parts[0]; // date without time !

        //dateTime = "06032016";
        dateTime = name;

        System.out.println("INFOs:: today is day " + date);

        return name;
    }

}