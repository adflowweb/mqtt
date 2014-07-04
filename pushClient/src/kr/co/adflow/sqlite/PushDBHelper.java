package kr.co.adflow.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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
	private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "PushDB";
	private static final String TABLE_USER = "user";
	private static final String TABLE_MESSAGE = "message";
	private static final String TABLE_JOB = "job";
	private static final String TABLE_TOPIC = "topic";
	// user Table Columns names
	private static final String USER_ID = "userid";
	private static final String PASSWORD = "password";
	private static final String TOKEN_ID = "tokenid";
	private static final String CURRENT_USER = "currentuser";
	private static final String[] USER_COLUMNS = { USER_ID, PASSWORD, TOKEN_ID,
			CURRENT_USER };
	private static final String[] MESSAGE_COLUMNS = { "id", "userid", "ack",
			"type", "content", "receivedate" };

	private static final String[] JOB_COLUMNS = { "id", "type", "topic",
			"content" };
	private static final String[] TOPIC_COLUMNS = { "userid", "topic",
			"subscribe" };

	// create table message ( id int unsigned not null auto_increment, sender
	// varchar(50), receiver varchar(50), issue datetime, issueSms datetime, sms
	// bool, timeout int, qos tinyint(1) unsigned, retained bool, reservation
	// datetime, type int unsigned,primary key(id)) engine=innodb default
	// charset=utf8;

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

		String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
				+ "id INTEGER, userid TEXT, ack INTEGER, "
				+ "type INTEGER, content TEXT, receivedate TEXT, PRIMARY KEY (id, userid))";
		// create message table
		db.execSQL(CREATE_MESSAGE_TABLE);

		String CREATE_JOB_TABLE = "CREATE TABLE job ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, topic TEXT, content TEXT)";
		// create job table
		db.execSQL(CREATE_JOB_TABLE);

		String CREATE_TOPIC_TABLE = "CREATE TABLE topic ( "
				+ "userid TEXT, topic TEXT, subscribe INTEGER, PRIMARY KEY(userid, topic))";
		// create topic table
		db.execSQL(CREATE_TOPIC_TABLE);

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
		// Drop older message table if existed
		db.execSQL("DROP TABLE IF EXISTS message");
		// Drop older job table if existed
		db.execSQL("DROP TABLE IF EXISTS job");

		// create fresh books table
		this.onCreate(db);
		Log.d(TAG, "onUpgrade종료()");
	}

	/**
	 * @param user
	 */
	public synchronized void addUser(User user) {
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
	public synchronized User getUser(String userid) {
		Log.d(TAG, "getUser시작(userid=" + userid + ")");
		User user = null;
		try {
			// 1. get reference to readable DB
			SQLiteDatabase db = this.getReadableDatabase();
			// 2. build query
			Cursor cursor = db.query(TABLE_USER, // a. table
					USER_COLUMNS, // b. column names
					" userid = ?", // c. selections
					new String[] { userid }, // d. selections args
					null, // e. group by
					null, // f. having
					null, // g. order by
					null); // h. limit

			Log.d(TAG, "rowCount=" + cursor.getCount());
			if (cursor.getCount() == 0) {
				return null;
			}
			// 3. if we got results get the first one
			if (cursor != null)
				cursor.moveToFirst();
			// 4. build book object
			user = new User();
			user.setUserID(cursor.getString(0));
			user.setPassword(cursor.getString(1));
			user.setTokenID(cursor.getString(2));
			user.setCurrentUser(cursor.getInt(3) != 0);
			// log
			Log.d(TAG, "getUser종료(user=" + user + ")");
			// 5. return user
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(TAG, "에러발생", e);
		}
		return user;
	}

	/**
	 * @param userid
	 * @return
	 */
	public synchronized User getCurrentUser() throws Exception {
		Log.d(TAG, "getCurrentUser시작()");
		User user = null;
		try {
			// 1. get reference to readable DB
			SQLiteDatabase db = this.getReadableDatabase();
			// 2. build query
			Cursor cursor = db.query(TABLE_USER, // a. table
					USER_COLUMNS, // b. column names
					" currentuser = ?", // c. selections
					new String[] { "1" }, // d. selections args
					null, // e. group by
					null, // f. having
					null, // g. order by
					null); // h. limit
			Log.d(TAG, "rowCount=" + cursor.getCount());
			if (cursor.getCount() == 0) {
				return null;
			}
			// 3. if we got results get the first one
			if (cursor != null)
				cursor.moveToFirst();

			// 4. build book object
			user = new User();
			user.setUserID(cursor.getString(0));
			user.setPassword(cursor.getString(1));
			user.setTokenID(cursor.getString(2));
			user.setCurrentUser(cursor.getInt(3) != 0);
			// log
			Log.d(TAG, "getCurrentUser종료(user=" + user + ")");

		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(TAG, "에러발생", e);
		}
		// 5. return user
		return user;
	}

	/**
	 * @param user
	 * @return
	 */
	public synchronized int updateUser(User user) {
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
	public synchronized void deleteUser(User user) {
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

	/**
	 * @param msg
	 * @throws JSONException
	 */
	public synchronized int addJob(int type, String topic, String content)
			throws Exception {
		Log.d(TAG, "addJob시작(type=" + type + ", topic=" + topic + ", content="
				+ content + ")");

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("type", type);
		values.put("topic", topic);
		values.put("content", content);

		// 3. insert
		db.insertOrThrow(TABLE_JOB, // table
				null, // nullColumnHack
				values);

		Cursor result = db.rawQuery("select last_insert_rowid()", null);

		int id = 0;
		// result(Cursor 객체)가 비어 있으면 false 리턴
		if (result.moveToFirst()) {
			id = result.getInt(0);
		}
		result.close();

		// 4. close
		db.close();
		Log.d(TAG, "addJob종료(id=" + id + ")");
		return id;
	}

	/**
	 * @param rst
	 * @return
	 */
	public synchronized Job getJob(int id) throws Exception {
		Log.d(TAG, "getJob시작()");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query
		Cursor cursor = db.query(TABLE_JOB, // a. table
				JOB_COLUMNS, // b. column names
				"id = ?", // c. selections
				new String[] { String.valueOf(id) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		// 4. build req object
		Job job = new Job();
		job.setId(cursor.getInt(0));
		job.setType(cursor.getInt(1));
		job.setTopic(cursor.getString(2));
		job.setContent(cursor.getString(3));
		// log
		Log.d(TAG, "getJob종료(job=" + job + ")");
		// 5. return job
		return job;
	}

	/**
	 * @param id
	 */
	public synchronized void deteletJob(int id) {
		Log.d(TAG, "deteletJob시작(id=" + id + ")");
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. delete
		db.delete(TABLE_JOB, // table name
				" id = ?", // selections
				new String[] { String.valueOf(id) }); // selections
														// args
		// 3. close
		db.close();
		// log
		Log.d(TAG, "deteletJob종료()");
	}

	/**
	 * @param rst
	 * @return
	 */
	public Job[] getJobList() throws Exception {
		Log.d(TAG, "getJobList시작()");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query

		// Cursor cursor =
		// db.rawQuery("select * from request where senddate is null", null);

		Cursor cursor = db.query(TABLE_JOB, // a. table
				JOB_COLUMNS, // b. column names
				null, // c. selections
				null, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		int count = cursor.getCount();
		Log.d(TAG, "잡레코드카운트=" + count);

		if (count == 0) {
			Log.d(TAG, "getJobList종료(req=null)");
			return null;
		}

		Job[] req = new Job[count];

		int i = 0;
		while (cursor.isAfterLast() == false) {
			// 4. build req object
			req[i] = new Job();
			req[i].setId(cursor.getInt(0));
			req[i].setType(cursor.getInt(1));
			req[i].setTopic(cursor.getString(2));
			req[i].setContent(cursor.getString(3));
			Log.d(TAG, "req[" + i + "]=" + req[i]);
			i++;
			cursor.moveToNext();
		}

		// log
		Log.d(TAG, "getJobList종료(req=" + req + ")");
		// 5. return req
		return req;
	}

	public synchronized void addMessage(String userID, JSONObject msg)
			throws Exception {
		Log.d(TAG, "addMessage시작(msg=" + msg + ")");

		// messageArrived시작(토픽=/users/nadir93,메시지={"id":6,"ack":true,"type":0,"content":
		// {"notification":{"notificationStyle":1,"contentTitle":"교육장소공지","contentText":"교육장소공지입니다.",
		// "ticker":"부산은행교육장소알림장소: 수림연수원 시간: 3월 22일 오전: 12시","summaryText":"장소: 수림연수원 시간: 3월 22일 오전:1시",
		// "image":""} } },qos=1)

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("userid", userID);
		values.put("id", msg.getInt("id"));
		values.put("ack", msg.getBoolean("ack") ? 1 : 0);
		values.put("type", msg.getInt("type"));
		Date now = new Date();
		values.put("receivedate", sdf.format(now));
		values.put("content", msg.getString("content"));

		// 3. insert
		db.insertOrThrow(TABLE_MESSAGE, // table
				null, // nullColumnHack
				values);

		// 4. close
		db.close();
		Log.d(TAG, "addMessage종료()");
	}

	public synchronized Message getMessage(int id) throws Exception {
		Log.d(TAG, "getMessage시작()");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query
		Cursor cursor = db.query(TABLE_MESSAGE, // a. table
				MESSAGE_COLUMNS, // b. column names
				"id = ?", // c. selections
				new String[] { String.valueOf(id) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		// 4. build book object
		Message msg = new Message();
		msg.setId(cursor.getInt(0));
		msg.setUserID(cursor.getString(1));
		msg.setAck(cursor.getInt(2) != 0);
		msg.setType(cursor.getInt(3));
		msg.setContent(cursor.getString(4));
		msg.setReceivedate(cursor.getString(5));
		// log
		Log.d(TAG, "getMessage종료(msg=" + msg + ")");
		// 5. return user
		return msg;
	}

	public synchronized Message[] getAllMessages() throws Exception {
		Log.d(TAG, "getAllMessages시작()");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query
		Cursor cursor = db.query(TABLE_MESSAGE, // a. table
				MESSAGE_COLUMNS, // b. column names
				null, // c. selections
				null, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		int count = cursor.getCount();
		Log.d(TAG, "메시지레코드카운트=" + count);

		if (count == 0) {
			Log.d(TAG, "getAllMessages종료(messages=null)");
			return null;
		}

		Message[] msg = new Message[count];

		int i = 0;
		while (cursor.isAfterLast() == false) {
			// 4. build book object
			msg[i] = new Message();
			msg[i].setId(cursor.getInt(0));
			msg[i].setUserID(cursor.getString(1));
			msg[i].setAck(cursor.getInt(2) != 0);
			msg[i].setType(cursor.getInt(3));
			msg[i].setContent(cursor.getString(4));
			msg[i].setReceivedate(cursor.getString(5));
			Log.d(TAG, "msg[" + i + "]=" + msg[i]);
			i++;
			cursor.moveToNext();
		}

		// log
		Log.d(TAG, "getMessage종료(msg=" + msg + ")");
		// 5. return user
		return msg;
	}

	/**
	 * 
	 */
	public synchronized Topic[] getTopic(String userid) throws Exception {
		Log.d(TAG, "getTopic시작(userid=" + userid + ")");
		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		// 2. build query
		Cursor cursor = db.query(TABLE_TOPIC, // a. table
				TOPIC_COLUMNS, // b. column names
				"userid = ?", // c. selections
				new String[] { String.valueOf(userid) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		int count = cursor.getCount();
		Log.d(TAG, "토픽레코드카운트=" + count);

		if (count == 0) {
			Log.d(TAG, "getTopic종료(topic=null)");
			return null;
		}

		Topic[] topic = new Topic[count];

		int i = 0;
		while (cursor.isAfterLast() == false) {
			// 4. build Topic object
			topic[i] = new Topic();
			topic[i].setUserid(cursor.getString(0));
			topic[i].setTopic(cursor.getString(1));
			topic[i].setSubscribe(cursor.getInt(2) != 0);

			Log.d(TAG, "topic[" + i + "]=" + topic[i]);
			i++;
			cursor.moveToNext();
		}

		// log
		Log.d(TAG, "getTopic종료(topic=" + topic + ")");
		// 5. return user
		return topic;
	}

	/**
	 * @param userID
	 * @param topic
	 */
	public synchronized void deleteTopic(String userID, String topic)
			throws Exception {
		Log.d(TAG, "deleteTopic시작(userID=" + userID + ", topic=" + topic + ")");
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		// 2. delete
		db.delete(TABLE_TOPIC, // table name
				" userid = ? and topic = ?", // selections
				new String[] { userID, topic }); // selections
													// args
		// 3. close
		db.close();
		// log
		Log.d(TAG, "deleteTopic종료()");

	}

	/**
	 * @param userID
	 * @param topic
	 * @param i
	 */
	public synchronized void addTopic(String userID, String topic, int subscribe)
			throws Exception {
		Log.d(TAG, "addTopic시작(userID=" + userID + ", topic=" + topic + ")");

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put("userid", userID);
		values.put("topic", topic);
		values.put("subscribe", subscribe);

		// 3. insert
		db.insertOrThrow(TABLE_TOPIC, // table
				null, // nullColumnHack
				values);

		// 4. close
		db.close();
		Log.d(TAG, "addTopic종료()");
	}
}
