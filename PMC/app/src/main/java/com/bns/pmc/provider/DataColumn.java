package com.bns.pmc.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DataColumn implements BaseColumns {
    public static final String SELECT_ALL_LIST = "select_all_list";
    public static final String SELECT_ALL_COUNT_LIST = "select_all_count_list";
    public static final String SELECT_TOTAL_LIST_DONT_READ = "select_total_list_dont_read";
    public static final String SELECT_TALK_BY_NUMBER = "select_talk_by_number";
    public static final String SELECT_TALK_SAVE_BY_NUMBER = "select_talk_save_by_number";
    public static final String SELECT_TALK_ACK_BY_NUMBER = "select_talk_ack_by_number";
    public static final String SELECT_TALK_ACK_BY_READ = "select_talk_ack_by_read";
    public static final String DELETE_ALL = "delete_all";
    public static final String DELETE_FIRST = "delete_first";
    public static final String DELETE_BY_ID = "delete_by_id";
    public static final String DELETE_BY_NUMBER = "delete_by_number";
    public static final String DELETE_SAVE_BY_NUMBER = "delete_save_by_number";
    public static final String UPDATE_DONT_READ_BY_NUMBER = "update_dont_read_by_number";
    public static final String UPDATE_ACK_BY_ID = "update_ack_by_id";
    public static final String INSERT_MESSAGE = "insert_msg";
    public static final String SELECT_VALID_ALL = "select_valid_all";
    public static final String SELECT_VALID_COUNT_LIST = "select_valid_count_list";
    public static final String INSERT_VALID = "insert_valid";
    public static final String DELETE_VALID_ALL = "delete_valid_all";
    public static final String DELETE_VALID_FIRST = "delete_valid_first";


    public static final String AUTHORITY = "com.bns.pmc";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri CONTENT_URI_LIST = Uri.withAppendedPath(AUTHORITY_URI, SELECT_ALL_LIST);
    public static final Uri CONTENT_URI_LIST_TOTAL_COUNT = Uri.withAppendedPath(AUTHORITY_URI, SELECT_ALL_COUNT_LIST);
    public static final Uri CONTENT_URI_LIST_TOTAL_DONT_READ = Uri.withAppendedPath(AUTHORITY_URI, SELECT_TOTAL_LIST_DONT_READ);
    public static final Uri CONTENT_URI_TALK_BY_NUMBER = Uri.withAppendedPath(AUTHORITY_URI, SELECT_TALK_BY_NUMBER);
    public static final Uri CONTENT_URI_TALK_SAVE_BY_NUMBER = Uri.withAppendedPath(AUTHORITY_URI, SELECT_TALK_SAVE_BY_NUMBER);
    public static final Uri CONTENT_URI_TALK_ACK_BY_NUMBER = Uri.withAppendedPath(AUTHORITY_URI, SELECT_TALK_ACK_BY_NUMBER);
    public static final Uri CONTENT_URI_TALK_ACK_BY_READ = Uri.withAppendedPath(AUTHORITY_URI, SELECT_TALK_ACK_BY_READ);
    public static final Uri CONTENT_URI_DEL_ALL = Uri.withAppendedPath(AUTHORITY_URI, DELETE_ALL);
    public static final Uri CONTENT_URI_DEL_FIRST = Uri.withAppendedPath(AUTHORITY_URI, DELETE_FIRST);
    public static final Uri CONTENT_URI_DEL_BY_ID = Uri.withAppendedPath(AUTHORITY_URI, DELETE_BY_ID);
    public static final Uri CONTENT_URI_DEL_BY_NUM = Uri.withAppendedPath(AUTHORITY_URI, DELETE_BY_NUMBER);
    public static final Uri CONTENT_URI_DEL_SAVE_BY_NUMBER = Uri.withAppendedPath(AUTHORITY_URI, DELETE_SAVE_BY_NUMBER);
    public static final Uri CONTENT_URI_UPDATE_DONT_READ_BY_NUMBER = Uri.withAppendedPath(AUTHORITY_URI, UPDATE_DONT_READ_BY_NUMBER);
    public static final Uri CONTENT_URI_UPDATE_ACK_BY_ID = Uri.withAppendedPath(AUTHORITY_URI, UPDATE_ACK_BY_ID);
    public static final Uri CONTENT_URI_INSERT_MSG = Uri.withAppendedPath(AUTHORITY_URI, INSERT_MESSAGE);
    public static final Uri CONTENT_URI_SELECT_VALID_ALL = Uri.withAppendedPath(AUTHORITY_URI, SELECT_VALID_ALL);
    public static final Uri CONTENT_URI_SELECT_VALID_TOTAL_COUNT = Uri.withAppendedPath(AUTHORITY_URI, SELECT_VALID_COUNT_LIST);
    public static final Uri CONTENT_URI_INSERT_VALID = Uri.withAppendedPath(AUTHORITY_URI, INSERT_VALID);
    public static final Uri CONTENT_URI_DEL_VALID_ALL = Uri.withAppendedPath(AUTHORITY_URI, DELETE_VALID_ALL);
    public static final Uri CONTENT_URI_DEL_VALID_FIRST = Uri.withAppendedPath(AUTHORITY_URI, DELETE_VALID_FIRST);

    public static final int COLUMN_NUMBER_TYPE_PTT = 0;
    public static final int COLUMN_NUMBER_TYPE_GROUP = 1;
    public static final int COLUMN_NUMBER_TYPE_NORMAL = 2;
    public static final int COLUMN_NUMBER_TYPE_CONTROL = 3;

    public static final int COLUMN_DATA_TYPE_NONE = 0;
    public static final int COLUMN_DATA_TYPE_JPG = 1;
    public static final int COLUMN_DATA_TYPE_BMP = 2;
    public static final int COLUMN_DATA_TYPE_PNG = 3;
    public static final int COLUMN_DATA_TYPE_MP3 = 4;
    public static final int COLUMN_DATA_TYPE_UNKNOWN = 5;

    public static final int COLUMN_RECV_SEND = 0;
    public static final int COLUMN_RECV_RECV = 1;

    public static final int COLUMN_DONT_READ_READ = 0;
    public static final int COLUMN_DONT_READ_DONT = 1;

    public static final int COLUMN_ACK_SEND = 0;
    public static final int COLUMN_ACK_ING = 1;

    public static final int COLUMN_STATE_SAVE = 0;
    public static final int COLUMN_STATE_FAIL = 1;
    public static final int COLUMN_STATE_SUCCESS = 2;
    public static final int DB_COLUMN_DATA_TYPE_JPG = 1;

    public class MessageColumn {
        public static final String DB_TABLE_MESSAGE = "MESSAGE";
        public static final String DB_COLUMN_ID = "_id";
        public static final String DB_COLUMN_MSG_ID = "msgID";
        public static final String DB_COLUMN_TOKEN = "token";
        public static final String DB_COLUMN_NUMBER = "number";
        public static final String DB_COLUMN_GROUP_MEMBER = "groupMember";
        public static final String DB_COLUMN_NUMBER_TYPE = "numberType";
        public static final String DB_COLUMN_MSG = "msg";
        public static final String DB_COLUMN_URL = "url";
        public static final String DB_COLUMN_DATA = "data";
        public static final String DB_COLUMN_DATA_TYPE = "dataType";
        public static final String DB_COLUMN_RECV = "recv";
        public static final String DB_COLUMN_DONT_READ = "dontRead";
        public static final String DB_COLUMN_ACK = "ack";
        public static final String DB_COLUMN_STATE = "state";
        public static final String DB_COLUMN_RESEND_COUNT = "resendCount";
        public static final String DB_COLUMN_CREATETIME = "createtime";
        public static final String DB_COLUMN_SUM_DONT_READ = "sum_dont_read";
        public static final String DB_COLUMN_COUNT_ALL = "count_all";
    }

    public class ValidationColumn {
        public static final String DB_TABLE_VALIDATION = "VALIDATION";
        public static final String DB_COLUMN_ID = "_id";
        public static final String DB_COLUMN_NUMBER = "number";
        public static final String DB_COLUMN_CREATETIME = "createtime";
        public static final String DB_COLUMN_VALID_COUNT_ALL = "valid_count_all";
    }
}