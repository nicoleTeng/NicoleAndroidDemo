package com.example.thread;

import java.util.HashMap;

import android.util.Log;

public class ThreadLocalTest {
    private static final String TAG = "ThreadLocalTest";

    static ThreadLocal<HashMap> mMap = new ThreadLocal<HashMap>() {
        
        @Override 
        protected HashMap initialValue() {
            Log.v(TAG, "txh " + Thread.currentThread().getName() + " initialValue");
            return new HashMap();
        }
    };

    public void run() {
        Thread[] runs = new Thread[3];
        
        for (int i = 0; i < runs.length; i++) {
            runs[i] = new Thread(new Task(i));
        }
        
        for (int i = 0; i < runs.length; i++) {
            runs[i].start();
        }
    }

    public static class Task implements Runnable {
        int id;

        public Task(int id0) {
            id = id0;
        }

        public void run() {
            Log.v(TAG, "txh " + Thread.currentThread().getName() + ": start");
            HashMap map = mMap.get();
            
            for (int i = 0; i < 10; i++) {
                map.put(i, i + id * 100);
                
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {

                }
            } 
            Log.v(TAG, "txh " + Thread.currentThread().getName() + ":" + map);
        }
    }
}


