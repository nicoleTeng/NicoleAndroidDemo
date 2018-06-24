package com.example.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.nicole.R;
import com.example.bitmap.BitmapUtils;
import com.example.util.Utils;

public class CanvasView extends View {
    private static final String TAG = "CanvasView";

    private Context mContext;
    private Paint mPaint = null;
    private float mDensity = 0;
    private Matrix mMatrix = null;
    private int mOri = 0;
    private Rect mImageRect = null;
    private Bitmap mBaseLayer = null;
    private Bitmap mFinalResult = null;
    private boolean mIsFirstDraw = true;
    private Bitmap bmTouchLayer = null;
    private Bitmap bmMosaicLayer = null;
    
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        mDensity = Utils.getDensity(context);
        mBaseLayer = BitmapUtils.getTargetBitmap(context.getResources(), R.drawable.girl, 900, 900);
        mFinalResult = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl).copy(Bitmap.Config.ARGB_8888, true);
        //mFinalResult = Bitmap.createBitmap(mBaseLayer);
        if (mFinalResult != null) {
            Log.v(TAG, "txh new CanvasView, mFinalResult = " + mFinalResult.getWidth() + " * " + mFinalResult.getHeight());
        }
        mMatrix = new Matrix();
        
        // test for get a rotate and scaled bitmap for filePath
        String filePath = "/storage/emulated/0/test.jpg";
        int maxSideLength = 720;
        int ori = BitmapUtils.getExifOrientation(filePath);
        Bitmap bitmap = BitmapUtils.getRotationAndScaledBitmap(filePath, ori, maxSideLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawColor(Color.WHITE);

        if (mBaseLayer == null) {
            return;
        }

        if (mIsFirstDraw) {
            caculateMatrix();
            mIsFirstDraw = false;
            bmTouchLayer = Bitmap.createBitmap(mImageRect.width(), mImageRect.height(), Config.ARGB_8888);
            bmMosaicLayer = Bitmap.createBitmap(mImageRect.width(), mImageRect.height(), Config.ARGB_8888);
        }

        if (mFinalResult != null) {
            canvas.drawBitmap(mFinalResult, mMatrix, null);
        }

        //Rect srcRect = new Rect(0, 0, mFinalResult.getHeight(), mFinalResult.getWidth());
        //canvas.setMatrix(mMatrix);
        //canvas.drawBitmap(mFinalResult, null, mImageRect, null);
        
        //drawARGB(canvas);
        //drawAxis(canvas);
        //drawText(canvas);
        //drawPoint(canvas);
        
        //drawLine(canvas);
        //drawCircle(canvas);
        //drawOval(canvas);
        //drawArc(canvas);
        //drawPath(canvas);
        //drawBitmap(canvas);
    }

    private void caculateMatrix() {
        Log.v(TAG, "txh caculateMatrix");
        float bw = mFinalResult.getWidth();
        float bh = mFinalResult.getHeight();
        setShowMatrix(mOri, bw, bh);
        
        if (mOri == 90 || mOri == 270) {
            float tmp = bw;
            bw = bh;
            bh = tmp;
        }
        float scale = 1f;
        int diff = 0;
        Log.v(TAG, "txh bw = " + bw + ", bh = " + bh);
        Log.v(TAG, "txh getWidth() = " + getWidth() + ", getHeight() = " + getHeight());
        if (bw / bh > (float)getWidth() / getHeight()) {
            scale = getWidth() / bw;
            diff = (int) (getHeight() - bh * scale);
            mImageRect = new Rect(0, diff / 2, getWidth(), getHeight() - diff / 2);
            mMatrix.postScale(scale, scale);
            mMatrix.postTranslate(0,  diff / 2);
            Log.v(TAG, "txh 1, scale = " + scale + ", diff = " + diff + ", imageRect = " + mImageRect);
        } else {
            scale = getHeight() / bh;
            mImageRect = new Rect(diff / 2, 0, getWidth() - diff / 2, getHeight());
            diff = (int) (getWidth() - bw * scale);
            mMatrix.postScale(scale, scale);
            mMatrix.postTranslate(diff / 2, 0);
            Log.v(TAG, "txh 2, scale = " + scale + ", diff = " + diff + ", imageRect = " + mImageRect);
        }
    }

    private void setShowMatrix(int ori, float x, float y) {
        switch (ori) {
        case 90:
            mMatrix.setRotate(ori);
            mMatrix.postTranslate(y, 0);
            break;
        case 180:
            mMatrix.setRotate(ori);
            mMatrix.postTranslate(x, y);
            break;
        case 270:
            mMatrix.setRotate(ori);
            mMatrix.postTranslate(0, x);
            break;
        default:
            break;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    public void updatePathMosaic(int count) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new CornerPathEffect(10));
        paint.setColor(Color.RED);

        //Bitmap bmTouchLayer = Bitmap.createBitmap(mImageRect.width(), mImageRect.height(), Config.ARGB_8888);
        if (bmTouchLayer == null) {
            Log.v(TAG, "txh return, bmTouchLayer = " + bmTouchLayer);
            return;
        }
        Canvas canvas = new Canvas(bmTouchLayer);

        Path path = new Path();
        path.addCircle((float)100, 100 + count * 100, 50, Path.Direction.CCW);
        canvas.drawPath(path, paint);

        //Bitmap bmMosaicLayer = Bitmap.createBitmap(mImageRect.width(), mImageRect.height(), Config.ARGB_8888);
        canvas.setBitmap(bmMosaicLayer);
        //canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(getPrintMosaic(mImageRect.width(), mImageRect.height()), 0, 0, null);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bmTouchLayer, 0, 0, paint);
        paint.setXfermode(null);
        canvas.save();

        Canvas resultCanvas = new Canvas(mFinalResult);
        resultCanvas.drawBitmap(bmMosaicLayer, new Matrix(), null);

        //bmTouchLayer.recycle();
        //bmMosaicLayer.recycle();
        //bmTouchLayer = null;
        //bmMosaicLayer = null;

        invalidate();
        long jvm_heap = Runtime.getRuntime().maxMemory();
        long total_heap = Runtime.getRuntime().totalMemory();
        long free_heap = Runtime.getRuntime().freeMemory();
        long native_heap = Debug.getNativeHeapAllocatedSize();
        Log.v(TAG, "txh total_heap = " + total_heap + ", free_heap = " +
            free_heap + ", jvm_heap = " + jvm_heap + ", native_heap = " + native_heap);
    }

    private Bitmap getPrintMosaic(int width, int height) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mosaic_print_grid);
        return createRepeater(width, height, originalBitmap);
    }

    private Bitmap createRepeater(int width, int height, Bitmap src) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int countX = (width + srcWidth - 1) / srcWidth;
        int countY = (height + srcHeight - 1) / srcHeight;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < countX; ++idx) {
            for (int idy = 0; idy < countY; ++idy) {
                canvas.drawBitmap(src, idx * srcWidth, idy * srcHeight, null);
            }
        }
        if (src != null && !src.isRecycled()) {
            src.recycle();
            src = null;
        }
        return bitmap;
    }
    
    private void drawAxis(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6 * mDensity);

        //����ɫ��x�ᣬ����ɫ��y��

        //��һ�λ���������
        mPaint.setColor(0xff00ff00);//��ɫ
        canvas.drawLine(0, 0, canvasWidth, 0, mPaint);//����x��
        mPaint.setColor(0xff0000ff);//��ɫ
        canvas.drawLine(0, 0, 0, canvasHeight, mPaint);//����y��

        //������ϵƽ�ƺ󣬵ڶ��λ���������
        canvas.translate(canvasWidth / 4, canvasWidth /4);//������ϵ�����½�ƽ��
        mPaint.setColor(0xff00ff00);//��ɫ
        canvas.drawLine(0, 0, canvasWidth, 0, mPaint);//����x��
        mPaint.setColor(0xff0000ff);//��ɫ
        canvas.drawLine(0, 0, 0, canvasHeight, mPaint);//����y��

        //�ٴ�ƽ������ϵ���ڴ˻�������ת����ϵ�������λ���������
        canvas.translate(canvasWidth / 4, canvasWidth / 4);//���ϴ�ƽ�ƵĻ������ٰ�����ϵ�����½�ƽ��
        canvas.rotate(30);//���ڵ�ǰ��ͼ����ϵ��ԭ����ת����ϵ
        mPaint.setColor(0xff00ff00);//��ɫ
        canvas.drawLine(0, 0, canvasWidth, 0, mPaint);//����x��
        mPaint.setColor(0xff0000ff);//��ɫ
        canvas.drawLine(0, 0, 0, canvasHeight, mPaint);//����y��
    }

    private void drawARGB(Canvas canvas) {
        canvas.drawARGB(255, 139, 197, 186);
    }
    
    private void drawText(Canvas canvas) {
        int textHeight = 50;
        int canvasWidth = canvas.getWidth();
        int halfCanvasWidth = canvasWidth / 2;
        float translateY = textHeight;
        
        mPaint.setTextSize(30);

        //���������ı�
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("���������ı�", 0, 0, mPaint);
        canvas.restore();
        translateY += textHeight * 2;

        //������ɫ�ı�
        mPaint.setColor(0xff00ff00);//��������Ϊ��ɫ
        canvas.save();
        canvas.translate(0, translateY);//�����������ƶ�
        canvas.drawText("������ɫ�ı�", 0, 0, mPaint);
        canvas.restore();
        mPaint.setColor(0xff000000);//��������Ϊ��ɫ
        translateY += textHeight * 2;

        //���������
        mPaint.setTextAlign(Paint.Align.LEFT);//���������
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("������ı�", 0, 0, mPaint);
        canvas.restore();
        translateY += textHeight * 2;

        //���þ��ж���
        mPaint.setTextAlign(Paint.Align.CENTER);//���þ��ж���
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("���ж����ı�", 0, 0, mPaint);
        canvas.restore();
        translateY += textHeight * 2;

        //�����Ҷ���
        mPaint.setTextAlign(Paint.Align.RIGHT);//�����Ҷ���
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("�Ҷ����ı�", 0, 0, mPaint);
        canvas.restore();
        mPaint.setTextAlign(Paint.Align.LEFT);//��������Ϊ�����
        translateY += textHeight * 2;

        //�����»���
        mPaint.setUnderlineText(true);//���þ����»���
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("�»����ı�", 0, 0, mPaint);
        canvas.restore();
        mPaint.setUnderlineText(false);//��������Ϊû���»���
        translateY += textHeight * 2;

        //���ƼӴ�����
        mPaint.setFakeBoldText(true);//����������Ϊ����
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("�����ı�", 0, 0, mPaint);
        canvas.restore();
        mPaint.setFakeBoldText(false);//���½���������Ϊ�Ǵ���״̬
        translateY += textHeight * 2;

        //�ı��ƻ������˳ʱ����ת
        canvas.save();
        canvas.translate(0, translateY);
        canvas.rotate(20);
        canvas.drawText("�ı��ƻ��������ת20��", 0, 0, mPaint);
        canvas.restore();
    }
    
    private void drawPoint(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int x = canvasWidth / 2;
        int deltaY = canvasHeight / 3;
        int y = deltaY / 2;
        mPaint.setColor(0xff8bc5ba); //������ɫ
        mPaint.setStrokeWidth(50 * mDensity); //�����߿�����������߿��޷����Ƶ�

        //����CapΪBUTT�ĵ�
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawPoint(x, y, mPaint);

        //����CapΪROUND�ĵ�
        canvas.translate(0, deltaY);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(x, y, mPaint);

        //����CapΪSQUARE�ĵ�
        canvas.translate(0, deltaY);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(x, y, mPaint);
    }

    private void drawLine(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int deltaY = canvasHeight / 3;
        mPaint.setColor(0xff8bc5ba); //������ɫ
        mPaint.setStrokeWidth(5 * mDensity); //�����߿�����������߿��޷����Ƶ�
        canvas.drawLine(0, 0, canvasWidth, deltaY, mPaint);

        float[] pts = {
                50,50,400,50,
                400,50,400,600
                //400,600,50,600,
                //60,600,50,50
        };
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawLines(pts, mPaint);
        
        float[] pts1 = {
                500,600,700,600,
                700,600,700,800
                //400,600,50,600,
                //60,600,50,50
        };
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLines(pts1, mPaint);
        
        float[] pts2 = {
                500,900,700,900,
                700,900,700,1200
                //400,600,50,600,
                //60,600,50,50
        };
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawLines(pts2, mPaint);
    }
    
    private void drawCircle(Canvas canvas){
        mPaint.setColor(0xff8bc5ba);//������ɫ
        mPaint.setStyle(Paint.Style.FILL);//Ĭ�ϻ�ͼΪ���ģʽ
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int halfCanvasWidth = canvasWidth / 2;
        int count = 3;
        int D = canvasHeight / (count + 1);
        int R = D / 2;

        //����Բ
        canvas.translate(0, D / (count + 1));
        canvas.drawCircle(halfCanvasWidth, R, R, mPaint);

        //ͨ����������Բ�γ�Բ��
        //1. ���Ȼ��ƴ�Բ
        canvas.translate(0, D + D / (count + 1));
        canvas.drawCircle(halfCanvasWidth, R, R, mPaint);
        //2. Ȼ�����СԲ����СԲ���Ǵ�Բ���γ�Բ��Ч��
        int r = (int)(R * 0.75);
        mPaint.setColor(0xffffffff);//����������Ϊ��ɫ����СԲ
        canvas.drawCircle(halfCanvasWidth, R, r, mPaint);

        //ͨ��������ͼģʽ����Բ��
        canvas.translate(0, D + D / (count + 1));
        mPaint.setColor(0xff8bc5ba);//������ɫ
        mPaint.setStyle(Paint.Style.STROKE);//��ͼΪ����ģʽ
        float strokeWidth = (float)(R * 0.25);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(halfCanvasWidth, R, R, mPaint);
    }
    
    private void drawOval(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        float quarter = canvasHeight / 4;
        float left = 10 * mDensity;
        float top = 0;
        float right = canvasWidth - left;
        float bottom= quarter;
        RectF rectF = new RectF(left, top, right, bottom);

        //������Բ��������
        mPaint.setStyle(Paint.Style.STROKE);//���û���Ϊ������ģʽ
        mPaint.setStrokeWidth(2 * mDensity);//�����߿�
        mPaint.setColor(0xff8bc5ba);//����������ɫ
        canvas.translate(0, quarter / 4);
        canvas.drawOval(rectF, mPaint);

        //������Բ�������
        mPaint.setStyle(Paint.Style.FILL);//���û���Ϊ���ģʽ
        canvas.translate(0, (quarter + quarter / 4));
        canvas.drawOval(rectF, mPaint);

        //��������Բ���γ������ߺ����ɫ��ͬ��Ч��
        canvas.translate(0, (quarter + quarter / 4));
        //1. ���Ȼ������ɫ
        mPaint.setStyle(Paint.Style.FILL);//���û���Ϊ���ģʽ
        canvas.drawOval(rectF, mPaint);//������Բ�ε����Ч��
        //2. ��������ɫ����Ϊ��ɫ������������
        mPaint.setStyle(Paint.Style.STROKE);//���û���Ϊ����ģʽ
        mPaint.setColor(0xff0000ff);//�������ɫΪ��ɫ
        canvas.drawOval(rectF, mPaint);//������Բ��������
    }
    
    private  void drawArc(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int count = 6;
        float ovalHeight = canvasHeight / (count + 1);
        float left = 10 * mDensity;
        float top = 0;
        float right = canvasWidth - left;
        float bottom= ovalHeight;
        RectF rectF = new RectF(left, top, right, bottom);

        mPaint.setStrokeWidth(2 * mDensity);//�����߿�
        mPaint.setColor(0xff8bc5ba);//������ɫ
        mPaint.setStyle(Paint.Style.FILL);//Ĭ�����û���Ϊ���ģʽ

        //������drawArc������������Բ
        canvas.translate(0, ovalHeight / count);
        canvas.drawArc(rectF, 0, 360, true, mPaint);

        //������Բ���ķ�֮һ,������ӱ��3��λ�ã���3����Ƶ�6���λ��
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, true, mPaint);
        
        //������Բ���ķ�֮һ,��useCenter����Ϊfalse
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, false, mPaint);


        //������Բ���ķ�֮һ��ֻ����������
        mPaint.setStyle(Paint.Style.STROKE);//���û���Ϊ����ģʽ
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, true, mPaint);

        //������Բ���ķ�֮һ,��useCenter����Ϊfalse
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, false, mPaint);
        
        //���ƴ��������ߵ���Բ���ķ�֮һ
        //1. �Ȼ�����Բ����䲿��
        mPaint.setStyle(Paint.Style.FILL);//���û���Ϊ���ģʽ
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, true, mPaint);
        //2. �ٻ�����Բ�������߲���
        mPaint.setStyle(Paint.Style.STROKE);//���û���Ϊ����ģʽ
        mPaint.setColor(0xff0000ff);//������������Ϊ��ɫ
        canvas.drawArc(rectF, 0, 90, true, mPaint);
    }

    private void drawPath(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int deltaX = canvasWidth / 4;
        int deltaY = (int)(deltaX * 0.75);

        mPaint.setColor(0xff8bc5ba);//���û�����ɫ
        mPaint.setStrokeWidth(4);//�����߿�

        /*--------------------------��Path�������-----------------------------*/
        mPaint.setStyle(Paint.Style.FILL);//���û���Ϊ���ģʽ
        Path path = new Path();
        //��Path�м���Arc
        RectF arcRecF = new RectF(0, 0, deltaX, deltaY);
        path.addArc(arcRecF, 0, 135);
        //��Path�м���Oval
        RectF ovalRecF = new RectF(deltaX, 0, deltaX * 2, deltaY);
        path.addOval(ovalRecF, Path.Direction.CCW);
        //��Path�����Circle
        path.addCircle((float)(deltaX * 2.5), deltaY / 2, deltaY / 2, Path.Direction.CCW);
        //��Path�����Rect
        RectF rectF = new RectF(deltaX * 3, 0, deltaX * 4, deltaY);
        path.addRect(rectF, Path.Direction.CCW);
        canvas.drawPath(path, mPaint);

        /*--------------------------��Path����--------------------------------*/
        mPaint.setStyle(Paint.Style.STROKE);//���û���Ϊ����ģʽ
        canvas.translate(0, deltaY * 2);
        Path path2 = path;
        canvas.drawPath(path2, mPaint);

        /*-----------------ʹ��lineTo��arcTo��quadTo��cubicTo����--------------*/
        mPaint.setStyle(Paint.Style.STROKE);//���û���Ϊ����ģʽ
        canvas.translate(0, deltaY * 2);
        Path path3 = new Path();
        //��pointList��¼��ͬ��path�ĸ��������ӵ�
        List<Point> pointList = new ArrayList<Point>();
        //1. ��һ���֣������߶�
        path3.moveTo(0, 0);
        path3.lineTo(deltaX / 2, 0);//�����߶�
        pointList.add(new Point(0, 0));
        pointList.add(new Point(deltaX / 2, 0));
        //2. �ڶ����֣�������Բ���Ͻǵ��ķ�֮һ�Ļ���
        RectF arcRecF1 = new RectF(0, 0, deltaX, deltaY);
        path3.arcTo(arcRecF1, 270, 90);//����Բ��
        pointList.add(new Point(deltaX, deltaY / 2));
        //3. �������֣�������Բ���½ǵ��ķ�֮һ�Ļ���
        //ע�⣬���Ǵ˴�������path��moveTo�����������ʵ��ƶ���������һ��Ҫ����arc�������
        path3.moveTo(deltaX * 1.5f, deltaY);
        RectF arcRecF2 = new RectF(deltaX, 0, deltaX * 2, deltaY);
        path3.arcTo(arcRecF2, 90, 90);//����Բ��
        pointList.add(new Point((int)(deltaX * 1.5), deltaY));
        //4. ���Ĳ��֣����ƶ��ױ���������
        //���ױ��������ߵ������ǵ�ǰ���ʵ�λ�ã�Ȼ����Ҫ���һ�����Ƶ㣬�Լ�һ���յ�
        //�ٴ�ͨ������path��moveTo�������ƶ�����
        path3.moveTo(deltaX * 1.5f, deltaY);
        //���ƶ��ױ���������
        path3.quadTo(deltaX * 2, 0, deltaX * 2.5f, deltaY / 2);
        pointList.add(new Point((int)(deltaX * 2.5), deltaY / 2));
        //5. ���岿�֣��������ױ��������ߣ����ױ��������ߵ����Ҳ�ǵ�ǰ���ʵ�λ��
        //����Ҫ�������Ƶ㣬���ȶ��ױ��������߶�һ�����Ƶ㣬���Ҳ��Ҫһ���յ�
        //�ٴ�ͨ������path��moveTo�������ƶ�����
        path3.moveTo(deltaX * 2.5f, deltaY / 2);
        //�������ױ���������
        path3.cubicTo(deltaX * 3, 0, deltaX * 3.5f, 0, deltaX * 4, deltaY);
        pointList.add(new Point(deltaX * 4, deltaY));

        //Path׼��������������Path���Ƶ�Canvas��
        canvas.drawPath(path3, mPaint);

        //������Path�����ӵ㣬�������Ǵ�ҶԱȹ۲�
        mPaint.setStrokeWidth(10);//�����strokeWidthҪ���õıȻ�pathʱҪ��
        mPaint.setStrokeCap(Paint.Cap.ROUND);//��������ΪԲ��״
        mPaint.setColor(0xff0000ff);//����Բ��Ϊ��ɫ
        for(Point p : pointList){
            //����pointList���������ӵ�
            canvas.drawPoint(p.x, p.y, mPaint);
        }
    }
    
    private void drawBitmap(Canvas canvas) {
        Bitmap bitmap = BitmapUtils.getTargetBitmap(getResources(), R.drawable.ic_launcher, 300, 300);
        if (bitmap == null) {
            return;
        }

        canvas.drawBitmap(bitmap, 0, 0, mPaint);

        //����Bitmap��һ���֣�����������
        //srcRect������Ҫ����Bitmap����һ����
        //srcRect��left��top��right��bottom��ֵ������Bitmap����ľֲ�����ϵΪ�����ġ�
        Rect srcRect = new Rect();
        srcRect.left = 0;
        srcRect.right = bitmap.getWidth();
        srcRect.top = 0;
        srcRect.bottom = (int)(0.33 * bitmap.getHeight());
        float radio = (float)(srcRect.bottom - srcRect.top)  / bitmap.getWidth();
        
        //dstRecF������Ҫ�����Ƶ�Bitmap���쵽����
        RectF dstRecF = new RectF();
        dstRecF.left = 0;
        dstRecF.right = canvas.getWidth();
        dstRecF.top = bitmap.getHeight();
        float dstHeight = (dstRecF.right - dstRecF.left) * radio;
        dstRecF.bottom = dstRecF.top + dstHeight;
        
        canvas.drawBitmap(bitmap, srcRect, dstRecF, mPaint);
    }
}
