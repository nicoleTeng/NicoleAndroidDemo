package com.example.bitmap;

import com.example.nicole.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ImageXfermode extends View {
    private static final String TAG = "ImageXfermode";
    private PorterDuffXfermode Xfermode_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private PorterDuffXfermode Xfermode_SRC = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    private PorterDuffXfermode Xfermode_DST = new PorterDuffXfermode(PorterDuff.Mode.DST);
    private PorterDuffXfermode Xfermode_SRC_IN = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private PorterDuffXfermode Xfermode_SRC_OUT = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    private PorterDuffXfermode Xfermode_XOR = new PorterDuffXfermode(PorterDuff.Mode.XOR);
    private PorterDuffXfermode Xfermode_DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private PorterDuffXfermode Xfermode_DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    private PorterDuffXfermode Xfermode_MULTIPLY = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
    private PorterDuffXfermode Xfermode_SCREEN = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);
    
    private Bitmap mBmp = null;

    private PorterDuffXfermode[] mXfermodeList = {
            Xfermode_CLEAR,
            Xfermode_SRC,
            Xfermode_DST,
            Xfermode_SRC_IN,
            Xfermode_SRC_OUT,
            Xfermode_XOR,
            Xfermode_DST_IN,
            Xfermode_DST_OUT,
            Xfermode_MULTIPLY,
            Xfermode_SCREEN
    };
    
    private PorterDuffXfermode mCurrentXfermode = mXfermodeList[0];
    
    public ImageXfermode(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBmp = BitmapUtils.getTargetBitmap(context.getResources(), R.drawable.girl, 300, 300);
        Log.v(TAG, "txh new ImageXfermode, mBmp = " + mBmp.getWidth() + " * " + mBmp.getHeight());
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawXfermode(canvas);
        //drawClipRect(canvas);
    }
    
    private void drawXfermode(Canvas canvas) {
        // ���ñ���ɫ
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int r = canvasWidth / 3;

        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        // ���ƻ�ɫ��Բ��
        Paint paint = new Paint();
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);

        // ������ɫ�ľ���
        paint.setXfermode(mCurrentXfermode);
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
        // ��󽫻���ȥ��Xfermode
        paint.setXfermode(null);

        canvas.restoreToCount(layerId);        
    }
    
    public void setXfermode(int index) {
        mCurrentXfermode = mXfermodeList[index - 1];
        Log.v("ImageXfermode", "txh current Xfermode = " + mCurrentXfermode);
        invalidate();
    }
    
    private void drawClipRect(Canvas canvas) {
        int width = mBmp.getWidth();
        int height = mBmp.getHeight();
        
        canvas.save();
        Paint mPaint = new Paint();
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, width, height, mPaint);
        canvas.drawRoundRect(0, 0, width, height, 5, 5, mPaint);
        
        mPaint.setColor(Color.YELLOW);
        canvas.drawRoundRect(50, 50, width-50, height-50, 10, 10, mPaint);
        //canvas.drawBitmap(mBmp, new Matrix(), mPaint);
        canvas.restore();

        canvas.save();
        //��ȡcanvas��ĳ����������
        canvas.clipRect(30, 30, width / 2, height / 2);
        //canvas.drawBitmap(mBmp, width, height, mPaint);

        //canvas.drawBitmap(mBmp, new Matrix(), mPaint);
        //canvas.drawRect(31, 31, width / 2, height / 2, mPaint);
        canvas.restore();
    }
    
}
