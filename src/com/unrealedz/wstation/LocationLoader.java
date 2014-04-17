package com.unrealedz.wstation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.unrealedz.wstation.bd.DataCityHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationLoader implements LocationListener {
	
	  private LocationManager locationManager;
	  private String provider;
	  private Context context;
	  private Location location;
	  double lat;
	  double lng; 
	
	LocationLoader(Context context){
		this.context = context;
	}
	
	public Location getLocation(){
		 // Get the location manager
	    locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	    
	    if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(provider);
            if (location != null) {
            	lat = location.getLatitude();
            	lng = location.getLongitude();
        	    Log.i("DEBUG", " lat:" + String.valueOf(location.getLatitude()));
        	    Log.i("DEBUG", " lon:" + String.valueOf(location.getLongitude()));
        	    try {
        	    	getCodeLocation();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
	    
	    }
	    return location;
	}
	
	public void LocationUpdate(){
		//locationManager.requestLocationUpdates(provider, 400, 1, this);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}
	

	@Override
	public void onLocationChanged(Location arg0) {
		lat = (int) (location.getLatitude());
	    lng = (int) (location.getLongitude());

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
	
	public String getCodeLocation() throws IOException{
		List<Address> addresses = null;
		//DataCityHelper dch = new DataCityHelper(context);
		String cityName = "";
		Geocoder geoCoder = new Geocoder(context,
                Locale.ENGLISH);
		//String s = "log" + location.getLatitude() + "lon" + location.getLongitude();
		if (geoCoder != null){
			/*List<Address> adr = geoCoder.getFromLocationName("Kiev", 1);
			Double l = adr.get(0).getLatitude();
			Log.i("DEBUG", " latitude:" + l);*/
		addresses = geoCoder.getFromLocation(lat, lng, 2);
		Log.i("DEBUG", " Address:" + addresses.size());
		Toast.makeText(context, String.valueOf(addresses.size()), 
     		   Toast.LENGTH_LONG).show();
		/*if (addresses.size() == 0){
			for (int i = 0; i< 10; i++){
				addresses = geoCoder.getFromLocation(lat, lng, 2);
				Log.i("DEBUG", " Address:" + addresses.size());
			}
		}*/
		for (Address adrs : addresses) {
            if (adrs != null) {

                String city = adrs.getLocality();
                if (city != null && !city.equals("")) {
                    cityName = city;
                    //System.out.println("city ::  " + cityName);
                    Log.i("DEBUG", " l:" + cityName);
                    Toast.makeText(context, cityName, 
                    		   Toast.LENGTH_LONG).show();
                } else {

                }
                // // you should also try with addresses.get(0).toSring();

            }
		}
		}
    		//return dch.getCityCode(cityName);
		return cityName;
		

		
	}

	public String getLocationCode(String nameLocation) {
		
		String codeLocation;
		
		DataHelper dh = new DataHelper(context);
		Cursor cursor = dh.getCursor(DbHelper.CITY_TABLE);
		String oldCodeLocation = cursor.getString(cursor.getColumnIndex(DbHelper.CITY_ID));
		
		String newCodeLocation = "";//getCodeLocation();
				
		if (newCodeLocation != oldCodeLocation){
			codeLocation = newCodeLocation;
		} else codeLocation = oldCodeLocation;
		
		
		
		return codeLocation;
	}

}
