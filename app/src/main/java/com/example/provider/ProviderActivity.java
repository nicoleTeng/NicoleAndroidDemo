package com.example.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.aidl.Book;
import com.example.app.User;
import com.example.nicole.R;

public class ProviderActivity extends Activity {
	private static final String TAG = "ProviderActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "txh onCreate, thread = " + Thread.currentThread().getName());
		setContentView(R.layout.provider_layout);

		Uri bookUri = Uri.parse("content://com.example.provider.book.provider/book");
		
		Log.v(TAG, "txh new ContentValues");
		ContentValues values = new ContentValues();
		values.put("_id", 6);
		values.put("name", "Programming");
		getContentResolver().insert(bookUri, values);
		
		Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"},
				null, null, null);
		while (bookCursor.moveToNext()) {
			Book book = new Book();
			book.bookId = bookCursor.getInt(0);
			book.bookName = bookCursor.getString(1);
			Log.v(TAG, "txh query book: " + book.toString());
		}
		bookCursor.close();
		
		Uri userUri = Uri.parse("content://com.example.provider.book.provider/user");
		
		Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"},
				null, null, null);
		while (userCursor.moveToNext()) {
			User user = new User();
			user.mUserId = userCursor.getInt(0);
			user.mUserName = userCursor.getString(1);
			user.mIsMale = userCursor.getInt(2) == 1;
			Log.v(TAG, "txh query user: " + user.toString());
		}
		userCursor.close();
	}
}
