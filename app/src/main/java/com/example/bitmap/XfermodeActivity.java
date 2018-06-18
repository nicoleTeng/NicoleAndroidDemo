package com.example.bitmap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nicole.R;

public class XfermodeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "XfermodeActivity";
    
    private ImageXfermode mImageXfermode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.layout_xfermode);
        
        mImageXfermode = (ImageXfermode) findViewById(R.id.imageview_id);
        
        Button button1 = (Button) findViewById(R.id.style_1);
        Button button2 = (Button) findViewById(R.id.style_2);
        Button button3 = (Button) findViewById(R.id.style_3);
        Button button4 = (Button) findViewById(R.id.style_4);
        Button button5 = (Button) findViewById(R.id.style_5);
        Button button6 = (Button) findViewById(R.id.style_6);
        Button button7 = (Button) findViewById(R.id.style_7);
        Button button8 = (Button) findViewById(R.id.style_8);
        Button button9 = (Button) findViewById(R.id.style_9);
        Button button10 = (Button) findViewById(R.id.style_10);
        
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View view) {
        int tag = Integer.parseInt((String) view.getTag());
        mImageXfermode.setXfermode(tag);
    }
}
