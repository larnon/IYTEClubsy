package com.example.tempest.iyteclubsy.notifications;

/**
 * Created by Bora Gültekin on 15.05.2018.
 */


import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;
import com.example.tempest.iyteclubsy.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private NotificationManager notificationManager;

    @Override public void onMessageReceived(RemoteMessage remoteMessage) {

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = new Random().nextInt(60000);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)  //a resource for your custom small icon
                .setContentTitle(remoteMessage.getNotification().getTitle()) //the "title" value you sent in your notification
                .setContentText(remoteMessage.getNotification().getBody()) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri);


        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

    }

}
