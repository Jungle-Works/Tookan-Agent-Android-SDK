package com.tookan;

import android.app.Application;

import tookan.sdk.models.TookanActivityLifecycleCallback;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TookanActivityLifecycleCallback.registerApplication(this);
    }

}
