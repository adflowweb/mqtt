package kr.co.adflow.pms.core.config;

public class PmsConfig {

	public static final String EXECUTOR_SERVER_ID = "S01";
	public static final int EXECUTOR_DELAY_TIME = 10000;
	public static final int EXECUTOR_SEND_LIMIT = 100;
	
	public static final String EXECUTOR_MESSAGE_CRON = "0,10,20,30,40,50 * * * * *";
	public static final String EXECUTOR_RESERVATION_CRON = "3,13,23,33,43,53 * * * * *";
	public static final String EXECUTOR_CALLBACK_CRON = "6,16,26,36,46,56 * * * * *";
	public static final String EXECUTOR_CREATE_TABLE_CRON = "0 0 12 * * *";

	public static final String HEADER_APPLICATION_TOKEN = "X-Application-Token";
	public static final String HEADER_APPLICATION_KEY = "X-Application-Key";
	public static final String HEADER_CONTENT_TYPE = "application/json";
	public static final int HEADER_APPLICATION_TOKEN_EXPIRED = 30;
	
	
	public static final String INTERCEPTER_IP_FILTER = "0.0.0.0";
	public static final String INTERCEPTER_IP_FILTER_DELIM = "/";

	public static final int MESSAGE_HEADER_TYPE_DEFAULT = 10;
	public static final int MESSAGE_HEADER_EXPIRY_DEFAULT = 0;
	public static final int MESSAGE_HEADER_QOS_DEFAULT = 2;
	public static final int MESSAGE_SIZE_LIMIT_DEFAULT = 1048576; //1MB
	public static final boolean MESSAGE_ACK_DEFAULT = true;
	public static final String MESSAGE_SERVICE_ID_DEFAULT = "kr.co.ktpowertel.push.mms";

	public static final int MESSAGE_STATUS_RECEIVER_NOT_FOUNT = -2;
	public static final int MESSAGE_STATUS_COUNT_OVER = -1;
	public static final int MESSAGE_STATUS_ABNORMAL = -99;
	public static final int MESSAGE_STATUS_SENDING = 0;
	public static final int MESSAGE_STATUS_SEND = 1;
	public static final int MESSAGE_STATUS_RESEVATION_CANCEL = 2;
	
	public static final String USER_ROLE_SERVICE_ADMIN = "svcadm";
	public static final String USER_ROLE_SERVICE = "svc";
	public static final String USER_ROLE_INTERFACE = "inf";
	public static final String USER_ROLE_SYSTEM = "sys";

	public static final int USER_STATUS_NORMAL = 0;
	public static final int USER_STATUS_ABNORMAL = -1;
	public static final int USER_STATUS_BROCK = 2;

	
	public static final String TOKEN_TYPE_APPLICATION = "A";
	public static final String TOKEN_TYPE_TOKEN = "T";
	
	public static final int SERVICE_REQUEST_FORMAT_TYPE_ERROR = -1;
	public static final int SERVICE_REQUEST_FORMAT_TYPE_UFMI1 = 1;
	public static final int SERVICE_REQUEST_FORMAT_TYPE_UFMI2 = 2;
	public static final int SERVICE_REQUEST_FORMAT_TYPE_PHONE = 3;
	
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE = "message";
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION = "reservation";
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK = "callback";

}
