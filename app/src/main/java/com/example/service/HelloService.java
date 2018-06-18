package com.example.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HelloService extends Service {
    private static final String TAG = "HelloService";
    private ServiceActivity mServiceActivity = null;
    // indicates how to behave if the service is killed
    private int mStartMode = Service.START_NOT_STICKY;
    private boolean mIsActive = true;
    private MyThread mThread = null;
    
    private IBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "txh onBind, thread = " + Thread.currentThread().getName());
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "txh onUnbind");
        return super.onUnbind(intent);
    }
    
    public class MyBinder extends Binder {

        public HelloService getService() {
            Log.v(TAG, "txh getService, thread = " + Thread.currentThread().getName());
            return HelloService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "txh onCreate");
        mThread = new MyThread();
        mThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "txh onStartCommand");
        return Service.START_REDELIVER_INTENT;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "txh onDestroy");
        stopTask();
    }
    
    public void setServiceActivity(ServiceActivity activity) {
        mServiceActivity = activity;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String value = formatter.format(date); 
        Log.v(TAG, "txh getDate = " + value + ", thread = " + Thread.currentThread().getName());
        return value;
    }
    
    class MyThread extends Thread {
        @Override
        public void run() {
            int i = 0;
            int total = 20;
            while (i < total) {
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v(TAG, "txh thread = " + Thread.currentThread().getName() + ", i = " + i );
            }
            stopSelf();
        }
    }

    public void stopTask() {
        mIsActive = false;
    }
}
