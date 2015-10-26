/*
 * 
 */
package kr.co.adflow.pms.core.config;

// TODO: Auto-generated Javadoc
/**
 * The Class StaticConfig.
 */
public class StaticConfig {

	/** The Constant HEADER_APPLICATION_TOKEN. */
	public static final String HEADER_APPLICATION_TOKEN = "X-Application-Token";

	/** The Constant HEADER_APPLICATION_KEY. */
	public static final String HEADER_APPLICATION_KEY = "X-Application-Key";

	/** The Constant HEADER_CONTENT_TYPE. */
	public static final String HEADER_CONTENT_TYPE = "application/json";

	/** The Constant INTERCEPTER_IP_FILTER. */
	public static final String INTERCEPTER_IP_FILTER = "0.0.0.0";

	/** The Constant INTERCEPTER_IP_FILTER_DELIM. */
	public static final String INTERCEPTER_IP_FILTER_DELIM = "/";

	/** The Constant MESSAGE_STATUS_RECEIVER_NOT_FOUNT. */
	public static final int MESSAGE_STATUS_RECEIVER_NOT_FOUNT = -2;

	/** The Constant MESSAGE_STATUS_COUNT_OVER. */
	public static final int MESSAGE_STATUS_COUNT_OVER = -1;

	/** The Constant MESSAGE_STATUS_ABNORMAL. */
	public static final int MESSAGE_STATUS_ABNORMAL = -99;

	/** The Constant MESSAGE_STATUS_SENDING. */
	public static final int MESSAGE_STATUS_SENDING = 0;

	/** The Constant MESSAGE_STATUS_SEND. */
	public static final int MESSAGE_STATUS_SEND = 1;

	/** The Constant MESSAGE_STATUS_RESEVATION_CANCEL. */
	public static final int MESSAGE_STATUS_RESEVATION_CANCEL = 2;

	/** The Constant USER_ROLE_SERVICE_ADMIN. */
	public static final String USER_ROLE_SERVICE_ADMIN = "svcadm";

	/** The Constant USER_ROLE_SERVICE. */
	public static final String USER_ROLE_SERVICE = "svc";

	/** The Constant USER_ROLE_INTERFACE. */
	public static final String USER_ROLE_INTERFACE = "inf";

	/** The Constant USER_ROLE_SYSTEM. */
	public static final String USER_ROLE_SYSTEM = "sys";

	/** The Constant USER_STATUS_NORMAL. */
	public static final int USER_STATUS_NORMAL = 0;

	/** The Constant USER_STATUS_ABNORMAL. */
	public static final int USER_STATUS_ABNORMAL = -1;

	/** The Constant USER_STATUS_BROCK. */
	public static final int USER_STATUS_BROCK = 2;

	/** The Constant TOKEN_TYPE_APPLICATION. */
	public static final String TOKEN_TYPE_APPLICATION = "A";

	/** The Constant TOKEN_TYPE_TOKEN. */
	public static final String TOKEN_TYPE_TOKEN = "T";

	/** The Constant SERVICE_REQUEST_FORMAT_TYPE_ERROR. */
	public static final int SERVICE_REQUEST_FORMAT_TYPE_ERROR = -1;

	/** The Constant SERVICE_REQUEST_FORMAT_TYPE_UFMI1. */
	public static final int SERVICE_REQUEST_FORMAT_TYPE_UFMI1 = 1;

	/** The Constant SERVICE_REQUEST_FORMAT_TYPE_UFMI2. */
	public static final int SERVICE_REQUEST_FORMAT_TYPE_UFMI2 = 2;

	/** The Constant SERVICE_REQUEST_FORMAT_TYPE_PHONE. */
	public static final int SERVICE_REQUEST_FORMAT_TYPE_PHONE = 3;

	/** The Constant CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE. */
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_MESSAGE = "message";

	/** The Constant CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION. */
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_RESERVATION = "reservation";

	/** The Constant CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK. */
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK = "callback";

	/** The Constant CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK_APP. */
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK_APP = "callback_app";

	/** The Constant CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK_PMA. */
	public static final String CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK_PMA = "callback_pma";

	public static final String RESPONSE_STATUS_OK = "ok";
	public static final String RESPONSE_STATUS_FAIL = "fail";
	public static final String RESPONSE_MESSAGE_EXCEPTION_CODE = "500000";
	public static final String USER_GROUP_CODE = "10000";

