package com.facwork.bonbin.data;

public class Informasi {

	String id, info;

	public Informasi(String id, String info) {
		this.id = id;
		this.info = info;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.info;
	}

	public String getinfo() {
		return info;
	}

	public void setinfo(String info) {
		this.info = info;
	}
}