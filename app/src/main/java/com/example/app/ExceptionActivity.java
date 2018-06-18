package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ExceptionActivity extends Activity {
    private static final String TAG = "ExceptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("ExceptionActivity");
        setContentView(textView);

        BatteryTest();
    }

    private void BatteryTest() {
        Battery aBattery = new Battery();
        aBattery.chargeBattery(0.5);
        aBattery.useBattery(-0.5);
        Log.v(TAG, "txh aBattery.getPower() = " + aBattery.getPower());
    }

    class Battery {
        private double power = 0.0; // percentage of battery

        public void chargeBattery(double p) {
            // power <= 1
            if (power + p < 1.) {
                power = power + p;
            } else {
                power = 1.;
            }
        }

        public boolean useBattery(double p) {
            try {
                //String a = null;
                //Log.v(TAG, "txh a = " + a.toString());
                test(p);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v(TAG, "txh catch Exception, e = " + e.getMessage());
                p = 0.0;
            }

            if (power >= p) {
                power = power - p;
                return true;
            } else {
                power = 0.0;
                return false;
            }
        }

        public double getPower() {
            return power;
        }
        
        private void test(double p) throws Exception { // I just throw, don't handle
            if (p < 0) {
                Exception e = new Exception("p must be positive");
                throw e;
            }
        }
    }
}
