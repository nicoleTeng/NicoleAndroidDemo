package com.example.app;

import android.app.Application;
import android.util.Log;

public class MainActivityImpl extends Application {
    private static final String TAG = "MainActivityImpl";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "txh onCreate");
    }

}
