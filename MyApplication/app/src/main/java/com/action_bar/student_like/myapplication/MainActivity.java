package com.action_bar.student_like.myapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

// for console output
import android.util.Log;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set activity_main.xml as layout
        setContentView(R.layout.activity_main);

        // reference to app_bar_main.xml -> @id/toolbar
        // defining layout for toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // reference to activity_main.xml -> @id/drawer_layout = SEITENMENÜ
        // defining layout for drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // public constructor -> ActionBarDrawerToggle (Activity activity, DrawerLayout drawerLayout, int drawerImageRes,
        // int openDrawerContentDescRes, int closeDrawerContentDescRes)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // reference to activity_main.xml -> @id/nav_view = UNTER DRAWER LAYOUT
        // defining layout for navigation view in drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Set a listener that will be notified when a menu item is clicked...
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Allows you to interact with Fragments in an Activity
        FragmentManager fragmentManager = getFragmentManager();

        // find definitions in activity_main_drawer.xml
        if (id == R.id.nav_camera) {
            // create new "console output"
            Log.d("myTag", "Changing to SecondActivity...");


        }
        else if (id == R.id.nav_gallery) {
            // create new "console output"
            Log.d("myTag", "Changing to MainActivity...");

            Intent intent = new Intent(this,
                    SecondActivity.class);

            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}