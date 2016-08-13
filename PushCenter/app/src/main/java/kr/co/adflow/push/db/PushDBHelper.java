package kr.co.adflow.push.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.adflow.push.BuildConfig;
import kr.co.adflow.push.util.DebugLog;

/**
 * Created by nadir93 on 15. 1. 23..
 */
public class PushDBHelper extends SQLiteOpenHelper {

    // TAG for debug
    //public static final String TAG = "푸시디비헬퍼";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Nameß
    private static final String DATABASE_NAME = "PushDB";
    private static final String TABLE_MESSAGE = "message";
    //private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack",
    //        "broadcast", "acked", "broadcasted", "receivedate", "token"};
    private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack", "receivedate", "token"};
    private static final String TABLE_JOB = "job";
    private static final String[] JOB_COLUMNS = {"id", "type", "topic", "content"};
    private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public PushDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DebugLog.d("PushDBHelper 생성자 시작(context = " + context + ")");
        DebugLog.d("PushDBHelper 생성자 종료()");
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
        DebugLog.d("onCreate 시작(db = " + db + ")");
        // SQL statement to create user table
//        String CREATE_USER_TABLE = "CREATE TABLE user ( "
//                + "userid TEXT PRIMARY KEY, " + "password TEXT, "
//                + "tokenid TEXT, currentuser INTEGER)";
//        // create user table
//        db.execSQL(CREATE_USER_TABLE);

//        String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
//                + "id INTEGER, userid TEXT, ack INTEGER, "
//                + "type INTEGER, content TEXT, receivedate TEXT, category TEXT, read INTEGER, PRIMARY KEY (id, userid))";
//        // create message table
//        db.execSQL(CREATE_MESSAGE_TABLE);

//        String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
//                + "msgid TEXT, serviceid TEXT, topic TEXT, payload BLOB, qos INTEGER, ack INTEGER, "
//                + "broadcast INTEGER, acked INTEGER, broadcasted INTEGER, receivedate TEXT, token TEXT, PRIMARY KEY (msgid, serviceid))";

        String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
                + "msgid TEXT, serviceid TEXT, topic TEXT, payload BLOB, qos INTEGER, ack INTEGER, "
                + "receivedate TEXT, token TEXT, PRIMARY KEY (msgid, serviceid))";

        // create message table
        db.execSQL(CREATE_MESSAGE_TABLE);

        String CREATE_JOB_TABLE = "CREATE TABLE job ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, topic TEXT, content TEXT)";
        //create job table
        db.execSQL(CREATE_JOB_TABLE);

//        String CREATE_TOPIC_TABLE = "CREATE TABLE topic ( "
//                + "userid TEXT, topic TEXT, subscribe INTEGER, PRIMARY KEY(userid, topic))";
//        // create topic table
//        db.execSQL(CREATE_TOPIC_TABLE);

//        String CREATE_DEVICE_TABLE = "CREATE TABLE device ( "
//                + "id TEXT, PRIMARY KEY(id))";
//        // create device table
//        db.execSQL(CREATE_DEVICE_TABLE);

//        db.execSQL("insert into device values (\"" + androidID + "\")");

        DebugLog.d("onCreate 종료()");
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
        DebugLog.d("onUpgrade 시작(db = " + db + ", oldVersion = " + oldVersion
                + ", newVersion = " + newVersion + ")");
        // Drop older user table if existed
        //db.execSQL("DROP TABLE IF EXISTS user");
        // Drop older message table if existed
        db.execSQL("DROP TABLE IF EXISTS message");
        // Drop older job table if existed
        db.execSQL("DROP TABLE IF EXISTS job");

