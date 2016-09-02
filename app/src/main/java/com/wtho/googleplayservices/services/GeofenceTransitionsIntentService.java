package com.wtho.googleplayservices.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.utils.GeofenceUtils;
import com.wtho.googleplayservices.utils.NotificationUtils;

import java.util.List;

/**
 * Created by WT on 9/1/2016.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            String errorMsg = GeofenceUtils.getErrorString(event.getErrorCode());
            Log.d(GPSApp.TAG, errorMsg);
            return;
        }
        int geofenceTransitionType = event.getGeofenceTransition();
        List<Geofence> triggeringGeofences = event.getTriggeringGeofences();
        String geofenceDetails = GeofenceUtils.getGeofenceTransitionDetails(triggeringGeofences);
        String geofenceEvent = GeofenceUtils.getGeofenceTransition(geofenceTransitionType) + "-" + geofenceDetails;
        if (geofenceTransitionType == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            NotificationUtils.showNotification(geofenceEvent);
        } else {
            NotificationUtils.showNotification(geofenceEvent);
        }
    }
}
