package com.elisa.olu.LocationInfrastructure;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.elisa.olu.IngresaActivity;
import com.elisa.olu.LoginScreenActivity;
import com.elisa.olu.R;
import com.elisa.olu.RegisterActivity;
import com.elisa.olu.SearchActivity;
import com.elisa.olu.TrainerHomeActivity;
import com.elisa.olu.TrainerRegisterActivity;
import com.elisa.olu.UbicacionActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DateFormat;
import java.util.Date;

import infrastructure.AppCommon;

public class FusedLocationService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Context mContext;

    GoogleApiClient googleApiClient;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates = false;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public FusedLocationService() {

    }

    public FusedLocationService(Context context) {
        this.mContext = context;
        buildGoogleApiClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        buildGoogleApiClient();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("msg-->","onLocationChanged");
        mCurrentLocation = location;
        onNewLocation(location);
        Log.i("msg-->","date updated: "+ DateFormat.getTimeInstance().format(new Date()));
        Log.i("msg-->","onLocationUpdated: getLatitude: "+ location.getLatitude());
        Log.i("msg-->","onLocationUpdated: getLongitude: "+ location.getLongitude());

        updateUI();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.i("msg-->","onLocationUpdated: getLongitude: "+ DateFormat.getTimeInstance().format(new Date()));
            updateUI();
        }
        startLocationUpdates();
    }

    private void updateUI() {
        if (mCurrentLocation == null) return;
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(FusedLocationService.this)
                .addOnConnectionFailedListener(FusedLocationService.this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Logger.showMessage("stop is called onDestroy");

        Log.i("msg-->","stop is called onDestroy "+ mCurrentLocation.getLongitude());

        stopLocationUpdates();
        googleApiClient.disconnect();

       // startLocationUpdates();
    }
    public void stopLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }


    @Override
    public boolean stopService(Intent name) {
//        Logger.showMessage("name: "+name);
        Log.i("msg-->","name: "+ name);
        return super.stopService(name);
    }

    private void startLocationUpdates() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        //Check whether setting of position information is valid before obtaining current position
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//                Logger.showMessage("location update request : "+locationSettingsResult);
                Log.i("msg-->","location update request : "+ locationSettingsResult);
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
//                        Logger.showMessage(" success code");
                        Log.i("msg-->","success code");

                        //Since the setting is valid, the current position is acquired
                        if (ContextCompat.checkSelfPermission(FusedLocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                            Logger.showMessage("location update request");
                            Log.i("msg-->","location update request");

                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                                    mLocationRequest, FusedLocationService.this);
                        }
                        break;
                }
            }
        });
    }

    /*    ------ New Location --------   */

    Location location;
    double latitude;
    double longitude;

    private void onNewLocation(Location location) {
        this.location = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        AppCommon.getInstance(this).setUserLatitude(latitude);
        AppCommon.getInstance(this).setUserLongitude(longitude);
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
}
