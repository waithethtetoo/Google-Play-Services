package com.wtho.googleplayservices.utils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.wtho.googleplayservices.GPSApp;

/**
 * Created by WT on 9/1/2016.
 */
public class FAUtils {
    public static final String ACTION_TAP_GET_CURRENT_PLACE_NAME = "Get Current Place Name";
    private static FAUtils objInstance;
    private FirebaseAnalytics mFirebaseAnalytics;

    public FAUtils() {
        initFA();
    }

    private void initFA() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(GPSApp.getContext());
    }

    public static FAUtils getObjInstance() {
        if (objInstance == null) {
            objInstance = new FAUtils();
        }
        return objInstance;
    }

    public void logAppEvent(String eventName) {
        mFirebaseAnalytics.logEvent(eventName, null);
    }
}
