package com.bns.pmc.provider;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.bns.pmc.provider.DataColumn.MessageColumn;
import com.bns.pmc.provider.DataColumn.ValidationColumn;
import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

public class DataProvider extends ContentProvider {

    private static final int TRANSACTION_WAIT_MAX_COUNT = 5;

    private static HashMap<String, String> sMessageProjectionMap;
    private static HashMap<String, String> sValidationProjectionMap;

    private static UriMatcher sUriMatcher;
    private static final int IDX_LIST = 1;
    private static final int IDX_LIST_TOTAL_COUNT = 2;
    private static final int IDX_LIST_TOTAL_DONT_READ = 3;
    private static final int IDX_TALK_BY_NUMBER = 4;
    private static final int IDX_TALK_SAVE_BY_NUMBER = 5;
    private static final int IDX_TALK_ACK_BY_NUMBER = 6;
    private static final int IDX_TALK_ACK_BY_READ = 7;
    private static final int IDX_DELETE_ALL = 8;
    private static final int IDX_DELETE_FIRST = 9;
    private static final int IDX_DELETE_BY_ID = 10;
    private static final int IDX_DELETE_BY_NUMBER = 11;
    private static final int IDX_DELETE_SAVE_BY_NUMBER = 12;
    private static final int IDX_UPDATE_DONT_READ_BY_NUMBER = 13;
    private static final int IDX_UPDATE_ACK_BY_ID = 14;
    private static final int IDX_INSERT_MSG = 15;
    private static final int IDX_VALID_LIST = 16;
    private static final int IDX_VALID_TOTAL_COUNT = 17;
    private static final int IDX_VALID_INSERT = 18;
    private static final int IDX_VALID_DELETE_ALL = 19;
    private static final int IDX_VALID_DELETE_FIRST = 20;

    private DBHelper mOpenHelper;

