package com.facwork.bonbin.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.facwork.bonbin.data.InformasiModel;

public class InformasiController {

	Context context;
	DBHelper dbHelper;
	String table = "informasi";
	Cursor c;

	/**
	 * Inisialisasi parameter yang ada di database disini contoh: String id =
	 * "id"; String idSync = "idSync";
	 */
	private String id = "id";
	private String info = "info";

	private SharedPreferences prefLocation;

	public InformasiController(Context ctx) {
		super();
		this.context = ctx;
		dbHelper = DBHelper.getDBAdapterInstance(ctx);
	}

	/*
	 * Untuk insert satu object
	 */
	public void insert(InformasiModel obj) {
		ContentValues values = new ContentValues();
		dbHelper.openDataBase();
		try {
			// Disini untuk meletakkan data sementara dari object
			// sebelum dimasukkan ke database
			ContentValues cv = new ContentValues();
			InformasiModel rm = obj;
			cv.put(info, rm.getinfo());
			dbHelper.insertRecordsInDB(table, cv);
			values.clear();

		} catch (SQLException e) {
			// TODO: handle exception
			throw e;
		}
		dbHelper.close();
	}

	/*
	 * Untuk menyimpan data sekaligus banyak
	 */
	public void insertAll(List<InformasiModel> objects) {

		try {
			dbHelper.openDataBase();
			for (int counter = 0; counter < objects.size(); counter++) {
				ContentValues cv = new ContentValues();
				InformasiModel rm = objects.get(counter);
				cv.put(info, rm.getinfo());
				dbHelper.insertRecordsInDB(table, cv);
				cv.clear();
			}
			dbHelper.close();

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List<InformasiModel> getAll() {
		List<InformasiModel> objects = new ArrayList<InformasiModel>();

		try {
			dbHelper.openDataBase();
			c = dbHelper.selectRecordsFromDB(table, null, null, null, null,
					null, null);
			while (c.moveToNext()) {
				// Disini masukkan object data kedalam list
				InformasiModel rm = new InformasiModel();
				rm.setId(c.getInt(c.getColumnIndex(id)));

				rm.setinfo(c.getString(c.getColumnIndex(info)));

				Log.i("coba", "Nama: " + rm.getinfo());
				objects.add(rm);
			}
			c.close();
			c = null;
			dbHelper.close();
		} catch (SecurityException e) {
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
