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
import kr.co.adflow.push.client.mqttv3.util.DebugLog;

/**
 * Created by nadir93 on 15. 10. 21..
 */
public class PushDBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PushDB";
    private static final String TABLE_MESSAGE = "message";
    //private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack",
    //        "broadcast", "acked", "broadcasted", "receivedate", "token"};
    private static final String[] MESSAGE_COLUMNS = {"msgid", "serviceid", "topic", "payload", "qos", "ack", "receivedate", "token", "agentack", "appack", "msgType"};
    private static final String TABLE_JOB = "job";
    private static final String[] JOB_COLUMNS = {"id", "type", "content"};
    private static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * @param context
     */
    public PushDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DebugLog.d("PushDBHelper 생성자 시작(context=" + context + ")");
        DebugLog.d("PushDBHelper 생성자 종료()");
    }

    /**
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        DebugLog.d("onCreate시작(db=" + db + ")");

        String CREATE_MESSAGE_TABLE = "CREATE TABLE message ( "
                + "msgid TEXT, serviceid TEXT, topic TEXT, payload BLOB, qos INTEGER, ack INTEGER, "
                + "receivedate TEXT, token TEXT, agentack INTEGER, appack INTEGER, broadcast INTEGER, msgType INTEGER, PRIMARY KEY (msgid))";

        // create message table
        db.execSQL(CREATE_MESSAGE_TABLE);

        String CREATE_JOB_TABLE = "CREATE TABLE job ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, content TEXT)";
        //create job table
        db.execSQL(CREATE_JOB_TABLE);

        DebugLog.d("onCreate종료()");
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DebugLog.d("onUpgrade 시작(db=" + db + ", oldVersion=" + oldVersion
                + ", newVersion=" + newVersion + ")");
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
            DebugLog.d("레코드 갯수=" + count);

            if (count == 0) {
                DebugLog.d("testQuery 종료()");
                return;
            }

            int i = 0;
            while (cursor.isAfterLast() == false) {
                // 4. build req object
                DebugLog.d("msgid=" + cursor.getString(0));
                DebugLog.d("serviceid=" + cursor.getString(1));
                DebugLog.d("topic=" + cursor.getString(2));
                DebugLog.d("receivedate=" + cursor.getString(6));
                DebugLog.d("token=" + cursor.getString(7));
                i++;
                cursor.moveToNext();
            }

            // log
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
            DebugLog.d("할 일 갯수=" + count);

            if (count == 0) {
                DebugLog.d("getJobList 종료(할 일 없음)");
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
                DebugLog.d("job[" + i + "]=" + job[i]);
                i++;
                cursor.moveToNext();
            }

            // log
            DebugLog.d("getJobList 종료(job=" + job + ")");
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
        DebugLog.d("addJob 시작(type=" + type + ", content="
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
            DebugLog.d("addJob 종료(id=" + id + ")");
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
        DebugLog.d("getJob 시작(id=" + id + ")");
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
            DebugLog.d("getJob 종료(job=" + job + ")");
            // 5. return job
            return job;
        } finally {
            db.close();
        }
    }

    /**
     * @param id
     */
    public synchronized void deteletJob(int id) throws Exception {
        DebugLog.d("deteletJob 시작(id=" + id + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // 2. delete
            db.delete(TABLE_JOB, // table name
                    " id = ?", // selections
                    new String[]{String.valueOf(id)}); // selections
            // args
            // log
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
    public synchronized void addMessage(String msgID, String serviceID, String topic,
                                        byte[] payload, int qos, int ack, String token, int msgType) throws Exception {
        DebugLog.d("addMessage 시작(msgID=" + msgID + ", serviceID=" + serviceID + ", topic=" + topic
                + ", payloadLength=" + payload.length + ", qos=" + qos + ", ack=" + ack + ", token=" + token + ", msgType=" + msgType + ")");
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
            values.put("msgType", msgType);
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
     * @param msgId
     * @return
     * @throws Exception
     */
    public synchronized Message getMessage(String msgId) throws Exception {
        DebugLog.d("getMessage 시작()");
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
            msg.setMsgType(cursor.getInt(10));
            // log
            DebugLog.d("getMessage 종료(msg=" + msg + ")");
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
        DebugLog.d("getBroadcastList 시작()");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_MESSAGE, // a. table
                    MESSAGE_COLUMNS, // b. column names
                    " appack = 0 AND msgType < 200 ", // c. selections
                    null, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    BuildConfig.JOB_LIMIT_COUNT); // h. limit /* "600" */
            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            int count = cursor.getCount();
            DebugLog.d("할 일 갯수=" + count);

            if (count == 0) {
                DebugLog.d("getBroadcastList 종료(할 일 없음)");
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
                message[i].setMsgType(cursor.getInt(10));

                DebugLog.d("message[" + i + "]=" + message[i]);
                i++;
                cursor.moveToNext();
            }

            // log
            DebugLog.d("getBroadcastList 종료(message=" + message + ")");
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
        DebugLog.d("deleteMessage 시작(msgId=" + msgId + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. delete
            db.delete(TABLE_MESSAGE, // table name
                    " msgid = ? ", // selections
                    new String[]{msgId}); // selections
            // args
            // log
            DebugLog.d("deleteMessage 종료()");
        } finally {
            db.close();
        }
    }

    /**
     * @param msgId
     * @return
     */
    public synchronized int updateAgentAck(String msgId) throws Exception {
        DebugLog.d("updateAgentAck 시작(msgId=" + msgId + ")");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues args = new ContentValues();
            args.put("agentack", 1);
            int count = db.update(TABLE_MESSAGE, args, " msgid = ? ", new String[]{msgId});
            DebugLog.d("updateAgentAck 종료(count=" + count + ")");
            return count;
        } finally {
            db.close();
        }
    }

    /**
     * @param msgId
     * @return
     */
    public synchronized int updateAppAck(String msgId) throws Exception {
        DebugLog.d("updateAppAck 시작(msgId=" + msgId + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues args = new ContentValues();
            args.put("appack", 1);
            int count = db.update(TABLE_MESSAGE, args, " msgid = ? ", new String[]{msgId});
            DebugLog.d("updateAppAck 종료(count=" + count + ")");
            return count;
        } finally {
            db.close();
        }
    }

    /**
     *
     */
    public synchronized void getMsgCount() throws Exception {
        DebugLog.d("getMsgCount시작()");
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
            DebugLog.d("getMsgCount종료(count=" + cursor.getString(0) + ")");
            cursor.moveToNext();
        }
        db.close();
    }

    /**
     * @throws Exception
     */
    public synchronized int deleteOldMessage(String msgId)
            throws Exception {
        DebugLog.d("deleteOldMessage 시작(msgId=" + msgId + ")");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. delete
            int count = db.delete(TABLE_MESSAGE, // table name
                    " msgid = ? ", // selections
                    new String[]{msgId}); // selections
            // args
            // log
            DebugLog.d("deleteOldMessage 종료(count=" + count + ")");
            return count;
        } finally {
            db.close();
        }
    }
}
