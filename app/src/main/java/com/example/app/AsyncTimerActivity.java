package com.example.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AsyncTimerActivity extends Activity {
    private static final String TAG = "AsyncTimerActivity";
    private static final String GET_CARD_ACTION = "com.example.cardno";
    private String mCardNo = null;
    private Object mLock = new Object();
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "txh onCreate, thread = " + Thread.currentThread().getName());
        mTextView = new TextView(this);
        mTextView.setText("cardNo: ");
        setContentView(mTextView);
        startAsyncTimerTask();

        startSendBroadcastTask();
    }

    private void startAsyncTimerTask() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "txh run, thread = " + Thread.currentThread().getName());
                if (mCardNo == null || mCardNo.length() == 0) {
                    initGetCardNo();
                }
                synchronized (mLock) {
                    if (mCardNo == null || mCardNo.length() == 0) {
                        try {
                            Log.d(TAG, "txh wait...");
                            mLock.wait(30*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d(TAG, "txh exception = " + e);
                        }
                    }
                }
                startAAAauth(mCardNo);
            }
        });
        task.start();
    }

    private void startSendBroadcastTask() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(GET_CARD_ACTION);
                sendBroadcast(intent);
            }
        }, 5 * 1000);
    }

    private void initGetCardNo() {
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "txh onReceive, thread = " + Thread.currentThread().getName());

                synchronized (mLock) {
                    mCardNo = "123456";
                    mLock.notify();
                    mTextView.setText("cardNo: " + mCardNo);
                    Toast.makeText(AsyncTimerActivity.this, "get cardNo" + mCardNo,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter("com.example.cardno"));
    }

    private void startAAAauth(String cardNo) {
        Log.d(TAG, "txh startAAAauth, cardNo = " + cardNo);
    }
}
