package com.elisa.olu.Firebase;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.elisa.olu.ChatScreenActivity;
import com.elisa.olu.HistoricalActivity;
import com.elisa.olu.R;
import com.elisa.olu.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ApiObject.TodayBookingObject;
import infrastructure.AppCommon;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private ServiceCallbacks serviceCallbacks;
    public static MyFirebaseMessagingService mInstance = null;
    static Context mContext;

    public static MyFirebaseMessagingService getInstance(Context _Context) {
        if (mInstance == null) {
            mInstance = new MyFirebaseMessagingService();
        }
        mContext = _Context;
        return mInstance;
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification().getBody(),
                remoteMessage.getData().get("notificationCount"), remoteMessage.getData());

        if (serviceCallbacks != null) {
            serviceCallbacks.refreshActivity();
        }
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, String notificationCount, Map<String, String> data) {
        String bookingType = data.get("bookingType");
        Intent intent;
        PendingIntent pendingIntent;
        if (bookingType != null) {
            intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else {
            intent = new Intent(this, ChatScreenActivity.class);
            intent.putExtra("AnotherUserID", data.get("userID"));
            intent.putExtra("name", data.get("firstName") + " " + data.get("lastName"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
        showPopup(data, notificationCount);

    }

    public boolean isRequestExpire(String bookingCreatedTime, String bookingDate, String bookingTIme) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInString = bookingDate + " " + bookingTIme;
        Date bookingDateTime = null;
        Date bookingCreatedDateObj = null;
        try {
            bookingDateTime = sdf.parse(dateInString);
            bookingCreatedDateObj = sdf.parse(bookingCreatedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long different = bookingDateTime.getTime() - bookingCreatedDateObj.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = different / minutesInMilli;

        if (elapsedHours < 15) {
            return true;
        } else {
            return false;
        }

    }

    private void showPopup(Map<String, String> data, String notificationCount) {
        if (!isApplicationSentToBackground(getApplicationContext())) {
            String bookingType = data.get("bookingType");
            if (bookingType != null) {
                if (bookingType.equals("0")) {
                    if (AppCommon.getInstance(this).getCurrentUser() == 1) {
                        String firsName = data.get("firstName");
                        String lastName = data.get("lastName");
                        String categoryID = data.get("categoryID");
                        String categoryName = data.get("categoryName");
                        String bookingDate = data.get("bookingDate");
                        String bookingID = data.get("bookingID");
                        String bookingStartTime = data.get("bookingStart");
                        String bookingEndTime = data.get("bookingEnd");
                        String bookingCreated = data.get("bookingCreated");
                        String bookingLatitude = data.get("bookingLatitude");
                        String bookingLongitude = data.get("bookingLongitude");
                        String address = data.get("address");
                        String bookingFor = data.get("bookingFor");

                        String userImageUrl = data.get("userImageUrl");
                        if (!isRequestExpire(bookingCreated, bookingDate, bookingStartTime)) {
                            TodayBookingObject obj = new TodayBookingObject(bookingDate, categoryName, firsName,
                                    lastName, bookingStartTime, bookingEndTime, bookingID, categoryID, bookingLatitude, bookingLongitude, address);

                            Intent push = new Intent();
                            push.putExtra("pendingObject", new Gson().toJson(obj));
                            push.putExtra("notificationCount", notificationCount);
                            push.putExtra("bookingType", bookingType);
                            push.putExtra("bookingID", bookingID);
                            push.putExtra("bookingFor", bookingFor);
                            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            push.setClass(this, NotificationPopupActivity.class);
                            this.startActivity(push);
                        }
                    }
                } else if (bookingType.equals("3")) {
                    if (AppCommon.getInstance(this).getCurrentUser() == 2) {
                        String bookingDate = data.get("bookingDate");
                        String bookingStartTime = data.get("bookingStart");
                        String bookingCreated = data.get("bookingCreated");
                        String bookingAcceptedTime = data.get("bookingUpdated");
                        Intent push = new Intent();
                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.putExtra("bookingDate", bookingDate);
                        push.putExtra("bookingStartTime", bookingStartTime);
                        push.putExtra("bookingCreated", bookingCreated);
                        push.putExtra("bookingUpdated", bookingAcceptedTime);
                        push.setClass(this, NotificationPopupForAcceptActivity.class);
                        this.startActivity(push);
                    }
                } else if (bookingType.equals("2")) {
                    if (AppCommon.getInstance(this).getCurrentUser() == 2) {
                        String bookingStartTime = data.get("bookingStart");
                        String bookingFor = data.get("bookingFor");


                        String bookingLatitude = data.get("bookingLatitude");
                        String bookingLongitude = data.get("bookingLongitude");
                        String address = data.get("address");
                        String categoryID = data.get("categoryID");
                        Intent push = new Intent();
                        push.putExtra("latitude", bookingLatitude);
                        push.putExtra("longitude", bookingLongitude);
                        push.putExtra("address", address);
                        push.putExtra("categoryId", categoryID);
                        push.putExtra("date", 1);
                        push.putExtra("bookingDate", data.get("bookingDate"));
                        push.putExtra("month", "");
                        push.putExtra("time", bookingStartTime);
                        push.putExtra("bookingType", bookingFor);
                        push.putExtra("isComingFromReservar", true);

                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.setClass(this, NotificationPopupForDeclineActivity.class);
                        this.startActivity(push);
                    }
                } else if (bookingType.equals("4")) {
                    if (AppCommon.getInstance(this).getCurrentUser() == 2) {
                        Intent push = new Intent();
                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.setClass(this, NotificationPopupForStartActivity.class);
                        this.startActivity(push);
                    }
                } else if (bookingType.equals("1")) {
                    if (AppCommon.getInstance(this).getCurrentUser() == 2) {

                        String firsName = data.get("firstName");
                        String lastName = data.get("lastName");
                        String categoryID = data.get("categoryID");
                        String categoryName = data.get("categoryName");
                        String bookingDate = data.get("bookingDate");
                        String bookingID = data.get("bookingID");
                        String bookingStartTime = data.get("bookingStart");
                        String bookingEndTime = data.get("bookingEnd");
                        String bookingCreated = data.get("bookingCreated");
                        String bookingLatitude = data.get("bookingLatitude");
                        String bookingLongitude = data.get("bookingLongitude");
                        String address = data.get("address");

                        TodayBookingObject obj = new TodayBookingObject(bookingDate, categoryName, firsName,
                                lastName, bookingStartTime, bookingEndTime, bookingID, categoryID, bookingLatitude, bookingLongitude, address);

                        AppCommon.getInstance(this).setStartTrainingObject(new Gson().toJson(obj));
                        Intent push = new Intent();
                        push.putExtra("bookingID", bookingID);
                        push.putExtra("userName", firsName + " " + lastName);
                        push.putExtra("category", categoryName);
                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.setClass(this, NotificationPopupForFinishActivity.class);
                        this.startActivity(push);
                    }
                } else if (bookingType.equals("5")) { // Trainer cancel booking
                    if (AppCommon.getInstance(this).getCurrentUser() == 2) {
                        String bookingStartTime = data.get("bookingStart");
                        String bookingFor = data.get("bookingFor");

                        String bookingLatitude = data.get("bookingLatitude");
                        String bookingLongitude = data.get("bookingLongitude");
                        String address = data.get("address");
                        String categoryID = data.get("categoryID");
                        Intent push = new Intent();
                        push.putExtra("latitude", bookingLatitude);
                        push.putExtra("longitude", bookingLongitude);
                        push.putExtra("address", address);
                        push.putExtra("categoryId", categoryID);
                        push.putExtra("date", 1);
                        push.putExtra("bookingDate", data.get("bookingDate"));
                        push.putExtra("month", "");
                        push.putExtra("time", bookingStartTime);
                        push.putExtra("bookingType", bookingFor);
                        push.putExtra("isComingFromReservar", true);
                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.setClass(this, NotificationPopupForCancelActivity.class);
                        this.startActivity(push);
                    }
                } else if (bookingType.equals("6")) {  // Auto Cancel
                    if (AppCommon.getInstance(this).getCurrentUser() == 2) {
                        String bookingStartTime = data.get("bookingStart");
                        String bookingFor = data.get("bookingFor");

                        String bookingLatitude = data.get("bookingLatitude");
                        String bookingLongitude = data.get("bookingLongitude");
                        String address = data.get("address");
                        String categoryID = data.get("categoryID");
                        Intent push = new Intent();
                        push.putExtra("latitude", bookingLatitude);
                        push.putExtra("longitude", bookingLongitude);
                        push.putExtra("address", address);
                        push.putExtra("categoryId", categoryID);
                        push.putExtra("date", 1);
                        push.putExtra("bookingDate", data.get("bookingDate"));
                        push.putExtra("month", "");
                        push.putExtra("time", bookingStartTime);
                        push.putExtra("bookingType", bookingFor);
                        push.putExtra("isComingFromReservar", true);
                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.setClass(this, NotificationPopupForAutoCancelActivity.class);
                        this.startActivity(push);
                    }
                } else if (bookingType.equals("7")) {  // User Cancel Notification goes to trainer
                    if (AppCommon.getInstance(this).getCurrentUser() == 1) {
                        String firsName = data.get("firstName");
                        String lastName = data.get("lastName");
                        String bookingStartTime = data.get("bookingStart");
                        String bookingFor = data.get("bookingFor");
                        String bookingID = data.get("bookingID");
                        String bookingLatitude = data.get("bookingLatitude");
                        String bookingLongitude = data.get("bookingLongitude");
                        String address = data.get("address");
                        String categoryID = data.get("categoryID");
                        Intent push = new Intent();
                        push.putExtra("latitude", bookingLatitude);
                        push.putExtra("longitude", bookingLongitude);
                        push.putExtra("address", address);
                        push.putExtra("firstName", firsName);
                        push.putExtra("lastName", lastName);
                        push.putExtra("categoryId", categoryID);
                        push.putExtra("date", 1);
                        push.putExtra("bookingDate", data.get("bookingDate"));
                        push.putExtra("month", "");
                        push.putExtra("time", bookingStartTime);
                        push.putExtra("bookingType", bookingFor);
                        push.putExtra("isComingFromReservar", true);
                        push.putExtra("bookingID",bookingID);
                        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        push.setClass(this, NotificationPopupForCancelTrainerActivity.class);
                        this.startActivity(push);
                    }
                }
            } else {
                ActivityManager am = (ActivityManager) getApplicationContext()
                        .getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
                if (!tasks.isEmpty()) {
                    ComponentName topActivity = tasks.get(0).topActivity;
                    if (!topActivity.getClassName().equals("ChatScreenActivity")) {
                        Intent intentBroad = new Intent();
                        intentBroad.setAction("com.received.message");
                        getApplicationContext().sendBroadcast(intentBroad);
                    }
                }
            }
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
}
