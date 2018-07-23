package com.example.bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.nicole.R;

public class DoubleExposureImageView extends ImageView {
    private static final String TAG = "DoubleExposureImageView";
    private Bitmap mFrondBitmap = null;
    private Bitmap mBackgroundBitmap = null;
    private Paint mPaint = null;
    private int mOpacity = 100;
    private static final float[] mMatrixGray = new float[] {  
        0.22f, 0.5f, 0.1f, 0, 0,  
        0.22f, 0.5f, 0.1f, 0, 0,  
        0.22f, 0.5f, 0.1f, 0, 0,  
        0, 0, 0, 1, 0  
    };

    public DoubleExposureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFrondBitmap = BitmapUtils.getTargetBitmap(
                context.getResources(), R.drawable.pink, 900, 900);
        mFrondBitmap = BitmapUtils.resizeBySlideLength(mFrondBitmap, 900);
        mBackgroundBitmap = BitmapUtils.getTargetBitmap(
                context.getResources(), R.drawable.girl, 900, 900);
        mBackgroundBitmap = BitmapUtils.resizeBySlideLength(mBackgroundBitmap, 900);

        mPaint = new Paint();
        //setScaleType(ScaleType.CENTER_INSIDE);
        setPaintStyle(1);
    }

	@Override
    public void onDraw(Canvas canvas) {
	    canvas.save();
	    Matrix matrix = new Matrix();
	    matrix.postTranslate((canvas.getWidth() - mBackgroundBitmap.getWidth()) / 2, 0);
        canvas.drawBitmap(mBackgroundBitmap, matrix, null);

        canvas.drawBitmap(mFrondBitmap, matrix, mPaint);
        canvas.restore();
    }

	@Override
    public boolean onTouchEvent(MotionEvent event) {
	    int action = event.getAction() & MotionEvent.ACTION_MASK;
	    int index = event.getActionIndex();
	    int activePointerId = event.getPointerId(index);
	    int count = event.getPointerCount();
	    Log.e(TAG, "txh onTouchEvent");
	    switch (action) {
	    case MotionEvent.ACTION_DOWN:
	        return actionDown(event);
	    case MotionEvent.ACTION_MOVE:
	        switch (count) {
    	        case 1: {
    	            return actionMoveOnePointer(event);
    	        }
    	        case 2: {
    	            return actionMoveTwoPointer(event);
    	        }
    	        default: {
    	            return true;
    	        }
	        }
	    case MotionEvent.ACTION_UP:
	        return true;
	    }
        return false;
	}

	private boolean actionDown(MotionEvent event) {
	    Rect rect = new Rect(0, 0, 100, 100);
	    rect.offset(10, 10);
	    rect.inset(-5, -5);
	    Log.v(TAG, "txh actionDown, rect = " + rect);
	    
	    return true;
	}
	
	private boolean actionMoveOnePointer(MotionEvent event) {
	    int index = event.getPointerId(0);
	    Log.v(TAG, "txh actionMoveOnePointer, index = " + index);
	    return true;
	}
	
	private boolean actionMoveTwoPointer(MotionEvent event) {
        int index = event.getPointerId(0);
        Log.v(TAG, "txh actionMoveTwoPointer, index = " + index);
        return true;
    }
	
	public void saveImage(File file) {
	    Bitmap bitmap = getFinalBitmap();
        if (bitmap != null) {
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }  
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
    public Bitmap getFinalBitmap() {
        int w = mBackgroundBitmap.getWidth();
        int h = mBackgroundBitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas newCanvas = new Canvas(newBitmap);
        newCanvas.drawBitmap(mBackgroundBitmap, 0, 0, null);

//        int intOldColor = mFrondBitmap.getPixel(0,0);
//        ColorFilter filter = new LightingColorFilter(intOldColor, Color.WHITE);
//        mPaint.setColorFilter(filter);
        newCanvas.drawBitmap(mFrondBitmap, 0, 0, mPaint);
        return newBitmap;
    }
    
    public void setPaintStyle(int value) {
        switch (value) {
        case 1: {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setAlpha(mOpacity);
            invalidate();
            break;
        }
        case 2: {
            setPorterDuffColorFilter2();
            invalidate();
            break;
        }
        case 3: {
            setPorterDuffColorFilter();
            invalidate();
            break;
        }
        case 4: {
            setColorMatrixColorFilter(0.8f, 1, 0.2f, 1);
            invalidate();
            break;
        }
        }
    }

    public void setOpacity(int value) {
        mOpacity = value;
        mPaint.setAlpha(value);
        invalidate();
    }

    // ͨ����ɫ���� ColorMatrix �޸���ԭͼ��� RGBA ֵ���Ӷ��ﵽ�˸ı�ͼƬ��ɫЧ����Ŀ�ġ�
    // ����ColorFilter �� ColorMatrixColorFilter�� �� Paint ��setColorFilter �Ϳ��Ըı�ͼƬ��չʾЧ������ɫ�����Ͷȣ��Աȶȵȣ�
    public void setColorMatrixColorFilter(float alpha, float red, float green, float blue) {
        float mRedFilter = red;  
        float mGreenFilter = green;  
        float mBlueFilter = blue; 
        float mAlphaFilter = alpha;  
        ColorMatrix mColorMatrix = new ColorMatrix(new float[] {  
                mRedFilter, 0, 0, 0, 100,     // Red
                0, mGreenFilter, 0, 0, 100,   // Green
                0, 0, mBlueFilter, 0, 0,      // Blue
                0, 0, 0, mAlphaFilter, 0,     // Alpha  
        });  
        mPaint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));  
    }

    private void setLightingColorFilter() {
        int oldColor = mFrondBitmap.getPixel(0,0);
    
        mPaint.reset();
        ColorFilter filter = new LightingColorFilter(oldColor, Color.TRANSPARENT);
        mPaint.setColorFilter(filter);
        mPaint.setAlpha(100);
    }

    // PorterDuffColorFilter: ���������PS���ͼ����ģʽ
    private void setPorterDuffColorFilter() {
        mPaint.reset();
        mPaint.setColorFilter(new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN)); //�䰵
        mPaint.setAlpha(100);
    }

    private void setPorterDuffColorFilter2() {
        mPaint.reset();
        mPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN)); //�䰵
        mPaint.setAlpha(100);
    }

    private Bitmap changeColor(Bitmap src, int colorToReplace, int colorThatWillReplace) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        src.getPixels(pixels, 0, width, 0, 0, width, height);
    
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
    
        int A, R, G, B;
        int pixel;
    
         // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                int index = y * width + x;
                pixel = pixels[index];
                if(pixel == colorToReplace){
                    //change A-RGB individually
                    A = Color.alpha(pixel);
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    pixels[index] = Color.argb(A,R,G,B); 
                    /*or change the whole color
                    pixels[index] = colorThatWillReplace;*/
                }
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }
}
//setDither(boolean dither):��������������Ե����󳡾���Ҫ�����ڻ��ƽ���ɫ�ʻ򺬽����ͼƬʱ��android�Բ���alphaͨ����ͼƬ�����һ��ת����
//��ΪRGB565 ��ʽ�ģ����ָ�ʽռ���ڴ�С������Ϊ��ˣ��ͻ��������ġ�ɫ�����龰�����˸о����ɵĲ�����ô��ͣ����������⣬android�����
//�����������Ὣԭʼ��ɫ�Ĺ��ɴ��������ߵ�ɫֵ����һЩ�ı䣬�Ӷ�����ɫ���ɸ��ӵ���ͣ����˾�����ƽ���Ĺ��ɣ�

//setFilterBitmap(boolean filter):�����������Ϊtrue�����Bitmapͼ��Ľ����˲����������

//setFlags(int flags):����������Paint�������涨��õ�һЩ���ԣ��翹��ݣ��������ȣ�

//setMaskFilter(MaskFilter maskFilter):���û���ʱͼƬ��ԵЧ����������ģ���͸���
//MaskFilter�����ΪPaint�����ԵЧ����
//       ��MaskFilter����չ���Զ�һ��Paint��Ե��alphaͨ��Ӧ��ת����Android���������漸��MaskFilter:
//        BlurMaskFilter   ָ����һ��ģ������ʽ�Ͱ뾶������Paint�ı�Ե��
//        EmbossMaskFilter  ָ���˹�Դ�ķ���ͻ�����ǿ������Ӹ���Ч����

//setPathEffect(PathEffect effect):���������ƻ�������(����)�ķ�ʽ:
//����౾��û����ʲô����Ĵ���ֻ�Ǽ̳���Object��ͨ������ʹ�õ������ļ������࣬��������ͼƬ������ʾ�ġ���ʹ�õ�ʱ��ͨ����
//PathEffect pe = new һ�����������;
//Ȼ��ʹ��Paint��setPathEffect(PathEffect pe)�������ɡ