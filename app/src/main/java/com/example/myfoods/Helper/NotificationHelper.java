package com.example.myfoods.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.myfoods.R;

public class NotificationHelper extends ContextWrapper {
    private static final String MY_CHANNEL_ID = "com.example.myfoods.MIREA";
    private static final String MY_CHANNEL_NAME = "Order foods";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
            createChannel();
        }
    }

    private void createChannel() {
        NotificationChannel myChannel = new NotificationChannel(MY_CHANNEL_ID, MY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        myChannel.enableLights(false);
        myChannel.enableVibration(true);
        myChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(myChannel);
    }

    public NotificationManager getManager() {
        if (manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOrderChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri){
        return  new Notification.Builder(getApplicationContext(), MY_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOrderChannelNotification(String title, String body, Uri soundUri){
        return  new Notification.Builder(getApplicationContext(), MY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
