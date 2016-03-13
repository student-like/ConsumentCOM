package com.aswipe_menu.student_like.simpletabtutorial;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.aswipe_menu.student_like.simpletabtutorial.Fragments.FragmentHistory;
import com.aswipe_menu.student_like.simpletabtutorial.Fragments.FragmentThree;
import com.aswipe_menu.student_like.simpletabtutorial.Fragments.FragmentConsume;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentConsume.OnFragmentInteractionListener {

    private ViewPager mViewPager;
    // LOG OUTPUT
    String LOG_TAG = MainActivity.class.getSimpleName();

    FragmentHistory global_hist = new FragmentHistory();

    // ============== INITIALISATION OF MAIN ==============
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // takes a layout and it set the view as the layout to the activity
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // disable <- back button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // start application on tab/ view 2
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(3);
    }

    // ============== FRAGMENT COMMUNICATION ==============
    @Override
    public void onFragmentInteraction(String userContent) {

        //Log.i(LOG_TAG, "infos: receving '" + userContent + "' from FragmentConsumption...");

       /* FragmentHistory secondFragment =
                (FragmentHistory) getSupportFragmentManager().findFragmentById(R.id.fragmentA);

        secondFragment.updateTextField(userContent);*/

        global_hist.updateTextField(userContent);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(global_hist, "HISTORY");
        adapter.addFragment(new FragmentConsume(), "CONSUME");
        adapter.addFragment(new FragmentThree(), "STATIST");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
