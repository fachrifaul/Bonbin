package com.facwork.bonbin.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsDB extends SQLiteOpenHelper {

	/** Database name */
	private static String DBNAME = "bonbin";
	private static String DB_PATH = "/data/data/com.facwork.bonbin/databases/";

	/** Version number of the database */
	private static int VERSION = 1;
	// private static int DB_VERSION = 2;

	/** Field 1 of the table locations, which is the primary key */
	public static final String FIELD_ROW_ID = "id";

	/** Field 2 of the table locations, stores the latitude */
	public static final String FIELD_LAT = "lat";

	/** Field 3 of the table locations, stores the longitude */
	public static final String FIELD_LNG = "lng";

	/** Field 4 of the table locations, stores the name location */
	public static final String FIELD_TITLE = "title";

	/** Field 8 of the table locations, stores the latitude */
	public static final String FIELD_IMAGE = "image";

	/** Field9 of the table locations, stores the latitude */
	public static final String FIELD_DESCRIPTION = "description";

	/** A constant, stores the the table name */
	private static final String DATABASE_TABLE = "bonbin";

	/** An instance variable for SQLiteDatabase */
	private SQLiteDatabase mDB;

	private Context myContext;
	public static LocationsDB dh = null;

	/** Constructor */
	// public LocationsDB(Context context) {
	// super(context, DBNAME, null, VERSION);
	// this.mDB = getWritableDatabase();
	// this.Context = context;
	// }

	public static LocationsDB getInstance(Context context) {
		if (dh == null) {
			dh = new LocationsDB(context.getApplicationContext());
		} else {
			if (!dh.mDB.isOpen()) {
				dh = new LocationsDB(context);
			}
		}
		return dh;
	}

	public LocationsDB(Context context) {
		super(context, DBNAME, null, VERSION);
		this.myContext = context;
		try {
			createDataBase();
			openDataBase();
		} catch (IOException e) {
		}

	}

	/**
	 * This is a callback method, invoked when the method getReadableDatabase()
	 * / getWritableDatabase() is called provided the database does not exists
	 * */
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// String sql = "create table " + DATABASE_TABLE + " ( " + FIELD_ROW_ID
	// + " integer primary key autoincrement , " + FIELD_LNG
	// + " double , " + FIELD_LAT + " double , " + FIELD_NAME
	// + " text " + " ) ";
	//
	// db.execSQL(sql);
	// }

	/** Inserts a new location to the table locations */
	public long insert(ContentValues contentValues) {
		long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
		return rowID;

	}

	/** Deletes all locations from the table */
	public int del() {
		int cnt = mDB.delete(DATABASE_TABLE, null, null);
		return cnt;
	}

	/** Count Data */
	public long alldata() {
		SQLiteDatabase db = getReadableDatabase();
		long cnt = DatabaseUtils.queryNumEntries(db, DATABASE_TABLE);
		return cnt;
	}

	/** Returns all the locations from the table */
	public Cursor getAllLocations() {
		return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,
				FIELD_LAT, FIELD_LNG, FIELD_TITLE, FIELD_IMAGE,
				FIELD_DESCRIPTION }, null, null, null, null, FIELD_TITLE);
	}

	public SQLiteDatabase getDB() {
		return mDB;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	private void createDataBase() throws IOException {

		try {
			this.getReadableDatabase();
		} catch (Exception e) {
		}

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

//		SQLiteDatabase db = getReadableDatabase();
//		long cnt = DatabaseUtils.queryNumEntries(db, DATABASE_TABLE);
//		System.out.println("jumlah " + cnt);

		try {
			String myPath = DB_PATH + DBNAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
			// database does't exist yet.
		}

		boolean res = false;
		if (checkDB != null) {
			try {
				Cursor c = checkDB.rawQuery("SELECT * FROM bonbin", null);
				c.close();
				res = true;
			} catch (Exception e) {

			}
			checkDB.close();
			return res;

		} else {
			return false;
		}

		// return checkDB != null ? true : false;

	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DBNAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DBNAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DBNAME;
		mDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		try {
			mDB = getWritableDatabase();
		} catch (Exception e) {
		}

	}

	@Override
	public synchronized void close() {

		if (mDB != null)
			mDB.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// int i = 0;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// DELETE DATABASE
		// String fileName = DB_PATH + DBNAME;
		// File oldFile = new File(fileName);
		// boolean resDelete = oldFile.delete();

		try {
			createDataBase();
			openDataBase();
		} catch (IOException e) {
		}
	}

}
