package com.example.bitmap;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.nicole.R;
import com.example.util.Utils;

public class DoubleExposureActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "DoubleEcposureActivity";
    private DoubleExposureImageView mImageView = null;
    private SeekBar mSeekBar = null;
    private SoundPool mSoundPool = null;
    private int mSoundID = -1;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_exposure_layout);
        
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

        mImageView = (DoubleExposureImageView) findViewById(R.id.imageview_id);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar_id);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mSoundID = mSoundPool.load(this, R.raw.wheel_adjuster_tone, 1);
        if (mSoundID == 0) {
            // 加载失败
        } else {
            // 加载成功   mSoundID == 1
        }

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                Log.v(TAG, "txh onProgressChanged, progress = " + progress);
                mImageView.setOpacity(progress);
                if (mSoundID > 0) {
                    //mSoundPool.play(mSoundID, 1, 1, 1, 1, 2);
                }
                
            }
        });
        
    }

    @Override
    public void onPause() {
        super.onPause();
        mSoundPool.pause(mSoundID);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            mImageView.setPaintStyle(1);
            break;
        }
        case R.id.style_2: {
            mImageView.setPaintStyle(2);
            break;
        }
        case R.id.style_3: {
            mImageView.setPaintStyle(3);
            break;
        }
        case R.id.style_4: {
            mImageView.setPaintStyle(4);
            break;
        }
        case R.id.button_add: {
            break;
        }
        default: {
            break;
        }
        }
    }
    
    
}
