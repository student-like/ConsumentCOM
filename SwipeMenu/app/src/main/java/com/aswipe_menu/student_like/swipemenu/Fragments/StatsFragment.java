package com.aswipe_menu.student_like.swipemenu.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aswipe_menu.student_like.swipemenu.MainActivity;
import com.aswipe_menu.student_like.swipemenu.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatsFragment extends Fragment {

    // ====================== LIST-VIEW DEFINITIONS =======================
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url2 = "http://www.json-generator.com/api/json/get/ceqmosPsde?indent=2";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public StatsFragment() {
       // System.out.println("INFOs:: Constructor...");
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatsFragment newInstance(int sectionNumber) {
        //System.out.println("INFOs:: HistoryFragment...");

        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //System.out.println("INFOs:: onCERATE...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        System.out.println("INFOs:: OnCreateView STATS...");

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("< HERE ARE THE STATS >");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //System.out.println("INFOs:: OnVIEWCREATED STATS...");

        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }


}