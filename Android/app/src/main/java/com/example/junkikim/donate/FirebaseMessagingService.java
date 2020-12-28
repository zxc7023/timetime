package com.example.junkikim.donate;

/**
 * Created by junkikim on 2016-11-19.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //추가한것
        sendNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("id_recv"),remoteMessage.getData().get("token"));
        Log.d("id_recv",remoteMessage.getData().get("id_recv"));
        Log.d("messageBody",remoteMessage.getData().get("message"));
        Log.d("token",remoteMessage.getData().get("token"));
    }

    private void sendNotification(String messageBody,String id,String token) {
        ////Log.d("messageBody",messageBody);
        Intent intent = new Intent(this,Mode_BuyTime_MainActivity.class);
        intent.putExtra("go",1000);
        intent.putExtra("id_recv",id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.pushlogo)
                .setContentTitle("새로운 쪽지가 왔어요")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(Color.BLUE,500,20000)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP,"Tag");
        wakeLock.acquire(5000);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0/* ID of notification */, notificationBuilder.build());
    }


}