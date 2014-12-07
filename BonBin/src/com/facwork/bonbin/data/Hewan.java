package com.facwork.bonbin.data;

public class Hewan {

	String id, title, lng, lat, gambar, deskripsi;
	double jarak;

	public Hewan(String id, String lng, String lat, String title,
			String gambar, String deskripsi, double jarak) {
		this.id = id;
		this.lng = lng;
		this.lat = lat;
		this.title = title;
		this.gambar = gambar;
		this.deskripsi = deskripsi;
		this.jarak = jarak;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getGambar() {
		return gambar;
	}

	public void setgambar(String gambar) {
		this.gambar = gambar;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	@Override
	public String toString() {
		return this.deskripsi;
	}

	public double getJarak() {
		return jarak;
	}

	public void setJarak(double jarak) {
		this.jarak = jarak;
	}
}