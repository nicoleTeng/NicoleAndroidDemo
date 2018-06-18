package com.example.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class DemoSurfaceView extends SurfaceView implements Callback {
    private static final String TAG = "DemoSurfaceView";
    LoopThread thread;

    public DemoSurfaceView(Context context) {
        super(context);
        init();
    }

    public DemoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        SurfaceHolder holder = getHolder();  
        holder.addCallback(this); //设置Surface生命周期回调 
        thread = new LoopThread(holder, getContext());
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        Log.v(TAG, "txh surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "txh surfaceCreated");
        thread.isRunning = true;  
        thread.start(); 
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "txh surfaceDestroyed");
        thread.isRunning = false;  
        try {  
            thread.join();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }
    }

    /** 
     * 执行绘制的绘制线程 
     * @author Administrator 
     */  
    class LoopThread extends Thread {  
   
        SurfaceHolder surfaceHolder;  
        Context context;  
        boolean isRunning;  
        float radius = 10f;  
        Paint paint;  
   
        public LoopThread(SurfaceHolder surfaceHolder, Context context) {  

            this.surfaceHolder = surfaceHolder;  
            this.context = context;  
            isRunning = false;  
   
            paint = new Paint();  
            paint.setColor(Color.YELLOW);  
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setAntiAlias(true);
        }  
   
        @Override  
        public void run() {  
   
            Canvas canvas = null;  
   
            while (isRunning) {  
                try {  
                    synchronized (surfaceHolder) {  
                        canvas = surfaceHolder.lockCanvas(null);
                        if (canvas != null) {
                            doDraw(canvas);
                        }
                        //通过它来控制帧数执行一次绘制后休息50ms
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }  
            }  
        }  
   
        public void doDraw(Canvas canvas) {  
            //这个很重要，清屏操作，清楚掉上次绘制的残留图像  
            canvas.drawColor(Color.BLACK);  
   
            canvas.translate(300, 300);  
            canvas.drawCircle(0,0, radius++, paint);  
   
            if (radius > 300) {  
                radius = 10f;  
            }  
        }  
    }  
}  
