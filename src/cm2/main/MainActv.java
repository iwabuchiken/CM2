package cm2.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cm2.items.FileItem;
import cm2.listeners.ButtonOnClickListener;
import cm2.listeners.ButtonOnTouchListener;
import cm2.listeners.CustomOnItemLongClickListener;
import cm2.utils.DBUtils;
import cm2.utils.FileListAdapter;
import cm2.utils.Methods;
import cm2.utils.RefreshDBTask;
import android.app.Activity;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActv extends ListActivity {
	
	/*----------------------------
	 * Constants
		----------------------------*/
	// Buttons => Initial states
	public static Methods.ButtonModes play_mode = Methods.ButtonModes.READY;
	public static Methods.ButtonModes pause_mode = Methods.ButtonModes.FREEZE;
	public static Methods.ButtonModes rec_mode = Methods.ButtonModes.READY;

	/*----------------------------
	 * Views
		----------------------------*/
	public static ImageButton ib_play;
	public static ImageButton ib_pause;
	public static ImageButton ib_rec;

	public static SeekBar sb;
	
	public static TextView tv_progress;

	public static ListView lv_main;	// Used => Methods.playFile()

	/*----------------------------
	 * Objects
		----------------------------*/
	public static MediaPlayer mp = null;

	public static MediaRecorder mr = null;

	public static Timer timer;

	public static Vibrator vib;
	
	/*----------------------------
	 * Folders & Paths
		----------------------------*/
	//
	public static String dirName_tapeatalk = "tapeatalk_records";
	
	public static String dirPath_tapeatalk;
	
	public static String dirName_mainFolder = "CM2";
	
	public static String dirPath_mainFolder;
	
	public static String dirPath_ExternalStorage = "/mnt/sdcard-ext";
	
//	public static String  rootDir;
	
	public static String currentFileName = null;

	/*----------------------------
	 * DB
		----------------------------*/
	public static String dbName = "CM2.db";
	
	public static String tableName_root = "CM2";
	
	public static String tableName_refreshLog = "refresh_log";
	
	/*----------------------------
	 * Numbers
		----------------------------*/
	public static int max = 5;	// Used => test1_setProgress2TextView()
	public static int counter = 0;

	// Prefs name for highlighting the item
	public static final String PREFS_HIGHLIGHT = "PREFS_HIGHLIGHT";

	/*----------------------------
	 * List-related items
		----------------------------*/
	static ArrayAdapter<String> adp;
	
	public static FileListAdapter flAdapter;
	
	public static List<FileItem> fiList;
	
		
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
		/*----------------------------
		 * Steps
		 * 1. Super
		 * 2. Set content
		----------------------------*/
		super.onCreate(savedInstanceState);

		
		//
		setContentView(R.layout.main);

		setTitle(this.getClass().getName());
		
		vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting => initial_setup()");
		
		
		initial_setup();
		
		//debug
//		show_table_list();
//		drop_table(tableName_root);
		
	}//public void onCreate(Bundle savedInstanceState)

    private void drop_table(String tableName_root) {
    	
		DBUtils dbu = new DBUtils(this, MainActv.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = dbu.dropTable(this, wdb, tableName_root);
		
		res = dbu.dropTable(this, wdb, MainActv.tableName_refreshLog);
		
	}//private void drop_table(String tableName_root) {

	private void show_table_list() {
		// TODO 自動生成されたメソッド・スタブ
		List<String> tableList = Methods.getTableList(this, dbName);
		
		for (String tableName : tableList) {
			
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "tableName: " + tableName);
			
		}
		
	}//private void show_table_list()

	private void initial_setup() {
		/*----------------------------
		 * Steps
		 * 0. Initialize fields
		 * 1. Set list view
		 * 2. Listeners
		 * 3. 
		 * 4. SeekBar
			----------------------------*/
		/*----------------------------
		 * 0. Initialize fields
			----------------------------*/
		initialize_fields();
		
		/*----------------------------
		 * 1. Set list view
			----------------------------*/
		set_listview();
		
		/*----------------------------
		 * 2. Listeners
			----------------------------*/
//		set_listeners();
		
	}//private void initial_setup()

	private void initialize_fields() {
		/*----------------------------
		 * 1. MediaPlayer
		 * 2. dirPath_tapeatalk
		 * 
			----------------------------*/
		
		mp = new MediaPlayer();
		
		dirPath_tapeatalk = new File(
				Environment.getExternalStorageDirectory().getAbsolutePath(), 
				dirName_tapeatalk)
					.getAbsolutePath();
		
		dirPath_mainFolder = new File(
				dirPath_ExternalStorage, dirName_mainFolder)
					.getAbsolutePath();
		
		// Log
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "dirPath_tapeatalk: " + dirPath_tapeatalk +
				"(Exists: " + new File(dirPath_tapeatalk).exists() + ")");
		
		Log.d("MainActv.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "dirPath_mainFolder: " + dirPath_mainFolder +
				"(Exists: " + new File(dirPath_mainFolder).exists() + ")");
		
	}//private void initialize_fields()

	private void set_listview() {
		/*----------------------------
		 * Steps
		 * 1. Get view
		 * 2. Get file list
		 * 3. Adapter
		 * 4. Set adp
			----------------------------*/
//		ListView lv = this.getListView();
		
		/*----------------------------
		 * 2. Get file list
		 * 		1. List<String>
		 * 		2. List<FileItem>
			----------------------------*/
		List<String> file_names_tapeatalk = 
						Methods.prepare_file_list_tapeatalk(this, tableName_root);
		
		if (file_names_tapeatalk == null) {
			
			// debug
			Toast.makeText(MainActv.this, "No data yet", 2000).show();
			
			return;
			
		}//if (file_names_tapeatalk == null)
////		String  rootDir = new File(
//		File[] files = new File(dirPath_tapeatalk).listFiles();
//		
//		List<String> file_names_tapeatalk = new ArrayList<String>();
//		
//		for (File file : files) {
//			
//			file_names_tapeatalk.add(file.getName());
//			
//		}//for (File file : files)
//		
		/*----------------------------
		 * 2.2. List<FileItem>
			----------------------------*/
//		flAdapter = new FileListAdapter<FileItem>();
		
//		fiList = Methods.prepare_fiList(this, DBUtils.mainTableName);
		
		/*----------------------------
		 * 3. Adapter
			----------------------------*/
		adp = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				file_names_tapeatalk
				);
