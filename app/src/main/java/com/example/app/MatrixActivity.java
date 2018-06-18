package com.example.app;

import com.example.nicole.R;
import com.example.view.MatrixView;

import android.app.Activity;
import android.os.Bundle;

public class MatrixActivity extends Activity {
    private static final String TAG = "MainActivity";
    private MatrixView mView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrix_layout);
        mView = (MatrixView) findViewById(R.id.view_id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView.release();
    }
}
