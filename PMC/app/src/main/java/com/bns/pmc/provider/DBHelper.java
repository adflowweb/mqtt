package com.bns.pmc.provider;

import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.provider.DataColumn.ValidationColumn;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "pushmsg.db";
	private static final int DB_VER = 1;
	
	private static final String sql_create_MESSAGE =
			"CREATE TABLE \"" + MessageColumn.DB_TABLE_MESSAGE +"\"" +
			"(" +
			" \"" + MessageColumn.DB_COLUMN_ID + /*_id*/"\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
			",\"" + MessageColumn.DB_COLUMN_MSG_ID + "\" TEXT" +
			",\"" + MessageColumn.DB_COLUMN_TOKEN +"\" TEXT" +
			",\"" + MessageColumn.DB_COLUMN_NUMBER + "\" TEXT" +
			",\"" + MessageColumn.DB_COLUMN_GROUP_MEMBER + "\" TEXT" + // Number가 Group번호일 경우 여기에 Sender를 넣어준다.
			",\"" + MessageColumn.DB_COLUMN_NUMBER_TYPE + "\" NUMERIC NOT NULL DEFAULT 0" + // Default PTT.
			",\"" + MessageColumn.DB_COLUMN_MSG + "\" TEXT" +
			",\"" + MessageColumn.DB_COLUMN_URL + "\" TEXT" +
			",\"" + MessageColumn.DB_COLUMN_DATA + "\" BLOB" +
			",\"" + MessageColumn.DB_COLUMN_DATA_TYPE + "\" NUMERIC NOT NULL DEFAULT 0" + // Default None.
			",\"" + MessageColumn.DB_COLUMN_RECV + "\" NUMERIC NOT NULL DEFAULT 1" + // Default Receive.
			",\"" + MessageColumn.DB_COLUMN_DONT_READ + "\" NUMERIC NOT NULL DEFAULT 1" + // Default Don't Read state.
			",\"" + MessageColumn.DB_COLUMN_ACK + "\" NUMERIC NOT NULL DEFAULT 0" + // Default Ack.
			",\"" + MessageColumn.DB_COLUMN_STATE + "\" NUMERIC NOT NULL DEFAULT 2" + // Default Success.
			",\"" + MessageColumn.DB_COLUMN_RESEND_COUNT + "\" NUMERIC NOT NULL DEFAULT 0" + // Default ResendCount 0.
			",\"" + MessageColumn.DB_COLUMN_CREATETIME + "\" REAL " +
			// 16Column.
			//",\"createtime\" TIMESTAMP DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')) " +
			");";
	
	private static final String sql_create_VALIDATION =
			"CREATE TABLE \"" + ValidationColumn.DB_TABLE_VALIDATION +"\"" +
			"(" +
			" \"" + ValidationColumn.DB_COLUMN_ID + "\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
			",\"" + MessageColumn.DB_COLUMN_NUMBER + "\" TEXT" +
			",\"" + MessageColumn.DB_COLUMN_CREATETIME + "\" REAL " +
			");";
	
	private static final String slq_drop_pushMsg =
			"DROP TABLE IF EXISTS " + DB_NAME;
	
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 생성된 DB가 없을 경우에 한번만 호출됨
		Log.d(PMCType.TAG, "DB Create");
		db.execSQL(sql_create_MESSAGE);
		db.execSQL(sql_create_VALIDATION);
		//insert_Message_test(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(PMCType.TAG, "DB Upgreade= "+ oldVersion +" " + newVersion);
		db.execSQL(slq_drop_pushMsg);
	}

	private void insert_Message_test(SQLiteDatabase db)
	{
		/*
		try {
			for (int i=0; i < 1000; i++)
			{
				ContentValues values = new ContentValues();
				values.put("phone", i);
				values.put("msg", "안녕하세요 " + i);
				values.put("createtime", System.currentTimeMillis());
				long result = db.insert(DB_TABLE_MESSAGE, null, values);
				//Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			
		}
				*/
		try {
			/*
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "200*433");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "PTT 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				values.put(DataColumn.Message.DB_COLUMN_RECV, "0");
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "200*433");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "PTT1 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				values.put(DataColumn.Message.DB_COLUMN_RECV, "0");
				values.put(DataColumn.Message.DB_COLUMN_STATE,"0");
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "200*433");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "PTT2 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				values.put(DataColumn.Message.DB_COLUMN_RECV, "1");
				//values.put(DataColumn.Message.DB_COLUMN_DONT_READ, "0");
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}*/
			
			
			
/*			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "200*433");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "PTT 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				values.put(DataColumn.Message.DB_COLUMN_RECV, "0");
				values.put(DataColumn.Message.DB_COLUMN_DONT_READ, "0");
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "200*433");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "안녕하세요 DB Test1 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values);
				Thread.sleep(1000);
			}
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "50*1209");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "안녕하세요 DB Test2 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				//values.put("read", "1");
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values);
				Thread.sleep(1000);				
			}
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "50*1209");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "반갑습니다. DB Test3 입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				//values.put("read", "0");
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);				
			}
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.Message.DB_COLUMN_PTT, "#81");
				values.put(DataColumn.Message.DB_COLUMN_GROUP_PTT, "200*433");
				values.put(DataColumn.Message.DB_COLUMN_MSG, "그룹 test1입니다.");
				values.put(DataColumn.Message.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				long result = db.insert(DataColumn.Message.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}*/
			
			/*{
				ContentValues values = new ContentValues();
				values.put(DataColumn.MessageColumn.DB_COLUMN_NUMBER, "#81");
				values.put(DataColumn.MessageColumn.DB_COLUMN_GROUP_MEMBER, "50*1209");
				values.put(DataColumn.MessageColumn.DB_COLUMN_MSG, "그룹 test2입니다.");
				values.put(DataColumn.MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				long result = db.insert(DataColumn.MessageColumn.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}*/
			
			{
				ContentValues values = new ContentValues();
				values.put(DataColumn.MessageColumn.DB_COLUMN_NUMBER, "관제");
				values.put(DataColumn.MessageColumn.DB_COLUMN_NUMBER_TYPE, DataColumn.COLUMN_NUMBER_TYPE_NORMAL);
				values.put(DataColumn.MessageColumn.DB_COLUMN_MSG, "01091278409\n링크:www.naver.com 입니다.\n200*135");
				values.put(DataColumn.MessageColumn.DB_COLUMN_NUMBER_TYPE, DataColumn.COLUMN_NUMBER_TYPE_CONTROL);
				values.put(DataColumn.MessageColumn.DB_COLUMN_CREATETIME, System.currentTimeMillis());
				long result = db.insert(DataColumn.MessageColumn.DB_TABLE_MESSAGE, null, values); 
				Thread.sleep(1000);			
			}

		} catch (Exception e) {
			
		}
	}
}
