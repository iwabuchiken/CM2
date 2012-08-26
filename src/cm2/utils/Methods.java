package cm2.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

import org.apache.commons.lang.StringUtils;


import cm2.listeners.DialogButtonOnClickListener;
import cm2.listeners.DialogButtonOnTouchListener;
import cm2.main.MainActv;

public class Methods {

	static int counter;		// Used => sortFileList()
	
	
	/****************************************
	 * Enums
	 ****************************************/
	public static enum DialogTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog, dlg_generic_dismiss_third_dialog,
		
		
		// dlg_create_folder.xml
		dlg_create_folder_ok, dlg_create_folder_cancel,

		// dlg_input_empty.xml
		dlg_input_empty_reenter, dlg_input_empty_cancel,
		
		// dlg_confirm_create_folder.xml
		dlg_confirm_create_folder_ok, dlg_confirm_create_folder_cancel,

		// dlg_confirm_remove_folder.xml
		dlg_confirm_remove_folder_ok, dlg_confirm_remove_folder_cancel,

		// dlg_drop_table.xml
		dlg_drop_table_btn_cancel, dlg_drop_table,
		
		// dlg_confirm_drop.xml
		dlg_confirm_drop_table_btn_ok, dlg_confirm_drop_table_btn_cancel,
		
		// dlg_add_memos.xml
		dlg_add_memos_bt_add, dlg_add_memos_bt_cancel, dlg_add_memos_bt_patterns,
		dlg_add_memos_gv,

		// dlg_move_files.xml
		dlg_move_files_move, dlg_move_files,
		
		// dlg_confirm_move_files.xml	=> ok, cancel, dlg tag
		dlg_confirm_move_files_ok, dlg_confirm_move_files_cancel, dlg_confirm_move_files,

		// dlg_item_menu.xml
		dlg_item_menu_bt_cancel, dlg_item_menu,

		// dlg_create_table.xml
		dlg_create_table_bt_create,

		// dlg_memo_patterns.xml
		dlg_memo_patterns,
		
		// dlg_register_patterns.xml
		dlg_register_patterns_register,

		// dlg_search.xml
		dlg_search, dlg_search_ok,

		// dlg_admin_patterns.xml

