package com.example.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.nicole.R;

public class MatrixView extends View {
    private static final String TAG = "MatrixView";
    private Bitmap mBitmap = null;
    private Paint mPaint = null;
    private Matrix mMatrix = null;

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Log.v(TAG, "txh init");
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sanqin);
        Log.v(TAG, "txh mBitmap = " + mBitmap.getWidth() + " * " + mBitmap.getHeight());
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mMatrix = new Matrix();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "txh onDraw");
        canvas.drawColor(Color.YELLOW);

        //doTranslate(canvas);

        //doRotate(canvas);
        
        //doScale(canvas);
        
        //doSkew(canvas);
        
        //doPreCalculate(canvas);
        
        //doPostCalculate(canvas);
        
        //doPolyToPoly(canvas);
        
        doRectToRect(canvas);
    }

    private void doTranslate(Canvas canvas) {
        mMatrix.setTranslate(100, 100);
        canvas.drawBitmap(mBitmap, mMatrix, null);
        canvas.drawCircle(100, 100, 20, mPaint);
    }

    private void doRotate(Canvas canvas) {
        //mMatrix.setRotate(15); // ��(0,0)Ϊ������ת
        mMatrix.setRotate(15, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2); // ��bitmap������ת
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    private void doScale(Canvas canvas) {
        //mMatrix.setScale(0.5f, 0.5f); // ��(0,0)Ϊ��������
        mMatrix.setScale(0.5f, 0.5f, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2); // ��bitmap��������
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    private void doSkew(Canvas canvas) {
        //mMatrix.setSkew(0.25f, 0.25f); // ��(0,0)Ϊ���Ĵ���
        mMatrix.setSkew(0.25f, 0.25f, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2); // ��bitmap���Ĵ���
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
    
    private void doPreCalculate(Canvas canvas) {
        mMatrix.setTranslate(100, 100);
        mMatrix.preRotate(30);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
    
    private void doPostCalculate(Canvas canvas) {
        mMatrix.setTranslate(100, 100);
        mMatrix.postRotate(30);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
    
    //��������ǳ�ǿ��ͨ���ı���������˿���ʵ��ƽ�ƣ���ת�����ţ����У�������ʵ��͸��
    private void doPolyToPoly(Canvas canvas) {
        mMatrix = new Matrix();
        float bWidth = mBitmap.getWidth();
        float bHeight = mBitmap.getHeight();
        float[] src = {0, 0, 0, bHeight, bWidth, bHeight,bWidth, 0};
        float[] dst = {0 + 150,0, 0, bHeight, bWidth, bHeight, bWidth - 150, 0};
        mMatrix.setPolyToPoly(src, 0, dst, 0, 4);
        // ����ķ������һ������: 0 ���仯
        //                        1 ƽ��  ӳ���һ����
        //                        2 ��ת  ӳ���һ������
        //                        3 ����  ӳ���һ��������
        //                        4 ͸��  ӳ���һ�����ĸ��
        
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    private void doRectToRect(Canvas canvas) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mMatrix = new Matrix();
        int screenWidth  = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float bWidth = mBitmap.getWidth();
        float bHeight = mBitmap.getHeight();
        RectF src = new RectF(0, 0, bWidth / 2, bHeight / 2);
        RectF dst = new RectF(0, 0, screenWidth, screenHeight);
        mMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.END);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    public void release() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}

