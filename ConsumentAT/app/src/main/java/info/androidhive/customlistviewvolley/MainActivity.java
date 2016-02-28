package info.androidhive.customlistviewvolley;

import info.androidhive.customlistviewvolley.adater.CustomListAdapter;
import info.androidhive.customlistviewvolley.app.AppController;
import info.androidhive.customlistviewvolley.model.Movie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends Activity {

    // ====================== DATA-BASE DEFINITIONS =======================
    // save when turning app off
    public static final String MY_PREFS_NAME = "saveParams";

    double amount;
    int choice;
    double consArray[] = new double [6];
    String articles[] = {"bier", "wein", "ofen", "ziga", "shot"};
    //enum articles {BIER, WEIN, OFEN, ZIGA, SHOT};

    String lastDayTime;
    String dateTime;

    // ====================== LIST-VIEW DEFINITIONS =======================
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    //private static final String url = "http://api.androidhive.info/json/movies.json2";
    private static final String url2 = "http://www.json-generator.com/api/json/get/ceqmosPsde?indent=2";

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d("myTag", url);
        //Log.d("myTag", url2);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        getDayTime(); // dateTime = date of today

        // create new instance of Database
        final DatabaseHandler db = new DatabaseHandler(this);

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

        // when item in list view gets clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                // load before saved object from class movie
                Movie movie = movieList.get(position);

                // get informations from Movie aka product
                amount = movie.getYear(); // year is amount
                choice = movie.getRating(); // rating = product position

                String u = Integer.toString(choice);
                Log.d("INFO: product", u);

                String output = Double.toString(amount);
                Log.d("INFO: amount2", output);

                consArray[choice+1] = consArray[choice+1] + amount;

                db.updateContact(dateTime, consArray[choice+1], articles[choice]);

            }
        });

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Log.d("INFO: WORKS", response.toString());
                        hidePDialog();

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
                VolleyLog.d("INFO: ERROR", "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

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

        dateTime = "29021016";

        System.out.println("INFO: today is day " + dateTime);

        return name;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}