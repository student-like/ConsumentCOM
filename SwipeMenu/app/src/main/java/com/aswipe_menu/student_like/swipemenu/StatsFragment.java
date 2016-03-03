package com.aswipe_menu.student_like.swipemenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatsFragment extends Fragment {

    // ====================== LIST-VIEW DEFINITIONS =======================
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url2 = "http://www.json-generator.com/api/json/get/ceqmosPsde?indent=2";

    private ProgressDialog pDialog;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public StatsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatsFragment newInstance(int sectionNumber) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("< HERE ARE THE STATS >");

        return rootView;
    }

}