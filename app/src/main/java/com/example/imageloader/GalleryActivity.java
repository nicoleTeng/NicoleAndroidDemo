package com.example.imageloader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

import com.example.nicole.R;

public class GalleryActivity extends Activity implements OnScrollListener {
    private static final String TAG = "GalleryActivity";
    private ImageAdapter mImageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gridview);
        
        GridView gridView = (GridView) findViewById(R.id.gridview1);
        mImageAdapter = new ImageAdapter(this);
        gridView.setAdapter(mImageAdapter);
        
        gridView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.v(TAG, "txh onScrollStateChanged, scrollState = " + scrollState);
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            mImageAdapter.setScrollState(true);
            mImageAdapter.notifyDataSetChanged();
        } else {
            mImageAdapter.setScrollState(false);
        }
        
    }
}
