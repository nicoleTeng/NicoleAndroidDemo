package com.example.util;

import java.io.Closeable;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Utils {
    private static final String TAG = "Utils";

    @SuppressLint("NewApi")
	public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //  /sdcard/Android/data/<application package>/cache 
            cachePath = context.getExternalCacheDir().getPath(); 
        } else {
            //  /data/data/<application package>/cache
            cachePath = context.getCacheDir().getPath();
        }
        Log.v(TAG, "txh getDiskCacheDir, path = " + cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }
    
    public static File getAlbumStorageDir(String albumName) {  
        // Get the directory for the user's public pictures directory.  
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);  
        if (!file.mkdirs()) {  
            Log.e(TAG, "txh Directory not created");  
        }  
        return file;  
    }
    
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
    
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
	
	public static String hashKeyFromUrl(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    public static void closeSilently(Closeable c) {
        if (c == null) return;
        
        try {
            c.close();
        } catch (Throwable t) {
            Log.v(TAG, "Close failed " + t);
        }
    }
    
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void showDisplayParameter(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.v(TAG, "txh showDisplayParameter width: " + dm.widthPixels);
        Log.v(TAG, "txh showDisplayParameter heigth: " + dm.heightPixels);
    }

    /*
     * get StatusBar's Height
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;  
        //获取status_bar_height资源的ID  
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");  
        if (resourceId > 0) {  
            //根据资源ID获取响应的尺寸值  
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);  
        }
        Log.e(TAG, "txh getStatusBarHeight = " + statusBarHeight);
        return statusBarHeight;
    }
    
    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        
        return dm.density;
    }
    
    @SuppressLint("NewApi")
	public static int getTargetSdkVersion(Context context) {
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
            Log.v(TAG, "txh info.applicationInfo.uid = " + info.applicationInfo.uid);
            Log.v(TAG, "txh info.applicationInfo.nativeLibraryDir = " + info.applicationInfo.nativeLibraryDir);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }
}
