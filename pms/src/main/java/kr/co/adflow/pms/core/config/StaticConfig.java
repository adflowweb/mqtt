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
	public final static String API_CODE_534 = "534";
	public final static String API_CODE_550 = "550";
	public final static String API_CODE_551 = "551";
	public final static String API_CODE_552 = "552";
	public final static String API_CODE_553 = "553";
	public final static String API_CODE_570 = "570";
	public final static String API_CODE_571 = "571";
	public final static String API_CODE_572 = "572";
	public final static String API_CODE_573 = "573";
	public final static String API_CODE_580 = "580";
	public final static String API_CODE_581 = "581";
	public final static String API_CODE_582 = "582";
	public final static String API_CODE_583 = "583";
	public final static String API_CODE_590 = "590";

	public final static String ERROR_CODE_519000 = "519000";
	public final static String ERROR_CODE_539000 = "539000";
	public final static String ERROR_CODE_559000 = "559000";
	public final static String ERROR_CODE_579000 = "579000";
	public final static String ERROR_CODE_589000 = "589000";
	public final static String ERROR_CODE_599000 = "599000";

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

	public final static String SUCCESS_CODE_534 = "534200";
	public final static String ERROR_CODE_534400 = "534400";
	public final static String ERROR_CODE_534401 = "534401";
	public final static String ERROR_CODE_534404 = "534404";
	public final static String ERROR_CODE_534500 = "534500";

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

	public final static String SUCCESS_CODE_570 = "570200";
	public final static String ERROR_CODE_570400 = "570400";
	public final static String ERROR_CODE_570401 = "570401";
	public final static String ERROR_CODE_570404 = "570404";
	public final static String ERROR_CODE_570500 = "570500";

	public final static String SUCCESS_CODE_571 = "571200";
	public final static String ERROR_CODE_571400 = "571400";
	public final static String ERROR_CODE_571401 = "571401";
	public final static String ERROR_CODE_571404 = "571404";
	public final static String ERROR_CODE_571500 = "571500";

	public final static String SUCCESS_CODE_572 = "572200";
	public final static String ERROR_CODE_572400 = "572400";
	public final static String ERROR_CODE_572401 = "572401";
	public final static String ERROR_CODE_572404 = "572404";
	public final static String ERROR_CODE_572500 = "572500";

	public final static String SUCCESS_CODE_573 = "573200";
	public final static String ERROR_CODE_573400 = "573400";
	public final static String ERROR_CODE_573401 = "573401";
	public final static String ERROR_CODE_573404 = "573404";
	public final static String ERROR_CODE_573500 = "573500";

	public final static String SUCCESS_CODE_580 = "580200";
	public final static String ERROR_CODE_580400 = "580400";
	public final static String ERROR_CODE_580401 = "580401";
	public final static String ERROR_CODE_580404 = "580404";
	public final static String ERROR_CODE_580500 = "580500";

	public final static String SUCCESS_CODE_581 = "581200";
	public final static String ERROR_CODE_581400 = "581400";
	public final static String ERROR_CODE_581401 = "581401";
	public final static String ERROR_CODE_581404 = "581404";
	public final static String ERROR_CODE_581500 = "581500";

	public final static String SUCCESS_CODE_582 = "582200";
	public final static String ERROR_CODE_582400 = "582400";
	public final static String ERROR_CODE_582401 = "582401";
	public final static String ERROR_CODE_582404 = "582404";
	public final static String ERROR_CODE_582500 = "582500";

	public final static String SUCCESS_CODE_583 = "583200";
	public final static String ERROR_CODE_583400 = "583400";
	public final static String ERROR_CODE_583401 = "583401";
	public final static String ERROR_CODE_583404 = "583404";
	public final static String ERROR_CODE_583500 = "583500";

	public final static String SUCCESS_CODE_590 = "590200";
	public final static String ERROR_CODE_590400 = "590400";
	public final static String ERROR_CODE_590401 = "590401";
	public final static String ERROR_CODE_590404 = "590404";
	public final static String ERROR_CODE_590500 = "590500";

}
