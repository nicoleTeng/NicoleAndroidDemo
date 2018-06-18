package com.example.draw;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nicole.R;
import com.example.util.Utils;

public class DrawActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "DrawActivity";
    private ImageDraw mImageView = null;
    private boolean mIsEarse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_draw);
        
        Button earseButton = (Button) findViewById(R.id.earse_id);
        earseButton.setOnClickListener(this);
        Button saveButton = (Button) findViewById(R.id.save_id);
        saveButton.setOnClickListener(this);
        
        Button styleButton1 = (Button) findViewById(R.id.style_1);
        Button styleButton2 = (Button) findViewById(R.id.style_2);
        Button styleButton3 = (Button) findViewById(R.id.style_3);
        Button styleButton4 = (Button) findViewById(R.id.style_4);
        styleButton1.setOnClickListener(this);
        styleButton2.setOnClickListener(this);
        styleButton3.setOnClickListener(this);
        styleButton4.setOnClickListener(this);
        
        Button colorButton1 = (Button) findViewById(R.id.color_1);
        Button colorButton2 = (Button) findViewById(R.id.color_2);
        Button colorButton3 = (Button) findViewById(R.id.color_3);
        Button colorButton4 = (Button) findViewById(R.id.color_4);
        colorButton1.setOnClickListener(this);
        colorButton2.setOnClickListener(this);
        colorButton3.setOnClickListener(this);
        colorButton4.setOnClickListener(this);

        mImageView = (ImageDraw) findViewById(R.id.imageview_id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.earse_id: {
            mIsEarse = !mIsEarse;
            if (mIsEarse) {
                mImageView.setEarseMode(true);
                ((Button) findViewById(R.id.earse_id)).setTextColor(Color.RED);
            } else {
                mImageView.setEarseMode(false);
                ((Button) findViewById(R.id.earse_id)).setTextColor(Color.WHITE);
            }
            break;
        }
        case R.id.save_id: {
            File photo = new File(Utils.getAlbumStorageDir("doubleExposure"),
                    String.format("double_exposure_%d.jpg", System.currentTimeMillis()));
            mImageView.saveImage(photo);

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  
            Uri contentUri = Uri.fromFile(photo);  
            intent.setData(contentUri);
            sendBroadcast(intent);
            break;
        }
        case R.id.style_1: {
            mImageView.setPaintStyle(10);
            break;
        }
        case R.id.style_2: {
            mImageView.setPaintStyle(20);
            break;
        }
        case R.id.style_3: {
            mImageView.setPaintStyle(30);
            break;
        }
        case R.id.style_4: {
            mImageView.setPaintStyle(40);
            break;
        }
        case R.id.color_1: {
            mImageView.setPaintColor(Color.RED);
            break;
        }
        case R.id.color_2: {
            mImageView.setPaintColor(Color.GREEN);
            break;
        }
        case R.id.color_3: {
            mImageView.setPaintColor(Color.BLUE);
            break;
        }
        case R.id.color_4: {
            mImageView.setPaintColor(Color.LTGRAY);
            break;
        }
        default: {
            break;
        }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageView.release();
    }
}
