package com.aswipe_menu.student_like.simpletabtutorial.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aswipe_menu.student_like.simpletabtutorial.Database.DatabaseHandler;
import com.aswipe_menu.student_like.simpletabtutorial.Database.DatabaseHandlerHistory;
import com.aswipe_menu.student_like.simpletabtutorial.DayTime.GetDayTime;
import com.aswipe_menu.student_like.simpletabtutorial.Product;
import com.aswipe_menu.student_like.simpletabtutorial.R;
import com.aswipe_menu.student_like.simpletabtutorial.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentConsume extends Fragment {

    String LOG_TAG = FragmentConsume.class.getSimpleName();

    // ----------------------- HISTORY DATA-BASE -------------------------
    int id_hist; // only for HISTORY

    // ----------------------- LOCAL DEFINITIONS -------------------------
    private OnFragmentInteractionListener inti_mListener;

    double amount;
    double prevCons[] = new double [6];
    double consAmount[] = new double [] {0.5, 0.33, 0.25, 1, 1, 0.125};
    String articles[] = {"Bier", "Bier", "Wein", "Ofen", "Ziga", "Shot"};

    //Context context;


    public FragmentConsume() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.i(LOG_TAG, "infos: ---------- START --------- CONSUME ----------");

        /*if (context == null){
            Log.i(LOG_TAG, "infos: OnCreate - context is null");
        }*/
        if (getActivity() == null){
            Log.i(LOG_TAG, "infos: OnCreate - getActivity is null");
        }

        // first declare DatabaseHandlers here to be able to getConetxt()
        DatabaseHandler db = new DatabaseHandler(getActivity());

        GetDayTime getTime = new GetDayTime(getActivity());

        // TODO send date & dateTime in array
        String date = getTime.getDayTime(0);

        // --------------  create basic DATABASE --------------
        Log.i(LOG_TAG, "infos: basic array definition...");

        // check if today already exist, else create new row with curr. day
        final String readTemp = getTime.SaveLoadDayTime(0);

        if (!readTemp.equals(date)) {
            Log.i(LOG_TAG, "infos: new row created for today...");

            db.addDay(date); // create new table for today
            getTime.SaveLoadDayTime(1); // set today as last day used
        }
        else {
            Log.i(LOG_TAG, "infos: a table for today already exists loading prev. vals...");

            for (int i=1;i<6;i++) {
                prevCons[i] = db.loadVal(date, articles[i-1]); // fill consArray with pre-defined vals from today
            }
            getTime.SaveLoadDayTime(1); // set today as last day used
        }

        // get product ARRAY from Product.java
        Product product = new Product();

        List <Product> productListWoa = new ArrayList<Product>();
        List <String> productList = product.getProductList();

        //final CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), productListWoa, 0);

        // get size of productList and fill listView with existing Products
        for (int i = 0; i < productList.size(); i++) {

            Product temp_product = new Product();

            temp_product.setTitle(productList.get(i));
            temp_product.setThumbnailUrl("image");
            temp_product.setRating(0);
            temp_product.setYear(0);

            // adding movie to movies array
            productListWoa.add(temp_product);
        }
        //adapter.notifyDataSetChanged();

        Product temp_product = new Product();
        temp_product.setTitle("WHATEVER ESXRTA");
        temp_product.setThumbnailUrl("image");
        temp_product.setRating(0);
        temp_product.setYear(0);
        // adding movie to movies array
        productListWoa.add(temp_product);

        // create rootView referencing to RecyclerView in fragment_consume.xml
        View rootView = inflater.inflate(R.layout.fragment_consume, container, false);

        // creating productListView with layout from  list_recy in fragment_consume_xml
        RecyclerView productListView = (RecyclerView) rootView.findViewById(R.id.list_recy);
        // set LayoutManager otherwise error...
        productListView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: getActivity and mListener have to be passed on to RecycleViewAdapter to then come back to the click Action function
        // sending data incl Activity to access to img files in drawable and receiving new adapter
        RecyclerViewAdapter setNewAdapter = new RecyclerViewAdapter(this.getActivity(), productListWoa, inti_mListener);

        productListView.setAdapter(setNewAdapter);

        /*// ------------------------ ON ITEM CLICK  ----------------------------
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                }

            });*/

        return rootView;
    }

    public void clickAction (int position, Activity activityImp, OnFragmentInteractionListener mListener){
        // ------------------------ ON ITEM CLICK  ----------------------------

        GetDayTime getTime = new GetDayTime(getActivity());

        DatabaseHandler db = new DatabaseHandler(activityImp);
        DatabaseHandlerHistory db_hist = new DatabaseHandlerHistory(activityImp);

        /*if (getActivity() == null){
            Log.i(LOG_TAG, "infos: activity is null");
        }
        if (activityImp == null){
            Log.i(LOG_TAG, "infos: activityImp ! is null");
        }
        if (this.getActivity() == null){
            Log.i(LOG_TAG, "infos: this.getACtivity is null");
        }*/

        String date = getTime.getDayTime(0);
        String dateTime = getTime.getDayTime(1);

       /* db.updateContact("2016-03-16", 55, "Bier");

        Log.i(LOG_TAG, "infos: articles position is " + articles[position]);
        Log.i(LOG_TAG, "infos: loaded date is "+ date);

        double temp_load = db.loadVal("2016-03-16", "Bier");
        temp_load = db.loadVal(date, articles[position]);*/

        //Log.i(LOG_TAG, "infos: position is "+ position);


        // update current date-Time + milliseconds and all
       // Log.i(LOG_TAG, "infos: get dayTime " + dateTime + "...");
        //Log.i(LOG_TAG, "infos: get date " + date + "...");

        // ------------------- adding infromation to CONSUMPTION table ---------------------
        prevCons[position] = db.loadVal(date, articles[position]);
        db.updateContact(date, prevCons[position] + amount, articles[position]);

        // ------------------ adding infromation to CONS_HISTORY table ---------------------
        amount = consAmount[position]; // year is amount

        // ------------------ adding infromation to CONS_HISTORY table ---------------------
        // update id_hist -> length of db_hist table
        id_hist = db_hist.numberOfRows();
        db_hist.addConsumption(dateTime, position); // incl time saved

        // ------------------ update FRAGMENT-HISTORY -----------------------------
        if (mListener != null) {
            Log.i(LOG_TAG, "infos: adding 1  to history ListView...");

            mListener.onFragmentInteraction(articles[position] + " " + amount + " L + " + position + " with id, " + dateTime);
        }
    }

    // ======================= FRAGMENT COMMUNICATION ======================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(LOG_TAG, "infos: ATTACH...");

        try {
            Log.i(LOG_TAG, "infos: ATTACHATTACHATTACHATTACHATTACH...");
            inti_mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.i(LOG_TAG, "infos: DEATTACH...");

        super.onDetach();
        inti_mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String userContent);
    }


}