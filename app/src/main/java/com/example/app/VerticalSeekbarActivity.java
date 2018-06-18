package com.example.app;

import com.example.nicole.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VerticalSeekbarActivity extends Activity {
	private static final String TAG = "VerticalSeekbarActivity";
	private static final int MESSAGE_SCROLL_TO = 1;
	private static final int FRAME_COUNT = 30;
	private static final int DELAYED_TIME = 33;
	
	private MoveImageView mImageView = null;
	private int mLastPosition = 0;
	private Boolean mMoveRight = true;
	private int mCount = 0;
	
	private Handler mHandler = new Handler() {
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case MESSAGE_SCROLL_TO: {
	            mCount++;
	            if (mCount <= FRAME_COUNT) {
	                float fraction = mCount / (float) FRAME_COUNT;
	                int scrollX = (int) (fraction * 100);
	                mImageView.scrollTo(scrollX, 0);
	                mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
	            } else {
	                mCount = 0;
	            }
	            break;
	        }
	        default:
	            break;
	        }
	    }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "txh onCreate");
		setContentView(R.layout.second);
		
		TextView textView = (TextView) findViewById(R.id.task_id);
        int taskId = getTaskId();
		textView.setText(Integer.toString(taskId));
		
		mImageView = (MoveImageView) findViewById(R.id.move_view_id); 
		Button button = (Button) findViewById(R.id.move_button_id);
		button.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mMoveRight) {
                    mLastPosition += -500;
                    mMoveRight = false;
                } else {
                    mLastPosition += 500;
                    mMoveRight = true;
                }
                mImageView.scrollViewByCusom(mLastPosition, 0);
            }
        });
		Button handlerButton = (Button) findViewById(R.id.move_handle_button_id);
		handlerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHandler.obtainMessage(MESSAGE_SCROLL_TO).sendToTarget();
            }
		    
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "txh onStart");
	}

	@Override
	protected void onResume() {
		super.onStart();
		Log.v(TAG, "txh onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "txh onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "txh onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "txh onDestroy");
	}
}
