package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.nicole.R;

public class HandlerThreadActivity extends Activity implements Handler.Callback {
    private static final String TAG = "HandlerThreadActivity";
    private Handler superHandler;
    private Handler normalHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        
        NormalThread normalThread = new NormalThread();
        normalThread.start();
        
        HandlerThread handlerThread = new HandlerThread("test");
        handlerThread.start();

        superHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int what = msg.what;
                if (what == 2) {
                    Log.d(TAG, "txh HandlerThread.getName()  = " + Thread.currentThread().getName());
                }
            }       
        };
        
        normalThreadUse();
        handlerThreadUse();
    }

    class NormalThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();

            normalHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    
                    int what = msg.what;
                    if (what == 1) {
                        Log.d(TAG, "txh NormalThread.getName() = " + Thread.currentThread().getName());
                    }
                }
            };
            Looper.loop();
        }
    }

    public void normalThreadUse() {
        
        if (normalHandler == null) {
            return;
        }
        normalHandler.sendEmptyMessage(1);
    }

    public void handlerThreadUse() {
        
        if (superHandler == null) {
            return;
        }
        superHandler.sendEmptyMessage(2);
    }

    @Override
    public boolean handleMessage(Message arg0) {
        // TODO Auto-generated method stub
        return false;
    }
}
