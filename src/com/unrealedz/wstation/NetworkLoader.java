package com.unrealedz.wstation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import com.unrealedz.wstation.bd.DataCityHelper;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.entity.CityDB;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.parsers.CityDbParser;
import com.unrealedz.wstation.parsers.WeatherParser;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkLoader extends AsyncTask<String, Void, Object> {
	
	Forecast forecast;
	CityDB cityDB;
	
	Object ob;
	
	Context context;
		
	public static interface LoaderCallBack{
		public void setLocationInfo();
		public void setCurrentDay();
		public void setWeekList();
		public void setCitiesDB();
	}
	
	LoaderCallBack loaderCallBack;
	
	NetworkLoader(Context context){
		
		this.context = context;
	}
	
	NetworkLoader(){
		
	}

	@Override
	protected Object doInBackground(String... params) {
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpRequest = null;
		InputStream stream = null;

        httpRequest = new HttpGet(params[1]);
        
        try {
			HttpResponse response = (HttpResponse) client.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			
			stream = entity.getContent();
			try {
				if (params[0].equals(MainActivity.GET_CITY_DB)) {
					//cityDB = new CityDbParser().parse(stream);
				ob = new CityDbParser().parse(stream);
				 
				}
				else if(params[0].equals(MainActivity.GET_FORECAST)){
				ob = new WeatherParser().parse(stream);
				//Log.i("FORECAST", "t:" + forecast.getCurrentForecast().getTemperature());

				}
				
				
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        
		return ob;
	}
	
	@Override
    protected void onPostExecute(Object result) {
      super.onPostExecute(result);
      
      if (result instanceof Forecast){
    	  
      DataHelper dh = new DataHelper(context);
      DataDayHelper ddh = new DataDayHelper(context);
      DataWeekHelper dwh = new DataWeekHelper(context);
      
      dh.cleanOldRecords();
      ddh.cleanOldRecords();
      dwh.cleanOldRecords();
      
      dh.insertCityItem((Forecast)result);
      ddh.insertDayItem((Forecast)result);
      dwh.insertDayItem((Forecast)result);

      	if (loaderCallBack != null){
    	  loaderCallBack.setLocationInfo();
    	  loaderCallBack.setWeekList();
      	}           

      } else if(result instanceof CitiesDB){
		
    	  DataCityHelper dataCity = new DataCityHelper(context);
    	  dataCity.cleanOldRecords();
    	  dataCity.insertCitiesDB((CitiesDB)result);
      }
	}
	
	public void setLoaderCallBack(LoaderCallBack loaderCallBack) {
		this.loaderCallBack = loaderCallBack;
	}
      

}
