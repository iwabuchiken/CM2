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
		
		// Log
		Log.d("FileListAdapter.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "file: " + fi.getFile_name() + " / " + "fi.getDuration()" + fi.getDuration());
		
		
		// Info
//		if(!fi.getFile_info().equals("")) {
		if(fi.getFile_info() != null) {
			TextView tv_info = (TextView) v.findViewById(R.id.list_row_tv_file_info);
			tv_info.setText(fi.getFile_info());
		}
		
		// Memo
//		if(!fi.getFile_info().equals("")) {
		if(fi.getMemos() != null) {
			TextView tv_memo= (TextView) v.findViewById(R.id.list_row_tv_memo);
			tv_memo.setText(fi.getMemos());
		}
		
		/*----------------------------
		 * 3. Highlight the view
		 * 		1. Get prefs
		 * 		2. Get previous position
		 * 		3. Highlight
			----------------------------*/
		SharedPreferences prefs = 
				actv.getSharedPreferences(MainActv.PREFS_HIGHLIGHT, MainActv.MODE_PRIVATE);

		int prev_position = prefs.getInt(MainActv.PREFS_HIGHLIGHT, -1);
		
		/*----------------------------
		 * 3.3. Highlight
			----------------------------*/
		if (prev_position == -1) {
		
//			v.setBackgroundColor(Color.BLUE);
			
		} else {//if (prev_position == -1)
			/*----------------------------
			 * Steps
			 * 1. Get previous item
			 * 2. Set hightlight
				----------------------------*/
			
//			// Log
//			Log.d("FileListAdapter.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "prev_position: " + prev_position);
//			
//			
////			View prev_view = lv.getChildAt(prev_position);
//			FileItem fi_prev = getItem(prev_position);
//			
//			// Log
//			Log.d("FileListAdapter.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "fi_prev.getFile_name(): " + fi_prev.getFile_name());
			
			/*----------------------------
			 * 3.3.2. Set hightlight
				----------------------------*/
			if (position == prev_position) {
				
				v.setBackgroundColor(Color.BLUE);
				
			} else {//if (position == prev_position)
				
				v.setBackgroundColor(Color.BLACK);
				
			}//if (position == prev_position)
			
//			// Log
//			Log.d("MainActivity.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "lv.getChildCount(): " + lv.getChildCount());
//			
//			
//			if (prev_view != null) {
//				
//				prev_view.setBackgroundColor(Color.BLACK);
//				
//			} else {//if (prev_view != null)
//				
//				// Log
//				Log.d("MainActivity.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "prev_view == null");
//				
//			}//if (prev_view != null)
//			
//			
//			v.setBackgroundColor(Color.BLUE);
			
		}//if (prev_position == -1)

		
		return v;
		
//		return super.getView(position, convertView, parent);
	}//public View getView(int position, View convertView, ViewGroup parent)

}//public class FileListAdapter<FileItem> extends ArrayAdapter<FileItem>
