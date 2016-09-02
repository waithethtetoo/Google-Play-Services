package com.wtho.googleplayservices.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.wtho.googleplayservices.fragments.ActivityRecognitionFragment;
import com.wtho.googleplayservices.fragments.GeofenceFragment;
import com.wtho.googleplayservices.fragments.LocationFragment;
import com.wtho.googleplayservices.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LocationFragment.ControllerLocationScreen,
        GeofenceFragment.ControllerGeofenceScreen {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.google_play_services);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            navigateToLocation();
        }
    }

    private void navigateToLocation() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new LocationFragment(), LocationFragment.TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onLocationRetrieveed(Location location) {
        super.onLocationRetrieved(location);
        LocationFragment locationFragment = (LocationFragment)
                getSupportFragmentManager().findFragmentByTag(LocationFragment.TAG);
        if (locationFragment != null) {
            locationFragment.setLocation(location);
        } else {
            Toast.makeText(getApplicationContext(), "can't find location by tag", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivitiesDetected(ArrayList<DetectedActivity> detectedActivities) {
        super.onActivitiesDetected(detectedActivities);
        ActivityRecognitionFragment activityRecognitionFragment =
                (ActivityRecognitionFragment) getSupportFragmentManager().findFragmentByTag(ActivityRecognitionFragment.TAG);
        if (activityRecognitionFragment != null) {
            activityRecognitionFragment.setDetectedActivities(detectedActivities);
        } else {
            Toast.makeText(getApplicationContext(), "can't find activity by tag", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onPlacePicked(Place pickedPlace) {
        super.onPlacePicked(pickedPlace);
        String tostMsg = String.format("Place : %s", pickedPlace.getAddress());
        Toast.makeText(getApplicationContext(), tostMsg, Toast.LENGTH_SHORT).show();
    }

    protected void onGetCurrentPlaceName(PlaceLikelihoodBuffer likelyPlaces) {
        super.onGetCurrentPlaceName(likelyPlaces);
        LocationFragment locationFragment = (LocationFragment) getSupportFragmentManager().findFragmentByTag(LocationFragment.TAG);
        if (locationFragment != null) {
            locationFragment.setLikelyNamesForCurrentPlace(likelyPlaces);
        } else {
            Toast.makeText(getApplicationContext(), "Can't find location by fragment tag", Toast.LENGTH_SHORT).show();
        }
    }

    public void onTapAddGeofences() {
        setupGeofences();
    }

    public void onTapStartPlacePicker() {
        startPlacePicker();
    }

    public void onTapGetCurrentPlaceName() {
        getCurrentPlaceName();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.gps_user_current_location:
                item.setCheckable(true);
                navigateToLocation();
                return true;
            case R.id.gps_activity_recognition:
                item.setCheckable(true);
                navigateToActivityRecognition();
                return true;
            case R.id.gps_geofences:
                item.setCheckable(true);
                navigateToGeofence();
                return true;
            case R.id.gps_map_activity:
                navigateToMapActivity();
                return true;
        }
        return false;
    }

    private void navigateToMapActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = MapActivity.newIntent();
                startActivity(intent);
            }
        }, 250);
    }

    private void navigateToGeofence() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new GeofenceFragment(), GeofenceFragment.TAG)
                .commit();
    }

    private void navigateToActivityRecognition() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new ActivityRecognitionFragment(), ActivityRecognitionFragment.TAG)
                .commit();
    }
}