	public static final int MESSAGE_TYPE_USER = 100;
	public static final int MESSAGE_TYPE_KEEPALIVE = 200;
	/* Interface Code */
	// 토큰 생성 API Code
	public final static String API_CODE_510 = "510";
	public final static String API_CODE_511 = "511";
	public final static String API_CODE_531 = "531";
	public final static String API_CODE_532 = "532";
	public final static String API_CODE_533 = "533";
	public final static String API_CODE_550 = "550";
	public final static String API_CODE_551 = "551";
	public final static String API_CODE_552 = "552";
	public final static String API_CODE_553 = "553";

	public final static String ERROR_CODE_519000 = "519000";
	public final static String ERROR_CODE_539000 = "539000";
	public final static String ERROR_CODE_559000 = "559000";

	public final static String SUCCESS_CODE_510 = "510200";
	public final static String ERROR_CODE_510400 = "510400";
	public final static String ERROR_CODE_510401 = "510401";
	public final static String ERROR_CODE_510500 = "510500";

	public final static String SUCCESS_CODE_511 = "511200";
	public final static String ERROR_CODE_511400 = "511400";
	public final static String ERROR_CODE_511401 = "511401";
	public final static String ERROR_CODE_511404 = "511404";
	public final static String ERROR_CODE_511500 = "511500";

	public final static String SUCCESS_CODE_512 = "512200";
	public final static String ERROR_CODE_512400 = "512400";
	public final static String ERROR_CODE_512401 = "512401";
	public final static String ERROR_CODE_512500 = "512500";

	public final static String SUCCESS_CODE_513 = "513200";
	public final static String ERROR_CODE_513404 = "513404";
	public final static String ERROR_CODE_513401 = "513401";
	public final static String ERROR_CODE_513500 = "513500";

	public final static String SUCCESS_CODE_514 = "514200";
	public final static String ERROR_CODE_514404 = "514404";
	public final static String ERROR_CODE_514401 = "514401";
	public final static String ERROR_CODE_514500 = "514500";

	public final static String SUCCESS_CODE_530 = "530200";
	public final static String ERROR_CODE_530401 = "530401";
	public final static String ERROR_CODE_530404 = "530404";
	public final static String ERROR_CODE_530500 = "530500";

	public final static String SUCCESS_CODE_531 = "531200";
	public final static String ERROR_CODE_531401 = "531401";
	public final static String ERROR_CODE_531404 = "531404";
	public final static String ERROR_CODE_531500 = "531500";

	public final static String SUCCESS_CODE_532 = "532200";
	public final static String ERROR_CODE_532401 = "532401";
	public final static String ERROR_CODE_532404 = "532404";
	public final static String ERROR_CODE_532500 = "532500";

	public final static String SUCCESS_CODE_533 = "533200";
	public final static String ERROR_CODE_533401 = "533401";
	public final static String ERROR_CODE_533404 = "533404";
	public final static String ERROR_CODE_533500 = "533500";

	public final static String SUCCESS_CODE_550 = "550200";
	public final static String ERROR_CODE_550400 = "550400";
	public final static String ERROR_CODE_550401 = "550401";
	public final static String ERROR_CODE_550404 = "550404";
	public final static String ERROR_CODE_550500 = "550500";

	public final static String SUCCESS_CODE_551 = "551200";
	public final static String ERROR_CODE_551400 = "551400";
	public final static String ERROR_CODE_551401 = "551401";
	public final static String ERROR_CODE_551404 = "551404";
	public final static String ERROR_CODE_551500 = "551500";

	public final static String SUCCESS_CODE_552 = "552200";
	public final static String ERROR_CODE_552400 = "552400";
	public final static String ERROR_CODE_552401 = "552401";
	public final static String ERROR_CODE_552404 = "552404";
	public final static String ERROR_CODE_552500 = "552500";

	public final static String SUCCESS_CODE_553 = "553200";
	public final static String ERROR_CODE_553400 = "553400";
	public final static String ERROR_CODE_553401 = "553401";
	public final static String ERROR_CODE_553404 = "553404";
	public final static String ERROR_CODE_553500 = "553500";

}
