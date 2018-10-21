package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.nicole.R;

/**
 * Created by nicole on 2018-6-25.
 */
public class LayoutActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_layout_vertical);
        //setContentView(R.layout.layout_layout_horizontal);
    }
}
