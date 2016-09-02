package com.wtho.googleplayservices.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.R;

/**
 * Created by WT on 9/1/2016.
 */
public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private GoogleMap mMap;

    public static Intent newIntent() {
        Intent intent = new Intent(GPSApp.getContext(), MapActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(16.7983545, 96.1474433);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Shwe Dagon Pagoda"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
