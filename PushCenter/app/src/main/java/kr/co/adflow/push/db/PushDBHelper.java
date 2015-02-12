package kr.co.adflow.push.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nadir93 on 15. 1. 23..
 */
public class PushDBHelper extends SQLiteOpenHelper {

    // TAG for debug
    public static final String TAG = "푸시디비헬퍼";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Nameß
    private static final String DATABASE_NAME = "PushDB";
    private static final String TABLE_MESSAGE = "message";
    //    private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack",
//            "broadcast", "acked", "broadcasted", "receivedate", "token"};
    private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack", "receivedate"};
    private static final String TABLE_JOB = "message";
    private static final String[] JOB_COLUMNS = {"id", "type", "topic", "content"};
    private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

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

        String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
                + "msgid TEXT, serviceid TEXT, topic TEXT, payload BLOB, qos INTEGER, ack INTEGER, "
                + "broadcast INTEGER, acked INTEGER, broadcasted INTEGER, receivedate TEXT, token TEXT, PRIMARY KEY (msgid, serviceid))";
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
        //db.execSQL("DROP TABLE IF EXISTS user");
        // Drop older message table if existed
        db.execSQL("DROP TABLE IF EXISTS message");
        // Drop older job table if existed
        db.execSQL("DROP TABLE IF EXISTS job");

        // create fresh books table
        this.onCreate(db);
        Log.d(TAG, "onUpgrade종료()");
    }

//    /**
//     * @param user
//     */
//    public synchronized void addUser(User user) {
//        Log.d(TAG, "addUser시작(user=" + user + ")");
//
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // 2. create ContentValues to add key "column"/value
//        ContentValues values = new ContentValues();
//        values.put("userid", user.getUserID()); // get userid
//        values.put("password", user.getPassword()); // get password
//        values.put("tokenid", user.getTokenID()); // get tokenid
//        values.put("currentuser", user.isCurrentUser() ? 1 : 0); // getcurrentuser
//
//        // 3. insert
//        db.insertOrThrow(TABLE_USER, // table
//                null, // nullColumnHack
//                values); // key/value -> keys = column names/ values = column
//        // values
//        // 4. close
//        db.close();
//        Log.d(TAG, "addUser종료()");
//    }

//    /**
//     * @param userid
//     * @return
//     */
//    public synchronized User getUser(String userid) {
//        Log.d(TAG, "getUser시작(userid=" + userid + ")");
//        User user = null;
//        try {
//            // 1. get reference to readable DB
//            SQLiteDatabase db = this.getReadableDatabase();
//            // 2. build query
//            Cursor cursor = db.query(TABLE_USER, // a. table
//                    USER_COLUMNS, // b. column names
//                    " userid = ?", // c. selections
//                    new String[]{userid}, // d. selections args
//                    null, // e. group by
//                    null, // f. having
//                    null, // g. order by
//                    null); // h. limit
//
//            Log.d(TAG, "rowCount=" + cursor.getCount());
//            if (cursor.getCount() == 0) {
//                return null;
//            }
//            // 3. if we got results get the first one
//            if (cursor != null)
//                cursor.moveToFirst();
//            // 4. build book object
//            user = new User();
//            user.setUserID(cursor.getString(0));
//            user.setPassword(cursor.getString(1));
//            user.setTokenID(cursor.getString(2));
//            user.setCurrentUser(cursor.getInt(3) != 0);
//            // log
//            Log.d(TAG, "getUser종료(user=" + user + ")");
//            db.close();
//            // 5. return user
//        } catch (CursorIndexOutOfBoundsException e) {
//            Log.e(TAG, "에러발생", e);
//        }
//        return user;
//    }

