package com.facwork.bonbin.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;



public class Route {

	private LocationManager locman;
	private LocationListener loclis;
	private List<String> l_providers;
	private String s_provider;
	private String uri;
	Context ctx;
	public Route(Context mContext, String method, String lat, String lon){
		ctx = mContext;
		
		String s_method = method;
		
		/* Use the LocationManager class to obtain GPS locations */
		
    	locman = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
		loclis = new LocationListener(){
			public void onLocationChanged(Location loc)

			{
				@SuppressWarnings("unused")
				String Text = "Current location is: " +
				"Latitud = " + loc.getLatitude() +
				" Longitud = " + loc.getLongitude() +
				" Accuracy = " + loc.getAccuracy();

//				Toast.makeText(ctx,Text,Toast.LENGTH_SHORT).show();
			}
			
			public void onStatusChanged(String s_provider, int status, Bundle extras)

			{

			}
			
			public void onProviderDisabled(String s_provider)

			{

			}

			public void onProviderEnabled(String s_provider)

			{

			}

		};/* End of Class MyLocationListener */
		 
		if (("GPS").equals(s_method)){
    		locman.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, loclis);
    	}
    	else{
    		locman.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, loclis);
    	}
		
		l_providers = locman.getProviders(true);
		Location last_known_location = null;
		
		for (int x=l_providers.size()-1; x>=0; x--) {
			last_known_location = locman.getLastKnownLocation(l_providers.get(x));
			s_provider = l_providers.get(x);
			if (last_known_location != null) break;
		}
		
		String s_lat = lat;
    	String s_lon = lon;

    	Log.i("tm", "Latitude: " +s_lat);
    	Log.i("tm", "Longitud: " +s_lon);
    	Log.i("tm", "last_known_location.getLatitude(): " +last_known_location.getLatitude());
    	Log.i("tm", "last_known_location.getLongitude(): " +last_known_location.getLongitude());
		
  	
		
  	  	uri = "http://maps.google.com/maps?saddr="
  	  		+last_known_location.getLatitude()+","+last_known_location.getLongitude()
  	  		+"&daddr="+s_lat+","+s_lon+"&z=17";
        }
	
	public void goToMap(){
		ctx.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));			
	}
	public void onResume() {
		locman.requestLocationUpdates(s_provider, 20000, 1, loclis);
	}
	
	public void onPause() {
		locman.removeUpdates(loclis);
	}    
	
	public void onDestroy() {
		locman.removeUpdates(loclis);
	} 
}

