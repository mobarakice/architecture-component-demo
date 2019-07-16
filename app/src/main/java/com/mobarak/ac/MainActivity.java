package com.mobarak.ac;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;

    public DrawerLayout drawerLayout;
    public DrawerNavigationView navigationView;
    //    public NavController navController;
    private LiveData<NavController> navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigation();
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    // Setting Up One Time Navigation
    private void setupNavigation() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);


//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
//
//        NavigationUI.setupWithNavController(navigationView, navController);

//        navigationView.setNavigationItemSelectedListener(this);
        setupBottomNavigationBar();


    }

    private void setupBottomNavigationBar() {

        List<Integer> navGraphIds = new ArrayList<>();
        navGraphIds.add(R.navigation.home);
        navGraphIds.add(R.navigation.list);
        navGraphIds.add(R.navigation.form);


        // Setup the bottom navigation view with a list of navigation graphs
        LiveData<NavController> controller = NavigationExtensions.setupWithNavController(
                navigationView,
                navGraphIds,
                getSupportFragmentManager(),
                R.id.nav_host_container,
                getIntent()
        );

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, navController -> {
            NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        });
        navController = controller;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null ? navController.getValue().navigateUp() : false;
    }
}
