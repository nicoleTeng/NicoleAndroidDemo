package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nicole.R;

public class CameraActivity extends Activity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button mSystemCamera;
    private Button mCustomCamera;
    private ImageView mImageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_camera);
        mSystemCamera = (Button) findViewById(R.id.start_system_camera_id);
        mCustomCamera = (Button) findViewById(R.id.start_custom_camera_id);
        mImageView = (ImageView) findViewById(R.id.imageview_id);
        
        mSystemCamera.setOnClickListener(this);
        mCustomCamera.setOnClickListener(this);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_system_camera_id: {
                dispatchTakePictureIntent();
                break;
            }
            case R.id.start_custom_camera_id: {
                break;
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }
}