    static {
    	// Message Table Column이 추가 되면 만들자!!
        sMessageProjectionMap = new HashMap<String, String>();
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_ID, MessageColumn.DB_COLUMN_ID);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_MSG_ID, MessageColumn.DB_COLUMN_MSG_ID);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_TOKEN, MessageColumn.DB_COLUMN_TOKEN);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_NUMBER, MessageColumn.DB_COLUMN_NUMBER);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_GROUP_MEMBER, MessageColumn.DB_COLUMN_GROUP_MEMBER);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_NUMBER_TYPE, MessageColumn.DB_COLUMN_NUMBER_TYPE);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_MSG, MessageColumn.DB_COLUMN_MSG);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_URL, MessageColumn.DB_COLUMN_URL);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_DATA, MessageColumn.DB_COLUMN_DATA);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_DATA_TYPE, MessageColumn.DB_COLUMN_DATA_TYPE);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_RECV, MessageColumn.DB_COLUMN_RECV);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_DONT_READ, MessageColumn.DB_COLUMN_DONT_READ);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_ACK, MessageColumn.DB_COLUMN_ACK);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_STATE, MessageColumn.DB_COLUMN_STATE);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_RESEND_COUNT, MessageColumn.DB_COLUMN_RESEND_COUNT);
        sMessageProjectionMap.put(MessageColumn.DB_COLUMN_CREATETIME, MessageColumn.DB_COLUMN_CREATETIME); // 16 Column.
        
        sValidationProjectionMap = new HashMap<String, String>();
        sValidationProjectionMap.put(ValidationColumn.DB_COLUMN_ID, ValidationColumn.DB_COLUMN_ID);
        sValidationProjectionMap.put(ValidationColumn.DB_COLUMN_NUMBER, ValidationColumn.DB_COLUMN_NUMBER);
        sValidationProjectionMap.put(ValidationColumn.DB_COLUMN_CREATETIME, ValidationColumn.DB_COLUMN_CREATETIME);

        // *은 Text #은 Number
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_ALL_LIST, IDX_LIST);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_ALL_COUNT_LIST, IDX_LIST_TOTAL_COUNT);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_TOTAL_LIST_DONT_READ, IDX_LIST_TOTAL_DONT_READ);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_TALK_BY_NUMBER + "/*", IDX_TALK_BY_NUMBER);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_TALK_SAVE_BY_NUMBER + "/*", IDX_TALK_SAVE_BY_NUMBER);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_TALK_ACK_BY_NUMBER + "/*", IDX_TALK_ACK_BY_NUMBER);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_TALK_ACK_BY_READ, IDX_TALK_ACK_BY_READ);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_ALL, IDX_DELETE_ALL);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_FIRST, IDX_DELETE_FIRST);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_BY_ID + "/#", IDX_DELETE_BY_ID);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_BY_NUMBER + "/*", IDX_DELETE_BY_NUMBER);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_SAVE_BY_NUMBER + "/*", IDX_DELETE_SAVE_BY_NUMBER);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.UPDATE_DONT_READ_BY_NUMBER + "/*", IDX_UPDATE_DONT_READ_BY_NUMBER);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.UPDATE_ACK_BY_ID + "/#", IDX_UPDATE_ACK_BY_ID);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.INSERT_MESSAGE, IDX_INSERT_MSG);
        
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_VALID_ALL, IDX_VALID_LIST);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.SELECT_VALID_COUNT_LIST, IDX_VALID_TOTAL_COUNT);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.INSERT_VALID, IDX_VALID_INSERT);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_VALID_ALL, IDX_VALID_DELETE_ALL);
        sUriMatcher.addURI(DataColumn.AUTHORITY, DataColumn.DELETE_VALID_FIRST, IDX_VALID_DELETE_FIRST);
    }
    
    @Override
    public ContentProviderResult[] applyBatch(
            ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        ContentProviderResult[] result = new ContentProviderResult[operations
                .size()];
        int i = 0;
        // Opens the database object in "write" mode.
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // Begin a transaction
        // db.beginTransaction();
        checkTransactionStart(db, TRANSACTION_WAIT_MAX_COUNT);
        try {
            for (ContentProviderOperation operation : operations) {
                // Chain the result for back references
                result[i++] = operation.apply(this, result, i);
            }

            db.setTransactionSuccessful();
        } catch (OperationApplicationException e) {
            Log.d(PMCType.TAG, "batch failed: " + e.getLocalizedMessage());
        } finally {
            db.endTransaction();
        }

        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numInserted = 0;
        String table = null;

        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            //case MESSAGE:
        	default:
                table = MessageColumn.DB_TABLE_MESSAGE;
                break;
        }

        if (table == null) {
            throw new SQLException("Failed to insert row into " + uri);
        } else {
            SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();
            // sqlDB.beginTransaction();
            checkTransactionStart(sqlDB, TRANSACTION_WAIT_MAX_COUNT);
            try {
                for (ContentValues cv : values) {
                    long newID = sqlDB.insertOrThrow(table, null, cv);
                    if (newID <= 0) {
                        throw new SQLException("Failed to insert row into "
                                + uri);
                    }
                }
                sqlDB.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                numInserted = values.length;
            } finally {
                sqlDB.endTransaction();
            }
        }

        return numInserted;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // db.beginTransactionNonExclusive();
        checkTransactionStart(db, TRANSACTION_WAIT_MAX_COUNT);
        try {
            Uri result = insertInTransaction(db, uri, values);
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // db.beginTransactionNonExclusive();
        checkTransactionStart(db, TRANSACTION_WAIT_MAX_COUNT);
        try {
            int updated = updateInTransaction(db, uri, values, selection,
                    selectionArgs);
            db.setTransactionSuccessful();
            return updated;
        } finally {
            db.endTransaction();
        }
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        // db.beginTransactionNonExclusive();
        checkTransactionStart(db, TRANSACTION_WAIT_MAX_COUNT);
        try {
            int deleted = deleteInTransaction(db, uri, selection, selectionArgs);
            db.setTransactionSuccessful();
            return deleted;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String strSelection,
            String[] selectionArgs, String sortOrder) {
    	/*Log.d(PMCType.TAG, "query uri: " + uri + ", projection: " + projection + ", selection: " + selection + ", \n selectionArgs: " + selectionArgs
        		+ ", sortOrder: " + sortOrder);*/
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String strTableName = null;
        String groupBy = null;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case IDX_LIST: 
            	{
	                /* SQLiteDatabase db = mOpenHelper.getReadableDatabase();
	                String sql = "select _id, ptt, groupPtt, msg, recv, dontRead, createtime, SUM(dontRead) as sum_dont_read from " + Message.DB_TABLE_MESSAGE + 
	                        " group by " + Message.DB_COLUMN_PTT + 
	                        " order by " + Message.DB_COLUMN_DONT_READ + ", " +
	                        Message.DB_COLUMN_CREATETIME + " desc";
	                return db.rawQuery(sql, null);*/
	            	strTableName = MessageColumn.DB_TABLE_MESSAGE;
	            	String[] proj = {MessageColumn.DB_COLUMN_ID, MessageColumn.DB_COLUMN_MSG_ID, MessageColumn.DB_COLUMN_TOKEN, 
	            			MessageColumn.DB_COLUMN_NUMBER, MessageColumn.DB_COLUMN_GROUP_MEMBER, MessageColumn.DB_COLUMN_NUMBER_TYPE, 
	            			MessageColumn.DB_COLUMN_MSG,  MessageColumn.DB_COLUMN_DATA, MessageColumn.DB_COLUMN_DATA_TYPE,
	            			MessageColumn.DB_COLUMN_RECV, MessageColumn.DB_COLUMN_DONT_READ, MessageColumn.DB_COLUMN_ACK, 
	            			MessageColumn.DB_COLUMN_STATE, MessageColumn.DB_COLUMN_RESEND_COUNT, MessageColumn.DB_COLUMN_CREATETIME, 
	            			"SUM(" + MessageColumn.DB_COLUMN_DONT_READ + ")" + " as " + MessageColumn.DB_COLUMN_SUM_DONT_READ};
	            	
	            	projection = proj;
	            	groupBy = MessageColumn.DB_COLUMN_NUMBER;
	            	sortOrder = MessageColumn.DB_COLUMN_DONT_READ + " desc, " + MessageColumn.DB_COLUMN_CREATETIME + " desc";
	            	qb.setProjectionMap(sMessageProjectionMap);
            	}
            	break;
            case IDX_LIST_TOTAL_COUNT : 
            	{
	            	strTableName = MessageColumn.DB_TABLE_MESSAGE;
	            	String[] proj = {"Count(*) as " + MessageColumn.DB_COLUMN_COUNT_ALL};
	            	projection = proj;
	            	qb.setProjectionMap(sMessageProjectionMap);
            	}
            	break;
            case IDX_LIST_TOTAL_DONT_READ : 
            	{
	            	strTableName = MessageColumn.DB_TABLE_MESSAGE;
	            	String[] proj = {"SUM(" + MessageColumn.DB_COLUMN_DONT_READ + ")" + 
	            	" as " + MessageColumn.DB_COLUMN_SUM_DONT_READ};
	            	
	            	projection = proj;
	            	qb.setProjectionMap(sMessageProjectionMap);
            	}
            	break;
            case IDX_TALK_BY_NUMBER: 
            	{
	                strTableName = MessageColumn.DB_TABLE_MESSAGE;
	                String strNumber = uri.getPathSegments().get(1);
	                strSelection = MessageColumn.DB_COLUMN_NUMBER + " = ?";
	                String[] args = { strNumber };
	                selectionArgs = args;
	                sortOrder = MessageColumn.DB_COLUMN_STATE + " desc, " + MessageColumn.DB_COLUMN_CREATETIME + " asc";
	                qb.setProjectionMap(sMessageProjectionMap);
            	}
                break;
            case IDX_TALK_SAVE_BY_NUMBER:
            	{
            		strTableName = MessageColumn.DB_TABLE_MESSAGE;
	                
	                strSelection = MessageColumn.DB_COLUMN_NUMBER + " = ? and " + MessageColumn.DB_COLUMN_STATE + 
	                		" = " + DataColumn.COLUMN_STATE_SAVE;
	                String strNumber = uri.getPathSegments().get(1);
	                String[] args = { strNumber };
	                selectionArgs = args;
	                qb.setProjectionMap(sMessageProjectionMap);
            	}
            	break;
            case IDX_TALK_ACK_BY_NUMBER:
            	{
            		strTableName = MessageColumn.DB_TABLE_MESSAGE;
	                strSelection = MessageColumn.DB_COLUMN_NUMBER + " = ? and " + MessageColumn.DB_COLUMN_ACK + 
	                		" = " + DataColumn.COLUMN_ACK_ING;
	                String strNumber = uri.getPathSegments().get(1);
	                String[] args = { strNumber };
	                selectionArgs = args;
	                qb.setProjectionMap(sMessageProjectionMap);
            	}
            	break;
            case IDX_TALK_ACK_BY_READ:
            	{
            		strTableName = MessageColumn.DB_TABLE_MESSAGE;
	                strSelection = MessageColumn.DB_COLUMN_DONT_READ + " = " + DataColumn.COLUMN_DONT_READ_READ + " and " + 
	                		MessageColumn.DB_COLUMN_ACK + " = " + DataColumn.COLUMN_ACK_ING;
	                qb.setProjectionMap(sMessageProjectionMap);
            	}
            	break;
            case IDX_VALID_LIST :
            	{
            		strTableName = ValidationColumn.DB_TABLE_VALIDATION;
	            	String[] proj = {ValidationColumn.DB_COLUMN_ID, ValidationColumn.DB_COLUMN_NUMBER, ValidationColumn.DB_COLUMN_CREATETIME};
	            	
	            	projection = proj;
	            	qb.setProjectionMap(sValidationProjectionMap);
            	}
            	break;
            case IDX_VALID_TOTAL_COUNT :
            	{
            		strTableName = ValidationColumn.DB_TABLE_VALIDATION;
	            	String[] proj = {"Count(*) as " + ValidationColumn.DB_COLUMN_VALID_COUNT_ALL};
	            	projection = proj;
	            	qb.setProjectionMap(sValidationProjectionMap);
            	}
            	break;
            default:
            	{
            		strTableName = MessageColumn.DB_TABLE_MESSAGE;
            		qb.setProjectionMap(sMessageProjectionMap);
            	}
                break;
        }

        qb.setTables(strTableName);

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        // db.beginTransactionNonExclusive();
        checkTransactionStart(db, TRANSACTION_WAIT_MAX_COUNT);
        Cursor c = null;
        try {
            c = qb.query(db, projection, strSelection, selectionArgs, groupBy,
                    null, sortOrder);
            // Tell the cursor what uri to watch, so it knows when its source
            // data
            // changes
            c.setNotificationUri(getContext().getContentResolver(), uri);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return c;
    }

    private Uri insertInTransaction(SQLiteDatabase db, Uri uri,
            ContentValues values) {
        Log.d(PMCType.TAG, "insert uri: " + uri + ", values: " + values);

        String tableName = null;
        final int match = sUriMatcher.match(uri);
        switch (match) {
        	case IDX_INSERT_MSG :
        		tableName = MessageColumn.DB_TABLE_MESSAGE;
        		break;
        	case IDX_VALID_INSERT :
        		tableName = ValidationColumn.DB_TABLE_VALIDATION;
        		break;
            default:
                tableName = MessageColumn.DB_TABLE_MESSAGE;
                break;
        }

        long rowId;
        rowId = db.insert(tableName, null, values);

        if (rowId > 0) {
            Uri taskUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(DataColumn.AUTHORITY_URI, null);
            return taskUri;
        }
        return null;
    }
    
    private int updateInTransaction(SQLiteDatabase db, Uri uri, ContentValues values, 
    		String whereClause, String[] whereArgs) {
    	/*Log.d(PMCType.TAG, "update uri: " + uri + ", values: " + values +
         ", whereClause: " + whereClause);*/
        int count = 0;

        String tableName = null;
        StringBuilder sb = new StringBuilder();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case IDX_UPDATE_DONT_READ_BY_NUMBER :
            	{
	            	tableName = MessageColumn.DB_TABLE_MESSAGE;
	            	values = new ContentValues();
	            	values.put(MessageColumn.DB_COLUMN_DONT_READ, DataColumn.COLUMN_DONT_READ_READ);
	                sb.append(MessageColumn.DB_COLUMN_NUMBER + " = ? and " + MessageColumn.DB_COLUMN_DONT_READ + 
	                		" = " + DataColumn.COLUMN_DONT_READ_DONT);
	                String strNumber = uri.getPathSegments().get(1);
	                String[] args = { strNumber };
	                whereArgs = args;
            	}
            	break;
            case IDX_UPDATE_ACK_BY_ID :
            	{
            		tableName = MessageColumn.DB_TABLE_MESSAGE;
	            	values = new ContentValues();
	            	values.put(MessageColumn.DB_COLUMN_ACK, DataColumn.COLUMN_ACK_SEND);
	                sb.append(MessageColumn.DB_COLUMN_ID + " = ?");
	                Long id = Long.parseLong(uri.getPathSegments().get(1));
	                String[] args = { id.toString() };
	                whereArgs = args;
            	}
            	break;
            default:
            	{
            		tableName = MessageColumn.DB_TABLE_MESSAGE;
            		sb.append(whereClause);
            	}
                break;

        }

        count = db.update(tableName, values, sb.toString(), whereArgs);
        getContext().getContentResolver().notifyChange(
                DataColumn.AUTHORITY_URI, null);
        return count;
    }

    private int deleteInTransaction(SQLiteDatabase db, Uri uri, String where, String[] whereArgs) {
        Log.d(PMCType.TAG, "deleteInTransaction uri: " + uri + ", where: " + where + ", whereArgs: " + whereArgs);
        int count;

        String tableName = null;
        StringBuilder sb = new StringBuilder();

        final int match = sUriMatcher.match(uri);
        switch (match) {
        	case IDX_DELETE_ALL: 
        		{
	        		tableName = MessageColumn.DB_TABLE_MESSAGE;
	        	}
	        	break;
        	case IDX_DELETE_FIRST: 
	        	{
	        		tableName = MessageColumn.DB_TABLE_MESSAGE;
	        		sb.append(MessageColumn.DB_COLUMN_ID + 
	        				" = (select min(" + MessageColumn.DB_COLUMN_ID + ") from " + 
	        				MessageColumn.DB_TABLE_MESSAGE + ")");
	        	}
	        	break;
            case IDX_DELETE_BY_ID: 
	            {
	                tableName = MessageColumn.DB_TABLE_MESSAGE;
	                Long id = Long.parseLong(uri.getPathSegments().get(1));
	                sb.append(MessageColumn.DB_COLUMN_ID + " = ?");
	
	                String[] args = { id.toString() };
	                whereArgs = args;
	            }
	            break;
            case IDX_DELETE_BY_NUMBER: 
	            {
	                tableName = MessageColumn.DB_TABLE_MESSAGE;
	                String phoneNumber = uri.getPathSegments().get(1);
	                sb.append(MessageColumn.DB_COLUMN_NUMBER + " = ?");
	
	                String[] args = { phoneNumber };
	                whereArgs = args;
	            }
	            break;
            case IDX_DELETE_SAVE_BY_NUMBER:
            	{
            		tableName = MessageColumn.DB_TABLE_MESSAGE;
	                String phoneNumber = uri.getPathSegments().get(1);
	                sb.append(MessageColumn.DB_COLUMN_NUMBER + " = ? and " + MessageColumn.DB_COLUMN_STATE + " = 0");
	
	                String[] args = { phoneNumber };
	                whereArgs = args;
            	}
            	break;
            case IDX_VALID_DELETE_ALL:
            	{
            		tableName = ValidationColumn.DB_TABLE_VALIDATION;
            	}
            	break;
            case IDX_VALID_DELETE_FIRST:
            	{
            		tableName = ValidationColumn.DB_TABLE_VALIDATION;
	        		sb.append(ValidationColumn.DB_COLUMN_ID + 
	        				" = (select min(" + ValidationColumn.DB_COLUMN_ID + ") from " + 
	        				ValidationColumn.DB_TABLE_VALIDATION + ")");
            	}
            	break;
            default:
            	{
	                tableName = MessageColumn.DB_TABLE_MESSAGE;
	                sb.append(where);
            	}
            	break;
        }

        count = db.delete(tableName, sb.toString(), whereArgs);

        getContext().getContentResolver().notifyChange(
                DataColumn.AUTHORITY_URI, null);
        return count;
    }
    
    private void checkTransactionStart(SQLiteDatabase db, int waitCount) {
        if (waitCount != TRANSACTION_WAIT_MAX_COUNT) {
            Log.e(PMCType.TAG, "checkTransactionStart IN, waitCount : " + waitCount);
        }

        if (waitCount < 0) {
            Log.e(PMCType.TAG, "use all waiting tickets... DB fail.");
            return;
        }

        try {
            // int i = 3/0;
            db.beginTransactionNonExclusive();
            // db.beginTransaction();
        } catch (Exception sqlException) {
            sqlException.printStackTrace();

            try {
                Thread.sleep(300);
            } catch (InterruptedException threadException) {
                threadException.printStackTrace();
            } finally {
                checkTransactionStart(db, --waitCount);
            }
        }
    }
}