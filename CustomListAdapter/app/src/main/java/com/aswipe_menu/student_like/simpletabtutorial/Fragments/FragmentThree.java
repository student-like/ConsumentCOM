package com.aswipe_menu.student_like.simpletabtutorial.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aswipe_menu.student_like.simpletabtutorial.R;


public class FragmentThree extends Fragment{

    String LOG_TAG = FragmentConsume.class.getSimpleName();

    public FragmentThree() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "infos: ---------- START --------- THREE ----------");

        // Inflate the layout for this fragment
        return null;
    }

}