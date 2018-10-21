package com.example.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TCPServerService extends Service {
	private static final String TAG = "TCPserverService";

	private boolean mIsServiceDestoryed = false;
	private String[] mDefinedMessages = new String[] {
			"Hi,",
			"What's your name?",
			"The weather today is well",
			"Do you know? I can talk with different people at the same time",
			"Could you tell me a joke"
	};

	@Override
	public void onCreate() {
	    Log.v(TAG, "txh onCreate, thread = " + Thread.currentThread().getName());
		new Thread(new TcpServer()).start();
		
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		mIsServiceDestoryed = true;
		super.onDestroy();
	}
	
	private class TcpServer implements Runnable {

		@Override
		public void run() {
			Log.v(TAG, "txh run thread = " + Thread.currentThread().getName());
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(8688);
			} catch (IOException e) {
				System.out.println("establish tcp server failed, port:8688");
				e.printStackTrace();
				return;
			}
			
			while (!mIsServiceDestoryed) {
				try {
					final Socket client = serverSocket.accept();
					System.out.println("accept");
					new Thread() {
						@Override
						public void run() {
							try {
								responseClient(client);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void responseClient(Socket client) throws IOException {
		Log.v(TAG, "txh responseClient");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(client.getOutputStream())), true);
		out.println("welcome to chat room!");
		
		while (!mIsServiceDestoryed) {
			Log.v(TAG, "txh 0000000000000");
			String str = in.readLine();
			Log.v(TAG, "txh 11111111111");
			System.out.println("msg from client:" + str);
			if (str == null) {
				break;
			}
			int i = new Random().nextInt(mDefinedMessages.length);
			String msg = mDefinedMessages[i];
			out.println(msg);
			System.out.println("send :" + msg);
		}
		
		System.out.println("client quit.");
		out.close();
		in.close();
		client.close();
	}
}

