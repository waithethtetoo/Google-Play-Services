package com.wtho.googleplayservices;

import android.app.Application;
import android.content.Context;

/**
 * Created by WT on 8/31/2016.
 */
public class GPSApp extends Application {
    public static final String TAG = GPSApp.class.getSimpleName();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
