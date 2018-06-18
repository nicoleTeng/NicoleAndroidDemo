package com.example.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class PowerConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerConnectionReceiver";
    private boolean mIsTaskRun = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "txh ==== onReceive ====");
        String action = "";
        if (intent != null) {
            action = intent.getAction();
            Log.v(TAG, "txh action = " + action);
            Toast.makeText(context, "onReceive: " + action, Toast.LENGTH_SHORT).show();
        }

        if (action.equals(Intent.ACTION_POWER_CONNECTED)
                || action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            //start task
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                status == BatteryManager.BATTERY_STATUS_FULL;

            boolean acCharge = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                    == BatteryManager.BATTERY_PLUGGED_AC;
            Log.v(TAG, "txh isCharging = " + isCharging + ", acCharge = " + acCharge);
            if (isCharging && acCharge && !mIsTaskRun) {
                startTask(context);
            }
        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            //stop task
            if (mIsTaskRun) {
                stopTask(context);
            }
        }
    }
    
    private void startTask(Context context) {
        Log.v(TAG, "txh startTask");
        Toast.makeText(context, "start task", Toast.LENGTH_LONG).show();
        mIsTaskRun = true;
    }
    
    private void stopTask(Context context) {
        Log.v(TAG, "txh stopTask");
        Toast.makeText(context, "stop task", Toast.LENGTH_LONG).show();
        mIsTaskRun = false;
    }
}