//    /**
//     * @return
//     * @throws Exception
//     */
//    public synchronized User getCurrentUser() throws Exception {
//        Log.d(TAG, "getCurrentUser시작()");
//        User user = null;
//        try {
//            // 1. get reference to readable DB
//            SQLiteDatabase db = this.getReadableDatabase();
//            // 2. build query
//            Cursor cursor = db.query(TABLE_USER, // a. table
//                    USER_COLUMNS, // b. column names
//                    " currentuser = ?", // c. selections
//                    new String[]{"1"}, // d. selections args
//                    null, // e. group by
//                    null, // f. having
//                    null, // g. order by
//                    null); // h. limit
//            Log.d(TAG, "rowCount=" + cursor.getCount());
//            if (cursor.getCount() == 0) {
//                return null;
//            }
//            // 3. if we got results get the first one
//            if (cursor != null)
//                cursor.moveToFirst();
//
//            // 4. build book object
//            user = new User();
//            user.setUserID(cursor.getString(0));
//            user.setPassword(cursor.getString(1));
//            user.setTokenID(cursor.getString(2));
//            user.setCurrentUser(cursor.getInt(3) != 0);
//            // log
//            Log.d(TAG, "getCurrentUser종료(user=" + user + ")");
//            db.close();
//
//        } catch (CursorIndexOutOfBoundsException e) {
//            Log.e(TAG, "에러발생", e);
//        }
//        // 5. return user
//        return user;
//    }

//    /**
//     * @param user
//     * @return
//     */
//    public synchronized int updateUser(User user) {
//        Log.d(TAG, "updateUser시작(user=" + user + ")");
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//        // 2. create ContentValues to add key "column"/value
//        ContentValues values = new ContentValues();
//        values.put("userid", user.getUserID()); // get userid
//        values.put("password", user.getPassword()); // get password
//        values.put("tokenid", user.getTokenID()); // get tokenid
//        values.put("currentuser", user.isCurrentUser() ? 1 : 0); // getcurrentuser
//
//        // 3. updating row
//        int i = db.update(TABLE_USER, // table
//                values, // column/value
//                USER_ID + " = ?", // selections
//                new String[]{user.getUserID()}); // selection
//        // args
//        // 4. close
//        db.close();
//        Log.d(TAG, "updateUser종료(update=" + i + ")");
//        return i;
//    }

//    /**
//     * @param user
//     */
//    public synchronized void deleteUser(User user) {
//        Log.d(TAG, "deleteUser시작(user=" + user + ")");
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//        // 2. delete
//        db.delete(TABLE_USER, // table name
//                USER_ID + " = ?", // selections
//                new String[]{user.getUserID()}); // selections
//        // args
//        // 3. close
//        db.close();
//        // log
//        Log.d(TAG, "deleteUser종료()");
//    }

    /**
     * @param type
     * @param topic
     * @param content
     * @return
     * @throws Exception
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

//    /**
//     * @param id
//     * @return
//     * @throws Exception
//     */
//    public synchronized Job getJob(int id) throws Exception {
//        Log.d(TAG, "getJob시작()");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//        // 2. build query
//        Cursor cursor = db.query(TABLE_JOB, // a. table
//                JOB_COLUMNS, // b. column names
//                "id = ?", // c. selections
//                new String[]{String.valueOf(id)}, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null); // h. limit
//        // 3. if we got results get the first one
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // 4. build req object
//        Job job = new Job();
//        job.setId(cursor.getInt(0));
//        job.setType(cursor.getInt(1));
//        job.setTopic(cursor.getString(2));
//        job.setContent(cursor.getString(3));
//        // log
//        Log.d(TAG, "getJob종료(job=" + job + ")");
//        db.close();
//        // 5. return job
//        return job;
//    }

//    /**
//     * @param id
//     */
//    public synchronized void deteletJob(int id) {
//        Log.d(TAG, "deteletJob시작(id=" + id + ")");
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//        // 2. delete
//        db.delete(TABLE_JOB, // table name
//                " id = ?", // selections
//                new String[]{String.valueOf(id)}); // selections
//        // args
//        // 3. close
//        db.close();
//        // log
//        Log.d(TAG, "deteletJob종료()");
//    }

