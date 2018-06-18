package com.example.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
	public int bookId;
	public String bookName;

    public Book() {
		
	}
	
	public Book(int id, String name) {
		bookId = id;
		bookName = name;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(bookId);
		out.writeString(bookName);
	}

	public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

		@Override
		public Book createFromParcel(Parcel in) {
			return new Book(in);
		}

		@Override
		public Book[] newArray(int size) {
			return new Book[size];
		}
		
	};
	
	public Book(Parcel in) {
		bookId = in.readInt();
		bookName = in.readString();
	}

	
	public String toString() {
		return "user: id = " + bookId + ", name = " + bookName;
	}
}
