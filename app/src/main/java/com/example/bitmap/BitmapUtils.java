package com.example.bitmap;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.Log;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";
    
    public static Bitmap getTargetBitmap(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        String imageType = options.outMimeType;
        Log.v(TAG, "txh options.outWidth = " + imageWidth + ", options.outHeight = " + imageHeight + ", imageType = " + imageType);

        //o.inSampleSize = computeSampleSize(o, -1, targetWidth*targetHeight);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(res, resId, options);
        Log.v(TAG, "txh bitmap.getWidth() = " + bitmap.getWidth() + ", bitmap.getHeight() = " + bitmap.getHeight());
        
        return bitmap;
    }
    
    public static Bitmap getTargetBitmap(FileDescriptor fd, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        //Log.v(TAG, "txh imageWidht = " + imageWidth + ", imageHeight = " + imageHeight + ", imageType = " + imageType);
        
        //o.inSampleSize = computeSampleSize(o, -1, targetWidth*targetHeight);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
        //Log.v(TAG, "txh bitmap.getWidth() = " + bitmap.getWidth() + ", bitmap.getHeight() = " + bitmap.getHeight());
        
        return bitmap;
    }

    public static int getExifOrientation(String filePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException ex) {
            Log.v(TAG, "failed to find file to read exif rotation: " + filePath);
        }
        return getExifOrientation(exif);
    }

    public static int getExifOrientation(ExifInterface exif) {
        int degree = 0;
        if (exif != null) {
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (ori != -1) {
                switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
                }
            }
        }
        return degree;
    }

    public static Bitmap getRotationAndScaledBitmap(Resources res, int resId, int ori, int maxLength) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int imageSide = Math.max(imageWidth, imageHeight);
        int sampleSize = 1;
        while (imageSide / 2 >= maxLength) {
            imageSide >>= 1;
            sampleSize <<= 1;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(res, resId, options);

        return rotateAndScaleBitmap(bitmap, ori, maxLength);
    }
    
    public static Bitmap getRotationAndScaledBitmap(String path, int ori, int maxSideLength) {
        if (maxSideLength <= 0 || path == null) {
            throw new IllegalArgumentException("bad argument to getRotationAndScaledBitmap");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        int w = options.outWidth;
        int h = options.outHeight;

        if (w <= 0 || h<= 0) {
            return null;
        }

        int imageSide = Math.max(w, h);
        int sampleSize = 1;
        while (imageSide / 2 >= maxSideLength) {
            imageSide >>= 1;
            sampleSize <<= 1;
        }
        
        if (sampleSize <= 0 || (int) (Math.min(w, h) / sampleSize) <= 0) {
            return null;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);

        return rotateAndScaleBitmap(bitmap, ori, maxSideLength);
    }
    
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {  
            final int halfWidth = width / 2;  
            final int halfHeight = height / 2;
            while ((halfWidth / inSampleSize) >= reqWidth
                    && (halfHeight / inSampleSize) >= reqHeight) {  
                inSampleSize *= 2;
            }  
        }
        return inSampleSize;
    }
    
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap resizeBySlideLength(Bitmap bitmap, int length) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float)length / width, (float)length / height);
//        if (scale >= 1.0) {
//            return bitmap;
//        }
        return  resizeBitmapByScale(bitmap, scale, false);
    }

    public static Bitmap resizeBitmapByScale(Bitmap bitmap, float scale, boolean recycle) {
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.scale(scale, scale);
        Paint patin = new Paint(Paint.FILTER_BITMAP_FLAG);// | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, null);
        if (recycle) {
            bitmap.recycle();
        }
        return newBitmap;
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        @SuppressWarnings("unchecked")
        SoftReference softRef = new SoftReference(
                BitmapFactory.decodeStream(input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap rotateAndScaleBitmap(Bitmap bitmap, int ori, int maxLength) {
        Matrix matrix = new Matrix();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = Math.min((float)maxLength / w, (float)maxLength / h);
        if (ori == ExifInterface.ORIENTATION_ROTATE_90 ||
                ori == ExifInterface.ORIENTATION_ROTATE_270 ||
                ori == ExifInterface.ORIENTATION_TRANSPOSE ||
                ori == ExifInterface.ORIENTATION_TRANSVERSE) {
            int tmp = w;
            w = h;
            h = tmp;
        }
        switch (ori) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90, w / 2f, h / 2f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180, w / 2f, h / 2f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270, w / 2f, h / 2f);
                break;
            default:
        }
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }
}