        // create fresh books table
        this.onCreate(db);
        DebugLog.d("onUpgrade 종료()");
    }

    public synchronized void testQuery() throws Exception {
        DebugLog.d("testQuery 시작()");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                    MESSAGE_COLUMNS, // b. column names
                    " datetime(receivedate) > datetime('2009-04-07  12:37:32') ", // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            int count = cursor.getCount();
            DebugLog.d("레코드 갯수 = " + count);

            if (count == 0) {
                DebugLog.d("testQuery 종료()");
                return;
            }

            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                DebugLog.d("msgid = " + cursor.getString(0));
                DebugLog.d("serviceid = " + cursor.getString(1));
                DebugLog.d("topic = " + cursor.getString(2));
                DebugLog.d("receivedate = " + cursor.getString(6));
                DebugLog.d("token = " + cursor.getString(7));
                i++;
                cursor.moveToNext();
            }

            // DebugLog
            DebugLog.d("testQuery 종료()");
        } finally {
            db.close();
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public synchronized Job[] getJobList() throws Exception {
        DebugLog.d("getJobList 시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_JOB, // a. table
                    JOB_COLUMNS, // b. column names
                    null, // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    BuildConfig.JOB_LIMIT_COUNT); // h. limit /* "600" */
            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            int count = cursor.getCount();
            DebugLog.d("해야 할 일 갯수 = " + count);

            if (count == 0) {
                DebugLog.d("getJobList 종료(해야 할 일이 없음)");
                return null;
            }

            Job[] job = new Job[count];
            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                job[i] = new Job();
                job[i].setId(cursor.getInt(0));
                job[i].setType(cursor.getInt(1));
                job[i].setTopic(cursor.getString(2));
                job[i].setContent(cursor.getString(3));
                DebugLog.d("job[" + i + "] = " + job[i]);
                i++;
                cursor.moveToNext();
            }

            // DebugLog
            DebugLog.d("getJobList 종료(job = " + job + ")");
            // 5. return req
            return job;
        } finally {
            db.close();
        }
    }

    /**
     * @param type
     * @param topic
     * @param content
     * @return
     * @throws Exception
     */
    public synchronized int addJob(int type, String topic, String content)
            throws Exception {
        DebugLog.d("addJob 시작(type = " + type + ", topic = " + topic + ", content = "
                + content + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = null;

        try {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("type", type);
            values.put("topic", topic);
            values.put("content", content);

            // 3. insert
            db.insertOrThrow(TABLE_JOB, // table
                    null, // nullColumnHack
                    values);
            result = db.rawQuery("select last_insert_rowid()", null);

            int id = 0;
            // result(Cursor 객체)가 비어 있으면 false 리턴
            if (result.moveToFirst()) {
                id = result.getInt(0);
            }

            DebugLog.d("addJob 종료(id = " + id + ")");
            return id;
        } finally {
            if (result != null) {
                result.close();
            }
            // 4. close
            db.close();
        }
    }

    /**
     * @param id
     * @return
     * @throws Exception
     */
    public synchronized Job getJob(int id) throws Exception {
        DebugLog.d("getJob 시작(id = " + id + ")");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_JOB, // a. table
                    JOB_COLUMNS, // b. column names
                    "id = ?", // c. selections
                    new String[]{String.valueOf(id)}, // d. selections args
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
            // DebugLog
            DebugLog.d("getJob 종료(job = " + job + ")");
            // 5. return job
            return job;
        } finally {
            db.close();
        }
    }

    /**
     * @param id
     */
    public synchronized void deteletJob(int id) {
        DebugLog.d("deteletJob 시작(id = " + id + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // 2. delete
            db.delete(TABLE_JOB, // table name
                    " id = ?", // selections
                    new String[]{String.valueOf(id)}); // selections
            // args
            // DebugLog
            DebugLog.d("deteletJob 종료()");
        } finally {
            // 3. close
            db.close();
        }
    }

    /**
     * @param msgID
     * @param serviceID
     * @param topic
     * @param payload
     * @param qos
     * @param ack
     * @param token
     * @throws Exception
     */
    public synchronized void addMessage(String msgID, String serviceID, String topic, byte[] payload, int qos, int ack, String token) throws Exception {
        DebugLog.d("addMessage 시작(msgID = " + msgID + ", serviceID = " + serviceID + ", topic = " + topic + ", payloadLength = " + payload.length + ", qos = " + qos + ", ack = " + ack + ", token = " + token + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("msgid", msgID);
            values.put("serviceid", serviceID);
            values.put("topic", topic);
            values.put("payload", payload);
            values.put("qos", qos);
            values.put("ack", ack);
            values.put("token", token);
            Date now = new Date();
            values.put("receivedate", sdf.format(now));

            // 3. insert
            db.insertOrThrow(TABLE_MESSAGE, // table
                    null, // nullColumnHack
                    values);
            DebugLog.d("addMessage 종료()");
        } finally {
            // 4. close
            db.close();
        }
    }

    /**
     * @param msgID
     * @return
     * @throws Exception
     */
    public synchronized Message getMessage(String msgID) throws Exception {
        DebugLog.d("getMessage 시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                    MESSAGE_COLUMNS, // b. column names
                    "msgid = ?", // c. selections
                    new String[]{msgID}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            // 4. build book object
            Message msg = new Message();
            msg.setMsgID(cursor.getString(0));
            msg.setServiceID(cursor.getString(1));
            msg.setTopic(cursor.getString(2));
            msg.setPayload(cursor.getBlob(3));
            msg.setQos(cursor.getInt(4));
            msg.setAck(cursor.getInt(5));
            msg.setReceivedate(cursor.getString(6));
            msg.setToken(cursor.getString(7));
            // DebugLog
            DebugLog.d("getMessage 종료(msg = " + msg + ")");
            // 5. return user
            return msg;
        } finally {
            db.close();
        }
    }

    /**
     * @throws Exception
     */
    public synchronized void deleteMessage(String msgid)
            throws Exception {
        DebugLog.d("deleteMessage 시작(msgid = " + msgid + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. delete
            db.delete(TABLE_MESSAGE, // table name
                    " msgid = ? ", // selections
                    new String[]{msgid}); // selections
            // args
            // DebugLog
            DebugLog.d("deleteMessage 종료()");
        } finally {
            db.close();
        }
    }

