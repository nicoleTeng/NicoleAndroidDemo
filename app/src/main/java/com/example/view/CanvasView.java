package com.example.view;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
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
import android.widget.Toast;

import com.example.nicole.R;
import com.example.bitmap.BitmapUtils;
import com.example.util.MediaFileUtils;
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

        //用绿色画x轴，用蓝色画y轴

        //第一次绘制坐标轴
        mPaint.setColor(0xff00ff00);//绿色
        canvas.drawLine(0, 0, canvasWidth, 0, mPaint);//绘制x轴
        mPaint.setColor(0xff0000ff);//蓝色
        canvas.drawLine(0, 0, 0, canvasHeight, mPaint);//绘制y轴

        //对坐标系平移后，第二次绘制坐标轴
        canvas.translate(canvasWidth / 4, canvasWidth /4);//把坐标系向右下角平移
        mPaint.setColor(0xff00ff00);//绿色
        canvas.drawLine(0, 0, canvasWidth, 0, mPaint);//绘制x轴
        mPaint.setColor(0xff0000ff);//蓝色
        canvas.drawLine(0, 0, 0, canvasHeight, mPaint);//绘制y轴

        //再次平移坐标系并在此基础上旋转坐标系，第三次绘制坐标轴
        canvas.translate(canvasWidth / 4, canvasWidth / 4);//在上次平移的基础上再把坐标系向右下角平移
        canvas.rotate(30);//基于当前绘图坐标系的原点旋转坐标系
        mPaint.setColor(0xff00ff00);//绿色
        canvas.drawLine(0, 0, canvasWidth, 0, mPaint);//绘制x轴
        mPaint.setColor(0xff0000ff);//蓝色
        canvas.drawLine(0, 0, 0, canvasHeight, mPaint);//绘制y轴
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

        //绘制正常文本
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("正常绘制文本", 0, 0, mPaint);
        canvas.restore();
        translateY += textHeight * 2;

        //绘制绿色文本
        mPaint.setColor(0xff00ff00);//设置字体为绿色
        canvas.save();
        canvas.translate(0, translateY);//将画笔向下移动
        canvas.drawText("绘制绿色文本", 0, 0, mPaint);
        canvas.restore();
        mPaint.setColor(0xff000000);//重新设置为黑色
        translateY += textHeight * 2;

        //设置左对齐
        mPaint.setTextAlign(Paint.Align.LEFT);//设置左对齐
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("左对齐文本", 0, 0, mPaint);
        canvas.restore();
        translateY += textHeight * 2;

        //设置居中对齐
        mPaint.setTextAlign(Paint.Align.CENTER);//设置居中对齐
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("居中对齐文本", 0, 0, mPaint);
        canvas.restore();
        translateY += textHeight * 2;

        //设置右对齐
        mPaint.setTextAlign(Paint.Align.RIGHT);//设置右对齐
        canvas.save();
        canvas.translate(halfCanvasWidth, translateY);
        canvas.drawText("右对齐文本", 0, 0, mPaint);
        canvas.restore();
        mPaint.setTextAlign(Paint.Align.LEFT);//重新设置为左对齐
        translateY += textHeight * 2;

        //设置下划线
        mPaint.setUnderlineText(true);//设置具有下划线
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("下划线文本", 0, 0, mPaint);
        canvas.restore();
        mPaint.setUnderlineText(false);//重新设置为没有下划线
        translateY += textHeight * 2;

        //绘制加粗文字
        mPaint.setFakeBoldText(true);//将画笔设置为粗体
        canvas.save();
        canvas.translate(0, translateY);
        canvas.drawText("粗体文本", 0, 0, mPaint);
        canvas.restore();
        mPaint.setFakeBoldText(false);//重新将画笔设置为非粗体状态
        translateY += textHeight * 2;

        //文本绕绘制起点顺时针旋转
        canvas.save();
        canvas.translate(0, translateY);
        canvas.rotate(20);
        canvas.drawText("文本绕绘制起点旋转20度", 0, 0, mPaint);
        canvas.restore();
    }
    
    private void drawPoint(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int x = canvasWidth / 2;
        int deltaY = canvasHeight / 3;
        int y = deltaY / 2;
        mPaint.setColor(0xff8bc5ba); //设置颜色
        mPaint.setStrokeWidth(50 * mDensity); //设置线宽，如果不设置线宽，无法绘制点

        //绘制Cap为BUTT的点
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawPoint(x, y, mPaint);

        //绘制Cap为ROUND的点
        canvas.translate(0, deltaY);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPoint(x, y, mPaint);

        //绘制Cap为SQUARE的点
        canvas.translate(0, deltaY);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPoint(x, y, mPaint);
    }

    private void drawLine(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int deltaY = canvasHeight / 3;
        mPaint.setColor(0xff8bc5ba); //设置颜色
        mPaint.setStrokeWidth(5 * mDensity); //设置线宽，如果不设置线宽，无法绘制点
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
        mPaint.setColor(0xff8bc5ba);//设置颜色
        mPaint.setStyle(Paint.Style.FILL);//默认绘图为填充模式
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int halfCanvasWidth = canvasWidth / 2;
        int count = 3;
        int D = canvasHeight / (count + 1);
        int R = D / 2;

        //绘制圆
        canvas.translate(0, D / (count + 1));
        canvas.drawCircle(halfCanvasWidth, R, R, mPaint);

        //通过绘制两个圆形成圆环
        //1. 首先绘制大圆
        canvas.translate(0, D + D / (count + 1));
        canvas.drawCircle(halfCanvasWidth, R, R, mPaint);
        //2. 然后绘制小圆，让小圆覆盖大圆，形成圆环效果
        int r = (int)(R * 0.75);
        mPaint.setColor(0xffffffff);//将画笔设置为白色，画小圆
        canvas.drawCircle(halfCanvasWidth, R, r, mPaint);

        //通过线条绘图模式绘制圆环
        canvas.translate(0, D + D / (count + 1));
        mPaint.setColor(0xff8bc5ba);//设置颜色
        mPaint.setStyle(Paint.Style.STROKE);//绘图为线条模式
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

        //绘制椭圆形轮廓线
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔为画线条模式
        mPaint.setStrokeWidth(2 * mDensity);//设置线宽
        mPaint.setColor(0xff8bc5ba);//设置线条颜色
        canvas.translate(0, quarter / 4);
        canvas.drawOval(rectF, mPaint);

        //绘制椭圆形填充面
        mPaint.setStyle(Paint.Style.FILL);//设置画笔为填充模式
        canvas.translate(0, (quarter + quarter / 4));
        canvas.drawOval(rectF, mPaint);

        //画两个椭圆，形成轮廓线和填充色不同的效果
        canvas.translate(0, (quarter + quarter / 4));
        //1. 首先绘制填充色
        mPaint.setStyle(Paint.Style.FILL);//设置画笔为填充模式
        canvas.drawOval(rectF, mPaint);//绘制椭圆形的填充效果
        //2. 将线条颜色设置为蓝色，绘制轮廓线
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔为线条模式
        mPaint.setColor(0xff0000ff);//设置填充色为蓝色
        canvas.drawOval(rectF, mPaint);//设置椭圆的轮廓线
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

        mPaint.setStrokeWidth(2 * mDensity);//设置线宽
        mPaint.setColor(0xff8bc5ba);//设置颜色
        mPaint.setStyle(Paint.Style.FILL);//默认设置画笔为填充模式

        //绘制用drawArc绘制完整的椭圆
        canvas.translate(0, ovalHeight / count);
        canvas.drawArc(rectF, 0, 360, true, mPaint);

        //绘制椭圆的四分之一,起点是钟表的3点位置，从3点绘制到6点的位置
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, true, mPaint);
        
        //绘制椭圆的四分之一,将useCenter设置为false
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, false, mPaint);


        //绘制椭圆的四分之一，只绘制轮廓线
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔为线条模式
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, true, mPaint);

        //绘制椭圆的四分之一,将useCenter设置为false
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, false, mPaint);
        
        //绘制带有轮廓线的椭圆的四分之一
        //1. 先绘制椭圆的填充部分
        mPaint.setStyle(Paint.Style.FILL);//设置画笔为填充模式
        canvas.translate(0, (ovalHeight + ovalHeight / count));
        canvas.drawArc(rectF, 0, 90, true, mPaint);
        //2. 再绘制椭圆的轮廓线部分
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔为线条模式
        mPaint.setColor(0xff0000ff);//设置轮廓线条为蓝色
        canvas.drawArc(rectF, 0, 90, true, mPaint);
    }

    private void drawPath(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int deltaX = canvasWidth / 4;
        int deltaY = (int)(deltaX * 0.75);

        mPaint.setColor(0xff8bc5ba);//设置画笔颜色
        mPaint.setStrokeWidth(4);//设置线宽

        /*--------------------------用Path画填充面-----------------------------*/
        mPaint.setStyle(Paint.Style.FILL);//设置画笔为填充模式
        Path path = new Path();
        //向Path中加入Arc
        RectF arcRecF = new RectF(0, 0, deltaX, deltaY);
        path.addArc(arcRecF, 0, 135);
        //向Path中加入Oval
        RectF ovalRecF = new RectF(deltaX, 0, deltaX * 2, deltaY);
        path.addOval(ovalRecF, Path.Direction.CCW);
        //向Path中添加Circle
        path.addCircle((float)(deltaX * 2.5), deltaY / 2, deltaY / 2, Path.Direction.CCW);
        //向Path中添加Rect
        RectF rectF = new RectF(deltaX * 3, 0, deltaX * 4, deltaY);
        path.addRect(rectF, Path.Direction.CCW);
        canvas.drawPath(path, mPaint);

        /*--------------------------用Path画线--------------------------------*/
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔为线条模式
        canvas.translate(0, deltaY * 2);
        Path path2 = path;
        canvas.drawPath(path2, mPaint);

        /*-----------------使用lineTo、arcTo、quadTo、cubicTo画线--------------*/
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔为线条模式
        canvas.translate(0, deltaY * 2);
        Path path3 = new Path();
        //用pointList记录不同的path的各处的连接点
        List<Point> pointList = new ArrayList<Point>();
        //1. 第一部分，绘制线段
        path3.moveTo(0, 0);
        path3.lineTo(deltaX / 2, 0);//绘制线段
        pointList.add(new Point(0, 0));
        pointList.add(new Point(deltaX / 2, 0));
        //2. 第二部分，绘制椭圆右上角的四分之一的弧线
        RectF arcRecF1 = new RectF(0, 0, deltaX, deltaY);
        path3.arcTo(arcRecF1, 270, 90);//绘制圆弧
        pointList.add(new Point(deltaX, deltaY / 2));
        //3. 第三部分，绘制椭圆左下角的四分之一的弧线
        //注意，我们此处调用了path的moveTo方法，将画笔的移动到我们下一处要绘制arc的起点上
        path3.moveTo(deltaX * 1.5f, deltaY);
        RectF arcRecF2 = new RectF(deltaX, 0, deltaX * 2, deltaY);
        path3.arcTo(arcRecF2, 90, 90);//绘制圆弧
        pointList.add(new Point((int)(deltaX * 1.5), deltaY));
        //4. 第四部分，绘制二阶贝塞尔曲线
        //二阶贝塞尔曲线的起点就是当前画笔的位置，然后需要添加一个控制点，以及一个终点
        //再次通过调用path的moveTo方法，移动画笔
        path3.moveTo(deltaX * 1.5f, deltaY);
        //绘制二阶贝塞尔曲线
        path3.quadTo(deltaX * 2, 0, deltaX * 2.5f, deltaY / 2);
        pointList.add(new Point((int)(deltaX * 2.5), deltaY / 2));
        //5. 第五部分，绘制三阶贝塞尔曲线，三阶贝塞尔曲线的起点也是当前画笔的位置
        //其需要两个控制点，即比二阶贝赛尔曲线多一个控制点，最后也需要一个终点
        //再次通过调用path的moveTo方法，移动画笔
        path3.moveTo(deltaX * 2.5f, deltaY / 2);
        //绘制三阶贝塞尔曲线
        path3.cubicTo(deltaX * 3, 0, deltaX * 3.5f, 0, deltaX * 4, deltaY);
        pointList.add(new Point(deltaX * 4, deltaY));

        //Path准备就绪后，真正将Path绘制到Canvas上
        canvas.drawPath(path3, mPaint);

        //最后绘制Path的连接点，方便我们大家对比观察
        mPaint.setStrokeWidth(10);//将点的strokeWidth要设置的比画path时要大
        mPaint.setStrokeCap(Paint.Cap.ROUND);//将点设置为圆点状
        mPaint.setColor(0xff0000ff);//设置圆点为蓝色
        for(Point p : pointList){
            //遍历pointList，绘制连接点
            canvas.drawPoint(p.x, p.y, mPaint);
        }
    }
    
    private void drawBitmap(Canvas canvas) {
        Bitmap bitmap = BitmapUtils.getTargetBitmap(getResources(), R.drawable.ic_launcher, 300, 300);
        if (bitmap == null) {
            return;
        }

        canvas.drawBitmap(bitmap, 0, 0, mPaint);

        //绘制Bitmap的一部分，并对其拉伸
        //srcRect定义了要绘制Bitmap的哪一部分
        //srcRect中left、top、right、bottom的值都是以Bitmap本身的局部坐标系为基础的。
        Rect srcRect = new Rect();
        srcRect.left = 0;
        srcRect.right = bitmap.getWidth();
        srcRect.top = 0;
        srcRect.bottom = (int)(0.33 * bitmap.getHeight());
        float radio = (float)(srcRect.bottom - srcRect.top)  / bitmap.getWidth();
        
        //dstRecF定义了要将绘制的Bitmap拉伸到哪里
        RectF dstRecF = new RectF();
        dstRecF.left = 0;
        dstRecF.right = canvas.getWidth();
        dstRecF.top = bitmap.getHeight();
        float dstHeight = (dstRecF.right - dstRecF.left) * radio;
        dstRecF.bottom = dstRecF.top + dstHeight;
        
        canvas.drawBitmap(bitmap, srcRect, dstRecF, mPaint);
    }
}
