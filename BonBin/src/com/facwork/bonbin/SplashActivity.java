package com.facwork.bonbin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facwork.bonbin.webservice.GetAllHewan;

public class SplashActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5500;
	SharedPreferences prefLocation;
	private LocationManager locationManager = null;
	@SuppressWarnings("unused")
	private LocationListener locationListener;
	// private Location location;
	private String bestProvider = null;
	private double userLon = 0;
	private double userLat = 0;

	private static final long JARAK_MINIMAL_UNTUK_UPDATE = 1; // dalam meter
	private static final long WAKTU_MINIMUM_UNTUK_UPDATE = 1000; // dalam detik

	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mContext = this;

		initLocationManager();
		TextView textsplash = (TextView) findViewById(R.id.textSplash);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Lato-Italic.ttf");
		textsplash.setTypeface(face);

		StartAnimations();// Menjalankan Method Start Animasi
		Thread splashThread = new Thread() {
			// Timer Splash
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					new AsyncGetDataFromWeb().execute();

					finish();
					Intent newIntent = new Intent(SplashActivity.this,
							SearchActivity.class);
					startActivityForResult(newIntent, 0);

				}
			}
		};
		splashThread.start();
	}

	// Disini Deklarasi Animasinya(StartAnimation)
	private void StartAnimations() {
		// TODO Auto-generated method stub
		// Animasi untuk Frame Layout mengunakan alpha.xml
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();
		RelativeLayout l = (RelativeLayout) findViewById(R.id.SplashLayout);
		l.clearAnimation();
		l.startAnimation(anim);

		// Animasi untuk ProgressBar1 mengunakan alpha.xml
		Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim1.reset();
		ProgressBar l1 = (ProgressBar) findViewById(R.id.progressBar1);
		l1.clearAnimation();
		l1.startAnimation(anim);

		// Animasi untuk Gambar mengunakan Translate.xml
		anim = AnimationUtils.loadAnimation(this, R.anim.translete);
		anim.reset();
		ImageView iv = (ImageView) findViewById(R.id.splashimg);
		TextView textsplash = (TextView) findViewById(R.id.textSplash);

		iv.clearAnimation();
		textsplash.clearAnimation();
		iv.startAnimation(anim);
		textsplash.startAnimation(anim);
	}

	/*
	 * Untuk inisialisasi locationmanager
	 */
	private void initLocationManager() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// List<String> listProviders = locationManager.getAllProviders();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		bestProvider = locationManager.getBestProvider(criteria, true);
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		// location = locationManager.getLastKnownLocation(bestProvider);
		if (!provider.contains("gps")) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
			final String message = "GPS anda tidak aktif."
					+ " Klik OK untuk mengaktifkan.";

			builder.setMessage(message)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface d, int id) {
									startActivity(new Intent(action));
									d.dismiss();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface d, int id) {
									d.cancel();
								}
							});
			builder.create().show();

		} else {
			if (bestProvider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
				// locationManager.requestLocationUpdates(bestProvider, 0, 1000,
				// locationListener);
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER,
						WAKTU_MINIMUM_UNTUK_UPDATE, JARAK_MINIMAL_UNTUK_UPDATE,
						new MyLocationListener());
			} else {
				Location loc = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				setUserLocation(loc);
			}
		}

	}

	public void setUserLocation(Location location) {
		userLat = location.getLatitude();
		userLon = location.getLongitude();
		prefLocation = getSharedPreferences("mkul", 0);
		String lat = Double.toString(userLat);
		String lon = Double.toString(userLon);

		Log.i("coba", "Latitude: " + lat);
		Log.i("coba", "Longitude: " + lon);
		Editor ed = prefLocation.edit();
		ed.putString("userLat", lat);
		ed.putString("userLon", lon);
		ed.commit();

	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			// String message = String.format(
			// "Lokasi berubah \n Lon: %1$s \n Lat: %2$s",
			// location.getLongitude(), location.getLatitude());
			// Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
			setUserLocation(location);
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			// Toast.makeText(mContext, "Status provider berubah",
			// Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			// Toast.makeText(mContext,
			// "Provider disabled oleh user. GPS offline",
			// Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			// Toast.makeText(mContext,
			// "Provider enabled oleh the user. GPS online",
			// Toast.LENGTH_LONG).show();
		}

	}

	class AsyncGetDataFromWeb extends AsyncTask<Void, Integer, Void> {

		protected void onPreExecute() {
			Log.i("coba", "async start");
		}

		@Override
		protected void onPostExecute(Void result) {
//			Toast.makeText(mContext,
//					"Provider enabled oleh the user. GPS online",
//					Toast.LENGTH_LONG).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			GetAllHewan gar = new GetAllHewan(mContext);
			gar.getAllPoi();
			gar.getInformasi();

			return null;
		}

	}
}
