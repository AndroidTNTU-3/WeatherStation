package com.unrealedz.wstation.fragments;

import com.unrealedz.wstation.MyCursorAdapter;
import com.unrealedz.wstation.R;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FragmentList extends Fragment{
	
	ListView listView;
	Cursor cursor;
	Context context;
	String[] fromFieldNames;
	int[] toViewIDs;
	MyCursorAdapter adapter;

	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		listView = (ListView) view.findViewById(R.id.listView1);
		fromFieldNames = new String[] 
				{DbHelper.TEMPERATURE_MIN, DbHelper.TEMPERATURE_MAX};
		toViewIDs = new int[]
				{R.id.textTmin, R.id.textTmax};
		context = container.getContext();
	    return view;		
	  }
	
	@Override
    public void onAttach( Activity activity ) {
		super.onAttach(activity);

	}
	
	
	@Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        DataWeekHelper dataWeekHelper = new DataWeekHelper(this.getActivity());
		Cursor cursor = dataWeekHelper.getCursor(DbHelper.WEEK_TABLE);
		//if (!MainActivity.isOnline(getActivity())){						// check is device online
			if(cursor.getCount() != 0) setCursor(cursor);				// check dataBase is not empty 
		//}
	}

	public void setCursor(Cursor c) {
		cursor = c;
		MyCursorAdapter adapter = new  MyCursorAdapter(
				context,					// Context
				R.layout.rowlayout,	// Row layout template
				cursor,					// cursor (set of DB records to map)
				fromFieldNames,			// DB Column names
				toViewIDs,				// View IDs to put information in
				0);	
        listView.setAdapter(adapter);
		
	}
	
	public void onDestroy() {
	    super.onDestroy();
	    ((MyCursorAdapter) listView.getAdapter()).getCursor().close();
	    
	  }

}
