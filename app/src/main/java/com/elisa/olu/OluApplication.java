package com.elisa.olu;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;


public class OluApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fresco.initialize(this);
        FacebookSdk.sdkInitialize(this);
        MultiDex.install(this);
    }

}