		// dlg_confirm_delete_patterns.xml
		dlg_confirm_delete_patterns_ok,
		
	}//public static enum DialogTags
	
	public static enum DialogItemTags {
		// dlg_moveFiles(Activity actv)
		dlg_move_files,
		
		// dlg_add_memos.xml
		dlg_add_memos_gv,

		// dlg_db_admin.xml
		dlg_db_admin_lv,

		// dlg_admin_patterns.xml
		dlg_admin_patterns_lv,

		// dlg_delete_patterns.xml
		dlg_delete_patterns_gv,
		
	}//public static enum DialogItemTags
	
	
	public static enum ButtonTags {
		// main.xml
		main_bt_play, main_bt_pause, main_bt_rec,
		
	}//public static enum ButtonTags

	public static enum ButtonModes {
		// Play
		READY, FREEZE, PLAY,
		// Rec
		REC,
	}

	public static enum ItemTags {
		
		// MainActivity.java
		dir_list,
		
		// ThumbnailActivity.java
		dir_list_thumb_actv,
		
		// Methods.java
		dir_list_move_files,
		
		// TIListAdapter.java
		tilist_checkbox,
		
		
	}//public static enum ItemTags

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum PrefenceLabels {
		
		CURRENT_PATH,
		
		thumb_actv,
		
		chosen_list_item,
		
	}//public static enum PrefenceLabels

	public static enum ListTags {
		// MainActivity.java
		actv_main_lv,
		
	}//public static enum ListTags

	public static enum LongClickTags {
		// MainActivity.java
		main_activity_list,
		
		
	}//public static enum LongClickTags

	
	/****************************************
	 * Vars
	 ****************************************/
	public static final int vibLength_click = 35;

	static int tempRecordNum = 20;

	/****************************************
	 * Methods
	 ****************************************/
	public static void sortFileList(File[] files) {
		// REF=> http://android-coding.blogspot.jp/2011/10/sort-file-list-in-order-by-implementing.html
		/*----------------------------
		 * 1. Prep => Comparator
		 * 2. Sort
			----------------------------*/
		/*----------------------------
		 * 1. Prep => Comparator
			----------------------------*/
		Comparator<? super File> filecomparator = new Comparator<File>(){
			
			public int compare(File file1, File file2) {
				/*----------------------------
				 * 1. Prep => Directory
				 * 2. Calculate
				 * 3. Return
					----------------------------*/
				
				int pad1=0;
				int pad2=0;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				int res = pad2-pad1+file1.getName().compareToIgnoreCase(file2.getName());
				
				return res;
			} 
		 };//Comparator<? super File> filecomparator = new Comparator<File>()
		 
		/*----------------------------
		 * 2. Sort
			----------------------------*/
		Arrays.sort(files, filecomparator);

	}//public static void sortFileList(File[] files)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean update_prefs_currentPath(Activity actv, String newPath) {
		
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Set value
//			----------------------------*/
//		editor.putString(MainActv.prefs_current_path, newPath);
//		
//		try {
//			editor.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}
		return false;
		
	}//public static boolean update_prefs_current_path(Activity actv, Strin newPath)

	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean clear_prefs_currentPath(Activity actv, String newPath) {
		
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Clear
//			----------------------------*/
//		try {
//			
//			editor.clear();
//			editor.commit();
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Prefs cleared");
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}

		return false;
		
	}//public static boolean clear_prefs_current_path(Activity actv, Strin newPath)

	
	/****************************************
	 *
	 * 
	 * <Caller> 1. Methods.enterDir()
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static String get_currentPath_from_prefs(Activity actv) {
		
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(MainActv.prefs_current_path, MainActv.MODE_PRIVATE);
//
//		return prefs.getString(MainActv.prefs_current_path, null);

		return null;
		
	}//public static String get_currentPath_from_prefs(Activity actv)

	public static void confirm_quit(Activity actv, int keyCode) {
		
//		// TODO 自動生成されたメソッド・スタブ
//		if (keyCode==KeyEvent.KEYCODE_BACK) {
//			
//			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
//			
//	        dialog.setTitle("アプリの終了");
//	        dialog.setMessage("アプリを終了しますか？");
//	        
//	        dialog.setPositiveButton("終了",new DialogListener(actv, dialog, 0));
//	        dialog.setNegativeButton("キャンセル",new DialogListener(actv, dialog, 1));
//	        
//	        dialog.create();
//	        dialog.show();
//			
//		}//if (keyCode==KeyEvent.KEYCODE_BACK)
		
	}//public static void confirm_quit(Activity actv, int keyCode)

	public static List<String> getTableList(Activity actv, String dbName) {
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				//
				tableList.add(c.getString(0));
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount(): " + c.getCount());
//		
		rdb.close();
		
		return tableList;
		
//		return null;
	}//public static List<String> getTableList()

	/****************************************
	 *		insertDataIntoDB()
	 * 
	 * <Caller> 
	 * 1. private static boolean refreshMainDB_3_insert_data(Activity actv, Cursor c)
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static int insertDataIntoDB(Activity actv, String tableName, Cursor c) {
		/*----------------------------
		 * Steps
		 * 0. Set up db
		 * 1. Move to first
		 * 2. Set variables
		 * 3. Obtain data
		 * 4. Insert data
		 * 5. Close db
		 * 6. Return => counter
			----------------------------*/
