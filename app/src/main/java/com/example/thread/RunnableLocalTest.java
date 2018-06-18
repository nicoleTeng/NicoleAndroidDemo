package com.example.thread;

import android.util.Log;

public class RunnableLocalTest {
    private static final String TAG = "RunnableLocalTest";

    public void run() {
        MutliThread m = new MutliThread();
        
        Thread t1 = new Thread(m, "Window 1");
        Thread t2 = new Thread(m, "Window 2");
        Thread t3 = new Thread(m, "Window 3");
        
        t1.start();
        t2.start();
        t3.start();
    }

    class MutliThread implements Runnable {
        private int ticket = 10;
    
        public void run() {
            while (ticket > 0) {
                Log.v(TAG, "txh " + ticket-- + " is saled by " + Thread.currentThread().getName());
            }
        }
    }
}
