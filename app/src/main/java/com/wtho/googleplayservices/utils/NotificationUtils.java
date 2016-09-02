package com.wtho.googleplayservices.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.wtho.googleplayservices.GPSApp;
import com.wtho.googleplayservices.R;
import com.wtho.googleplayservices.activities.MainActivity;

/**
 * Created by WT on 9/1/2016.
 */
public class NotificationUtils {
    private static final int NOTIFICATION_ID = 101;

    public static void showNotification(String notificationText) {
        Context context = GPSApp.getContext();
        String title = context.getString(R.string.app_name);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.mipmap.google_play_services)
                .setContentTitle(title)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText));

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
