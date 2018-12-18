package com.tucan.olu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class MyReceiverForSession extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager mManager = (NotificationManager) context.getApplicationContext().getSystemService(context.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context.getApplicationContext(), SplashActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Resources res = context.getResources();
        Notification notification = new Notification.Builder(context)
                .setContentTitle("RECORDATORIO ")
                .setContentText("Tienes una OLU actividad programada en 30 minutos.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(pendingNotificationIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mManager.notify(0, notification);
    }

}