//    public synchronized Message[] getAllMessages() throws Exception {
//        DebugLog.d("getAllMessages시작()");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//        // 2. build query
//        Cursor cursor = db.query(TABLE_MESSAGE, // a. table
//                MESSAGE_COLUMNS, // b. column names
//                null, // c. selections
//                null, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null); // h. limit
//        // 3. if we got results get the first one
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        int count = cursor.getCount();
//        DebugLog.d("메시지레코드카운트 = " + count);
//
//        if (count == 0) {
//            DebugLog.d("getAllMessages종료(messages=null)");
//            return null;
//        }
//
//        Message[] msg = new Message[count];
//
//        int i = 0;
//        while (cursor.isAfterLast() == false) {
//            // 4. build book object
//            msg[i] = new Message();
//            msg[i].setId(cursor.getInt(0));
//            msg[i].setUserID(cursor.getString(1));
//            msg[i].setAck(cursor.getInt(2) != 0);
//            msg[i].setType(cursor.getInt(3));
//            msg[i].setContent(cursor.getString(4));
//            msg[i].setReceivedate(cursor.getString(5));
//            msg[i].setCategory(cursor.getString(6));
//            msg[i].setRead(cursor.getInt(7) != 0);
//            DebugLog.d("msg[" + i + "] = " + msg[i]);
//            i++;
//            cursor.moveToNext();
//        }
//
//        // DebugLog
//        DebugLog.d("getMessage종료(msg = " + msg + ")");
//        db.close();
//        // 5. return user
//        return msg;
//    }

//    /**
//     *
//     */
//    public synchronized Topic[] getTopic(String userid) throws Exception {
//        DebugLog.d("getTopic시작(userid = " + userid + ")");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//        // 2. build query
//        Cursor cursor = db.query(TABLE_TOPIC, // a. table
//                TOPIC_COLUMNS, // b. column names
//                "userid = ?", // c. selections
//                new String[]{String.valueOf(userid)}, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null); // h. limit
//        // 3. if we got results get the first one
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        int count = cursor.getCount();
//        DebugLog.d("토픽레코드카운트 = " + count);
//
//        if (count == 0) {
//            DebugLog.d("getTopic종료(topic=null)");
//            return null;
//        }
//
//        Topic[] topic = new Topic[count];
//
//        int i = 0;
//        while (cursor.isAfterLast() == false) {
//            // 4. build Topic object
//            topic[i] = new Topic();
//            topic[i].setUserid(cursor.getString(0));
//            topic[i].setTopic(cursor.getString(1));
//            topic[i].setSubscribe(cursor.getInt(2) != 0);
//
//            DebugLog.d("topic[" + i + "] = " + topic[i]);
//            i++;
//            cursor.moveToNext();
//        }
//
//        // DebugLog
//        DebugLog.d("getTopic종료(topic = " + topic + ")");
//        db.close();
//        // 5. return user
//        return topic;
//    }

//    /**
//     * @param userID
//     * @param topic
//     */
//    public synchronized void deleteTopic(String userID, String topic)
//            throws Exception {
//        DebugLog.d("deleteTopic시작(userID = " + userID + ", topic = " + topic + ")");
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//        // 2. delete
//        db.delete(TABLE_TOPIC, // table name
//                " userid = ? and topic = ?", // selections
//                new String[]{userID, topic}); // selections
//        // args
//        // 3. close
//        db.close();
//        // DebugLog
//        DebugLog.d("deleteTopic종료()");
//
//    }

//    /**
//     * @param userID
//     * @param topic
//     * @param subscribe
//     * @throws Exception
//     */
//    public synchronized void addTopic(String userID, String topic, int subscribe)
//            throws Exception {
//        DebugLog.d("addTopic시작(userID = " + userID + ", topic = " + topic + ")");
//
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // 2. create ContentValues to add key "column"/value
//        ContentValues values = new ContentValues();
//        values.put("userid", userID);
//        values.put("topic", topic);
//        values.put("subscribe", subscribe);
//
//        // 3. insert
//        db.insertOrThrow(TABLE_TOPIC, // table
//                null, // nullColumnHack
//                values);
//
//        // 4. close
//        db.close();
//        DebugLog.d("addTopic종료()");
//    }

    public synchronized void getMsgCount() {
        DebugLog.d("getMsgCount 시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                new String[]{"count(*)"}, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            // 4. build Topic object
            DebugLog.d("getMsgCount 종료(count = " + cursor.getString(0) + ")");
            cursor.moveToNext();
        }
        db.close();
    }

//    public synchronized int getUnreadCount() {
//        DebugLog.d("getUnreadCount시작()");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//        // 2. build query
//        Cursor cursor = db.query(TABLE_MESSAGE, // a. table
//                new String[]{"count(*)"}, // b. column names
//                " read = 1 and type < 100 ", // c. selections //안읽은 메시지 = 1 , 읽은
//                // 메시지 = 0
//                null, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null); // h. limit
//        // 3. if we got results get the first one
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        int count = 0;
//        while (cursor.isAfterLast() == false) {
//            count = Integer.parseInt(cursor.getString(0));
//            // 4. build Topic object
//            DebugLog.d("getUnreadCount종료(count = " + count + ")");
//            cursor.moveToNext();
//        }
//        return count;
//    }


}
