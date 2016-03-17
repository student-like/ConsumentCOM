package com.aswipe_menu.student_like.simpletabtutorial.Fragments;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentHistory extends Fragment{

    String LOG_TAG = FragmentConsume.class.getSimpleName();

    // ====================== FragmentHistory DEFINITIONS =======================
    String [] produkteArray = {};
    ArrayAdapter<String> productAdapter_hist;

    // =================== INITIALISATION OF MAIN ==================
    public FragmentHistory() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        Log.i(LOG_TAG, "infos: ---------- START --------- HISTORY ----------");

        final DatabaseHandlerHistory db_hist = new DatabaseHandlerHistory(getActivity());
        final DatabaseHandler db = new DatabaseHandler(getActivity());

        // get product ARRAY from Product.java
        Product product = new Product();
        List <String> consumedProducts = product.getProductList();

        // --------------  create EMPTY LIST --------------
        List<String> productList = new ArrayList<>(Arrays.asList(produkteArray));

        productAdapter_hist =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.fragment_history,// ID der XML-Layout Datei
                        R.id.fragment_history_text_view, // ID des TextViews
                        productList); // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        ListView productListView = (ListView) rootView.findViewById(R.id.list2);
        productListView.setAdapter(productAdapter_hist);

        // --------------  FILL ARRAY WITH HIST VALS --------------
        String temp_prod;
        String temp_dayTime;
        //long temp_rowID;

        long itms2add = db_hist.getNrOfRows();
        Log.i(LOG_TAG, "infos: loading HISTORY values with "+itms2add+" rows...");

        if (itms2add!=0) {
            for (int i = 1; i <= itms2add; i++) {
                temp_prod = db_hist.loadConsumedProduct(i, "last_consumed");
                //Log.i(LOG_TAG, "infos: loading file with PRODUCT '" + temp_prod + "'...");

                // get ids of saved items
                temp_dayTime = db_hist.loadConsumedProduct(i, "dayTime");
                //Log.i(LOG_TAG, "infos: loading file with id '" + temp_dayTime + "'...");

                //temp_rowID = db_hist.loadConsumedProduct2(i);
                //Log.i(LOG_TAG, "infos: loading file with rowID '" + temp_rowID + "'...");

                // adding new product on top of ListView productListView
                productAdapter_hist.insert(consumedProducts.get(Integer.parseInt(temp_prod)) + " L + " + temp_prod + " with id, " + temp_dayTime, 0);

            }
        }

        // ----------------------- ON ITEM CLICK  -----------------------------
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                double prevConsumed;

                // ================ delete product in  database ================
                String temp_str = productAdapter_hist.getItem(position);

                // ---------------- delete in CONSUME database -------------------
                String[] separated_cons = temp_str.split(" ");

                Log.i(LOG_TAG, "infos: CONSUM loading dateTime '" + separated_cons[7] + "' where choice '"+separated_cons[0]+"' and amount '"+separated_cons[1]+"' ...");
                prevConsumed = db.loadVal(separated_cons[7], separated_cons[0]);

                // separated_cons[0] = product
                // separated_cons[1] = amount
                // separated_cons[7] = date

                db.updateContact(separated_cons[7], (prevConsumed - Double.parseDouble(separated_cons[1])), separated_cons[0]);

                // ---------------- delete in HISTOY database -------------------
                String[] separated_hist = temp_str.split(",");
                separated_hist[1] = separated_hist[1].trim();

                //Log.i(LOG_TAG, "infos: HISTORY deleting id " + separated_hist[1] + " ...");
                db_hist.deleteContact(separated_hist[1]);

                // ================ delete product in listView ================
                productAdapter_hist.remove(temp_str);
                productAdapter_hist.notifyDataSetChanged();

                db_hist.doTheVacuum();
            }
        });

        return rootView;
    }

    // ============== FRAGMENT COMMUNICATION ==============
    public void updateTextField(String newText) {

        ListView listView = (ListView) getActivity().findViewById(R.id.list2);
        listView.setAdapter(productAdapter_hist);

        Log.i(LOG_TAG, "infos: adding '" + newText + "' to history ListView...");

        productAdapter_hist.insert(newText, 0);
        productAdapter_hist.notifyDataSetChanged();

    }
}