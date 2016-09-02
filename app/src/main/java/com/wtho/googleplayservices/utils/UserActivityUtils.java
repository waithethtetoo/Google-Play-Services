package com.wtho.googleplayservices.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;

import com.google.android.gms.location.DetectedActivity;
import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.R;

/**
 * Created by WT on 9/1/2016.
 */
public class UserActivityUtils {

    public static String getActivityByType(int type) {
        Resources resources = GPSApp.getContext().getResources();
        switch (type) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidetifiable);
        }
    }

    public static PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(GPSApp.getContext(), DetectedActivity.class);
        return PendingIntent.getService(GPSApp.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
