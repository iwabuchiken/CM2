package cm2.items;

public class FileItem {

//	int db_id;
	String file_name;
	String file_path;
	
	long date_added;
	long date_modified;
	
	long duration;
	
	String file_info;
	String memos;

	String located_at;
	
	/****************************************
	 * Constructors
	 ****************************************/
	public FileItem(
						String file_name, String file_path,
						long duration,
						long date_added, long date_modified, 
						String file_info, String memos,
						String located_at) {
		
		this.file_name = file_name;
		this.file_path = file_path;
		
		this.date_added = date_added; 
		this.date_modified = date_modified;
		
		this.duration = duration;
		
		this.file_info = file_info; 
		this.memos = memos;
		
	}//public FileItem()

	public FileItem(String file_name) {
	
		this.file_name = file_name;
	
	}//public FileItem()

	/****************************************
	 * Methods
	 ****************************************/
//	public int getDb_id() {
//		return db_id;
//	}

	public String getFile_name() {
		return file_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public long getDate_added() {
		return date_added;
	}

	public long getDate_modified() {
		return date_modified;
	}

	public String getFile_info() {
		return file_info;
	}

	public String getMemos() {
		return memos;
	}

	public long getDuration() {
		return duration;
	}

	public String getLocated_at() {
		return located_at;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setFile_info(String file_info) {
		this.file_info = file_info;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public void setLocated_at(String located_at) {
		this.located_at = located_at;
	}
	
}//public class FileItem
