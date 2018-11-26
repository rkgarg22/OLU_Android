package com.elisa.olu;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

public class MyAlarmService extends Service {
    NotificationManager mManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (isApplicationSentToBackground(getApplicationContext())) {
            mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(this.getApplicationContext(), SplashActivity.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Resources res = this.getResources();
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Has cumplido 24 horas como no disponible! ")
                    .setContentText("No Olvides estar disponible para ser visible a miles de usuarios.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(res,R.mipmap.ic_launcher))
                    .setContentIntent(pendingNotificationIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mManager.notify(0, notification);
        }
    }


    private boolean isApplicationSentToBackground(Context mcontext) {
        // TODO Auto-generated method stub
        ActivityManager am = (ActivityManager) mcontext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mcontext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
