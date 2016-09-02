package com.wtho.googleplayservices.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.utils.GPSConstants;

import java.util.ArrayList;

/**
 * Created by WT on 9/1/2016.
 */
public class DetectedActivitiesIntentService extends IntentService {
    public DetectedActivitiesIntentService() {
        super(DetectedActivitiesIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        ArrayList<DetectedActivity> detectedActivities = (ArrayList<DetectedActivity>) result.getProbableActivities();

        Log.d(GPSApp.TAG, "Activities Detected");
        Intent localIntent = new Intent(GPSConstants.BA_ACTIVITIES_DETECTED);
        localIntent.putExtra(GPSConstants.IE_DETECTED_ACTIVITIES, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
