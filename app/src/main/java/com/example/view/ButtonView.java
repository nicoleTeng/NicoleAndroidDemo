package com.example.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.nicole.R;

public class ButtonView extends View {
    private static final String TAG = "ButtonView";
    private String mText = null;
    private String mEllipsis = null;
    private Paint mPaint = null;
    private int MARGIN = 15;

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mText = context.getResources().getString(R.string.normal_test);
        //mText = context.getResources().getString(R.string.draw);
        mEllipsis = context.getResources().getString(R.string.ellipsis);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textWidth = mPaint.measureText(mText);
        float ellipsisWidth = mPaint.measureText(mEllipsis);
        
        int x = (int) (canvas.getWidth() - textWidth - 2 * MARGIN);
        int y = canvas.getHeight() - 2 * MARGIN;
        if (x < 0) {
            x = MARGIN;
            canvas.drawText(mText, x, y, mPaint);
            int ellipsisX = (int) (canvas.getWidth() - ellipsisWidth);
            //canvas.drawText(mText, 0, mText.length() / 3, x, y, mPaint);
            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.WHITE);
            rectPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(new Rect(ellipsisX, 0, canvas.getWidth(), canvas.getHeight()), rectPaint);

            canvas.drawText(mEllipsis, ellipsisX, y, mPaint);
        } else {
            canvas.drawText(mText, x, y, mPaint);
        }
    }
}
