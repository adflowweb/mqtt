package kr.co.adflow.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class PushDBHelper extends SQLiteOpenHelper {

	// TAG for debug
	public static final String TAG = "푸시디비헬퍼";

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "PushDB";
	private static final String TABLE_USER = "user";
	// user Table Columns names
	private static final String USER_ID = "userid";
	private static final String PASSWORD = "password";
	private static final String TOKEN_ID = "tokenid";
	private static final String CURRENT_USER = "currentuser";
	private static final String[] COLUMNS = { USER_ID, PASSWORD, TOKEN_ID,
			CURRENT_USER };

	public PushDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(TAG, "PushDBHelper생성자시작(context=" + context + ")");
		Log.d(TAG, "PushDBHelper생성자종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate시작(db=" + db + ")");
		// SQL statement to create user table
		String CREATE_USER_TABLE = "CREATE TABLE user ( "
				+ "userid TEXT PRIMARY KEY, " + "password TEXT, "
				+ "tokenid TEXT, currentuser INTEGER)";
		// create user table
		db.execSQL(CREATE_USER_TABLE);
		Log.d(TAG, "onCreate종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade시작(db=" + db + ", oldVersion=" + oldVersion
				+ ", newVersion=" + newVersion + ")");
		// Drop older user table if existed
		db.execSQL("DROP TABLE IF EXISTS user");

		// create fresh books table
		this.onCreate(db);
		Log.d(TAG, "onUpgrade종료()");
	}

	/**
	 * @param user
	 */
	public void addUser(User user) {
		Log.d(TAG, "addUser시작(user=" + user + ")");

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("userid", user.getUserID()); // get userid
		values.put("password", user.getPassword()); // get password
		values.put("tokenid", user.getTokenID()); // get tokenid
		values.put("currentuser", user.isCurrentUser() ? 1 : 0); // getcurrentuser

		// 3. insert
		db.insertOrThrow(TABLE_USER, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values
		// 4. close
		db.close();
		Log.d(TAG, "addUser종료()");
	}

	/**
	 * @param userid
	 * @return
	 */
	public User getUser(String userid) {
		Log.d(TAG, "getUser시작(userid=" + userid + ")");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query
		Cursor cursor = db.query(TABLE_USER, // a. table
				COLUMNS, // b. column names
				" userid = ?", // c. selections
				new String[] { userid }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();
		// 4. build book object
		User user = new User();
		user.setUserID(cursor.getString(0));
		user.setPassword(cursor.getString(1));
		user.setTokenID(cursor.getString(2));
		user.setCurrentUser(cursor.getInt(3) != 0);
		// log
		Log.d(TAG, "getUser종료(user=" + user + ")");
		// 5. return user
		return user;
	}

	/**
	 * @param userid
	 * @return
	 */
	public User getCurrentUser() throws Exception {
		Log.d(TAG, "getCurrentUser시작()");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query
		Cursor cursor = db.query(TABLE_USER, // a. table
				COLUMNS, // b. column names
				" currentuser = ?", // c. selections
				new String[] { "1" }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		// 4. build book object
		User user = new User();
		user.setUserID(cursor.getString(0));
		user.setPassword(cursor.getString(1));
		user.setTokenID(cursor.getString(2));
		user.setCurrentUser(cursor.getInt(3) != 0);
		// log
		Log.d(TAG, "getCurrentUser종료(user=" + user + ")");
		// 5. return user
		return user;
	}

	/**
	 * @param user
	 * @return
	 */
	public int updateUser(User user) {
		Log.d(TAG, "updateUser시작(user=" + user + ")");
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("userid", user.getUserID()); // get userid
		values.put("password", user.getPassword()); // get password
		values.put("tokenid", user.getTokenID()); // get tokenid
		values.put("currentuser", user.isCurrentUser() ? 1 : 0); // getcurrentuser

		// 3. updating row
		int i = db.update(TABLE_USER, // table
				values, // column/value
				USER_ID + " = ?", // selections
				new String[] { user.getUserID() }); // selection
													// args
		// 4. close
		db.close();
		Log.d(TAG, "updateUser종료(update=" + i + ")");
		return i;
	}

	/**
	 * @param user
	 */
	public void deleteUser(User user) {
		Log.d(TAG, "deleteUser시작(user=" + user + ")");
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. delete
		db.delete(TABLE_USER, // table name
				USER_ID + " = ?", // selections
				new String[] { user.getUserID() }); // selections
													// args
		// 3. close
		db.close();
		// log
		Log.d(TAG, "deleteUser종료()");
	}

}
