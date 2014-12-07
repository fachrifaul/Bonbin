package com.facwork.bonbin.data;

import android.os.Parcel;
import android.os.Parcelable;

public class HewanModel implements Parcelable {

	private int id;
	private String name;
	private String lat;
	private String lon;
	private String imgUrl;

	private double jarak;
	private String desc;

	public HewanModel() {
		super();
	}

	public HewanModel(Parcel in) {
		// TODO Auto-generated constructor stub
		id = in.readInt();
		name = in.readString();
		lat = in.readString();
		lon = in.readString();
		imgUrl = in.readString();

		jarak = in.readDouble();
		desc = in.readString();
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
		dest.writeString(name);
		dest.writeString(lat);
		dest.writeString(lon);
		dest.writeString(imgUrl);

		dest.writeDouble(jarak);
		dest.writeString(desc);
	}

	public static final Parcelable.Creator<HewanModel> CREATOR = new Parcelable.Creator<HewanModel>() {

		@Override
		public HewanModel createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new HewanModel(in);
		}

		@Override
		public HewanModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new HewanModel[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public double getJarak() {
		return jarak;
	}

	public void setJarak(double jarak) {
		this.jarak = jarak;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
