package com.facwork.bonbin;

import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.facwork.bonbin.data.Hewan;
import com.facwork.bonbin.database.Constants.Extra;
import com.facwork.bonbin.database.LocationsContentProvider;
import com.facwork.bonbin.database.LocationsDB;
import com.facwork.bonbin.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PetaActivity extends ActionBarActivity implements
		LoaderCallbacks<Cursor> {
	ArrayList<Hewan> daftarHewan = new ArrayList<Hewan>();
	GoogleMap googleMap;
	Context context;
	String id, title, lng, lat, gambar, deskripsi;
	double jaraks;

	MapView mapView;
	IMapController mapViewController;

	LocationManager locationManager;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	public static final GeoPoint POS = new GeoPoint(-6.8920,107.6071);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peta);
		context = this;

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);
		mapView.setUseDataConnection(true);
		mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);

		mapViewController = mapView.getController();
		mapViewController.setZoom(18);
		 mapViewController.setCenter(POS);
		 mapViewController.animateTo(POS);

		// Invoke LoaderCallbacks to retrieve and draw already saved
		// locations in map
		getSupportLoaderManager().initLoader(0, null, this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		gps_enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		network_enabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		Location net_loc = null, gps_loc = null;

		if (gps_enabled)
			gps_loc = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (network_enabled)
			net_loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (gps_loc != null && net_loc != null) {
			if (gps_loc.getAccuracy() >= net_loc.getAccuracy()) {
				updateLoc(gps_loc);
			} else {
				updateLoc(net_loc);

			}
			// I used this just to get an idea (if both avail, its upto you //
			// which you want to take as I taken location with more accuracy)
		}

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Invoke LoaderCallbacks to retrieve and draw already saved
			// locations in map
			getSupportLoaderManager().initLoader(0, null, this);
		}

		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);
		final LinearLayout layoutOSM = (LinearLayout) findViewById(R.id.mapviewOSM);
		final LinearLayout layoutGoogleMaps = (LinearLayout) findViewById(R.id.mapviewgGoogleMaps);

		rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_osm) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					layoutOSM.setVisibility(View.VISIBLE);
					layoutGoogleMaps.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_normal) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					layoutOSM.setVisibility(View.GONE);
					layoutGoogleMaps.setVisibility(View.VISIBLE);
				} else if (checkedId == R.id.rb_satellite) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					layoutOSM.setVisibility(View.GONE);
					layoutGoogleMaps.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	//
	// void inputData(final LatLng point) {
	// AlertDialog.Builder alert = new AlertDialog.Builder(this);
	//
	// alert.setTitle("Title");
	// alert.setMessage("Message");
	//
	// // Set an EditText view to get user input
	// final EditText input = new EditText(this);
	// alert.setView(input);
	//
	// alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// String value = input.getText().toString();
	//
	// // Drawing marker on the map
	// drawMarker(point, value);
	//
	// // Creating an instance of ContentValues
	// ContentValues contentValues = new ContentValues();
	//
	// // Setting latitude in ContentValues
	// contentValues.put(LocationsDB.FIELD_LAT, point.latitude);
	//
	// // Setting longitude in ContentValues
	// contentValues.put(LocationsDB.FIELD_LNG, point.longitude);
	//
	// // Setting zoom in ContentValues
	// contentValues.put(LocationsDB.FIELD_TITLE, value);
	//
	// // // Creating an instance of LocationInsertTask
	// LocationInsertTask insertTask = new LocationInsertTask();
	// //
	// // Storing the latitude, longitude and zoom level to SQLite
	// // database
	// insertTask.execute(contentValues);
	//
	// Toast.makeText(getBaseContext(), "Marker is added to the Map",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	//
	// alert.show();
	// }

	private void drawMarker(final LatLng point, final String judul) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);
		markerOptions.title(judul);

		googleMap.addMarker(markerOptions).showInfoWindow();

		// final int _id;
		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(
					com.google.android.gms.maps.model.Marker marker) {

				String string = marker.getId();
				String[] parts = string.split("m");
				String id = parts[1]; // 034556
				System.out.println(marker.getId() + " - "
						+ Integer.parseInt(id) + " = " + marker.getTitle()
						+ " = " + marker.getTitle().split(" ")[0]);

				// startDetailActivity(Integer.parseInt(id),
				// Integer.parseInt(marker.getTitle().split(" ")[0]));

				Hewan item = daftarHewan.get(Integer.parseInt(marker.getTitle()
						.split(" ")[0]) - 1);

				Intent intent = new Intent(PetaActivity.this,
						DetailActivity.class);
				intent.putExtra(Extra.ID, item.getID());
				intent.putExtra(Extra.TITLE, item.getTitle());
				intent.putExtra(Extra.LAT, item.getLat());
				intent.putExtra(Extra.LNG, item.getLng());
				intent.putExtra(Extra.IMAGES, item.getGambar());
				intent.putExtra(Extra.DESCRIPTION, item.getDeskripsi());
				intent.putExtra(Extra.JARAK, item.getJarak());

				startActivity(intent);
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
			}
		});

		googleMap.addMarker(markerOptions).showInfoWindow();

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	// private class LocationInsertTask extends
	// AsyncTask<ContentValues, Void, Void> {
	// @Override
	// protected Void doInBackground(ContentValues... contentValues) {
	//
	// /**
	// * Setting up values to insert the clicked location into SQLite
	// * database
	// */
	// getContentResolver().insert(LocationsContentProvider.CONTENT_URI,
	// contentValues[0]);
	// return null;
	// }
	// }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;

		// Fetches all the rows from locations table
		return new CursorLoader(this, uri, null, null, null, null);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		int locationCount = 0;
		double lat = 0;
		double lng = 0;
		float zoom = 18;
		String judul, idhewan;

		// Number of locations available in the SQLite database table
		locationCount = arg1.getCount();

		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();

		// Untuk mengambil lokasi user saat ini
		SharedPreferences prefLocation = context
				.getSharedPreferences("mkul", 0);
		double latUser = Double.parseDouble(prefLocation.getString("userLat",
				"0"));
		double lonUser = Double.parseDouble(prefLocation.getString("userLon",
				"0"));

		for (int i = 0; i < locationCount; i++) {

			double latP = Double.parseDouble(arg1.getString(arg1
					.getColumnIndex(LocationsDB.FIELD_LAT)));
			double lonP = Double.parseDouble(arg1.getString(arg1
					.getColumnIndex(LocationsDB.FIELD_LNG)));

			double jarak = Utils.getDistanceBetweenTwoLocation(latUser,
					lonUser, latP, lonP);
			jarak = Utils.RoundDecimal(jarak, 2);

			// Get the latitude
			lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));

			// Get the longitude
			lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));

			// Get the title location
			judul = arg1
					.getString(arg1.getColumnIndex(LocationsDB.FIELD_TITLE));

			// Get the title location
			idhewan = arg1.getString(arg1
					.getColumnIndex(LocationsDB.FIELD_ROW_ID));

			// dari database
			daftarHewan
					.add(new Hewan(
							arg1.getString(arg1
									.getColumnIndex(LocationsDB.FIELD_ROW_ID)),
							arg1.getString(arg1
									.getColumnIndex(LocationsDB.FIELD_LNG)),
							arg1.getString(arg1
									.getColumnIndex(LocationsDB.FIELD_LAT)),
							arg1.getString(arg1
									.getColumnIndex(LocationsDB.FIELD_TITLE)),
							arg1.getString(arg1
									.getColumnIndex(LocationsDB.FIELD_IMAGE)),
							arg1.getString(arg1
									.getColumnIndex(LocationsDB.FIELD_DESCRIPTION)),
							jarak));

			// Creating an instance of LatLng to plot the location in Google
			// Maps
			GeoPoint locationOSM = new GeoPoint(lat, lng);
			LatLng location = new LatLng(lat, lng);

			// Drawing the marker in the Google Maps
			drawMarker(locationOSM, judul);
			drawMarker(location, idhewan + " " + judul);

			// Traverse the pointer to the next row
			arg1.moveToNext();
		}

		if (locationCount > 0) {
			// Moving CameraPosition to last clicked position
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,
					lng)));

			// Setting the zoom level in the map on last position is clicked
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
	}

	private void drawMarker(GeoPoint location, String judul) {
		Drawable nodeIcon = getResources().getDrawable(R.drawable.pin_blue);
		Marker nodeMarker = new Marker(mapView);
		nodeMarker.setPosition(location);
		nodeMarker.setIcon(nodeIcon);
		nodeMarker.setTitle(judul);
		nodeMarker.setInfoWindow(new CustomInfoWindow(mapView));
		mapView.getOverlays().add(nodeMarker);

		// nodeMarker.setOnMarkerClickListener(new OnMarkerClickListener() {
		//
		// @Override
		// public boolean onMarkerClick(Marker marker, MapView arg1) {
		// // TODO Auto-generated method stub
		// int _id = 0;
		// System.out.println(marker.getTitle().toString());
		// if (marker.getTitle().equals("Rektorat"))
		// _id = 0;
		// else
		// _id = 1;
		// // startImagePagerActivity(_id);
		// return false;
		// }
		// });

	}

	public class CustomInfoWindow extends MarkerInfoWindow {
		public CustomInfoWindow(MapView mapView) {
			super(R.layout.bonuspack_bubble, mapView);
		}
	}

	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.peta, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			return true;
		}

		// if (item.getItemId() == R.id.item_search) {
		// finish();
		// startActivity(new Intent(this, SearchActivity.class));
		// overridePendingTransition(R.anim.right_slide_in,
		// R.anim.right_slide_out);
		// }
		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, myLocationListener);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(myLocationListener);
	}

	@Override
	protected void onStop() {
		Log.w("TAG", "App stopped");

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.w("TAG", "App destoryed");

		super.onDestroy();
	}

	@SuppressWarnings("unused")
	private void updateLoc(Location loc) {

		MyLocationIconOverlay MyLocationIconOverlay = new MyLocationIconOverlay(
				getApplicationContext(), mapView, loc);

		if (MyLocationIconOverlay == null) {
			MyLocationIconOverlay = new MyLocationIconOverlay(
					getApplicationContext(), mapView, loc);
			mapView.getOverlays().add(MyLocationIconOverlay);
		} else {
			mapView.getOverlays().remove(MyLocationIconOverlay);
			mapView.invalidate();
			MyLocationIconOverlay = new MyLocationIconOverlay(
					getApplicationContext(), mapView, loc);
			mapView.getOverlays().add(MyLocationIconOverlay);
		}
		if (loc != null) {
			mapView.invalidate();

			GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(),
					loc.getLongitude());

			ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(
					getApplicationContext());
			mapView.getOverlays().add(myScaleBarOverlay);

			// mapViewController.setCenter(UINSGD);
			// mapViewController.setCenter(locGeoPoint);
			// mapViewController.animateTo(locGeoPoint);
			mapViewController.setZoom(18);
			mapView.invalidate();

		}

	}

	private LocationListener myLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			updateLoc(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			// mapView.getOverlays().remove(MyLocationIconOverlay);
		}

	};

	@SuppressWarnings("deprecation")
	private class MyLocationIconOverlay extends MyLocationOverlay {

		// private Context mContext;
		private Location mlastLocation;

		public MyLocationIconOverlay(Context context, MapView mapView,
				Location lastLocation) {
			super(context, mapView);
			// mContext = context;
			mlastLocation = lastLocation;
		}

		@Override
		public void draw(Canvas canvas, MapView mapview, boolean arg2) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapview, arg2);

			// if (!overlayItemArray.isEmpty()) {

			// overlayItemArray have only ONE element only, so I hard code
			// to get(0)
			// GeoPoint in = overlayItemArray.get(0).getPoint();
			GeoPoint locGeoPoint = new GeoPoint(mlastLocation.getLatitude(),
					mlastLocation.getLongitude());
			Point out = new Point();
			mapview.getProjection().toPixels(locGeoPoint, out);

			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.loc_icon);
			canvas.drawBitmap(bm, out.x - bm.getWidth() / 2, // shift the
																// bitmap
																// center
					out.y - bm.getHeight() / 2, // shift the bitmap center
					null);
			// }
		}

	}

}
