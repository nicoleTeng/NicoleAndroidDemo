package com.example.imageloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.util.Log;

import com.example.nicole.R;

public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";
    private String[] mUrlList = Images.imageThumbUrls;
    private Context mContext;
    private ImageLoader mImageLoader;
    private boolean mIsGridViewIdle = true;
    
    public ImageAdapter(Context context) {
        mContext = context;
        mImageLoader = ImageLoader.build(mContext);
    }

    @Override
    public int getCount() {
        Log.v(TAG, "txh getCount, mUrlList.length = " + mUrlList.length);
        return mUrlList.length;
    }

    @Override
    public String getItem(int position) {
        return mUrlList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.v(TAG, "txh getView, position = " + position + ", convertView = " + convertView);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item,
                    parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageView imageView = holder.imageView;
        final String tag = (String) imageView.getTag();
        final String url = getItem(position);
        if (!url.equals(tag)) {
            imageView.setImageResource(R.drawable.ic_launcher);
        }
        if (mIsGridViewIdle) {
            imageView.setTag(url);
            mImageLoader.bindBitmap(url, imageView, 115, 115);
        }
        
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
    
    public void setScrollState(boolean value) {
        mIsGridViewIdle = value;
    }
}
