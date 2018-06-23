package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.nicole.R;

public class CustomViewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customview);
    }
}
