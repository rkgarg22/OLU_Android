package com.tucan.olu;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.tucan.olu.Firebase.NotificationPopupActivity;
import com.tucan.olu.Firebase.NotificationPopupForAcceptActivity;
import com.tucan.olu.Firebase.NotificationPopupForAutoCancelActivity;
import com.tucan.olu.Firebase.NotificationPopupForCancelActivity;
import com.tucan.olu.Firebase.NotificationPopupForCancelTrainerActivity;
import com.tucan.olu.Firebase.NotificationPopupForDeclineActivity;
import com.tucan.olu.Firebase.NotificationPopupForFinishActivity;
import com.tucan.olu.Firebase.NotificationPopupForStartActivity;
import com.tucan.olu.LocationInfrastructure.FusedLocationService;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ApiObject.TodayBookingObject;
import infrastructure.AppCommon;

public class SplashActivity extends GenricActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCommon.getInstance(this).setUserLatitude(0.0f);
        AppCommon.getInstance(this).setUserLongitude(0.0f);
//        new FusedLocationTracker(this);
        //startService(new Intent(getApplicationContext(), FusedLocationService.class));

        Locale locale = new Locale(AppCommon.getInstance(this).getSelectedLanguage());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //printHashKey(SplashActivity.this);

//        byte[] sha1 = {
//                (byte) 0xCE, (byte)0xF6, (byte)0xC1, 0x14, 0x1A, 0x30, 0x0C, (byte) 0xFB, 0x09, 0x79, (byte)0x69,
//                (byte)0xF4, (byte)0xFE, 0x2E, (byte) 0xA9, (byte)0x61, (byte)0x63, (byte)0xD2, (byte)0xC5, 0x62,
//        };
//        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));
        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    if (AppCommon.getInstance(SplashActivity.this).isUserLogIn()) {
                        if (SplashActivity.this.getIntent().getStringExtra("userID") != null) {
                            Intent intent = new Intent(SplashActivity.this, ChatScreenActivity.class);
                            intent.putExtra("AnotherUserID", SplashActivity.this.getIntent().getStringExtra("userID"));
                            intent.putExtra("name", SplashActivity.this.getIntent().getStringExtra("firstName") + " " + SplashActivity.this.getIntent().getStringExtra("lastName"));
                            //intent.putExtra("name", SplashActivity.this.getIntent().getStringExtra("firstName"));
                            startActivity(intent);
                        } else if (SplashActivity.this.getIntent().getStringExtra("bookingType") != null) {
                            if (AppCommon.getInstance(SplashActivity.this).getCurrentUser() == 2) {
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashActivity.this, TrainerHomeActivity.class);
                                startActivity(intent);
                            }
                            showPopup(SplashActivity.this.getIntent().getStringExtra("notificationCount"));
                        } else {
                            if (AppCommon.getInstance(SplashActivity.this).getCurrentUser() == 2) {
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashActivity.this, TrainerHomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    } else {
                        Intent i = new Intent(SplashActivity.this, SelectUserTypeActivity.class);
                        i.putExtra("isComingFromSetting", false);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        t.start();
    }

    public boolean isRequestExpire(String bookingCreatedTime, String bookingDate, String bookingTIme) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String dateInString = bookingDate + " " + bookingTIme;
        //Date bookingDateTime = null;
        Date bookingCreatedDateObj = null;
        try {
           //bookingDateTime = sdf.parse(dateInString);
            bookingCreatedDateObj = sdf.parse(bookingCreatedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        long different = c.getTimeInMillis() - bookingCreatedDateObj.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = different / minutesInMilli;

        if (elapsedHours > 15) {
            return true;
        } else {
            return false;
        }
    }

    private void showPopup(String notificationCount) {
        String bookingType = SplashActivity.this.getIntent().getStringExtra("bookingType");
        if (bookingType != null) {
            if (bookingType.equals("0")) {
                if (AppCommon.getInstance(this).getCurrentUser() == 1) {
                    String bookingFor = SplashActivity.this.getIntent().getStringExtra("bookingFor");
                    String firsName = SplashActivity.this.getIntent().getStringExtra("firstName");
                    String lastName = SplashActivity.this.getIntent().getStringExtra("lastName");
                    String categoryID = SplashActivity.this.getIntent().getStringExtra("categoryID");
                    String categoryName = SplashActivity.this.getIntent().getStringExtra("categoryName");
                    String bookingDate = SplashActivity.this.getIntent().getStringExtra("bookingDate");
                    String bookingID = SplashActivity.this.getIntent().getStringExtra("bookingID");
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingEndTime = SplashActivity.this.getIntent().getStringExtra("bookingEnd");
                    String bookingCreated = SplashActivity.this.getIntent().getStringExtra("bookingCreated");
                    String bookingLatitude = SplashActivity.this.getIntent().getStringExtra("bookingLatitude");
                    String bookingLongitude = SplashActivity.this.getIntent().getStringExtra("bookingLongitude");
                    String address = SplashActivity.this.getIntent().getStringExtra("address");
                    String userImageUrl = SplashActivity.this.getIntent().getStringExtra("userImageUrl");
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
                    String bookingDate = SplashActivity.this.getIntent().getStringExtra("bookingDate");
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingCreated = SplashActivity.this.getIntent().getStringExtra("bookingCreated");
                    String bookingAcceptedTime = SplashActivity.this.getIntent().getStringExtra("bookingUpdated");
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
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingFor = SplashActivity.this.getIntent().getStringExtra("bookingFor");
                    String bookingLatitude = SplashActivity.this.getIntent().getStringExtra("bookingLatitude");
                    String bookingLongitude = SplashActivity.this.getIntent().getStringExtra("bookingLongitude");
                    String address = SplashActivity.this.getIntent().getStringExtra("address");
                    String categoryID = SplashActivity.this.getIntent().getStringExtra("categoryID");
                    Intent push = new Intent();
                    push.putExtra("latitude", bookingLatitude);
                    push.putExtra("longitude", bookingLongitude);
                    push.putExtra("address", address);
                    push.putExtra("categoryId", categoryID);
                    push.putExtra("date", 1);
                    push.putExtra("bookingDate", SplashActivity.this.getIntent().getStringExtra("bookingDate"));
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

                    String firsName = SplashActivity.this.getIntent().getStringExtra("firstName");
                    String lastName = SplashActivity.this.getIntent().getStringExtra("lastName");
                    String categoryID = SplashActivity.this.getIntent().getStringExtra("categoryID");
                    String categoryName = SplashActivity.this.getIntent().getStringExtra("categoryName");
                    String bookingDate = SplashActivity.this.getIntent().getStringExtra("bookingDate");
                    String bookingID = SplashActivity.this.getIntent().getStringExtra("bookingID");
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingEndTime = SplashActivity.this.getIntent().getStringExtra("bookingEnd");
                    String bookingCreated = SplashActivity.this.getIntent().getStringExtra("bookingCreated");
                    String bookingLatitude = SplashActivity.this.getIntent().getStringExtra("bookingLatitude");
                    String bookingLongitude = SplashActivity.this.getIntent().getStringExtra("bookingLongitude");
                    String address = SplashActivity.this.getIntent().getStringExtra("address");

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
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingFor = SplashActivity.this.getIntent().getStringExtra("bookingFor");

                    String bookingLatitude = SplashActivity.this.getIntent().getStringExtra("bookingLatitude");
                    String bookingLongitude = SplashActivity.this.getIntent().getStringExtra("bookingLongitude");
                    String address = SplashActivity.this.getIntent().getStringExtra("address");
                    String categoryID = SplashActivity.this.getIntent().getStringExtra("categoryID");
                    Intent push = new Intent();
                    push.putExtra("latitude", bookingLatitude);
                    push.putExtra("longitude", bookingLongitude);
                    push.putExtra("address", address);
                    push.putExtra("categoryId", categoryID);
                    push.putExtra("date", 1);
                    push.putExtra("bookingDate", SplashActivity.this.getIntent().getStringExtra("bookingDate"));
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
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingFor = SplashActivity.this.getIntent().getStringExtra("bookingFor");

                    String bookingLatitude = SplashActivity.this.getIntent().getStringExtra("bookingLatitude");
                    String bookingLongitude = SplashActivity.this.getIntent().getStringExtra("bookingLongitude");
                    String address = SplashActivity.this.getIntent().getStringExtra("address");
                    String categoryID = SplashActivity.this.getIntent().getStringExtra("categoryID");
                    Intent push = new Intent();
                    push.putExtra("latitude", bookingLatitude);
                    push.putExtra("longitude", bookingLongitude);
                    push.putExtra("address", address);
                    push.putExtra("categoryId", categoryID);
                    push.putExtra("date", 1);
                    push.putExtra("bookingDate", SplashActivity.this.getIntent().getStringExtra("bookingDate"));
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
                    String firsName = SplashActivity.this.getIntent().getStringExtra("firstName");
                    String lastName = SplashActivity.this.getIntent().getStringExtra("lastName");
                    String bookingStartTime = SplashActivity.this.getIntent().getStringExtra("bookingStart");
                    String bookingFor = SplashActivity.this.getIntent().getStringExtra("bookingFor");

                    String bookingLatitude = SplashActivity.this.getIntent().getStringExtra("bookingLatitude");
                    String bookingLongitude = SplashActivity.this.getIntent().getStringExtra("bookingLongitude");
                    String address = SplashActivity.this.getIntent().getStringExtra("address");
                    String categoryID = SplashActivity.this.getIntent().getStringExtra("categoryID");
                    String bookingID = SplashActivity.this.getIntent().getStringExtra("bookingID");
                    Intent push = new Intent();
                    push.putExtra("latitude", bookingLatitude);
                    push.putExtra("longitude", bookingLongitude);
                    push.putExtra("address", address);
                    push.putExtra("firstName", firsName);
                    push.putExtra("lastName", lastName);
                    push.putExtra("categoryId", categoryID);
                    push.putExtra("date", 1);
                    push.putExtra("bookingDate", SplashActivity.this.getIntent().getStringExtra("bookingDate"));
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

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("msg", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("msg", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("msg", "printHashKey()", e);
        }
    }
}
