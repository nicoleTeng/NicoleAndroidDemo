package com.example.service;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

@SuppressLint("NewApi")
public class CategoryTask extends Thread {
    private static final String TAG = "CategoryTask";
    public static final Uri URI_FILES_EXTERNAL = MediaStore.Files.getContentUri("external");
    public static final Uri URI_IMAGE = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final int INDEX_ID = 0;
    //public static final int INDEX_TITLE = 1;
    //public static final int INDEX_MIME_TYPE = 2;
    public static final int INDEX_DATA = 1;
    public static final String[] BASE_FILES_PROJECTION = {
        BaseColumns._ID,
        MediaColumns.DATA
    };
    private Context mContext = null;
    
    public CategoryTask(Context context) {
        mContext = context;
    }
    
    @Override
    public void run() {
        Log.v(TAG, "txh CategoryTask run, currentThread.name = " + Thread.currentThread().getName());
        long before = System.currentTimeMillis();
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(URI_IMAGE, BASE_FILES_PROJECTION, null, null, null);
        if (cursor == null) {
            throw new RuntimeException("cannot get cursor for image");
        }
        Log.v(TAG, "txh cursor.size = " + cursor.getCount());
        try {
            while (cursor.moveToNext()) {
                loadFromCursor(cursor);
            } 
        } finally {
            cursor.close();
        }
        long costTime = System.currentTimeMillis() - before;
        Log.v(TAG, "txh costTime = " + costTime + "ms");
    }
    
    private void loadFromCursor(Cursor cursor) {
        int id = cursor.getInt(INDEX_ID);
        //String title = cursor.getString(INDEX_TITLE);
        String path = cursor.getString(INDEX_DATA);
        
        // TODO: getTagFromAlgorithm, then write to database
        Log.v(TAG, "txh loadFromCursor, id = " + id + ", path = " + path);
        
    }
}
