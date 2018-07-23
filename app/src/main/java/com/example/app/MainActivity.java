package com.example.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.activity.SecondActivity;
import com.example.aidl.BookManagerActivity;
import com.example.aidl.MessengerActivity;
import com.example.animation.AnimationActivity;
import com.example.bitmap.AsyncTaskActivity;
import com.example.bitmap.DoubleExposureActivity;
import com.example.bitmap.XfermodeActivity;
import com.example.draw.DrawActivity;
import com.example.imageloader.GalleryActivity;
import com.example.leetCode.LeetCodeActivity;
import com.example.network.JsonActivity;
import com.example.network.TCPClientActivity;
import com.example.nicole.R;
import com.example.provider.ProviderActivity;
import com.example.recyclerview.RecyclerActivity;
import com.example.service.ServiceActivity;
import com.example.thread.HandlerActivity;
import com.example.thread.ThreadActivity;
import com.example.util.StringUtils;
import com.example.util.Utils;
import com.example.video.VideoPlayActivity;

import java.util.HashMap;

public class MainActivity extends ListActivity {
    private static final String TAG = "MainActivity";
    private PowerConnectionReceiver mReceiver = null;

    static final String[] listItems = {
        "Action Send intent",
        "Dialog",
        "Second activity",
        "Layout",
        "Layout Weight",
        "Vertical seekbar",
        "Content provider",
        "Tcp client",
        "Book manager",
        "Thread",
        "Handler",
        "HandlerThread",
        "Cache bitmap",
        "Gallery",
        "Double exposure",
        "Draw",
        "Xfermode",
        "SurfaceView",
        "Canvas",
        "Matrix",
        "Exception",
        "Json",
        "Video Player",
        //"Video Recorder",
        "Start Camera",
        "Service Demo",
        "LeeCode",
        "MessengerActivity",
        "RecyclerActivity",
        "Custom View",
        "Animation"
    };
    
    static final Class<?>[] activities = {
        GalleryActivity.class,
        DialogActivity.class,
        SecondActivity.class,
        LayoutActivity.class,
        LayoutWeightActivity.class,
        VerticalSeekbarActivity.class,
        ProviderActivity.class,
        TCPClientActivity.class,
        BookManagerActivity.class,
        ThreadActivity.class,
        HandlerActivity.class,
        HandlerThreadActivity.class,
        AsyncTaskActivity.class,
        GalleryActivity.class,
        DoubleExposureActivity.class,
        DrawActivity.class,
        XfermodeActivity.class,
        SurfaceViewActivity.class,
        CanvasActivity.class,
        MatrixActivity.class,
        ExceptionActivity.class,
        JsonActivity.class,
        VideoPlayActivity.class,
        //VideoRecordActivity.class,
        CameraActivity.class,
        ServiceActivity.class,
        LeetCodeActivity.class,
        MessengerActivity.class,
        RecyclerActivity.class,
        CustomViewActivity.class,
        AnimationActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.layout_main);

        Utils.getTargetSdkVersion(this);
        Utils.showDisplayParameter(this);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems));

        testStringMerge();

        //registerPowerReceiver();
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        if (position == 0) {
            startShareActivity();
        } else if (activities[position].getName().equals("GalleryActivity")) {
            startGalleryActivity();
        } else {
            Intent intent = new Intent(this, activities[position]);
            startActivity(intent);
        }
    }
    
    private void startGalleryActivity() {
        boolean isWifi = Utils.isWifi(this);
        if (!isWifi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notices");
            builder.setMessage("first use, should download from website," +
            		" the total size is about 5MB, sure to download?");
            builder.setPositiveButton("Yes", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        } else {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        }
    }
    
    private void startShareActivity() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "share");
        intent.putExtra(Intent.EXTRA_TEXT, "share from nicole!!!");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        //unregisterReceiver(mReceiver);
    }

    private void testStringMerge() {
        String[] wordA = {"Hi", "Nice", "to", "meet", "you!"};
        String[] wordB = {"nice", "to", "meet", "you", "too!"};
        String value1 = StringUtils.mergeString(wordA, wordB);
        String value2 = StringUtils.mergeStringWithStringBuilder(wordA, wordB);
        HashMap map = new HashMap<Integer, String>();

        Debug.startMethodTracing("gallery3d"); //开始，文件保存到 "/sdcard/gallery3d.trace"
        // ...
        Debug.stopMethodTracing();    //结束

    }

    private void registerPowerReceiver() {
        mReceiver = new PowerConnectionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mReceiver, filter);
    }

}