//		/*----------------------------
//		 * 0. Set up db
//			----------------------------*/
//		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
//		
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//		
//		/*----------------------------
//		 * 1. Move to first
//			----------------------------*/
//		c.moveToFirst();
//
//		/*----------------------------
//		 * 2. Set variables
//			----------------------------*/
//		int counter = 0;
//		int counter_failed = 0;
//		
//		/*----------------------------
//		 * 3. Obtain data
//			----------------------------*/
//		for (int i = 0; i < c.getCount(); i++) {
//
//			String[] values = {
//					String.valueOf(c.getLong(0)),
//					c.getString(1),
//					c.getString(2),
//					String.valueOf(c.getLong(3)),
//					String.valueOf(c.getLong(4))
//			};
//
//			/*----------------------------
//			 * 4. Insert data
//			 * 		1. Insert data to tableName
//			 * 		2. Record result
//			 * 		3. Insert data to backupTableName
//			 * 		4. Record result
//				----------------------------*/
//			boolean blResult = 
//						dbu.insertData(wdb, tableName, DBUtils.cols_for_insert_data, values);
//				
//			if (blResult == false) {
//				// Log
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "i => " + i + "/" + "c.getLong(0) => " + c.getLong(0));
//				
//				counter_failed += 1;
//				
//			} else {//if (blResult == false)
//				counter += 1;
//			}
//
//			//
//			c.moveToNext();
//			
//			if (i % 100 == 0) {
//				// Log
//				Log.d("Methods.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", "Done up to: " + i);
//				
//			}//if (i % 100 == 0)
//			
//		}//for (int i = 0; i < c.getCount(); i++)
//		
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "All data inserted: " + counter);
//		
//		/*----------------------------
//		 * 5. Close db
//			----------------------------*/
//		wdb.close();
//		
//		/*----------------------------
//		 * 6. Return => counter
//			----------------------------*/
//		//debug
//		// Log
//		Log.d("Methods.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "counter_failed(sum): " + counter_failed);
//		
//		return counter;

		return -1;
		
	}//private static int insertDataIntoDB(Activity actv, Cursor c)

	private static boolean insertDataIntoDB(
			Activity actv, String tableName, String[] types, String[] values) {
//		/*----------------------------
//		* Steps
//		* 1. Set up db
//		* 2. Insert data
//		* 3. Show message
//		* 4. Close db
//		----------------------------*/
//		/*----------------------------
//		* 1. Set up db
//		----------------------------*/
//		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
//		
//		SQLiteDatabase wdb = dbu.getWritableDatabase();
//		
//		/*----------------------------
//		* 2. Insert data
//		----------------------------*/
//		boolean result = dbu.insertData(wdb, tableName, types, values);
//		
//		/*----------------------------
//		* 3. Show message
//		----------------------------*/
//		if (result == true) {
//		
//			// debug
//			Toast.makeText(actv, "Data stored", 2000).show();
//			
//			/*----------------------------
//			* 4. Close db
//			----------------------------*/
//			wdb.close();
//			
//			return true;
//			
//		} else {//if (result == true)
//		
//			// debug
//			Toast.makeText(actv, "Store data => Failed", 200).show();
//			
//			/*----------------------------
//			* 4. Close db
//			----------------------------*/
//			wdb.close();
//			
//			return false;
//		
//		}//if (result == true)
//		
//		/*----------------------------
//		* 4. Close db
//		----------------------------*/

		return false;
		
	}//private static boolean insertDataIntoDB()


	public static boolean set_pref(Activity actv, String pref_name, String value) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Set value
//			----------------------------*/
//		editor.putString(pref_name, value);
//		
//		try {
//			editor.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}

		return false;
	}//public static boolean set_pref(String pref_name, String value)

	public static String get_pref(Activity actv, String pref_name, String defValue) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * Return
//			----------------------------*/
//		return prefs.getString(pref_name, defValue);
		
		return null;

	}//public static boolean set_pref(String pref_name, String value)

	public static boolean set_pref(Activity actv, String pref_name, int value) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * 2. Get editor