//		flAdapter = new FileListAdapter(
//				this,
//				R.layout.main,
//				fiList
//				);
		
		/*----------------------------
		 * 4. Set adp
			----------------------------*/
//		lv.setAdapter(adp);
//		setAdapter(adp);
//		setListAdapter(adp);
		
//		setListAdapter(flAdapter);
		setListAdapter(adp);
		
		
	}//private void set_listview()

	private void set_listeners() {
		/*----------------------------
		 * Steps
		 * 1. Touch
		 * 2. Click
		 * 
		 * 3. SeekBar
		 * 
		 * 4. ListView => LongClick
			----------------------------*/
		/*----------------------------
		 * 1. Touch
			----------------------------*/
		ib_play = (ImageButton) findViewById(R.id.main_iv_play);
		ib_pause = (ImageButton) findViewById(R.id.main_iv_pause);
		ib_rec = (ImageButton) findViewById(R.id.main_iv_rec);
		
		// Tags
		ib_play.setTag(Methods.ButtonTags.main_bt_play);
		ib_pause.setTag(Methods.ButtonTags.main_bt_pause);
		ib_rec.setTag(Methods.ButtonTags.main_bt_rec);
		
		// Enable
		setup_buttons();
//		Methods.update_buttonImages(this);
		
		
		
		// Listener
		ib_play.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_pause.setOnTouchListener(new ButtonOnTouchListener(this));
		ib_rec.setOnTouchListener(new ButtonOnTouchListener(this));
		
		/*----------------------------
		 * 2. Click
			----------------------------*/
		ib_play.setOnClickListener(new ButtonOnClickListener(this));
		ib_pause.setOnClickListener(new ButtonOnClickListener(this));
		ib_rec.setOnClickListener(new ButtonOnClickListener(this));
		
		/*----------------------------
		 * 3. SeekBar
			----------------------------*/
		tv_progress = (TextView) findViewById(R.id.main_tv_progress);
		
		sb = (SeekBar) findViewById(R.id.main_sb);
		
		
		
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO 自動生成されたメソッド・スタブ
				
				if (fromUser) {
					
					if (mp != null) {
						
						// Log
						Log.d("MainActivity.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "mp != null");
						
						
						int dur = mp.getDuration();
						
						// Log
						Log.d("MainActivity.java"
								+ "["
								+ Thread.currentThread().getStackTrace()[2]
										.getLineNumber() + "]", "dur: " + dur + " / progress: " + progress);
						
						
//						counter = dur * (int) ((float) progress / 100);
						
						counter = (int) (dur * ((float) progress / 100) / 1000);
						
						mp.seekTo((int) (dur * (float) progress / 100));
						
						
						
//						// Log
//						Log.d("MainActivity.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
//										.getLineNumber() + "]", "counter: " + counter);
//						
//						Log.d("MainActivity.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
////										.getLineNumber() + "]", "dur * (int) ((float) progress / 100): " + dur * (int) ((float) progress / 100));
//										.getLineNumber() + "]", "(int) dur * ((float) progress / 100): " + (int) dur * ((float) progress / 100));
//
//						Log.d("MainActivity.java"
//								+ "["
//								+ Thread.currentThread().getStackTrace()[2]
////										.getLineNumber() + "]", "dur * (int) ((float) progress / 100): " + dur * (int) ((float) progress / 100));
//										.getLineNumber() + "]", "(int) (dur * ((float) progress / 100) / 1000): " + (int) (dur * ((float) progress / 100) / 1000));

					}//if (mp != null)
					
					
//					tv_progress.setText(String.valueOf(progress));
					
//					int value = (int) (float) progress / 100 * max;
//					
////					tv_progress.setText(String.valueOf(value));
//					tv_progress.setText(String.valueOf(value + 1));
					
				}//if (fromUser)
//				tv_progress.setText(String.valueOf(progress));
				
			}//public void onProgressChanged
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO 自動生成されたメソッド・スタブ
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO 自動生成されたメソッド・スタブ
				
			}

				
		});//sb.setOnSeekBarChangeListener

		/*----------------------------
		 * 4. ListView => LongClick
			----------------------------*/
		ListView lv = this.getListView();
		
		lv.setTag(Methods.LongClickTags.main_activity_list);
		
		lv.setOnItemLongClickListener(new CustomOnItemLongClickListener(this));
	}//private void set_listeners()

	private void setup_buttons() {
		
		// Play
		ib_play.setEnabled(true);
		ib_play.setImageResource(R.drawable.cm_play_70x70_v3);

		// Pause
		ib_pause.setEnabled(false);
//		ib_pause.setImageResource(R.drawable.cm_simple_pause_70x70);
		ib_pause.setImageResource(R.drawable.cm_pause_dis_70x70);

		// Rec
		ib_rec.setEnabled(true);
		ib_rec.setImageResource(R.drawable.cm_simple_rec_70x70);

//		// Play
//		if (play_mode == Methods.ButtonModes.ON) {
//			
//			ib_play.setEnabled(true);
//			
//			ib_play.setImageResource(R.drawable.cm_play_70x70_v3);
//			
//		} else if (play_mode == Methods.ButtonModes.OFF) {//if (play_mode == true)
//			
//			ib_play.setEnabled(false);
//			ib_play.setImageResource(R.drawable.cm_play_dis_70x70);
//			
//		} else if (play_mode == Methods.ButtonModes.STOP) {//if (play_mode == true)
//			
//		}//if (play_mode == true)
//		
//		// Pause
//		if (pause_mode == Methods.ButtonModes.ON) {
//			
//			ib_pause.setEnabled(true);
//			
//			ib_pause.setImageResource(R.drawable.cm_pause_70x70);
//			
//		} else if (pause_mode == Methods.ButtonModes.OFF) {//if (pause_mode == true)
//			
//			ib_pause.setEnabled(false);
//			ib_pause.setImageResource(R.drawable.cm_pause_dis_70x70);
//			
//		}//if (pause_mode == true)
//		
//		// Rec
//		if (rec_mode == Methods.ButtonModes.ON) {
//			
//			ib_rec.setEnabled(true);
//			
//			ib_rec.setImageResource(R.drawable.cm_rec_70x70);
//			
//		} else if (rec_mode == Methods.ButtonModes.OFF) {//if (rec_mode == true)
//			
//			ib_rec.setEnabled(false);
//			ib_rec.setImageResource(R.drawable.cm_rec_dis_70x70);
//			
//		} else if (rec_mode == Methods.ButtonModes.STOP) {//if (rec_mode == true)
//			
//		}//if (rec_mode == true)
//
		
	}//private void setup_buttons()


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		vib.vibrate(Methods.vibLength_click);
		
		switch (item.getItemId()) {
		
		case R.id.main_opt_menu_refresh_db://---------------------------------------

			vib.vibrate(Methods.vibLength_click);
			
			RefreshDBTask task_ = new RefreshDBTask(this);
			
			task_.execute("Start");
			
			break;
			
		}//switch (item.getItemId())
		
		return super.onOptionsItemSelected(item);
		
	}//public boolean onOptionsItemSelected(MenuItem item)

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}
    
    
}//public class MainActv extends Activity
