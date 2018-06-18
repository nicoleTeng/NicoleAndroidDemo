package com.example.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.nicole.R;
import com.example.util.MyConstants;

public class MessengerActivity extends Activity {
    private static final String TAG = "MessengerActivity";
    
    private Messenger mGetReplyMessenger = new Messenger(new MyHandler());
    
    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MyConstants.MSG_FROM_SERVICE: {
                String serviceMessage = msg.getData().getString("reply");
                Log.v(TAG, "txh getData from service: " + serviceMessage);
                break;
            }
            default:
                super.handleMessage(msg);
            }
        }
    }
    
    private ServiceConnection mConn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger(service);
            Message message = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello, this is client.");
            message.setData(bundle);
            message.replyTo = mGetReplyMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }
    
    @Override
    public void onDestroy() {
        unbindService(mConn);
        super.onDestroy();
    }
}
