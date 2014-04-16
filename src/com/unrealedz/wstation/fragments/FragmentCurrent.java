package com.unrealedz.wstation.fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.*;

import com.unrealedz.wstation.NetworkLoader;
import com.unrealedz.wstation.R;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DbHelper;

public class FragmentCurrent extends Fragment{
	
	TextView city;
	TextView region;
	TextView temperature;
	ImageView imageView;
	
	NetworkLoader nLoader;
	Cursor cursorCity;
	Cursor cursorCurrent;
	
	Context context;
			
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.current, container, false);
		
		city = (TextView) v.findViewById(R.id.tvCity);
		region = (TextView) v.findViewById(R.id.tvRegion);
		temperature = (TextView) v.findViewById(R.id.tvTemperature);
		imageView = (ImageView) v.findViewById(R.id.imageCurrent);
		context = container.getContext();
	    return v;
	  }
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    DataHelper dh = new DataHelper(this.getActivity());
	    DataDayHelper dd = new DataDayHelper(this.getActivity());
	    cursorCity = dh.getCursor(DbHelper.CITY_TABLE);
		cursorCurrent = dd.getCursor(DbHelper.CURRENT_DAY_TABLE);
		
		Log.i("DEBUG CUR", "City:" + String.valueOf(cursorCity.getCount()) + "Current:" + String.valueOf(cursorCurrent.getCount()));
		//if (!MainActivity.isOnline(getActivity())){						// check is device online
		
			if((cursorCity.getCount() != 0) && (cursorCurrent.getCount() != 0)) setData(cursorCity, cursorCurrent);							// check dataBase is not empty 
			//else Toast.makeText(this.getActivity(), "Network offline, Please check the internet connection", Toast.LENGTH_LONG).show();
			//else if (!MainActivity.isOnline(getActivity())) Toast.makeText(this.getActivity(), "Network offline, Please check the internet connection", Toast.LENGTH_LONG).show();
		//}
		
	  }
	
	public void onStart() {
	    super.onStart();
	  }

	public void setData(Cursor cursorCity, Cursor cursorCurrent) {
		
		if (cursorCity.moveToFirst()){	
		city.setText(cursorCity.getString(cursorCity.getColumnIndex(DbHelper.CITY_NAME)));
		region.setText(cursorCity.getString(cursorCity.getColumnIndex(DbHelper.REGION)));
		
		}
		
		if (cursorCurrent.moveToFirst()){
		Log.i("DEBUG CUR", String.valueOf(cursorCurrent.getColumnIndex(DbHelper.TEMPERATURE)));
		temperature.setText(cursorCurrent.getString(cursorCurrent.getColumnIndex(DbHelper.TEMPERATURE)) + "°");
		
		String pictureName =  cursorCurrent.getString(cursorCurrent.getColumnIndex(DbHelper.PICTURE_NAME));
		pictureName = pictureName.substring(0, pictureName.lastIndexOf("."));
		int id = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		imageView.setImageResource(id);
		
		/*int id = context.getResources().getIdentifier(myString, "drawable", context.getPackageName());
		Drawable drawable = context.getResources().getDrawable(id);
		imageView.setImageDrawable(drawable);*/
		
		}
	}
	
	
	public void onDestroy() {
	    super.onDestroy();
	    cursorCity.close();
	    cursorCurrent.close();
	  }

}
