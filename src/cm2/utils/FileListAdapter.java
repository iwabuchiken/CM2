package cm2.utils;

import java.util.List;

import cm2.items.FileItem;
import cm2.main.MainActv;
import cm2.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileListAdapter extends ArrayAdapter<FileItem>{

	// Inflater
	LayoutInflater inflater;

	//
	Activity actv;
	
	
	public FileListAdapter(Context con, int resourceId, List<FileItem> items) {
		super(con, resourceId, items);
		
		// Inflater
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.actv = (Activity) con;
		
	}//public FileListAdapter(Context con, int resourceId, List<FileItem> items)

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*----------------------------
		 * Steps
		 * 1. Get view
		 * 1-2. Get item
		 * 2. Set text => File name
		 * 2-2. Set duration, etc.
		 * 
		 * 3. Highlight the view
			----------------------------*/
		
		// Log
		Log.d("FileListAdapter.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "position: " + position);
		
		
		/*----------------------------
		 * 1. Get view
			----------------------------*/
		View v;
		
    	if (convertView != null) {
			v = convertView;
		} else {//if (convertView != null)
//			v = inflater.inflate(R.layout.list_row, null);
			v = inflater.inflate(R.layout.list_row, null);
		}//if (convertView != null)

    	/*----------------------------
		 * 1-2. Get item
			----------------------------*/
    	FileItem fi = getItem(position);
    	
    	/*----------------------------
		 * 2. Set text => File name
			----------------------------*/
		TextView tv = (TextView) v.findViewById(R.id.list_row_tv_file_name);
		
		tv.setText(fi.getFile_name());

		/*----------------------------
		 * 2-2. Set duration, etc.
			----------------------------*/
		// Duration
		TextView tv_duration = (TextView) v.findViewById(R.id.list_row_tv_duration);
//		tv_duration.setText(String.valueOf(fi.getDuration()));
		tv_duration.setText(Methods.convert_millSeconds2digitsLabel(fi.getDuration()));

		
		return v;
		
//		return super.getView(position, convertView, parent);
	}//public View getView(int position, View convertView, ViewGroup parent)

}//public class FileListAdapter<FileItem> extends ArrayAdapter<FileItem>
