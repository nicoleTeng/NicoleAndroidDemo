package com.example.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.util.MyConstants;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    
    private final Messenger mMessenger = new Messenger(new MessengerHander());
    
    private static class MessengerHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT: {
                    //get data from Client
                    String clientMessage = msg.getData().getString("msg");
                    Log.v(TAG, "txh get msg from client: " + clientMessage);
                    
                    // reply data to Client
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                    Bundle data = new Bundle();
                    data.putString("reply", "get your message, call you later.");
                    replyMessage.setData(data);
                    
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mMessenger.getBinder();
    }

}
