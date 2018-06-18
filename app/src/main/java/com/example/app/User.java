package com.example.app;

import java.io.Serializable;

import android.util.Log;

public class User implements Serializable {
	private static final String TAG = "User";
	private static final long serialVersionUID = 1L;
	
	public int mUserId;
	public String mUserName;
	public boolean mIsMale;
	
	public User() {
		
	}

	public User(int userId, String userName, boolean isMale) {
		mUserId = userId;
		mUserName = userName;
		mIsMale = isMale;
	}
	
	public int getUserId() {
		return mUserId;
	}

	public String getUserName() {
		return mUserName;
	}
	
	public boolean getIsMale() {
		return mIsMale;
	}
	
	public String toString() {
		return "user: id = " + mUserId + ", name = " + mUserName + ", isMale = " + mIsMale;
	}
}
