package cm2.items;

public class FileItem {

//	int db_id;
	String file_name;
	String file_path;
	String table_name;
	
	String memo;
	String genre;

	long duration;

	long registered_at;
	long modified_at;
	
	
	/****************************************
	 * Constructors
	 ****************************************/
	public FileItem(
						String file_name, String file_path, String table_name,
						
						String memo, String genre,

						long duration, long registered_at, long modified_at) {
		
		this.file_name = file_name;
		this.file_path = file_path;
		
		this.table_name = table_name; 
		this.memo = memo;
		
		this.genre = genre; 

		this.duration = duration;
		
		this.registered_at = registered_at;
		this.modified_at = modified_at;
		
	}//public FileItem()

	public FileItem(String file_name) {
	
		this.file_name = file_name;
	
	}//public FileItem()

	public String getFile_name() {
		return file_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public String getTable_name() {
		return table_name;
	}

	public String getMemo() {
		return memo;
	}

	public String getGenre() {
		return genre;
	}

	public long getDuration() {
		return duration;
	}

	public long getRegistered_at() {
		return registered_at;
	}

	public long getModified_at() {
		return modified_at;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setModified_at(long modified_at) {
		this.modified_at = modified_at;
	}

	/****************************************
	 * Methods
	 ****************************************/
//	public int getDb_id() {
//		return db_id;
//	}

	
}//public class FileItem
