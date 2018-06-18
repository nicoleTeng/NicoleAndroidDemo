package com.example.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.nicole.R;
import com.example.bitmap.BitmapUtils;

public class ImageShow extends ImageView {
    private Bitmap mBackgroundBitmap = null;

    public ImageShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundBitmap = BitmapUtils.getTargetBitmap(
                context.getResources(), R.drawable.girl, 900, 900);
        mBackgroundBitmap = BitmapUtils.resizeBySlideLength(mBackgroundBitmap, 900);
    }

	@Override
    public void onDraw(Canvas canvas) {
	    canvas.save();
	    
	    Matrix matrix = new Matrix();
	    matrix.postTranslate((canvas.getWidth() - mBackgroundBitmap.getWidth()) / 2, 0);
        canvas.drawBitmap(mBackgroundBitmap, matrix, null);
        
        canvas.restore();
    }

    public Bitmap getFinalBitmap() {
        int w = mBackgroundBitmap.getWidth();
        int h = mBackgroundBitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas newCanvas = new Canvas(newBitmap);
        newCanvas.drawBitmap(mBackgroundBitmap, 0, 0, null);

        return newBitmap;
    }

    public Bitmap getBackgroundBitmap() {
        return mBackgroundBitmap;
    }
}