//    /**
//     * @return
//     * @throws Exception
//     */
//    public Job[] getJobList() throws Exception {
//        Log.d(TAG, "getJobList시작()");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//        // 2. build query
//
//        // Cursor cursor =
//        // db.rawQuery("select * from request where senddate is null", null);
//
//        Cursor cursor = db.query(TABLE_JOB, // a. table
//                JOB_COLUMNS, // b. column names
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
//        Log.d(TAG, "잡레코드카운트=" + count);
//
//        if (count == 0) {
//            Log.d(TAG, "getJobList종료(req=null)");
//            return null;
//        }
//
//        Job[] req = new Job[count];
//
//        int i = 0;
//        while (cursor.isAfterLast() == false) {
//            // 4. build req object
//            req[i] = new Job();
//            req[i].setId(cursor.getInt(0));
//            req[i].setType(cursor.getInt(1));
//            req[i].setTopic(cursor.getString(2));
//            req[i].setContent(cursor.getString(3));
//            Log.d(TAG, "req[" + i + "]=" + req[i]);
//            i++;
//            cursor.moveToNext();
//        }
//
//        // log
//        Log.d(TAG, "getJobList종료(req=" + req + ")");
//        db.close();
//        // 5. return req
//        return req;
//    }

    public synchronized void addMessage(String msgID, String serviceID, String topic, byte[] payload, int qos, int ack, int broadcast, String token) throws Exception {
        Log.d(TAG, "addMessage시작(msgID=" + msgID + ", serviceID=" + serviceID + ", topic=" + topic + ", payloadLength=" + payload.length + ", qos=" + qos + ", ack=" + ack + ")");
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
            values.put("broadcast", broadcast);
            values.put("acked", 0);
            values.put("broadcasted", 0);
            values.put("token", token);

//            if (serviceID.equals("kr.co.adflow.push.pma")) {
//                values.put("broadcast", 0);
//            } else {
//                values.put("broadcast", 1); // 브로드캐스팅해야함
//            }

            // values.put("status", 0); // default 0
            Date now = new Date();
            values.put("receivedate", sdf.format(now));

            // 3. insert
            db.insertOrThrow(TABLE_MESSAGE, // table
                    null, // nullColumnHack
                    values);
            Log.d(TAG, "addMessage종료()");
        } finally {
            // 4. close
            db.close();
        }
    }

    public synchronized void updateAcked(String serviceID, String msgID) throws Exception {
        Log.d(TAG, "updateAcked시작(serviceID=" + serviceID + ", msgID=" + msgID + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_MESSAGE + " SET "
                    + " acked = 1 WHERE "
                    + " serviceid  = '" + serviceID + "' and id = '" + msgID + "' ");
            Log.d(TAG, "updateAcked종료()");
        } finally {
            // 4. close
            db.close();
        }
    }

    public synchronized void updateBroadCast(String serviceID, String msgID) throws Exception {
        Log.d(TAG, "updateBroadCast시작(serviceID=" + serviceID + ", msgID=" + msgID + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_MESSAGE + " SET "
                    + " broadcast = 1 WHERE "
                    + " serviceid  = '" + serviceID + "' and id = '" + msgID + "' ");
            Log.d(TAG, "updateBroadCast종료()");
        } finally {
            // 4. close
            db.close();
        }

//        try {
//            db.execSQL("UPDATE " + TABLE_MESSAGE + " SET "
//                    + " status = status + 10 WHERE "
//                    + " serviceid  = " + serviceID + " and id = " + msgID + " ");
//            Log.d(TAG, "updateBroadCast종료()");
//        } finally {
//            // 4. close
//            db.close();
//        }
    }

    public synchronized void updateBroadCasted(String serviceID, String msgID) throws Exception {
        Log.d(TAG, "updateBroadCasted시작(serviceID=" + serviceID + ", msgID=" + msgID + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_MESSAGE + " SET "
                    + " broadcasted = 1 WHERE "
                    + " serviceid  = '" + serviceID + "' and id = '" + msgID + "' ");
            Log.d(TAG, "updateBroadCasted종료()");
        } finally {
            // 4. close
            db.close();
        }
    }

