package com.facwork.bonbin.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.facwork.bonbin.data.Hewan;
import com.facwork.bonbin.data.Informasi;
import com.facwork.bonbin.utils.Utils;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
	private static final String DB_NAME = "bonbin";
	private static final int DB_VER = 1;

	private static final String TB_DATA = "bonbin";
	private static final String TB_DATA_INFO = "informasi";
	public static final String FIELD_ROW_ID = "id";
	public static final String FIELD_LAT = "lat";
	public static final String FIELD_LNG = "lng";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_IMAGE = "image";
	public static final String FIELD_DESCRIPTION = "description";

	public static final String FIELD_ID_INFO = "id";
	public static final String FIELD_INFO = "info";

	private static DatabaseHelper dbInstance;
	private static SQLiteDatabase db;
	private SharedPreferences prefLocation;
	Context context;

	private DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VER);
		this.context = context;
	}

	public static DatabaseHelper getInstance(Context context) {
		if (dbInstance == null) {
			dbInstance = new DatabaseHelper(context);
			db = dbInstance.getWritableDatabase();
		}

		return dbInstance;
	}

	@Override
	public synchronized void close() {
		super.close();
		if (dbInstance != null) {
			dbInstance.close();
		}
	}

	public ArrayList<Hewan> getAllHewan() {
		ArrayList<Hewan> daftarHewan = new ArrayList<Hewan>();

		Cursor cursor = db.query(TB_DATA, new String[] { FIELD_ROW_ID,
				FIELD_LNG, FIELD_LAT, FIELD_TITLE, FIELD_IMAGE,
				FIELD_DESCRIPTION }, null, null, null, null, FIELD_TITLE);

		// Untuk mengambil lokasi user saat ini
		prefLocation = context.getSharedPreferences("mkul", 0);
		double latUser = Double.parseDouble(prefLocation.getString("userLat",
				"0"));
		double lonUser = Double.parseDouble(prefLocation.getString("userLon",
				"0"));

		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();

			do {

				double latP = Double.parseDouble(cursor.getString(cursor
						.getColumnIndex(FIELD_LAT)));
				double lonP = Double.parseDouble(cursor.getString(cursor
						.getColumnIndex(FIELD_LNG)));

				double jarak = Utils.getDistanceBetweenTwoLocation(latUser,
						lonUser, latP, lonP);
				jarak = Utils.RoundDecimal(jarak, 2);

				Hewan kamus = new Hewan(cursor.getString(cursor
						.getColumnIndex(FIELD_ROW_ID)), cursor.getString(cursor
						.getColumnIndex(FIELD_LNG)), cursor.getString(cursor
						.getColumnIndex(FIELD_LAT)), cursor.getString(cursor
						.getColumnIndex(FIELD_TITLE)), cursor.getString(cursor
						.getColumnIndex(FIELD_IMAGE)), cursor.getString(cursor
						.getColumnIndex(FIELD_DESCRIPTION)), jarak);

				daftarHewan.add(kamus);

			} while (cursor.moveToNext());
		}
		return daftarHewan;

	}

	public List<Informasi> getInformasi() {
		List<Informasi> listinfo = new ArrayList<Informasi>();

		Cursor cursor = db.query(TB_DATA_INFO, new String[] { FIELD_ID_INFO,
				FIELD_INFO }, null, null, null, null, FIELD_INFO);
		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();

			do {
				Informasi info = new Informasi(cursor.getString(cursor
						.getColumnIndex(FIELD_ROW_ID)), cursor.getString(cursor
						.getColumnIndex(FIELD_INFO)));
				listinfo.add(info);

			} while (cursor.moveToNext());
		}
		return listinfo;

	}
	//
	// public Hewan getHewan(String scan) {
	// // String scan = "3Singa";
	// // Hewan data = null;
	// Cursor cursor = db.query(TB_DATA, new String[] {
	// FIELD_ROW_ID, FIELD_LNG,
	// FIELD_LAT, FIELD_TITLE,
	// FIELD_IMAGE, FIELD_DESCRIPTION },
	// FIELD_TITLE + "=?", new String[] { scan }, null,
	// null, null, null);
	//
	// // Untuk mengambil lokasi user saat ini
	// prefLocation = context.getSharedPreferences("mkul", 0);
	// double latUser = Double.parseDouble(prefLocation.getString("userLat",
	// "0"));
	// double lonUser = Double.parseDouble(prefLocation.getString("userLon",
	// "0"));
	//
	// if (cursor != null)
	// cursor.moveToFirst();
	//
	// double latP = Double.parseDouble(cursor.getString(cursor
	// .getColumnIndex( FIELD_LAT)));
	// double lonP = Double.parseDouble(cursor.getString(cursor
	// .getColumnIndex( FIELD_LNG)));
	//
	// double jarak = Utils.getDistanceBetweenTwoLocation(latUser, lonUser,
	// latP, lonP);
	// jarak = Utils.RoundDecimal(jarak, 2);
	// Hewan data = new Hewan(
	// cursor.getString(cursor.getColumnIndex( FIELD_ROW_ID)),
	// cursor.getString(cursor.getColumnIndex( FIELD_LNG)),
	// cursor.getString(cursor.getColumnIndex( FIELD_LAT)),
	// cursor.getString(cursor.getColumnIndex( FIELD_TITLE)),
	// cursor.getString(cursor.getColumnIndex( FIELD_IMAGE)),
	// cursor.getString(cursor.getColumnIndex( FIELD_DESCRIPTION)), jarak);
	// // }
	//
	// return data;
	// }

}
