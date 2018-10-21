package com.example.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsSeekBar;

public class VerticalSeekBar extends AbsSeekBar {
	private static final String TAG = "VerticalSeekBar";
	private Drawable mThumb;
	private int height;
	private int width;

	public interface OnSeekBarChangeListener {
		void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser);

		void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar);

		void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar);
	}

	private OnSeekBarChangeListener mOnSeekBarChangeListener;

	public VerticalSeekBar(Context context) {
		this(context, null);
	}

	public VerticalSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.seekBarStyle);
	}

	public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		mOnSeekBarChangeListener = l;

	}

	void onStartTrackingTouch() {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStartTrackingTouch(this);
		}
	}

	void onStopTrackingTouch() {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStopTrackingTouch(this);
		}
	}

	void onProgressRefresh(float scale, boolean fromUser) {
		Drawable thumb = mThumb;
		if (thumb != null) {
			setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
			invalidate();
		}
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), fromUser);
		}
	}

	private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
		int available = w - getPaddingLeft() - getPaddingRight();
		System.out.println("--setThumbPos--available=" + available + "  scale=" + scale + "  getPaddingLeft=" + getPaddingLeft());

		int thumbWidth = thumb.getIntrinsicWidth();
		int thumbHeight = thumb.getIntrinsicHeight();
		// available -= thumbWidth;
		// available += getThumbOffset() / 2;
		System.out.println("--available=" + available);
		int thumbPos = (int) (scale * available);
		int topBound, bottomBound;
		if (gap == Integer.MIN_VALUE) {
			Rect oldBounds = thumb.getBounds();
			topBound = oldBounds.top;
			bottomBound = oldBounds.bottom;
		} else {
			topBound = gap;
			bottomBound = gap + thumbHeight;
		}
		thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
	}

	protected void onDraw(Canvas c) {
		System.out.println("--onDraw--height=" + height);
		c.rotate(-90);
		c.translate(-height, 0);
		super.onDraw(c);
	}

	@SuppressLint("NewApi")
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable drawable = getThumb();
		//Log.v(TAG, "txh Height = " + drawable.getIntrinsicHeight());
		//Log.v(TAG, "txh Width=" + drawable.getIntrinsicWidth());
		width = drawable.getIntrinsicWidth();
		height = View.MeasureSpec.getSize(heightMeasureSpec);
		height = 200 + getPaddingLeft() * 2;
		System.out.println("height=" + height + "width=" + width);
		this.setMeasuredDimension(width, height);
	}

	@Override
	public void setThumb(Drawable thumb) {
		mThumb = thumb;
		super.setThumb(thumb);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setPressed(true);
			onStartTrackingTouch();
			trackTouchEvent(event);
			break;

		case MotionEvent.ACTION_MOVE:
			trackTouchEvent(event);
			attemptClaimDrag();
			break;

		case MotionEvent.ACTION_UP:
			trackTouchEvent(event);
			onStopTrackingTouch();
			setPressed(false);
			break;

		case MotionEvent.ACTION_CANCEL:
			onStopTrackingTouch();
			setPressed(false);
			break;
		}
		return true;
	}

	private void trackTouchEvent(MotionEvent event) {
		final int Height = getHeight();
		System.out.println("--trackTouchEvent-getHeight=" + getHeight() + "  getPaddingBottom=" + getPaddingBottom() + "  getPaddingLeft=" + getPaddingLeft());
		final int available = Height - getPaddingLeft() - getPaddingRight();
		System.out.println("--available=" + available);
		int Y = (int) event.getY();
		float scale;
		float progress = 0;
		if (Y > Height - getPaddingRight()) {
			scale = 0.0f;
		} else if (Y < getPaddingTop()) {
			scale = 1.0f;
		} else {
			scale = (float) (Height- Y - getPaddingLeft() ) / (float) available;
		}

		final int max = getMax();
		progress = scale * max;
		System.out.println("--scale=" + scale + "  progress=" + (int) progress);
		setProgress((int) progress);
	}

	private void attemptClaimDrag() {
		if (getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			KeyEvent newEvent = null;
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_UP:
				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP);
				break;
			default:
				newEvent = new KeyEvent(KeyEvent.ACTION_DOWN, event.getKeyCode());
				break;
			}
			return newEvent.dispatch(this);
		}
		return false;
	}
}