//			----------------------------*/
//		SharedPreferences.Editor editor = prefs.edit();
//
//		/*----------------------------
//		 * 3. Set value
//			----------------------------*/
//		editor.putInt(pref_name, value);
//		
//		try {
//			editor.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Excption: " + e.toString());
//			
//			return false;
//		}

		return false;
	}//public static boolean set_pref(String pref_name, String value)

	public static int get_pref(Activity actv, String pref_name, int defValue) {
//		SharedPreferences prefs = 
//				actv.getSharedPreferences(pref_name, MainActv.MODE_PRIVATE);
//
//		/*----------------------------
//		 * Return
//			----------------------------*/
//		return prefs.getInt(pref_name, defValue);

		return -1;
	}//public static boolean set_pref(String pref_name, String value)

	public static Dialog dlg_template_cancel(Activity actv, int layoutId, int titleStringId,
			int cancelButtonId, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static Dialog dlg_template_okCancel(Activity actv, int layoutId, int titleStringId,
			int okButtonId, int cancelButtonId, DialogTags okTag, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
	
	}//public static Dialog dlg_template_okCancel()

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)

	/****************************************
	 *	getMillSeconds_now()
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener # case main_bt_start
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static long getMillSeconds_now() {
		
		Calendar cal = Calendar.getInstance();
		
		return cal.getTime().getTime();
		
	}//private long getMillSeconds_now(int year, int month, int date)

	public static String get_TimeLabel(long millSec) {
		
		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		 
		return sdf1.format(new Date(millSec));
		
	}//public static String get_TimeLabel(long millSec)

	public static String  convert_millSeconds2digitsLabel(long millSeconds) {
		/*----------------------------
		 * Steps
		 * 1. Prepare variables
		 * 2. Build a string
		 * 3. Return
			----------------------------*/
		/*----------------------------
		 * 1. Prepare variables
			----------------------------*/
		int seconds = (int)(millSeconds / 1000);
		
		int minutes = seconds / 60;
		
		int digit_seconds = seconds % 60;
		
		/*----------------------------
		 * 2. Build a string
			----------------------------*/
		StringBuilder sb = new StringBuilder();
		
		if (String.valueOf(minutes).length() < 2) {
			
			sb.append("0");
			
		}//if (String.valueOf(minutes).length() < 2)
		
		sb.append(String.valueOf(minutes));
		sb.append(":");

		if (String.valueOf(digit_seconds).length() < 2) {
			
			sb.append("0");
			
		}//if (String.valueOf(digit_seconds).length() < 2)

		sb.append(String.valueOf(digit_seconds));
		
		/*----------------------------
		 * 3. Return
			----------------------------*/
		return sb.toString();
		
	}//public static void  convert_millSeconds2digitsLabel()

	public static List<String> prepare_file_list_tapeatalk(Activity actv, String tableName) {
		/*----------------------------
		 * 1. Set up db
		 * 2. Table exists?
		 * 3. Query
		 * 
		 * 4. Build a list
		 * 5. Return
			----------------------------*/
		
		
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 2. Table exists?
			----------------------------*/
		boolean res = dbu.tableExists(wdb, tableName);

		if (res == false) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "テーブルを作ります");
			
			res = dbu.createTable(
								wdb, 
								tableName, 
								DBUtils.cols_main_table, 
								DBUtils.col_types_main_table);
			
			if (res == false) {
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "テーブルを作れませんでした");
				
				wdb.close();
				
				return null;
				
			} else {//if (res == false)
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "テーブルを作りました");
				
			}//if (res == false)
			
		} else {//if (res == false)
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "テーブルはすでにあります");
			
		}//if (res == false)

		wdb.close();
		
		/*----------------------------
		 * 3. Query
			----------------------------*/
		SQLiteDatabase rdb = dbu.getReadableDatabase();
		
		String sql = "SELECT * FROM " + tableName;
		
		Cursor c = rdb.rawQuery(sql, null);
		
		actv.startManagingCursor(c);
		
		if (c.getCount() < 1) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "No records: " + tableName);
			
			rdb.close();
			
			return null;
			
		}//if (c.getCount() < 1)
		
		/*----------------------------
		 * 4. Build a list
			----------------------------*/
		List<String> file_names_tapeatalk = new ArrayList<String>();
		
		c.moveToFirst();
		
		for (int i = 0; i < c.getCount(); i++) {
			
			file_names_tapeatalk.add(c.getString(2));
		
			c.moveToNext();
			
		}//for (int i = 0; i < c.getCount(); i++)
		
		/*----------------------------
		 * 5. Return
			----------------------------*/
		return file_names_tapeatalk;
		
	}//public static List<String> prepare_file_list_tapeatalk(Activity actv)

	
}//public class Methods
