package com.wtho.googleplayservices;

import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by WT on 8/31/2016.
 */
public class GPSConstants {

    public static final String PACKAGE_NAME = "com.wtho.googleplayservices";
    public static final String BA_ACTIVITIES_DETECTED = PACKAGE_NAME + ".BA_ACTIVITIES_DETECTED";
    public static final String IE_DETECTED_ACTIVITIES = PACKAGE_NAME + ".IE_DETECTED_ACTIVITIES";

    public static final int LOCATION_DETECTION_INTERVAL = 5000;
    public static final int ACTIVITY_DETECTION_INTERVAL = 5000;

    public static final double LAT_MY_HOME = 16.805391;
    public static final double LNG_MY_HOME = 96.130773;

    public static final double LAT_MY_OFFICE = 16.820667;
    public static final double LNG_MY_OFFICE = 96.124219;

    public static final int GEOFENCE_RADIUS = 100;
    public static final int GEOFENCE_EXPIRATION = 24 * 60 * 60 * 1000;

    public static final HashMap<String, LatLng> GEOFENCES = new HashMap<>();

    static {
        Resources resources = GPSApp.getContext().getResources();
        GEOFENCES.put(resources.getString(R.string.geofence_my_home), new LatLng(LAT_MY_HOME, LNG_MY_HOME));
        GEOFENCES.put(resources.getString(R.string.geofence_my_office), new LatLng(LAT_MY_OFFICE, LNG_MY_OFFICE));
    }
}
