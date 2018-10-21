package com.example.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Custom implements Parcelable {
	public int mCustomId;
	public String mCustomName;
	public boolean mIsMale;
	//public Book mBook;
	
	public Custom(int id, String name, boolean isMale) {
		mCustomId = id;
		mCustomName = name;
		mIsMale = isMale;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mCustomId);
		out.writeString(mCustomName);
		out.writeInt(mIsMale ? 1 : 0);
		//out.writeParcelable(mBook, 0);
	}

	public static final Parcelable.Creator<Custom> CREATOR =
			new Parcelable.Creator<Custom>() {

				@Override
				public Custom createFromParcel(Parcel source) {
					return new Custom(source);
				}

				@Override
				public Custom[] newArray(int size) {
					return new Custom[size];
				}
		
			};
			
	private Custom(Parcel source) {
		mCustomId = source.readInt();
		mCustomName = source.readString();
		mIsMale = source.readInt() == 1;
		//mBook = source.readParcelable(Thread.currentThread().getContextClassLoader());
	}
}
