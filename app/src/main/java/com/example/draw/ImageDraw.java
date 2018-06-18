package com.example.draw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ImageDraw extends ImageShow {
    private static final String TAG = "ImageDraw";
    
    private int mOffsetX = 0;
    private int mOffsetY = 0;
    private int mPathWidth = 10;
    private int mPathColor = Color.BLUE;
    private Paint mPaint = null;
    private Path mTouchPath = null;
    private Matrix mMatrix;
    private List<DrawPathUnit> mDrawPaths;
    private List<DrawPathUnit> mEarsePaths;
    private Bitmap mBaseBitmap;
    private Bitmap mDrawBitmap;
    private boolean mIsInit = true;
    private boolean mEarseMode = false;

    public ImageDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPaint.setXfermode(null);
        mPaint.setColor(mPathColor);
        mPaint.setStrokeWidth(mPathWidth);

        mDrawPaths = new ArrayList<DrawPathUnit>();
        mEarsePaths = new ArrayList<DrawPathUnit>();

        mBaseBitmap = super.getBackgroundBitmap();
        mDrawBitmap = Bitmap.createBitmap(mBaseBitmap);

        mMatrix = new Matrix();
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsInit) {
            mMatrix.postTranslate((canvas.getWidth() - mBaseBitmap.getWidth()) / 2, 0);
            mOffsetX = (canvas.getWidth() - mBaseBitmap.getWidth()) / 2;
            mOffsetY = (canvas.getHeight() - mBaseBitmap.getHeight()) / 2;
            mIsInit = false;
        }
        if (mBaseBitmap != null) {
            canvas.drawBitmap(mBaseBitmap, mMatrix, null);
        }
        if (mDrawBitmap != null) {
            canvas.drawBitmap(mDrawBitmap, mMatrix, null);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int x = (int) event.getX() - mOffsetX;
        int y = (int) event.getY() - mOffsetY;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mTouchPath = new Path();
                mTouchPath.moveTo(x, y);
                if (mEarseMode) {
                    mEarsePaths.add(new DrawPathUnit(mTouchPath, mPathWidth));
                } else {
                    mDrawPaths.add(new DrawPathUnit(mTouchPath, mPathWidth, mPathColor));
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mTouchPath.lineTo(x, y);
                if (mEarseMode) {
                    updateEarseDraw();
                } else {
                    updatePathDraw();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        invalidate();
        return true;
    }
    
    private void updatePathDraw() {
        Canvas canvas = new Canvas(mDrawBitmap);
        mPaint.setXfermode(null);
        
        for (int i = 0; i < mDrawPaths.size(); i++) {
            mPaint.setStrokeWidth(mDrawPaths.get(i).getPathWidth());
            mPaint.setColor(mDrawPaths.get(i).getPathColor());
            canvas.drawPath(mDrawPaths.get(i).getPath(), mPaint);
        }
    }
    
    private void updateEarseDraw() {
        Canvas canvas = new Canvas(mDrawBitmap);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        
        for (int i = 0; i < mEarsePaths.size(); i++) {
            mPaint.setStrokeWidth(mEarsePaths.get(i).getPathWidth());
            canvas.drawPath(mEarsePaths.get(i).getPath(), mPaint);
        }
    }
    
    public void setEarseMode(boolean value) {
        mEarseMode = value;
        Log.v(TAG, "txh setEarseMode, mEarseMode = " + mEarseMode);
    }
    
    public void setPaintStyle(int value) {
        mPathWidth = value;
        invalidate();
    }

    public void setPaintColor(int color) {
        mPathColor = color;
        invalidate();
    }
    
    public void saveImage(File file) {
        Bitmap finalBitmap = getFinalBitmap();
        if (finalBitmap != null) {
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Bitmap getFinalBitmap() {
        Canvas canvas = new Canvas(mBaseBitmap);
        canvas.drawBitmap(mDrawBitmap, new Matrix(), null);
        return mBaseBitmap;
    }
    
    public void release() {
        if (mBaseBitmap != null && !mBaseBitmap.isRecycled()) {
            mBaseBitmap.recycle();
            mBaseBitmap = null;
        }
        if (mDrawBitmap != null && !mDrawBitmap.isRecycled()) {
            mDrawBitmap.recycle();
            mDrawBitmap = null;
        }
        if (mDrawPaths != null) {
            mDrawPaths.clear();
            mDrawPaths = null;
        }
        if (mEarsePaths != null) {
            mEarsePaths.clear();
            mEarsePaths = null;
        }
    }
}
