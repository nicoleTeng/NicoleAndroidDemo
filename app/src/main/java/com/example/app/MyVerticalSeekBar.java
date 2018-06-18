package com.example.app;

import com.example.nicole.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class MyVerticalSeekBar extends View {
	private static final String TAG = "MyVerticalSeekBar";
	private static final int PROGRESS_MAX = 100;
	private Paint mPaint;
	private Bitmap mThumb;
	private float mViewWidth = 0;
	private float mViewHeight = 0;
	private float mThumbPositionY = -1;
	private int mProgress;

	public MyVerticalSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		 mPaint = new Paint();
		 mPaint.setColor(context.getResources().getColor(R.color.selected_color));
		 mPaint.setAntiAlias(true);
		 mPaint.setStrokeWidth(3);
		 mThumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.thumb);
	}

	protected void onDraw(Canvas canvas) {
		mViewWidth = canvas.getWidth();
		mViewHeight = canvas.getHeight();
		float centerX = mViewWidth / 2 - mThumb.getWidth() / 2;
        float padding = mThumb.getHeight() / 3;
        float positionY = mThumbPositionY;
        if (mViewHeight - mThumbPositionY < mThumb.getHeight()) {
        	positionY = mViewHeight - mThumb.getHeight();
        }
        if (positionY > mThumb.getHeight()) {
        	canvas.drawLine(mViewWidth / 2, mThumb.getHeight() / 2,
        			mViewWidth / 2, positionY - padding, mPaint);
        }
		canvas.drawBitmap(mThumb, centerX, positionY, mPaint);
		if (positionY < mViewHeight - mThumb.getHeight() - mThumb.getHeight() / 2 - padding) {
			canvas.drawLine(mViewWidth / 2, positionY + mThumb.getHeight() + padding,
					mViewWidth / 2, mViewHeight - mThumb.getHeight() / 2, mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventY = event.getY();
		Log.e(TAG, "txh onTouchEvent, eventY = " + eventY);
		if (!isEnabled()) {
			return false;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				mThumbPositionY = eventY;
				break;
			}
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP: {
				if (eventY < 0) {
					mThumbPositionY = 0;
				} else if (eventY > mViewHeight) {
					mThumbPositionY = mViewHeight;
			    } else {
			    	mThumbPositionY = eventY;
			    }
				
				break;
			}
		}
		caculateProgress(mThumbPositionY);
		invalidate();
		return true;
	}

	private void caculateProgress(float position) {
		mProgress = (int) ((mViewHeight - position) / mViewHeight * PROGRESS_MAX);
		Log.v(TAG, "txh, caculateProgress position = " + position + ", mProgress = " + mProgress);
	}
	
	private void calculateSpeed() {
	    VelocityTracker velocityTracker = VelocityTracker.obtain();
	    velocityTracker.computeCurrentVelocity(1000);
	    int xVelocity = (int) velocityTracker.getXVelocity();
	    int yVelocity = (int) velocityTracker.getYVelocity();
	    Log.v(TAG, "txh ==== calculateSpeed, xVelocity = " + xVelocity + ", yVelocity = " + yVelocity);
	    velocityTracker.clear();
	    velocityTracker.recycle();
	}
}
