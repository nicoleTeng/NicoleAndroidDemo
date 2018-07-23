package com.example.aidl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

public class BookManagerService extends Service {
	private static final String TAG = "BookManagerService";
	private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
	
	private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
	private RemoteCallbackList<IOnNewBookArrivedListener> mListenerLists
	        = new RemoteCallbackList<IOnNewBookArrivedListener>();
	
	private Binder mBinder = new IBookManager.Stub() {
        
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.v(TAG, "txh getBookList, thread = " + Thread.currentThread().getName());
            //SystemClock.sleep(5000); 
            return mBookList;
        }
        
        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            Log.v(TAG, "txh addBook, thread = " + Thread.currentThread().getName());
        }

        @SuppressLint("NewApi")
		@Override
        public void registerListener(IOnNewBookArrivedListener listener)
                throws RemoteException {
            Log.v(TAG, "txh registerListener, thread = " + Thread.currentThread().getName());
            mListenerLists.register(listener);
            Log.v(TAG, "txh listener.size() = " + mListenerLists.getRegisteredCallbackCount());
        }

        @SuppressLint("NewApi")
		@Override
        public void unregisterListener(IOnNewBookArrivedListener listener)
                throws RemoteException {
            Log.v(TAG, "txh unregisterListener, thread = " + Thread.currentThread().getName());
            mListenerLists.unregister(listener);
            Log.v(TAG, "txh listener.size() = " + mListenerLists.getRegisteredCallbackCount());
        }
    };
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "txh onCreate");
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        
        new Thread(new ServiceWorker()).start();
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// ��֤�ͻ������
	    int check = checkCallingOrSelfPermission("com.example.aidl.permission.ACCESS_BOOK_SERVICE");
	    Log.v(TAG, "txh onBind, check = " + check);
	    if (check == PackageManager.PERMISSION_DENIED) {
	        return null;
	    }
		return mBinder;
	}
	
	@Override
	public void onDestroy() {
	    mIsServiceDestoryed.set(true);
	    super.onDestroy();
	}

	private void onNewBookArrived(Book book) throws RemoteException {
	    mBookList.add(book);
	    final int N = mListenerLists.beginBroadcast();
	    
	    for (int i = 0; i < N; i++) {
	        IOnNewBookArrivedListener listener = mListenerLists.getBroadcastItem(i);
	        if (listener != null) {
	            try {
	                listener.onNewBookArrived(book);
	            } catch (RemoteException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    mListenerLists.finishBroadcast();
	}
	
	private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book book = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            
        }
	}
}
