package com.wtho.googleplayservices;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by WT on 8/31/2016.
 */
public class BaseActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int ACCESS_PERMISSIONS_LOCATION_REQUEST = 1;
    private static final int ACCESS_PERMISSIONS_LAST_LOCATION = 2;
    private static final int ACCESS_PERMISSIONS_SETUP_GEOFENCES = 3;
    private static final int ACCESS_PERMISSIONS_CURRENT_PLACE_NAME = 4;
    protected static final int REQUEST_CODE_PLACE_PICKER = 11;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place pickedPlace = PlacePicker.getPlace(getApplicationContext(), data);
                onPlacePicked(pickedPlace);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mActivityDetectionBR,
                new IntentFilter(GPSConstants.BA_ACTIVITIES_DETECTED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mActivityDetectionBR);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_PERMISSIONS_LOCATION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeLocationRequest();
                }
                return;
            }
            case ACCESS_PERMISSIONS_LAST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location location = getLastLocation();
                    onLocationRetrieved(location);
                }
                break;
            }
            case ACCESS_PERMISSIONS_SETUP_GEOFENCES: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupGeofences();
                }
                break;
            }
            case ACCESS_PERMISSIONS_CURRENT_PLACE_NAME: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentPlaceName();
                }
                break;
            }
        }
    }

    /**
     * make location request with runtime permission checking
     */
    private void makeLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(GPSConstants.LOCATION_DETECTION_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCEESSS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED {

        }
    }

    private void onPlacePicked(Place pickedPlace) {
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = getLastLocation();
        if (location != null) {
            onLocationRetrieved(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(GPSApp.TAG, "GPS onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(GPSApp.TAG, "GPS onLocationChange");
        onLocationRetrieved(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(GPSApp.TAG, "GPS onConnectionFailed");
    }


}