//    public synchronized void addMessage(String userID, JSONObject msg)
//            throws Exception {
//        Log.d(TAG, "addMessage시작(msg=" + msg + ")");
//
//        // messageArrived시작(토픽=/users/nadir93,메시지={"id":6,"ack":true,"type":0,"content":
//        // {"notification":{"notificationStyle":1,"contentTitle":"교육장소공지","contentText":"교육장소공지입니다.",
//        // "ticker":"부산은행교육장소알림장소: 수림연수원 시간: 3월 22일 오전: 12시","summaryText":"장소: 수림연수원 시간: 3월 22일 오전:1시",
//        // "image":""} } },qos=1)
//
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // 2. create ContentValues to add key "column"/value
//        ContentValues values = new ContentValues();
//        values.put("userid", userID);
//        values.put("id", msg.getInt("id"));
//        values.put("ack", msg.getBoolean("ack") ? 1 : 0);
//        values.put("type", msg.getInt("type"));
//        Date now = new Date();
//        values.put("receivedate", sdf.format(now));
//        values.put("content", msg.getString("content"));
//        if (msg.has("category")) {
//            values.put("category", msg.getString("category"));
//        }
//        values.put("read", 1); // 찬호 요청
//
//        // 3. insert
//        db.insertOrThrow(TABLE_MESSAGE, // table
//                null, // nullColumnHack
//                values);
//
//        // 4. close
//        db.close();
//        Log.d(TAG, "addMessage종료()");
//    }

//    /**
//     * @return
//     * @throws Exception
//     */
//    public Job[] getJobList() throws Exception {
//        Log.d(TAG, "getJobList시작()");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        try {
//            // 2. build query
//            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
//                    MESSAGE_COLUMNS, // b. column names
//                    null, // c. selections
//                    null, // d. selections args
//                    null, // e. group by
//                    null, // f. having
//                    null, // g. order by
//                    null); // h. limit
//            // 3. if we got results get the first one
//            if (cursor != null)
//                cursor.moveToFirst();
//
//            int count = cursor.getCount();
//            Log.d(TAG, "잡레코드카운트=" + count);
//
//            if (count == 0) {
//                Log.d(TAG, "getJobList종료(req=null)");
//                return null;
//            }
//
//            Job[] job = new Job[count];
//            int i = 0;
//            while (cursor.isAfterLast() == false) {
//                // 4. build req object
//                job[i] = new Job();
//                job[i].setId(cursor.getInt(0));
//                job[i].setType(cursor.getInt(1));
//                job[i].setTopic(cursor.getString(2));
//                job[i].setContent(cursor.getString(3));
//                Log.d(TAG, "job[" + i + "]=" + job[i]);
//                i++;
//                cursor.moveToNext();
//            }
//
//            // log
//            Log.d(TAG, "getJobList종료(job=" + job + ")");
//            // 5. return req
//            return job;
//        } finally {
//            db.close();
//        }
//    }

    /**
     * @return
     * @throws Exception
     */
    public Message[] getJobList() throws Exception {
        Log.d(TAG, "getJobList시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // 2. build query
            // Cursor cursor =
            // db.rawQuery("select * from request where senddate is null", null);

            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                    MESSAGE_COLUMNS, // b. column names
                    " ack != acked or broadcast != broadcasted ", // c. selections // 에크작업이나 브로드캐스팅작업이 완료안된것만 리스트업
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

            Message[] req = new Message[count];

            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                req[i] = new Message();
                req[i].setMsgID(cursor.getString(0));
                req[i].setServiceID(cursor.getString(1));
                req[i].setTopic(cursor.getString(2));
                req[i].setPayload(cursor.getBlob(3));
                req[i].setQos(cursor.getInt(4));
                req[i].setAck(cursor.getInt(5));
                req[i].setBroadcast(cursor.getInt(6));
                req[i].setAcked(cursor.getInt(7));
                req[i].setBroadcasted(cursor.getInt(8));
                req[i].setReceivedate(cursor.getString(9));
                req[i].setToken(cursor.getString(10));

                Log.d(TAG, "req[" + i + "]=" + req[i]);
                i++;
                cursor.moveToNext();
            }

            // log
            Log.d(TAG, "getJobList종료(req=" + req + ")");
            // 5. return req
            return req;
        } finally {
            db.close();
        }
    }


