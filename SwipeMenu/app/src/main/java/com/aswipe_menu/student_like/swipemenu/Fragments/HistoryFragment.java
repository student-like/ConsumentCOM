package com.aswipe_menu.student_like.swipemenu.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aswipe_menu.student_like.swipemenu.DatabaseHandlerHistory;
import com.aswipe_menu.student_like.swipemenu.ListView.AppController;
import com.aswipe_menu.student_like.swipemenu.ListView.CustomListAdapter;
import com.aswipe_menu.student_like.swipemenu.ListView.Movie;
import com.aswipe_menu.student_like.swipemenu.MainActivity;
import com.aswipe_menu.student_like.swipemenu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryFragment extends Fragment {

    // ====================== DATA-BASE DEFINITIONS =======================
    int numbOfRows = 0;// return number of rows of HISTORY, is totally conusmed products

    // ====================== LIST-VIEW DEFINITIONS =======================
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url2 = "http://www.json-generator.com/api/json/get/ceqmosPsde?indent=2";
    private ProgressDialog pDialog;

    // ====================== END OF    DEFINITIONS =======================

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public HistoryFragment() {}

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryFragment newInstance(int sectionNumber) {

        //System.out.println("INFOs:: new_instance HISTORY...");
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

   /* @Override
    public void onPause() {
        //System.out.println("INFOs:: HISTORY is on pause...");
        super.onPause();
    }

    @Override
    public void onResume() {
        System.out.println("INFOs:: HISTORY is on resume...");
        super.onResume();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pDialog = new ProgressDialog(this.getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        System.out.println("INFOs:: ON-CREATE-VIEW History...");

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // ---------------------- load HISTORY into array ---------------------
        final DatabaseHandlerHistory db_hist = new DatabaseHandlerHistory(this.getContext());
        numbOfRows = db_hist.numberOfRows();

        // ====================== LIST-VIEW INITIALISATION =======================
        final List<Movie> movieList = new ArrayList<Movie>();

        final ListView listView_hist = (ListView) rootView.findViewById(R.id.list);
        final CustomListAdapter adapter_hist = new CustomListAdapter(this.getActivity(), movieList ,1);
        listView_hist.setAdapter(adapter_hist);

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //Log.d("INFOs:: WORKS", response.toString());

                        //long temp = db_hist.getNrOfRows();
                        //System.out.println("INFOs:: OUTPUT of ROWID " + temp +  "... -> HISTORY");
                        //System.out.println("INFOs:: numBofRows " + numbOfRows + "... -> HISTORY");

                        // Parsing json
                        for (int i = (numbOfRows); i > 0; i--) {
                            try {

                                // load previously saved values
                                int return_temp = db_hist.loadConsumedProduct(i);
                                //System.out.println("INFOs:: READING FROM consumption_history " + return_temp +  " rowNumb " +i+ " ... -> HISTORY");

                                JSONObject obj = response.getJSONObject(return_temp);
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
                        adapter_hist.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                VolleyLog.d("INFOs:: ERROR HistoryFragment", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
        hidePDialog();

        // ====================== LIST VIEW CLICK INITIALISATION =======================
        listView_hist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //System.out.println("INFOs:: ID for deletion is "+id+" minus "+numbOfRows+"...");
                db_hist.deleteContact((int) (long) (numbOfRows - id));
                db_hist.doTheVacuum();

                HistoryFragment.newInstance(position);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //System.out.println("INFOs:: OnVIEWCREATED HISTORY...");
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
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
}