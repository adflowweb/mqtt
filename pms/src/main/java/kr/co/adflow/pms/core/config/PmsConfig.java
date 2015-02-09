package kr.co.adflow.pms.core.config;

public class PmsConfig {

	public static final int EXECUTOR_DELAY_TIME = 10000;
	public static final int EXECUTOR_SEND_LIMIT = 100;
	public static final String EXECUTOR_SERVER_ID = "S01";

	public static final String HEADER_APPLICATION_TOKEN = "X-Application-Token";
	public static final String HEADER_APPLICATION_KEY = "X-Application-Key";
	public static final String HEADER_CONTENT_TYPE = "application/json";
	public static final int HEADER_APPLICATION_TOKEN_EXPIRED = 30;
	
	
	public static final String INTERCEPTER_IP_FILTER = "0.0.0.0";

	public static final int MESSAGE_HEADER_TYPE_DEFAULT = 20;
	public static final int MESSAGE_HEADER_EXPIRY_DEFAULT = 0;
	public static final int MESSAGE_HEADER_QOS_DEFAULT = 2;
	public static final String MESSAGE_SERVICE_ID_DEFAULT = "kr.co.ktpowertel.push.mms";

	public static final int MESSAGE_STATUS_ABNORMAL = -1;
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

}
