package com.example.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nicole.R;

public class ServiceActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ServiceActivity";
    private Intent mIntent = null;
    private Boolean mIsBound = false;
    private Boolean mIsStart = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.v(TAG, "txh onServiceConnected");
            HelloService service = ((HelloService.MyBinder)binder).getService();
            service.getDate();
            service.setServiceActivity(ServiceActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "txh onServiceDisconnected");
        }
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_demo);
        
        Button startIntentService = (Button) findViewById(R.id.startIntentService_id);
        Button startService = (Button) findViewById(R.id.startService_id);
        Button stopService = (Button) findViewById(R.id.stopService_id);
        Button bindService = (Button) findViewById(R.id.bindService_id);
        Button unBindService = (Button) findViewById(R.id.unBindService_id);
        Button categoryService = (Button) findViewById(R.id.categoryService_id);
        
        startIntentService.setOnClickListener(this);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unBindService.setOnClickListener(this);
        categoryService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.startIntentService_id: {
            mIntent = new Intent(this, HelloIntentService.class);
            startService(mIntent);
            mIsStart = true;
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            break;
        }
        case R.id.startService_id: {
            //if (!mIsStart) {
                Log.v(TAG, "txh startService");
                mIntent = new Intent(this, HelloService.class);
                startService(mIntent);
                mIsStart = true;
            //}
            break;
        }
        case R.id.stopService_id: {
           // if (mIsStart) {
                Log.v(TAG, "txh stopService");
                stopService(mIntent);
                mIsStart = false;
            //}
            break;
        }
        case R.id.bindService_id: {
            if (!mIsBound) {
                Log.v(TAG, "txh bindService");
                mIntent = new Intent(this, HelloService.class);
                bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
                mIsBound = true;
            }
            break;
        }
        case R.id.unBindService_id: {
            if (mIsBound) {
                Log.v(TAG, "txh unbindService");
                unbindService(mConnection);
                mIsBound = false;
            }
            break;
        }
        case R.id.categoryService_id: {
            mIntent = new Intent(this, CategoryService.class);
            startService(mIntent);
            mIsStart = true;
            break;
        }
        }
    }
    
    @Override
    public void onDestroy() {
        Log.v(TAG, "txh onDestroy");
        if (mIsBound) {
            Log.v(TAG, "txh unbindService");
            unbindService(mConnection);
            mIsBound = false;
        }
        if (mIsStart) {
            Log.v(TAG, "txh stopService");
            stopService(mIntent);
            mIsStart = false;
        }
        super.onDestroy();
    }
}
