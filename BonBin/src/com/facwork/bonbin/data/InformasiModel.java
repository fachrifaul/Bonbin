package com.facwork.bonbin.data;

import android.os.Parcel;
import android.os.Parcelable;

public class InformasiModel implements Parcelable {

	private int id;
	private String info;

	public InformasiModel() {
		super();
	}

	public InformasiModel(Parcel in) {
		// TODO Auto-generated constructor stub
		id = in.readInt();
		info = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(info);
	}

	public static final Parcelable.Creator<InformasiModel> CREATOR = new Parcelable.Creator<InformasiModel>() {

		@Override
		public InformasiModel createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new InformasiModel(in);
		}

		@Override
		public InformasiModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InformasiModel[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getinfo() {
		return info;
	}

	public void setinfo(String info) {
		this.info = info;
	}

}
