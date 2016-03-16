package com.aswipe_menu.student_like.simpletabtutorial.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aswipe_menu.student_like.simpletabtutorial.CustomListAdapter;
import com.aswipe_menu.student_like.simpletabtutorial.Database.DatabaseHandler;
import com.aswipe_menu.student_like.simpletabtutorial.Database.DatabaseHandlerHistory;
import com.aswipe_menu.student_like.simpletabtutorial.DayTime.GetDayTime;
import com.aswipe_menu.student_like.simpletabtutorial.Product;
import com.aswipe_menu.student_like.simpletabtutorial.R;
import com.aswipe_menu.student_like.simpletabtutorial.RecyclerViewAdapter;

import org.json.JSONArray;

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

    String LOG_TAG = FragmentConsume.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    double amount;
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

        final GetDayTime getTime = new GetDayTime(getActivity());

        // get date !
        date = getTime.getDayTime(0);

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

        // sending data incl Activity to access to img files in drawable and receiving new adapter
        RecyclerViewAdapter setNewAdapter = new RecyclerViewAdapter(this.getActivity(), productListWoa);

        productListView.setAdapter(setNewAdapter);


        /*// ------------------------ ON ITEM CLICK  ----------------------------
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // update current date-Time + milliseconds and all
                getDayTime();

                amount = consAmount[position]; // year is amount

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
        });*/

        return rootView;
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