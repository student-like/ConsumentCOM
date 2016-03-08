package com.aswipe_menu.student_like.simpletabtutorial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class TwoFragment extends Fragment{

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // accessing root
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        // ---------------- fill Array in frag2 --------------
        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_two_list);
        // Defined Array values to show in ListView
        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };
        // Define a new Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ---------------- fill Array in frag2 --------------

        // ---------------- change txt in frag2 --------------
        TextView textview = (TextView) rootView.findViewById(R.id.fragment_two_text_view);
        textview.setText("penis");


        // ---------------- change txt in frag2 --------------

        // Inflate the layout for this fragment
        return rootView;
    }

    public class ListViewAndroidExample extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // ---------------- fill Array in frag2 --------------
            // Get ListView object from xml
            ListView listView = (ListView) findViewById(R.id.fragment_two_list);

            // Defined Array values to show in ListView
            String[] values = new String[]{"Android List View",
                    "Adapter implementation",
                    "Simple List View In Android",
                    "Create List View Android",
                    "Android Example",
                    "List View Source Code",
                    "List View Array Adapter",
                    "Android Example List View"
            };

            // Define a new Adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            // Assign adapter to ListView
            listView.setAdapter(adapter);
        }
    }

}