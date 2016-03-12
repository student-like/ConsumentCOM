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

import com.aswipe_menu.student_like.simpletabtutorial.DatabaseHandlerHistory;
import com.aswipe_menu.student_like.simpletabtutorial.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentHistory extends Fragment{

    String LOG_TAG = FragmentConsume.class.getSimpleName();

    // ====================== DATA-BASE DEFINITIONS =======================
    int numbOfRows = 0;// return number of rows of HISTORY, is totally conusmed products

    // ====================== FragmentHistory DEFINITIONS =======================
    String [] produkteArray = {};
    String [] consumedProducts = {
            "Bier 0.5",
            "Bier 0.33",
            "Wein 1/8",
            "Ofen 1x",
            "Ziga 1x",
            "Shot 1x"
    };

    // ====================== ADAPTER DEFINITIONS =======================
    ArrayAdapter<String> productAdapter_hist;

    // =================== INITIALISATION OF MAIN ==================
    public FragmentHistory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final DatabaseHandlerHistory db_hist = new DatabaseHandlerHistory(this.getContext());

        // --------------  create EMPTY LIST --------------
        long itms2add = db_hist.getNrOfRows();
        Log.i(LOG_TAG, "infos: loading HISTORY values with "+itms2add+" rows...");

        List<String> productList = new ArrayList<>(Arrays.asList(produkteArray));

        productAdapter_hist =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.history_fragment ,// ID der XML-Layout Datei
                        R.id.fragment_one_text_view, // ID des TextViews
                        productList); // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.history_fragment, container, false);

        ListView productListView = (ListView) rootView.findViewById(R.id.list2);
        productListView.setAdapter(productAdapter_hist);

        // --------------  FILL ARRAY WITH HIST VALS --------------
        int temp_val;
        int temp_id;

        if (itms2add!=0) {
            for (int i = 1; i <= itms2add; i++) {
                temp_val = db_hist.loadConsumedProduct(i);

                // get ids of saved items
                temp_id = db_hist.loadProduct_ID(i);
                Log.i(LOG_TAG, "infos: loading file with _id " + temp_id + "...");

                productAdapter_hist.add(consumedProducts[temp_val] + "L + " + temp_val + " with _id, " + temp_id);

            }
        }

        // -------------- ON ITEM CLICK  --------------
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                // -------------- delete product in database --------------
                String temp_str = productAdapter_hist.getItem(position);

                String[] separated = temp_str.split(",");
                // trimm string from space after ","
                separated[1] = separated[1].trim();

                Log.i(LOG_TAG, "infos: deleting id " + separated[1] + " minus " + numbOfRows + "...");

                db_hist.deleteContact(Integer.parseInt(separated[1]));

                // -------------- delete product in listView --------------
                productAdapter_hist.remove(temp_str);
                productAdapter_hist.notifyDataSetChanged();

                //Log.i(LOG_TAG, "infos: inserting " + product2insert + " ...");
            }
        });

        return rootView;
    }

    // ============== FRAGMENT COMMUNICATION ==============
    public void updateTextField(String newText) {

        ListView listView = (ListView) getActivity().findViewById(R.id.list2);
        listView.setAdapter(productAdapter_hist);

        //String temp_str = productAdapter_hist.getItem(1);

        //Log.i(LOG_TAG, "infos: loading <<<< " + temp_str + " >>>>> TRANSMISSION <<< " + newText + " >>> rows...");

        productAdapter_hist.add("FIRST TETX");

        productAdapter_hist.notifyDataSetChanged();

    }
}