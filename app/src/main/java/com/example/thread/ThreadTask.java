package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public abstract class ThreadTask<T> extends Thread {
    private static final String TAG = "ThreadTask";
    private static Handler handler;
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public ThreadTask() {

    }
    
    private static class MHandler extends Handler {
        
        public MHandler(Looper looper) {
            super(looper);
        }
        
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ResultData resultData = (ResultData) msg.obj;
            resultData.threadTask.onResult(resultData.data);
        }
    }
    
    private Handler getHandler() {
        if (handler == null) {
            synchronized(MHandler.class) {
                handler = new MHandler(Looper.getMainLooper());
            }
        }
        return handler;
    }

    public void doWork() {
        executorService.execute(new Runnable() {
            
            @Override
            public void run() {
                Message message = Message.obtain();
                message.obj = new ResultData<T>(ThreadTask.this, onDoInBackground()) ;
                getHandler().sendMessage(message);
            }
        });
    }
    
    public void onStart() {
        
    }
    
    public abstract T onDoInBackground();
    
    public void onResult(T t) {
        
    }
    
    public void excute() {
        onStart();
        doWork();
    }
    
    private static class ResultData<T> {
        ThreadTask threadTask ;
        T data ;
        
        public ResultData(ThreadTask threadTask, T data) {
            this.threadTask = threadTask ;
            this.data = data ;
        }
    }

}
