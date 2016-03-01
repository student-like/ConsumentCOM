package com.action_bar.student_like.myapplication;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

// for console output
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

import com.action_bar.student_like.myapplication.ListView.AppController;
import com.action_bar.student_like.myapplication.ListView.CustomListAdapter;
import com.action_bar.student_like.myapplication.ListView.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // ====================== DATA-BASE DEFINITIONS =======================
    // save when turning app off
    public static final String MY_PREFS_NAME = "saveParams";

    double amount;
    int choice;
    double consArray[] = new double [6];
    String articles[] = {"bier", "wein", "ofen", "ziga", "shot"};

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
    // ====================== END OF NEW DEFINITIONS =======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create new instance of Database
        final DatabaseHandler db = new DatabaseHandler(this);

        // set activity_main.xml as layout
        setContentView(R.layout.activity_main);

        // reference to app_bar_main.xml -> @id/toolbar
        // defining layout for toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // reference to activity_main.xml -> @id/drawer_layout = SEITENMENÜ
        // defining layout for drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // public constructor -> ActionBarDrawerToggle (Activity activity, DrawerLayout drawerLayout, int drawerImageRes,
        // int openDrawerContentDescRes, int closeDrawerContentDescRes)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // reference to activity_main.xml -> @id/nav_view = UNTER DRAWER LAYOUT
        // defining layout for navigation view in drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Set a listener that will be notified when a menu item is clicked...
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        getDayTime(); // dateTime = date of today

        // check if today already exist, else create new row with curr. day
        final String readTemp = SaveLoadDayTime(0);

        if (!readTemp.equals(dateTime)) {
            db.addDay(dateTime); // create new table for today

            SaveLoadDayTime(1); // set today as last day used
            System.out.println("INFOs: new row created for today...");
        }
        else {
            System.out.println("INFOs: a table for today already exists loading prev. vals...");

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
                Log.d("INFOs: product", u);

                String output = Double.toString(amount);
                Log.d("INFOs: amount2", output);

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
                        Log.d("INFOs: WORKS", response.toString());
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
                VolleyLog.d("INFOs: ERROR", "Error: " + error.getMessage());
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

            System.out.println("INFOs: -> reading " + lastDayTime + " from shared preferences...");
        }
        else // SAVE
        {
            System.out.println("INFOs: <- writing " + dateTime + " to shared preferences...");
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

        dateTime = "28021016";

        System.out.println("INFOs: today is day " + dateTime);

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Allows you to interact with Fragments in an Activity
        FragmentManager fragmentManager = getFragmentManager();

        // find definitions in activity_main_drawer.xml
        if (id == R.id.nav_camera) {
            // create new "console output"
            Log.d("myTag", "Changing to SecondActivity...");


        }
        else if (id == R.id.nav_gallery) {
            // create new "console output"
            Log.d("myTag", "Changing to MainActivity...");

            Intent intent = new Intent(this,
                    SecondActivity.class);

            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
