package com.example.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.nicole.R;
import com.example.provider.ProviderActivity;

public class DialogActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "DialogActivity";
	private ArrayList<byte[]> mLeakyContainer = new ArrayList<byte[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "txh onCreate");
		setContentView(R.layout.layout_dialog);
		TextView textView = (TextView) findViewById(R.id.task_id);
        int taskId = getTaskId();
		textView.setText(this.getClass().getName() + ".taskId = " + Integer.toString(taskId));

		Button showSystemDialog = (Button) findViewById(R.id.show_system_dialog);
		Button showCustomDialog = (Button) findViewById(R.id.show_custom_dialog);
		Button moveButton = (Button) findViewById(R.id.move_button);
		Button notifyButton = (Button) findViewById(R.id.notify_button);
		Button heapButton = (Button) findViewById(R.id.heap_button);
		
		showSystemDialog.setOnClickListener(this);
		showCustomDialog.setOnClickListener(this);
		moveButton.setOnClickListener(this);
		notifyButton.setOnClickListener(this);
		heapButton.setOnClickListener(this);
		Context cApplication = getApplicationContext();
	    Context cActivity = heapButton.getContext();
	    Log.v(TAG, "txh cApplication = " + cApplication + ", cActivity = " + cActivity);
	    Resources rApplication = cApplication.getResources();
	    Resources rActivity = cActivity.getResources();
	    Log.v(TAG, "txh rApplication = " + rApplication + ", rActivity = " + rActivity);
		
		//int slipLimitDistance = ViewConfiguration.get(this.getBaseContext()).getScaledTouchSlop();
		//Log.v(TAG, "txh slip limit distance = " + slipLimitDistance);
		//userSerialize();
		int cpu_count = Runtime.getRuntime().availableProcessors();
		Log.v(TAG, "txh cpu_count = " + cpu_count);
	}

    @SuppressLint("NewApi")
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.show_system_dialog: {
            createSystemDialog();
            break;
        }
        case R.id.show_custom_dialog: {
            createCustomDialog();
            break;
        }
        case R.id.move_button: {
            moveButton((Button) v);
            break;
        }
        case R.id.notify_button: {
            sendNotification();
            break;
        }
        case R.id.heap_button: {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            int largeMemoryClass = activityManager.getLargeMemoryClass();
            int memoryClass = activityManager.getMemoryClass();

            // if in Manifest.xml, you set application android:largeHeap = true,
            // then the memory is largeMemoryClass, otherwise is memoryClass
            Log.d(TAG, "txh largeMemoryClass = " + largeMemoryClass);
            Log.d(TAG, "txh memoryClass = " + memoryClass);
            
            byte[] b = new byte[100 * 1000 * 1000];
            mLeakyContainer.add(b);
            break;
        }
        default:
            break;
        }
        
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "txh onStart");
	}

	@Override
	protected void onRestart() {
		super.onStart();
		Log.v(TAG, "txh onRestart");
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

	private void createSystemDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		    .setTitle(R.string.hello_world)
		    .setMessage(R.string.message)
		    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (dialog != null) {
						dialog.dismiss();
					}
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.create();
		dialog.show();
	}
	
	@SuppressLint("NewApi")
	private void createCustomDialog() {
		View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
		final AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle).create();
		dialog.show();
		
		TextView message = (TextView) view.findViewById(R.id.message_id);
		message.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		Window window = dialog.getWindow();
		window.setContentView(R.layout.custom_dialog);
		window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		
		Button cancelBtn = (Button) view.findViewById(R.id.cancel_id);
		Button okBtn = (Button) view.findViewById(R.id.ok_id);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});

		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}

	// 序列化
	private void userSerialize() {
		Log.v(TAG, "txh userSerialize");
		User user = new User(0, "jake", true);
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		try {
			//File file = new File(filePath + "/cache.txt");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath + "/cache.txt"));
			out.writeObject(user);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 反序列化
	private void userDeSerialize() {
		Log.v(TAG, "txh userDeSerialize");
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath + "/cache.txt"));
			User user = (User) in.readObject();
			Log.v(TAG, "txh user = " + user.getUserId() + ", " + user.getUserName() + ", "
					+ user.getIsMale());
			in.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void customSerialize() {
		android.os.Parcel _data = android.os.Parcel.obtain();
		Custom custom = new Custom(3, "rose", false);
		custom.writeToParcel(_data, 0);
	}

	private void moveButton(Button button) {
	    MarginLayoutParams params = (MarginLayoutParams) button.getLayoutParams();
	    params.width += 100;
        params.leftMargin += 100;
        //button.setLayoutParams(params);
        button.requestLayout();
	}
	
	private void sendNotification() {
	    Notification notification = new Notification();
	    notification.icon = R.drawable.ic_launcher;
	    notification.tickerText = "hello world";
	    notification.when = System.currentTimeMillis();
	    notification.flags = Notification.FLAG_AUTO_CANCEL;
	    
	    Intent intent = new Intent(this, VerticalSeekbarActivity.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(
	            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.layout_notification);
	    remoteView.setTextViewText(R.id.msg, "chapter_5");
	    remoteView.setImageViewResource(R.id.icon, R.drawable.thumb);
	    PendingIntent openActivity2PendingIntent = PendingIntent.getActivity(
	            this, 0, new Intent(this, ProviderActivity.class),
	            PendingIntent.FLAG_UPDATE_CURRENT);
	    remoteView.setOnClickPendingIntent(R.id.button, openActivity2PendingIntent);
	    notification.contentView = remoteView;
	    notification.contentIntent = pendingIntent;
	    NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	    manager.notify(2, notification);
	}
}
