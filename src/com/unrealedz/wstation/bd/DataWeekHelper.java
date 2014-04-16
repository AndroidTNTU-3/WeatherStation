package com.unrealedz.wstation.bd;

import java.util.List;

import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.entity.ForecastDay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataWeekHelper {

	private SQLiteDatabase db;
	private List<ForecastDay> forecastDays;
	
	public DataWeekHelper(Context context) {
        DbHelper openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
       
    }
	
	public void insertDayItem(Forecast forecast) {
		forecastDays = forecast.getForecastDays();
		ForecastDay forecastDay = new ForecastDay();
		for(ForecastDay fd: forecastDays){
			forecastDay.setDate(fd.getDate());
			forecastDay.setHour(fd.getHour());
			forecastDay.setCloudId(fd.getCloudId());
			forecastDay.setPictureName(fd.getPictureName());
			forecastDay.setPpcp(fd.getPpcp());
			forecastDay.setTemperatureMin(fd.getTemperatureMin());
			forecastDay.setTemperatureMax(fd.getTemperatureMax());
			forecastDay.setPressureMin(fd.getPressureMin());
			forecastDay.setPressureMax(fd.getPressureMax());
			forecastDay.setWindMin(fd.getWindMin());
			forecastDay.setWindMax(fd.getWindMax());
			forecastDay.setWindRumb(fd.getWindRumb());
			forecastDay.setHumidityMin(fd.getHumidityMin());
			forecastDay.setHumidityMax(fd.getHumidityMax());
			forecastDay.setWpi(fd.getWpi());
			insertForecast(forecastDay);
		}

		
	}
	
	public void cleanOldRecords() {
        db.delete(DbHelper.WEEK_TABLE, null, null);
    }
	
	private long insertForecast(ForecastDay forecastDay) {
	       Log.i("DEBUG DB", "insertWeek");
	        ContentValues values = getDayValues(forecastDay);
	        return db.insert(DbHelper.WEEK_TABLE, null, values);
	    }
	
	 public Cursor getCursor(String tableName) {	    	
	    	Cursor cursor = db.query(tableName, null, null, null, null, null, null);
	        return cursor;
	    }
	
	 private ContentValues getDayValues(ForecastDay forecastDay) {
	        ContentValues values = new ContentValues();
	        values.put(DbHelper.DATE, forecastDay.getDate());
	        values.put(DbHelper.HOUR, forecastDay.getHour());
	        values.put(DbHelper.CLOUD_ID, forecastDay.getCloudId());
	        values.put(DbHelper.PICTURE_NAME, forecastDay.getPictureName());
	        values.put(DbHelper.PPCP, forecastDay.getPpcp());
	        values.put(DbHelper.TEMPERATURE_MIN, forecastDay.getTemperatureMin());
	        values.put(DbHelper.TEMPERATURE_MAX, forecastDay.getTemperatureMax());
	        values.put(DbHelper.PRESSURE_MIN, forecastDay.getPressureMin());
	        values.put(DbHelper.PRESSURE_MAX, forecastDay.getPressureMax());
	        values.put(DbHelper.WIND_MIN, forecastDay.getWindMin());
	        values.put(DbHelper.WIND_MAX, forecastDay.getWindMax());
	        values.put(DbHelper.WIND_RUMB, forecastDay.getWindRumb());
	        values.put(DbHelper.HUMIDITY_MIN, forecastDay.getHumidityMin());
	        values.put(DbHelper.HUMIDITY_MAX, forecastDay.getHumidityMax());
	        values.put(DbHelper.WPI, forecastDay.getWpi());
	        return values;
	 }
	
}
