package com.wtho.googleplayservices.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.utils.GPSConstants;
import com.wtho.googleplayservices.utils.GeofenceUtils;
import com.wtho.googleplayservices.controllers.ControllerActivityRecognition;

import java.util.ArrayList;

/**
 * Created by WT on 8/31/2016.
 */
public class BaseActivity extends AppCompatActivity
        implements ControllerActivityRecognition,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int ACCESS_PERMISSIONS_LOCATION_REQUEST = 1;
    private static final int ACCESS_PERMISSIONS_LAST_LOCATION = 2;
    private static final int ACCESS_PERMISSIONS_SETUP_GEOFENCES = 3;
    private static final int ACCESS_PERMISSIONS_CURRENT_PLACE_NAME = 4;
    protected static final int REQUEST_CODE_PLACE_PICKER = 11;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private ActivityDetectionBroadcastReceiver mActivityDetectionBR;
    private GeofenceResultCallback mGeofenceResultCallback;

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

    public void setupGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google Api Client is not connected", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_PERMISSIONS_SETUP_GEOFENCES);
                return;
            }
            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                    GeofenceUtils.getGeofencingRequest(),
                    GeofenceUtils.getGeofencePendingIntent()).setResultCallback(mGeofenceResultCallback);
        }
    }

    public void getCurrentPlaceName() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_PERMISSIONS_CURRENT_PLACE_NAME);
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                onGetCurrentPlaceName(placeLikelihoods);
            }

            private void onGetCurrentPlaceName(PlaceLikelihoodBuffer placeLikelihoods) {
            }
        });
    }

    /**
     * make location request with runtime permission checking
     */
    private void makeLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(GPSConstants.LOCATION_DETECTION_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_PERMISSIONS_LOCATION_REQUEST);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void onPlacePicked(Place pickedPlace) {
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

    protected void onLocationRetrieved(Location location) {
    }

    protected void onActivitiesDetected(ArrayList<DetectedActivity> detectedActivities) {

    }

    protected void onGetCurrentPlaceName(PlaceLikelihoodBuffer likelyPlaces) {

    }

    private Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_PERMISSIONS_LAST_LOCATION);
            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    protected void startPlacePicker() {
        try {
            Intent intent = new PlacePicker.IntentBuilder().build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(GPSApp.TAG, e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(GPSApp.TAG, e.getMessage());
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


    @Override
    public void requestActivityUpdate() {

    }

    @Override
    public void removeActivityUpdate() {

    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> detectedActivities = intent.getParcelableArrayListExtra(GPSConstants.IE_DETECTED_ACTIVITIES);
            if (detectedActivities != null) {
                onActivitiesDetected(detectedActivities);
            }
        }
    }

    public static class ActivityRecognitionResultCallback implements ResultCallback<Status> {
        @Override
        public void onResult(@NonNull Status status) {
            if (status.isSuccess()) {
                Toast.makeText(GPSApp.getContext(), "Successfull", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GPSApp.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class GeofenceResultCallback implements ResultCallback<Status> {
        @Override
        public void onResult(@NonNull Status status) {
            if (status.isSuccess()) {
                Toast.makeText(GPSApp.getContext(), "Geofences Added", Toast.LENGTH_SHORT).show();
            } else {
                String errorMsg = GeofenceUtils.getErrorString(status.getStatusCode());
                Log.d(GPSApp.TAG, errorMsg);
                Toast.makeText(GPSApp.getContext(), GPSApp.TAG + "-" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
