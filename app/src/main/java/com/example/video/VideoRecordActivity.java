package com.example.video;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.example.nicole.R;

public class VideoRecordActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "VideoRecordActivity";

    private ImageButton mBtnStartStop;
    private ImageButton mBtnSet;
    private ImageButton mBtnShowFile;
    
    private Chronometer mTimer;
    
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    
	private Camera mCamera;
    private Camera.Parameters mParameters;
    
    private MediaRecorder mMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindowFeature();
        setContentView(R.layout.video_activity_main);

        initView();
        initCameraAndSurfaceViewHolder();
        prepareMediaRecorder();
    }

    private void initCameraAndSurfaceViewHolder() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mCamera = Camera.open();
    }

    private void initListeners() {
        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "txh onClick");
                if (isRecording()) {
                    Log.d(TAG, "txh stop record");
                    stopRecording();
                    mTimer.stop();
                    mBtnStartStop.setBackgroundResource(R.drawable.rec_start);
                } else if (startRecording()) {
                    Log.d(TAG, "txh start record");
                    mTimer.setBase(SystemClock.elapsedRealtime());
                    mTimer.start();
                    mBtnStartStop.setBackgroundResource(R.drawable.rec_stop);
                }
            }
        });
        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "txh setting");
                Toast.makeText(VideoRecordActivity.this, "���ô�����...",Toast.LENGTH_SHORT).show();
            }
        });
        mBtnShowFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VideoRecordActivity.this, ShowVideoActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean prepareMediaRecorder() {
    	Log.v(TAG, "txh prepareMediaRecorder");
    	mCamera.unlock();

    	mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());

        String path = getSDPath();
        if (path != null) {

            File dir = new File(path + "/VideoRecorderTest");
            if (!dir.exists()) {
                dir.mkdir();
            }
            path = dir + "/" + getDate() + ".mp4";
            mMediaRecorder.setOutputFile(path);
            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                releaseMediaRecorder();
                e.printStackTrace();
            }
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }
    public boolean startRecording() {
        if (prepareMediaRecorder()) {
            mMediaRecorder.start();
            return true;
        } else {
            releaseMediaRecorder();
        }
        return false;
    }

    public void stopRecording() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
        }
        releaseMediaRecorder();
    }

    private void initView() {
        mSurfaceView  = (SurfaceView)findViewById(R.id.capture_surfaceview);
        mBtnStartStop = (ImageButton) findViewById(R.id.ib_stop);
        mBtnSet= (ImageButton) findViewById(R.id.capture_imagebutton_setting);
        mBtnShowFile= (ImageButton) findViewById(R.id.capture_imagebutton_showfiles);
        mTimer= (Chronometer) findViewById(R.id.crm_count_time);
    }

    public boolean isRecording() {
        return mMediaRecorder != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(R.layout.activity_main);

        //initView();
        //initCameraAndSurfaceViewHolder();
        initListeners();
    }

    private void initWindowFeature() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // ѡ��֧�ְ�͸��ģʽ,����SurfaceView��activity��ʹ�á�
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    /**
     * ��ȡϵͳʱ��
     */
    public static String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // ��ȡ���
        int month = ca.get(Calendar.MONTH);         // ��ȡ�·�
        int day = ca.get(Calendar.DATE);            // ��ȡ��
        int minute = ca.get(Calendar.MINUTE);       // ��
        int hour = ca.get(Calendar.HOUR);           // Сʱ
        int second = ca.get(Calendar.SECOND);       // ��

        String date = "" + year + (month + 1 )+ day + hour + minute + second;
        Log.d(TAG, "txh date:" + date);

        return date;
    }

    /**
     * ��ȡSD path
     */
    public String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// ��ȡ�ⲿ�洢�ĸ�Ŀ¼
            return sdDir.toString();
        }

        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Log.v(TAG, "txh surfaceCreated");
        mParameters = mCamera.getParameters();
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setParameters(mParameters);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    Log.d(TAG, "txh auto focus success");
                }
            }
        });
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

            //������������ܰ����ǻ�ȡ�����Ԥ��֡�����ǿ���������ʵʱ�ش���ÿһ֡
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Log.i(TAG, "txh get preview frame ��ȡԤ��֡...");
                    new ProcessFrameAsyncTask().execute(data);
                    Log.d(TAG, "txh preview frame size Ԥ��֡��С��" + String.valueOf(data.length));
                }
            });
        } catch (IOException e) {
            Log.d(TAG, "txh set camera preview failed �������Ԥ��ʧ��", e);
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.v(TAG, "txh surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.v(TAG, "txh surfaceDestroyed");
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    private class ProcessFrameAsyncTask extends AsyncTask<byte[],Void,String> {

        @Override
        protected String doInBackground(byte[]... params) {
            processFrame(params[0]);
            return null;
        }

        private void processFrame(byte[] frameData) {
            Log.i(TAG, "txh processFrame ���ڴ���Ԥ��֡...");
            Log.i(TAG, "txh frame size Ԥ��֡��С"+String.valueOf(frameData.length));
            Log.i(TAG, "txh Ԥ��֡�������...");
            //�������ע�͵��Ĵ����ǰ�Ԥ��֡���������sd���У���.yuv��ʽ����
//            String path = getSDPath();
//            File dir = new File(path + "/FrameTest");
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            path = dir + "/" + "testFrame"+".yuv";
//            File file =new File(path);
//            try {
//                FileOutputStream fileOutputStream=new FileOutputStream(file);
//                BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
//                bufferedOutputStream.write(frameData);
//                Log.i(TAG, "Ԥ��֡�������...");
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


    }
}
