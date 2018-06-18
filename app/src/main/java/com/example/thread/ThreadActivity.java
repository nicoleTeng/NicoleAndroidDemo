package com.example.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.nicole.R;

public class ThreadActivity extends Activity {
    private static final String TAG = "ThreadActivity";
    private static final Boolean USE_THREAD_POOL = false;
    private TextView mTextView = null;
    private MyAsyncTask mMyAsyncTask = null;
    private int CASE_NO = 7;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "txh onCreate");
        setContentView(R.layout.thread_layout);
        mTextView = (TextView) findViewById(R.id.result_id);

        Thread thread = Thread.currentThread();
        Log.v(TAG, "txh thread.getName() = " + thread.getName());
        try {
            thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        doThreadTest(CASE_NO);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "txh onDestroy");
        if (mMyAsyncTask != null) {
            mMyAsyncTask.cancel(true);
        }
    }

    private static Handler mHandler;
    // 创建线程的方法一：
    class MyThread extends Thread {
        
        @Override
        public void run() {
            Process.setThreadPriority(19);
            Looper.prepare();
            mHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    
                }
            };
            Looper.loop();
            Log.v(TAG, "txh MyThread, currentThread.name = " + Thread.currentThread().getName());
        }
    }
    
    // 创建线程的方法二：
    class MyRunnable implements Runnable {

        @Override
        public void run() {
            Log.v(TAG, "txh MyRunnable, currentThread.name = " + Thread.currentThread().getName());
        }
    }

    // 创建线程的方法三：
    class MyCallable implements Callable<Integer> {
        
        @Override
        public Integer call() throws Exception {  
            Log.v(TAG, "txh MyCallable, currentThread.name = " + Thread.currentThread().getName());  
            Thread.sleep(1000);
            return new Integer(8);
        }
    }
    
    void callableFutureTaskThread() {
        MyCallable callable = new MyCallable();
        FutureTask<Integer> futureTask  = new FutureTask<Integer>(callable);
        
        if (USE_THREAD_POOL) {
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(futureTask);
            executor.shutdown();
        } else {
            Thread newThread = new Thread(futureTask);  
            newThread.start();
        }
          
        try {  
            Log.e(TAG, "txh testThread, blocking");
            Integer result = futureTask.get();  
            Log.v(TAG, "txh result = " + result);  
        } catch (InterruptedException ignored) {
            
        } catch (ExecutionException ignored) {  
            
        }  
    }
    
    /*
     * AsyncTask虽然提供了cancel(true)方法来停止任务，但是这个方法只是中断了这个线程，并不能真正意思上的停止任务，
     * 这也是很多人说 AsyncTask的弊端: 极容易造成内存溢出的。
     * 几种结束任务的间接实现方式：
     * 1. 判断标志位的办法：
     * 2. 合理的利用Exception
     */
    class MyAsyncTask extends AsyncTask<Integer, Void, Boolean> {

        /*
         * 运行在后台线程
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.v(TAG, "txh doInBackground, params = " + params + ", this = " + this);
            int i = 10;
            while (i > 0) {
                Log.v(TAG, "txh i = " + i);
//                if (isCancelled()) {
//                    Log.e(TAG, "txh break");
//                    return false;
//                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
            return true;
        }
        
        /*
         * 运行在UI线程，参数为doInBackground返回值
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        protected void onPostExecute(Boolean result) {
            if (isCancelled()) {
                return;
            }
            if (result) {
                Log.v(TAG, "txh onPostExecute, result = " + result + ", this = " + this);
                mTextView.setText(this + " has finished!");
            }
        }
    }
    
    private void doThreadTest(int caseNo) {
        switch (caseNo) {
            case 1: {
                // 直接用Thread
                Thread task1 = new MyThread();
                task1.start();
                break;
            }
            case 2: {
                // 方法二：可以给线程命名
                // new Thread(new MyRunnable(), "Thread 222");
                Thread task2 = new Thread(new MyRunnable());
                task2.start();
                break;
            }
            case 3: {
                // 方法三：Callable + FutureTask
                callableFutureTaskThread();
                break;
            }
            case 4: {
                // Thread 与 Runnable 的区别：
                // 每个线程拥有自己的静态变量
                ThreadLocalTest test1 = new ThreadLocalTest(); 
                test1.run();
                break;
            }
            case 5: {
                // 多个线程共享数据
                RunnableLocalTest test2 = new RunnableLocalTest();
                test2.run();
                break;
            }
            case 6: {
                // AsyncTask
                mMyAsyncTask = new MyAsyncTask();
                mMyAsyncTask.execute(10);
                break;
            }
            case 7: {
                createThreadTask();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void createThreadTask() {
        ThreadTask<Integer> task = new ThreadTask<Integer>() {

            @Override
            public void onStart() {
                super.onStart();
                Log.v(TAG, "txh onStart, thread = " + Thread.currentThread().getName()) ;
            }

            @Override
            public Integer onDoInBackground() {
                Log.v(TAG, "txh onDoInBackground, thread = " + Thread.currentThread().getName()) ;
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return 1;
            }

            @Override
            public void onResult(Integer value) {
                super.onResult(value);
                Log.v(TAG, "txh onResult, thread = " + Thread.currentThread().getName() + ", result = " + value) ;
            }
        };
        task.excute();
    }
}
