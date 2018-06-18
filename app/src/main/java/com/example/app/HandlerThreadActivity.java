package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.nicole.R;

/**
 * 程序说明：UI Thread 通过handler向其他线程发送数据并进行打印
 */
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

        /* 将handlerThread中创建的looper传递给Handler。
         * 也就意味着该Handler收到Message后，程序在HandlerThread创建的线程中运行  */
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