//    public synchronized Message getMessage(int id) throws Exception {
//        Log.d(TAG, "getMessage시작()");
//        // 1. get reference to readable DB
//        SQLiteDatabase db = this.getReadableDatabase();
//        // 2. build query
//        Cursor cursor = db.query(TABLE_MESSAGE, // a. table
//                MESSAGE_COLUMNS, // b. column names
//                "id = ?", // c. selections
//                new String[]{String.valueOf(id)}, // d. selections args
//                null, // e. group by
//                null, // f. having
//                null, // g. order by
//                null); // h. limit
//        // 3. if we got results get the first one
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // 4. build book object
//        Message msg = new Message();
//        msg.setId(cursor.getInt(0));
//        msg.setUserID(cursor.getString(1));
//        msg.setAck(cursor.getInt(2) != 0);
//        msg.setType(cursor.getInt(3));
//        msg.setContent(cursor.getString(4));
//        msg.setReceivedate(cursor.getString(5));
//        msg.setCategory(cursor.getString(6));
//        msg.setRead(cursor.getInt(7) != 0);
//        // log
//        Log.d(TAG, "getMessage종료(msg=" + msg + ")");
//        db.close();
//        // 5. return user
//        return msg;
//    }

//    public synchronized Message[] getAllMessages() throws Exception {
//        Log.d(TAG, "getAllMessages시작()");
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
//        Log.d(TAG, "메시지레코드카운트=" + count);
//
//        if (count == 0) {
//            Log.d(TAG, "getAllMessages종료(messages=null)");
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
//            Log.d(TAG, "msg[" + i + "]=" + msg[i]);
//            i++;
//            cursor.moveToNext();
//        }
//
//        // log
//        Log.d(TAG, "getMessage종료(msg=" + msg + ")");
//        db.close();
//        // 5. return user
//        return msg;
//    }

//    /**
//     *
//     */
//    public synchronized Topic[] getTopic(String userid) throws Exception {
//        Log.d(TAG, "getTopic시작(userid=" + userid + ")");
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
//        Log.d(TAG, "토픽레코드카운트=" + count);
//
//        if (count == 0) {
//            Log.d(TAG, "getTopic종료(topic=null)");
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
//            Log.d(TAG, "topic[" + i + "]=" + topic[i]);
//            i++;
//            cursor.moveToNext();
//        }
//
//        // log
//        Log.d(TAG, "getTopic종료(topic=" + topic + ")");
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
//        Log.d(TAG, "deleteTopic시작(userID=" + userID + ", topic=" + topic + ")");
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//        // 2. delete
//        db.delete(TABLE_TOPIC, // table name
//                " userid = ? and topic = ?", // selections
//                new String[]{userID, topic}); // selections
//        // args
//        // 3. close
//        db.close();
//        // log
//        Log.d(TAG, "deleteTopic종료()");
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
//        Log.d(TAG, "addTopic시작(userID=" + userID + ", topic=" + topic + ")");
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
//        Log.d(TAG, "addTopic종료()");
//    }

    public synchronized void getMsgCount() {
        Log.d(TAG, "getMsgCount시작()");
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
            Log.d(TAG, "getMsgCount종료(count=" + cursor.getString(0) + ")");
            cursor.moveToNext();
        }
        db.close();
    }


//    public synchronized int getUnreadCount() {
//        Log.d(TAG, "getUnreadCount시작()");
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
//            Log.d(TAG, "getUnreadCount종료(count=" + count + ")");
//            cursor.moveToNext();
//        }
//        return count;
//    }


}
