package com.example.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class AnimateView extends View {
    private static final String TAG = "AnimateView";
    
    float radius = 10;  
    Paint paint;  

    public AnimateView(Context context) {  
        super(context);  
        paint = new Paint();  
        paint.setColor(Color.YELLOW);  
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
    }  

    public AnimateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();  
        paint.setColor(Color.YELLOW);  
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
    }

    @Override  
    protected void onDraw(Canvas canvas) {
        canvas.translate(300, 300);  
        canvas.drawCircle(0, 0, radius++, paint);            

        if (radius > 200) {  
            radius = 10f;  
        }  

        invalidate(); //通过调用这个方法让系统自动刷新视图  
    }
    
	@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.v(TAG, "txh onLayout, changed = " + changed + ", left = " + left
                + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        final boolean isPortrait = metrics.widthPixels < metrics.heightPixels;
        Log.v(TAG, "txh onMeasure, isPortrait = " + isPortrait
                + ", widthMeasureSpec = " + View.MeasureSpec.toString(widthMeasureSpec)
                + ", heightMeasureSpec = " + View.MeasureSpec.toString(heightMeasureSpec));
        setMeasuredDimension(400, 300);
        Log.v(TAG, "txh getMeasuredWidth() = " +  getMeasuredWidth() +
                 ", getMeasuredHeight() = " + getMeasuredHeight());
    }
}
