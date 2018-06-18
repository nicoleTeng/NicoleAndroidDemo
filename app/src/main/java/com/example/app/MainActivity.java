package com.example.app;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.aidl.Book;
import com.example.aidl.BookManagerActivity;
import com.example.aidl.MessengerActivity;
import com.example.bitmap.AsyncTaskActivity;
import com.example.bitmap.DoubleExposureActivity;
import com.example.bitmap.XfermodeActivity;
import com.example.draw.DrawActivity;
import com.example.imageloader.GalleryActivity;
import com.example.leetCode.LeetCodeActivity;
import com.example.network.JsonActivity;
import com.example.network.TCPClientActivity;
import com.example.provider.ProviderActivity;
import com.example.service.ServiceActivity;
import com.example.thread.HandlerActivity;
import com.example.thread.ThreadActivity;
import com.example.util.StringUtils;
import com.example.util.Utils;
import com.example.video.VideoPlayActivity;

import com.example.nicole.R;

public class MainActivity extends ListActivity {
    private static final String TAG = "MainActivity";

    static AtomicBoolean mNeedWait = new AtomicBoolean(false);
    private PowerConnectionReceiver mReceiver = null;
    private int mOuterCount = 0;

    static final String[] listItems = {
        "Action Send intent",
        "Dialog",
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
        "MessengerActivity"
    };
    
    static final Class<?>[] activities = {
        GalleryActivity.class,
        DialogActivity.class,
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
        MessengerActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "txh onCreate");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.layout_main);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
        Class a = Human.class;
        Class b = null;
        try {
            b = Class.forName("com.example.aidl.Book");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        //Log.v(TAG, "txh a.getName() = " + a.getName() + ", b.getName() = " + b.getName()
        //        + ", " + b.getFields() + ", " + b.getMethods());
        try {
            Book book = (Book) b.newInstance();
            //Log.v(TAG, "txh book = " + book.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        String msg = "test";
        Boolean isNullString = TextUtils.isEmpty(msg);
        Log.v(TAG, "txh isNullString = " + isNullString + ", msg = " + msg);
        
        Log.v(TAG, "txh before mNeedWait = " + mNeedWait.get());
        //mNeedWait.compareAndSet(false, true);
        mNeedWait.set(true);
        Log.v(TAG, "txh after mNeedWait = " + mNeedWait.get());
        //Log.v(TAG, "txh appversion = " + Utils.getAppVersion(this));
        
        testArrayToList();
        Utils.getTargetSdkVersion(this);
        
        //notHasPermission(this);
        Utils.showDisplayParameter(this);
        
        String[] wordA = {"Hi", "Nice", "to", "meet", "you!"};
        String[] wordB = {"nice", "to", "meet", "you", "too!"};
        String value1 = StringUtils.mergeString(wordA, wordB);
        String value2 = StringUtils.mergeStringWithStringBuilder(wordA, wordB);

        /*mReceiver = new PowerConnectionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mReceiver, filter);*/
        
        //createNoMedia();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "txh onDestroy");
        //unregisterReceiver(mReceiver);
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
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");     
        intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");      
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @TargetApi(23)
    private boolean notHasPermission(Context context) {
        Log.v(TAG, "txh notHasPermission, Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(null, null, 0);
                Log.v(TAG, "txh hot hase permission, true");
                return true;
            }
        }
        Log.v(TAG, "txh hot hase permission, false");
        return false;
    }
    
    class Human {
        public Human() {
            mOuterCount = 4;
        }
        
        private int age;
        
        public int getAge() {
            return age;
        }
        
        public String toString() {
            return "Human age: " + age ;
        }
    }
    
    void testArrayToList() {
        int[] a_int = { 1, 2, 3, 4 };  
        /* 预期输出应该是1,2,3,4,但实际上输出的仅仅是一个引用, 这里它把a_int当成了一个元素 */  
        List a_int_List = Arrays.asList(a_int);  
        foreach(a_int_List);  
        /* 为此我们需要这样遍历其中元素 */  
        foreachForBase(a_int_List);  
  
        /* 段落二:对象类型的数组使用asList,是我们预期的 */  
        Integer[] a_Integer = new Integer[] { 1, 2, 3, 4 };  
        List a_Integer_List = Arrays.asList(a_Integer);  
        foreach(a_Integer_List);  
  
        /* 段落三:当更新数组或者asList之后的List,另一个将自动获得更新 */  
        a_Integer_List.set(0, 0);  
        foreach(a_Integer_List);  
        foreach(a_Integer);  
  
        a_Integer[0] = 5;  
        foreach(a_Integer_List);  
        foreach(a_Integer);  
  
        /* 段落四:对基本类型数组,通过asList之后的List修改对应的值后,在运行时会报出异常  
         * 但是基本类型数组对应的List是会发生变化的,这是毫无疑问的 
         */  
          
        /* 
         * a_int_List.set(0, 0);  
         * foreach(a_int_List); foreach(a_int); 
         */  
  
        a_int[0] = 5;  
        foreachForBase(a_int_List);  
        foreach(a_int);  
  
    }  
  
    /* 打印方法 */  
    private static void foreach(List list) {  
        for (Object object : list) {  
            System.out.print(object + " ");  
        }  
        System.out.println();  
  
    }  
  
    private static void foreachForBase(List a_int_List) {  
        int[] _a_int = (int[]) a_int_List.get(0);  
        foreach(_a_int);  
    }  
  
    private static void foreach(int[] a_int) {  
        for (int i : a_int) {  
            System.out.print(i + " ");  
        }  
        System.out.println();  
    }  
  
    private static void foreach(Integer[] _a_Integer) {  
        for (int i : _a_Integer) {  
            System.out.print(i + " ");  
        }  
        System.out.println();  
    }
    
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        
    }
    
    private void createNoMedia() {
        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/Pictures/Screenshots";
        File nomedia = new File(path, "/.nomedia");
        Log.v(TAG, "txh createNoMedia, path = " + path);
        if (!nomedia.exists()) { 
            try {  
                nomedia.createNewFile();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
        }
    }
}
