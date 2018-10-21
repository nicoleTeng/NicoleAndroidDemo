package com.example.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nicole.R;

public class TCPClientActivity extends Activity implements OnClickListener{
	private static final String TAG = "TCPClientActivity";
	
	private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
	private static final int MESSAGE_SOCKET_CONNECTED = 2;
	
	private Button mSendButton;
	private TextView mMessageTextView;
	private EditText mMessageEditText;
	
	private PrintWriter mPrintWriter;
	private Socket mClientSocket;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_RECEIVE_NEW_MSG: {
				mMessageTextView.setText(mMessageTextView.getText()
						+ (String) msg.obj);
				break;
			}
			case MESSAGE_SOCKET_CONNECTED: {
				mSendButton.setEnabled(true);
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
		setContentView(R.layout.activity_tcpclient);
		
		mMessageTextView = (TextView) findViewById(R.id.textView_id);
		mMessageEditText = (EditText) findViewById(R.id.edittext_id);
		mSendButton = (Button) findViewById(R.id.button_id);
		mSendButton.setOnClickListener(this);
		
		Intent service = new Intent(this, TCPServerService.class);
		startService(service);
		Log.v(TAG, "txh startService, thread = " + Thread.currentThread().getName());
		
		new Thread() {
			@Override
			public void run() {
				connectTCPserver();
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		if (mClientSocket != null) {
			try {
				mClientSocket.shutdownInput();
				mClientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	private void connectTCPserver() {
		Log.v(TAG, "txh connectTCPserver");
		Socket socket = null;
		while (socket == null) {
			try {
				socket = new Socket("localhost", 8688);
				mClientSocket = socket;
				mPrintWriter = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
				System.out.println("connect server success.");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				SystemClock.sleep(1000);
				e.printStackTrace();
				System.out.println("connect tcp server failed, retry...");
			}
		}
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (!TCPClientActivity.this.isFinishing()) {
				String msg = br.readLine();
				System.out.println("receive :" + msg);
				if (msg != null) {
					String time = formatDateTime(System.currentTimeMillis());
					final String showedMsg = "server " + time + ":" + msg + "\n";
					mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
				}
			}
			System.out.println("quit...");
			mPrintWriter.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

	private String formatDateTime(long time) {
		return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_id: {
				final String msg = mMessageEditText.getText().toString();
				if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
				    new Thread() {
				        @Override
				        public void run() {
				            mPrintWriter.println(msg);
				        }
				    }.start();
				    
					mMessageEditText.setText("");
					String time = formatDateTime(System.currentTimeMillis());
					final String showedMsg = "self " + time + ":" + msg + "\n";
					mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
				}
				break;
			}
			default: {
				break;
			}
		}
		return;
	}
}
