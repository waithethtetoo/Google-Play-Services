package com.wtho.googleplayservices.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.services.GeofenceTransitionsIntentService;
import com.wtho.googleplayservices.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WT on 9/1/2016.
 */
public class GeofenceUtils {
    public static String getErrorString(int errorCode) {
        Resources resources = GPSApp.getContext().getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return resources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return resources.getString(R.string.geofence_registered_too_many);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return resources.getString(R.string.geofence_provided_too_many_pending_intent);
            default:
                return resources.getString(R.string.geofence_unkown_error);
        }
    }

    public static String getGeofenceTransitionDetails(List<Geofence> geofencesList) {
        StringBuilder geofenceDetails = new StringBuilder();
        for (Geofence geofence : geofencesList) {
            geofenceDetails.append(geofence.getRequestId() + ",");
        }
        return geofenceDetails.toString();
    }

    public static String getGeofenceTransition(int geofenceTransition) {
        Resources resources = GPSApp.getContext().getResources();
        switch (geofenceTransition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return resources.getString(R.string.geofence_entered_into);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return resources.getString(R.string.geofence_exit);
            default:
                return resources.getString(R.string.geofence_unkown_transition);
        }
    }

    public static GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(populateDummyGeofenceList());
        return builder.build();
    }

    public static PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(GPSApp.getContext(), GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(GPSApp.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private static List<Geofence> populateDummyGeofenceList() {
        List<Geofence> geofenceList = new ArrayList<>();
        for (Map.Entry<String, LatLng> entry : GPSConstants.GEOFENCES.entrySet()) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(entry.getValue().latitude, entry.getValue().longitude, GPSConstants.GEOFENCE_RADIUS)
                    .setExpirationDuration(GPSConstants.GEOFENCE_EXPIRATION)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
        return geofenceList;
    }
}
