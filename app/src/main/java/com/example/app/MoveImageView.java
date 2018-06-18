package com.example.app;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Scroller;

public class MoveImageView extends ImageView {
    private int mLastX = 0;
    private int mLastY = 0;
    private Scroller mScroller;

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }
    
    public void scrollViewByCusom(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        // 1000ms内滑向destX，效果就是慢慢滑动
        mScroller.startScroll(scrollX, 0, deltaX, 0, 1000);
        invalidate();
    }
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
