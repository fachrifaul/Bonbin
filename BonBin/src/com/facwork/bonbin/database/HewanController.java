package com.facwork.bonbin.database;

import java.util.ArrayList;
import java.util.List;

import com.facwork.bonbin.data.HewanModel;
import com.facwork.bonbin.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;



public class HewanController {

	Context context;
	DBHelper dbHelper;
	String table = "bonbin";
	Cursor c;
	
	/**
	 * Inisialisasi parameter yang ada di database disini
	 * contoh:
	 * String id = "id";
	 * String idSync = "idSync";
	 */
	private String id = "id";
	private String name = "title";
	private String lat = "lat";
	private String lon = "lng";
	private String imgUrl = "image";
	private String desc = "description";
	
	private SharedPreferences prefLocation;
	public HewanController(Context ctx){
		super();
		this.context = ctx;
		dbHelper = DBHelper.getDBAdapterInstance(ctx);
	}
	
	/*
	 * Untuk insert satu object
	 */
	public void insert(HewanModel obj){
		ContentValues values = new ContentValues();
		dbHelper.openDataBase();
		try{
			//Disini untuk meletakkan data sementara dari object
			//sebelum dimasukkan ke database
			ContentValues cv = new ContentValues();
			HewanModel rm = obj;
			cv.put(name, rm.getName());

			cv.put(imgUrl, rm.getImgUrl());
			cv.put(lat, rm.getLat());
			cv.put(lon, rm.getLon());

			cv.put(desc, rm.getDesc());
			dbHelper.insertRecordsInDB(table, cv);
			values.clear();
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			throw e;
		}
		dbHelper.close();
	}
	
	/*
	 * Untuk menyimpan data sekaligus banyak
	 */
	public void insertAll(List<HewanModel> objects){
		
		try{
			dbHelper.openDataBase();
			for (int counter = 0; counter < objects.size(); counter++) {
				ContentValues cv = new ContentValues();
				HewanModel rm = objects.get(counter);
				cv.put(name, rm.getName());

				cv.put(imgUrl, rm.getImgUrl());
				cv.put(lat, rm.getLat());
				cv.put(lon, rm.getLon());

				cv.put(desc, rm.getDesc());
				dbHelper.insertRecordsInDB(table, cv);
				cv.clear();
			}
			dbHelper.close();
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public List<HewanModel> getAll(){
		List<HewanModel> objects = new ArrayList<HewanModel>();
		
		//Untuk mengambil lokasi user saat ini
		prefLocation = context.getSharedPreferences("mkul", 0);
		double latUser = Double.parseDouble(prefLocation.getString("userLat", "0"));
		double lonUser = Double.parseDouble(prefLocation.getString("userLon", "0"));
		
		try{
			dbHelper.openDataBase();
			c = dbHelper.selectRecordsFromDB(table, null, null, null, null, null, null);
			while (c.moveToNext()){
				//Disini masukkan object data kedalam list
				HewanModel rm = new HewanModel();
				rm.setId(c.getInt(c.getColumnIndex(id)));

				rm.setImgUrl(c.getString(c.getColumnIndex(imgUrl)));
				rm.setLat(c.getString(c.getColumnIndex(lat)));
				rm.setLon(c.getString(c.getColumnIndex(lon)));
				rm.setName(c.getString(c.getColumnIndex(name)));

				rm.setDesc(c.getString(c.getColumnIndex(desc)));
				
				Log.i("coba", "Nama: "+rm.getName());
				double latP = Double.parseDouble(rm.getLat());
				double lonP = Double.parseDouble(rm.getLon());
				double jarak = Utils.getDistanceBetweenTwoLocation(
						latUser, lonUser, latP, lonP);
				jarak = Utils.RoundDecimal(jarak, 2);
				rm.setJarak(jarak);
				objects.add(rm);
			}
			c.close();
			c = null;
			dbHelper.close();
		}catch (SecurityException e) {
			e.printStackTrace();// TODO: handle exception
		}
		return objects;
	}
	
	public void removeall() {
		dbHelper.openDataBase();
		dbHelper.deleteRecordInDB(table, null, null);
		dbHelper.close();
	}
}
