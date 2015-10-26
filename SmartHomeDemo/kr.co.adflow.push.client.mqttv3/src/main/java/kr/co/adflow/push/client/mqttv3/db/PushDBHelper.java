/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.adflow.push.client.mqttv3.BuildConfig;

/**
 * Created by nadir93 on 15. 10. 21..
 */
public class PushDBHelper extends SQLiteOpenHelper {

    // TAG for debug
    public static final String TAG = "PushDBHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PushDB";
    private static final String TABLE_MESSAGE = "message";
    //private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack",
    //        "broadcast", "acked", "broadcasted", "receivedate", "token"};
    private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack", "receivedate", "token", "agentack", "appack"};
    private static final String TABLE_JOB = "job";
    private static final String[] JOB_COLUMNS = {"id", "type", "content"};
    private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * @param context
     */
    public PushDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "PushDBHelper 생성자 시작(context=" + context + ")");
        Log.d(TAG, "PushDBHelper 생성자 종료()");
    }

    /**
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate시작(db=" + db + ")");

        String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
                + "msgid TEXT, serviceid TEXT, topic TEXT, payload BLOB, qos INTEGER, ack INTEGER, "
                + "receivedate TEXT, token TEXT, agentack INTEGER, appack INTEGER, broadcast INTEGER, PRIMARY KEY (msgid))";

        // create message table
        db.execSQL(CREATE_MESSAGE_TABLE);

        String CREATE_JOB_TABLE = "CREATE TABLE job ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, content TEXT)";
        //create job table
        db.execSQL(CREATE_JOB_TABLE);

        Log.d(TAG, "onCreate종료()");
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade 시작(db=" + db + ", oldVersion=" + oldVersion
                + ", newVersion=" + newVersion + ")");
        // Drop older message table if existed
        db.execSQL("DROP TABLE IF EXISTS message");
        // Drop older job table if existed
        db.execSQL("DROP TABLE IF EXISTS job");
        // create fresh books table
        this.onCreate(db);
        Log.d(TAG, "onUpgrade 종료()");
    }

    public synchronized void testQuery() throws Exception {
        Log.d(TAG, "testQuery 시작()");

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
            Log.d(TAG, "레코드 갯수=" + count);

            if (count == 0) {
                Log.d(TAG, "testQuery 종료()");
                return;
            }

            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                Log.d(TAG, "msgid=" + cursor.getString(0));
                Log.d(TAG, "serviceid=" + cursor.getString(1));
                Log.d(TAG, "topic=" + cursor.getString(2));
                Log.d(TAG, "receivedate=" + cursor.getString(6));
                Log.d(TAG, "token=" + cursor.getString(7));
                i++;
                cursor.moveToNext();
            }

            // log
            Log.d(TAG, "testQuery 종료()");
        } finally {
            db.close();
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public synchronized Job[] getJobList() throws Exception {
        Log.d(TAG, "getJobList 시작()");
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
            Log.d(TAG, "할 일 갯수=" + count);

            if (count == 0) {
                Log.d(TAG, "getJobList 종료(할 일 없음)");
                return null;
            }

            Job[] job = new Job[count];
            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                job[i] = new Job();
                job[i].setId(cursor.getInt(0));
                job[i].setType(cursor.getInt(1));
                job[i].setContent(cursor.getString(2));
                Log.d(TAG, "job[" + i + "]=" + job[i]);
                i++;
                cursor.moveToNext();
            }

            // log
            Log.d(TAG, "getJobList 종료(job=" + job + ")");
            // 5. return req
            return job;
        } finally {
            db.close();
        }
    }

    /**
     * @param type
     * @param content
     * @return
     * @throws Exception
     */
    public synchronized int addJob(int type, String content)
            throws Exception {
        Log.d(TAG, "addJob 시작(type=" + type + ", content="
                + content + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = null;

        try {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("type", type);
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
            Log.d(TAG, "addJob 종료(id=" + id + ")");
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
        Log.d(TAG, "getJob 시작(id=" + id + ")");
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
            //job.setTopic(cursor.getString(2));
            job.setContent(cursor.getString(3));
            // log
            Log.d(TAG, "getJob 종료(job=" + job + ")");
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
        Log.d(TAG, "deteletJob 시작(id=" + id + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // 2. delete
            db.delete(TABLE_JOB, // table name
                    " id = ?", // selections
                    new String[]{String.valueOf(id)}); // selections
            // args
            // log
            Log.d(TAG, "deteletJob 종료()");
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
    public synchronized void addMessage(String msgID, String serviceID, String topic,
                                        byte[] payload, int qos, int ack, String token) throws Exception {
        Log.d(TAG, "addMessage 시작(msgID=" + msgID + ", serviceID=" + serviceID + ", topic=" + topic
                + ", payloadLength=" + payload.length + ", qos=" + qos + ", ack=" + ack + ", token=" + token + ")");
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
            values.put("agentack", 0);
            values.put("appack", 0);
            Date now = new Date();
            values.put("receivedate", sdf.format(now));

            // 3. insert
            db.insertOrThrow(TABLE_MESSAGE, // table
                    null, // nullColumnHack
                    values);
            Log.d(TAG, "addMessage 종료()");
        } finally {
            // 4. close
            db.close();
        }
    }

    /**
     * @param msgId
     * @return
     * @throws Exception
     */
    public synchronized Message getMessage(String msgId) throws Exception {
        Log.d(TAG, "getMessage 시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                    MESSAGE_COLUMNS, // b. column names
                    "msgid = ?", // c. selections
                    new String[]{msgId}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit
            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            // 4. build book object
            Message msg = new Message();
            msg.setMsgId(cursor.getString(0));
            msg.setServiceId(cursor.getString(1));
            msg.setTopic(cursor.getString(2));
            msg.setPayload(cursor.getBlob(3));
            msg.setQos(cursor.getInt(4));
            msg.setAck(cursor.getInt(5));
            msg.setReceivedate(cursor.getString(6));
            msg.setToken(cursor.getString(7));
            msg.setAgentAck(cursor.getInt(8));
            msg.setAppAck(cursor.getInt(9));
            // log
            Log.d(TAG, "getMessage 종료(msg=" + msg + ")");
            // 5. return user
            return msg;
        } finally {
            db.close();
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public synchronized Message[] getBroadcastList() throws Exception {
        Log.d(TAG, "getBroadcastList 시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                    MESSAGE_COLUMNS, // b. column names
                    " appack = 0 ", // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    BuildConfig.JOB_LIMIT_COUNT); // h. limit /* "600" */
            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            int count = cursor.getCount();
            Log.d(TAG, "할 일 갯수=" + count);

            if (count == 0) {
                Log.d(TAG, "getBroadcastList 종료(할 일 없음)");
                return null;
            }

            Message[] message = new Message[count];
            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                message[i] = new Message();
                message[i].setMsgId(cursor.getString(0));
                message[i].setServiceId(cursor.getString(1));
                message[i].setTopic(cursor.getString(2));
                message[i].setPayload(cursor.getBlob(3));
                message[i].setQos(cursor.getInt(4));
                message[i].setAck(cursor.getInt(5));
                message[i].setReceivedate(cursor.getString(6));
                message[i].setToken(cursor.getString(7));
                message[i].setAgentAck(cursor.getInt(8));
                message[i].setAppAck(cursor.getInt(9));

                Log.d(TAG, "message[" + i + "]=" + message[i]);
                i++;
                cursor.moveToNext();
            }

            // log
            Log.d(TAG, "getBroadcastList 종료(message=" + message + ")");
            // 5. return req
            return message;
        } finally {
            db.close();
        }
    }

    /**
     * @throws Exception
     */
    public synchronized void deleteMessage(String msgId)
            throws Exception {
        Log.d(TAG, "deleteMessage 시작(msgId=" + msgId + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. delete
            db.delete(TABLE_MESSAGE, // table name
                    " msgid = ? ", // selections
                    new String[]{msgId}); // selections
            // args
            // log
            Log.d(TAG, "deleteMessage 종료()");
        } finally {
            db.close();
        }
    }

    /**
     * @param msgId
     * @return
     */
    public synchronized int updateAgentAck(String msgId) {
        Log.d(TAG, "updateAgentAck 시작(msgId=" + msgId + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues args = new ContentValues();
            args.put("agentack", 1);
            int count = db.update(TABLE_MESSAGE, args, " msgid = ? ", new String[]{msgId});
            Log.d(TAG, "updateAgentAck 종료(count=" + count + ")");
            return count;
        } finally {
            db.close();
        }
    }

    /**
     * @param msgId
     * @return
     */
    public synchronized int updateAppAck(String msgId) {
        Log.d(TAG, "updateAppAck 시작(msgId=" + msgId + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues args = new ContentValues();
            args.put("appack", 1);
            int count = db.update(TABLE_MESSAGE, args, " msgid = ? ", new String[]{msgId});
            Log.d(TAG, "updateAppAck 종료(count=" + count + ")");
            return count;
        } finally {
            db.close();
        }
    }

    /**
     *
     */
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
}
