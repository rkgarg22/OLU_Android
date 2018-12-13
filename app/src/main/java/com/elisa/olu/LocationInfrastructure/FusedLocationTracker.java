package com.elisa.olu.LocationInfrastructure;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.elisa.olu.IngresaActivity;
import com.elisa.olu.LoginScreenActivity;
import com.elisa.olu.RegisterActivity;
import com.elisa.olu.SearchActivity;
import com.elisa.olu.TrainerHomeActivity;
import com.elisa.olu.TrainerRegisterActivity;
import com.elisa.olu.UbicacionActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import infrastructure.AppCommon;

public class FusedLocationTracker extends Service {

    private final Context mContext;

    Location location;
    double latitude;
    double longitude;


    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    protected FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public FusedLocationTracker(Context context) {
        this.mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };
        createLocationRequest();
        getLocation();
    }

    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void onNewLocation(Location location) {
        this.location = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        AppCommon.getInstance(mContext).setUserLatitude(latitude);
        AppCommon.getInstance(mContext).setUserLongitude(longitude);
        if (mContext != null && mContext instanceof LoginScreenActivity) {
            stopLocationUpdates();
            ((LoginScreenActivity) mContext).setLocation(latitude, longitude);
        }
        else if (mContext != null && mContext instanceof RegisterActivity) {
            stopLocationUpdates();
            ((RegisterActivity) mContext).setLocation(latitude, longitude);
        }
        else if (mContext != null && mContext instanceof TrainerRegisterActivity) {
            stopLocationUpdates();
            ((TrainerRegisterActivity) mContext).setLocation(latitude, longitude);
        }
        else if (mContext != null && mContext instanceof TrainerHomeActivity) {
            ((TrainerHomeActivity) mContext).setLocation(this.location);
        }
        else if (mContext != null && mContext instanceof UbicacionActivity) {
            stopLocationUpdates();
            ((UbicacionActivity) mContext).locationGet(this.location);
        }else if (mContext != null && mContext instanceof IngresaActivity) {
            stopLocationUpdates();
            ((IngresaActivity) mContext).locationGet(location);
        } else if (mContext != null && mContext instanceof SearchActivity) {
            stopLocationUpdates();
            ((SearchActivity) mContext).locationGet(location);
        }

    }

    /**
     * javax.mail
     * v
     * Function to get the user's current location
     *
     * @return
     */
    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(((Activity) this.mContext), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);

        } catch (Exception e) {
        }
    }


    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {

        if (ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(((Activity) this.mContext), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }

    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}