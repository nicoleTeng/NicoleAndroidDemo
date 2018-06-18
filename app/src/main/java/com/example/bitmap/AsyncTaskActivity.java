package com.example.bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nicole.R;
import com.example.util.DiskLruCache;
import com.example.util.Utils;

@SuppressLint("NewApi")
public class AsyncTaskActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AsyncTaskActivity";
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String IMAGE_URL = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
    
    private ImageView mImageView;
    private ImageView mImageViewCache;
    private Button mMemoryCacheButton;
    private Button mDownloadButton;
    private Button mDiskCacheButton;
    
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private Context mContext = null;
    private InitDiskCacheTask mInitDiskCacheTask = null;
    
    // ARGB_8888: 4bytes
    // Camera picture takes memory: 3120*4160*4 = 49MB
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitmap_layout);
        mContext = this;

        mImageView = (ImageView) findViewById(R.id.imageview_id);
        mImageViewCache = (ImageView) findViewById(R.id.imageview_id_2);
        mMemoryCacheButton = (Button) findViewById(R.id.button_memory_cache_id);
        mDownloadButton = (Button) findViewById(R.id.button_download_id);
        mDiskCacheButton = (Button) findViewById(R.id.button_disk_cache_id);

        mMemoryCacheButton.setOnClickListener(this);
        mDownloadButton.setOnClickListener(this);
        mDiskCacheButton.setOnClickListener(this);

        createMemoryCache();
        loadBitmap(R.drawable.flower, mImageView);
        
        createDiskCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mInitDiskCacheTask != null) {
            mInitDiskCacheTask.cancel(true);
        }
        
    }
    private void createMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); // NX551J  256M
        Log.v(TAG, "txh createMemoryCache, maxMemory = " + maxMemory + "M");
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //int size = bitmap.getByteCount() / 1024;
                int size = bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                return size;
            }
        };
    }
    
    private Bitmap loadBitmap(int resId, ImageView imageView) {
        String key = String.valueOf(resId);
        Bitmap bitmap = getBitmapFromMemCache(key);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher);
            BitmapWorkerTask task = new BitmapWorkerTask();
            //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, resId);
            task.execute(resId);
        }
        return bitmap;
    }
    
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... params) {
            final String imageKey = String.valueOf(params[0]);
            
            Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap == null) {
                bitmap = BitmapUtils.getTargetBitmap(getResources(), params[0], 900, 900);
            }
            
            addBitmapToMemoryCache(imageKey, bitmap);
            return bitmap;
        }
        
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                mImageView.setImageBitmap(result);
            }
        }
    }
    
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    
    //////////////////////////////////////////////////////////////////////
    private void createDiskCache() {
        File cacheDir = Utils.getDiskCacheDir(this, DISK_CACHE_SUBDIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        mInitDiskCacheTask = new InitDiskCacheTask();
        mInitDiskCacheTask.execute(cacheDir);
    }
    
    class InitDiskCacheTask extends AsyncTask<File, Void, Boolean> {

        @Override
        protected Boolean doInBackground(File... params) {
            Log.v(TAG, "txh InitDiskCacheTask, doInBackground");
            File cacheDir = params[0];
            int version = Utils.getAppVersion(mContext);
            try {
                mDiskLruCache = DiskLruCache.open(cacheDir, version, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
        protected void onPostExecute(Boolean result) {
            Log.v(TAG, "txh onPostExecute, result = " + result);
            if (isCancelled()) {
                return;
            }
            if (result) {
                downloadBitmap();
            }
        }
    }
    
    private void downloadBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String imageUrl = IMAGE_URL;
                    String key = Utils.hashKeyFromUrl(imageUrl);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadUrlToStream(imageUrl, outputStream)) {
                            editor.commit(); // 提交写入操作
                        } else {
                            editor.abort(); // 发生异常时回退整个操作
                        }
                    }
                    mDiskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        Log.v(TAG, "txh downloadUrlToStream");
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);   // 8K
            out = new BufferedOutputStream(outputStream, 8 * 1024); // 8K
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    private void showBitmapFromDisk(String imageUrl) {
        Log.e(TAG, "txh showBitmapFromDisk");
        String key = Utils.hashKeyFromUrl(imageUrl);
        Bitmap bitmap = getBitmapFromMemCache(key);
        if (bitmap != null) {
            mImageViewCache.setImageBitmap(bitmap);
            Log.v(TAG, "txh find in memory cache, return");
            return;
        }
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                FileInputStream is = (FileInputStream) snapShot.getInputStream(0);
                FileDescriptor fileDescriptor = is.getFD();
                bitmap = BitmapUtils.getTargetBitmap(fileDescriptor, 500, 500);
                Log.v(TAG, "txh snapShot = " + snapShot + ", bitmap = " + bitmap);
                mImageViewCache.setImageBitmap(bitmap);
                if (bitmap != null) {
                    addBitmapToMemoryCache(key, bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFromDisk() {
        try {
            String imageUrl = IMAGE_URL;  
            String key = Utils.hashKeyFromUrl(imageUrl);  
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_memory_cache_id: {
            Bitmap bitmap = getBitmapFromMemCache(String.valueOf(R.drawable.flower));
            if (bitmap != null) {
                mImageViewCache.setImageBitmap(bitmap);
            }
            break;
        }
        case R.id.button_download_id: {
            downloadBitmap();
            break;
        }
        case R.id.button_disk_cache_id: {
            showBitmapFromDisk(IMAGE_URL);
            break;
        }
        }
        
    }
}
