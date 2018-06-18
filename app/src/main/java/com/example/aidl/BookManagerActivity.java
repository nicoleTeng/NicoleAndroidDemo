package com.example.aidl;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nicole.R;

public class BookManagerActivity extends Activity {
    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private TextView mTextView = null;
    private TextView mNewTextView = null;
    private IBookManager mRemoteBookManager = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_NEW_BOOK_ARRIVED:
                mNewTextView.setText(msg.obj.toString());
                break;
            default:
                break;
            }
        }
    };
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "txh onServiceConnected, thread = " + Thread.currentThread().getName());
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                List<Book> list = bookManager.getBookList();
                Log.v(TAG, "txh list.type = " + list.getClass().getCanonicalName());
                Book newBook = new Book(3, "Java");
                bookManager.addBook(newBook);
                
                list = bookManager.getBookList();
                mTextView.setText(list.toString());
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            
        }
        
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "txh onCreate, thread = " + Thread.currentThread().getName());
        setContentView(R.layout.activity_book_manager);
        mTextView = (TextView) findViewById(R.id.textView_id);
        mNewTextView = (TextView) findViewById(R.id.new_textView_id);
        Button button = (Button) findViewById(R.id.get_booklist_id);
        button.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mRemoteBookManager != null
                        && mRemoteBookManager.asBinder().isBinderAlive()) {
                    try {
                        mRemoteBookManager.getBookList();
                        mTextView.setText(mRemoteBookManager.getBookList().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        });
        
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null &&
                mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
    
    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new
            IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.v(TAG, "txh onNewBookArrived, thread = " + Thread.currentThread().getName());
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };
}
