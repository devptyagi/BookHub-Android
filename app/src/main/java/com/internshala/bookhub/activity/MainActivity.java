package com.internshala.bookhub.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.widget.*;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.internshala.bookhub.fragment.AboutFragment;
import com.internshala.bookhub.fragment.DashboardFragment;
import com.internshala.bookhub.fragment.FavoritesFragment;
import com.internshala.bookhub.fragment.ProfileFragment;
import com.internshala.bookhub.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    CoordinatorLayout coordinatorLayout;
    FrameLayout frameLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    MenuItem previousMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView)findViewById(R.id.navigationView);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        frameLayout = (FrameLayout)findViewById(R.id.frame);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        previousMenuItem = null;
        setToolbar();

        openDashboard();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(previousMenuItem != null) {
                    previousMenuItem.setChecked(true);
                }

                item.setCheckable(true);
                item.setChecked(true);
                previousMenuItem = item;

                switch (item.getItemId()) {
                    case R.id.dashboard :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DashboardFragment()).commit();
                        getSupportActionBar().setTitle("Dashboard");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.favorites :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FavoritesFragment()).commit();
                        getSupportActionBar().setTitle("Favorites");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ProfileFragment()).commit();
                        getSupportActionBar().setTitle("Profile");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new AboutFragment()).commit();
                        getSupportActionBar().setTitle("About");
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    protected void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ACM Portal");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.frame);
        if(!(frag instanceof DashboardFragment)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DashboardFragment()).commit();
            getSupportActionBar().setTitle("Dashboard");
        }else {
            super.onBackPressed();
        }
    }

    public void openDashboard() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DashboardFragment()).commit();
        getSupportActionBar().setTitle("Dashboard");
        navigationView.setCheckedItem(R.id.dashboard);
    }
}
