/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tucan.olu.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.tucan.olu.LocationInfrastructure.FusedLocationService;

import java.util.List;

import API.PretoAppService;
import API.ServiceGenerator;
import APIResponse.CommonStringResponse;
import infrastructure.AppCommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";

   public static final String ACTION_PROCESS_UPDATES =
            "com.tucan.olu" + ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    LocationResultHelper locationResultHelper = new LocationResultHelper(
                            context, locations);
                    locationResultHelper.saveResults();

                    saveUserLocation(context,locations.get(0).getLatitude(),locations.get(0).getLongitude());
                   // Toast.makeText(context, "Received latitude and longitude : "
                            //+LocationResultHelper.getSavedLocationResult(context), Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    public void saveUserLocation(final Context context, final Double latitude, final Double longitude) {
        PretoAppService pretoAppService = ServiceGenerator.createService(PretoAppService.class);
        Call call = pretoAppService.updateUserLocation(AppCommon.getInstance(context).getUserID(), String.valueOf(latitude), String.valueOf(longitude), "es");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    CommonStringResponse commonStringResponse = (CommonStringResponse) response.body();
                    if (commonStringResponse.getSuccess().equals("1")) {
                        AppCommon.getInstance(context).setUserLongitude(longitude);
                        AppCommon.getInstance(context).setUserLatitude(latitude);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
