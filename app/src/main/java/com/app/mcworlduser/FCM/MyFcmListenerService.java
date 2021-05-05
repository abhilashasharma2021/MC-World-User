/*
 * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 *  @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 *   All Rights Reserved.
 *   Proprietary and confidential :  All information contained herein is, and remains
 *   the property of ToXSL Technologies Pvt. Ltd. and its partners.
 *   Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.app.mcworlduser.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.app.mcworlduser.Activities.MainActivity;

import com.app.mcworlduser.BuildConfig;
import com.app.mcworlduser.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


;

public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmListenerService";

    int id;
    private PrefStore prefStore;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        prefStore = new PrefStore(this);
        Map data = remoteMessage.getData();
        if (BuildConfig.DEBUG) {
            Log.e("bundle Data", "" + data);
        }
        String from = remoteMessage.getFrom();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "From: " + from);
        }

        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        bundle.putBoolean("is_push", true);
        if (prefStore.containValue(Const.STORE_PROFILE_DATA)) {
            generateNotification(this, bundle);
        }
    }


    public void generateNotification(Context context, Bundle extra) {
        String NOTIFICATION_CHANNEL_ID = extra.getString("controller");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        id = Integer.parseInt(extra.getString("model_id"));

        long[] vibrate = {0, 100, 200, 300};

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Order", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setDescription(extra.getString("description"));
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtras(extra);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentText(extra.getString("description"))
                .setAutoCancel(false)
                .setVibrate(vibrate)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);


        mNotificationManager.notify(id, mBuilder.build());
    }


}

