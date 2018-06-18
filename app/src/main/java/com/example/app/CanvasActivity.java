package com.example.app;

import com.example.view.CanvasView;

import com.example.nicole.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class CanvasActivity extends Activity {
    private static final String TAG = "CanvasActivity";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.canvas_main);
        
        Button button = (Button) findViewById(R.id.button_draw_id);
        final CanvasView canvasView = (CanvasView) findViewById(R.id.canvas_view_id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                canvasView.updatePathMosaic(count);
                count++;
            }
        });
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
