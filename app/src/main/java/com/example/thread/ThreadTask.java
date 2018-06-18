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
    /**
     * 受限于硬件、内存和性能，我们不可能无限制的创建任意数量的线程，因为每一台机器允许的最大线程是一个有界值。
     * 也就是说ThreadPoolExecutor管理的线程数量是有界的。线程池就是用这些有限个数的线程，去执行提交的任务。
     * 
     * 线程池，newFixedThreadPool()创建固定大小的线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     */
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
    
    /*
     * handler优化：单例模式，保证handler只有一个实例
     */
    private Handler getHandler() {
        if (handler == null) {
            synchronized(MHandler.class) {
                handler = new MHandler(Looper.getMainLooper());
            }
        }
        return handler;
    }

    public void doWork() {
        /*
         * 线程池优化：避免不断创建线程
         */
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
        //start();  // 会回调Thread.run方法,run方法运行在一个新线程中
        doWork();
    }
    
    /**
     * handler发送数据的实体
     * @param <Data>
     */
    private static class ResultData<T> {
        ThreadTask threadTask ;
        T data ;
        
        public ResultData(ThreadTask threadTask, T data) {
            this.threadTask = threadTask ;
            this.data = data ;
        }
    }

}
