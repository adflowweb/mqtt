package com.bns.pmc.util;

public class PMCType {
	public static final String TAG = "PMC";
	
	public static final String BNS_PMC_ACTIVITY_PACKAGE_MAIN = "com.bns.pmc.MainActivity";
	public static final String BNS_PMC_ACTIVITY_PACKAGE_NEW = "com.bns.pmc.NewActivity";
	public static final String BNS_PMC_ACTIVITY_PACKAGE_TALK = "com.bns.pmc.TalkActivity";
	public static final String BNS_PMC_ACTIVITY_PACKAGE_SET = "com.bns.pmc.SettingsActivity";

	public static final String BNS_PMC_SERVICE_CALLER = "com.bns.pmc.receiver.ServiceCaller";
	public static final String BNS_PMC_SERVICE_START_ACTION = "com.bns.action.PMC_START_SERVICE";
	public static final String BNS_PMC_SERVICE_REPEAT_ACTION = "com.bns.action.PMC_REPEAT_SERVICE";
	
	// New Msg Action
	public static final String BNS_PMC_ACTION_NEWMSG_CONTACT = "com.bns.pmc.action.newmsg.contact";
	public static final String BNS_PMC_ACTION_NEWMSG_NEW = "com.bns.pmc.action.newmsg.new";
	public static final String BNS_PMC_ACTION_NEWMSG_REPLY = "com.bns.pmc.action.newmsg.reply";
	public static final String BNS_PMC_ACTION_NEWMSG_FORWARDING = "com.bns.pmc.action.newmsg.forwarding";
	
	// 메신져 앱 간의 메시지 수신
	public static final String BNS_PMC_KTP_PUSH_USERMSG = "kr.co.ktpowertel.push.userMessage";
	// 관제시스템 메시지 수신
	public static final String BNS_PMC_KTP_PUSH_MMS = "kr.co.ktpowertel.push.mms";
	// F/W 업데이트 정보 수신
	public static final String BNS_PMC_KTP_PUSH_FWUPDATE_INFO = "kr.co.ktpowertel.push.fwUpdateInfo";
	// MQTT 세션
	public static final String BNS_PMC_KTP_PUSH_CONN_STATE = "kr.co.ktpowertel.push.connStatus";
	// DIG 계정 정보 수신
	public static final String BNS_PMC_KTP_PUSH_DIG = "kr.co.ktpowertel.push.digAccountInfo";
	
	public static final String BNS_PMC_PMCSERVICE_NOTIPOPUP = "com.bns.pmc.NotiPopupActivity";
	public static final String BNS_PMC_PMCSERVICE_NOTIFICATION = "com.bns.pmc.PMCService.Notification";
	
	public static final String BNS_PMC_ADFLOW_PUSH_PACKAGE = "kr.co.adflow.push";
	public static final String BNS_PMC_ADFLOW_PUSH_SERVICE_PUSHSERVICE = "kr.co.adflow.push.service.PushService";
	
	public static final String BNS_PMC_MMS_RECV_MSG_ID = "msgId";
	public static final String BNS_PMC_MMS_RECV_TOKEN = "token";
	public static final String BNS_PMC_MMS_RECV_ACK = "ack";
	public static final String BNS_PMC_MMS_RECV_CONTENT = "content";
	public static final String BNS_PMC_MMS_RECV_CONTENT_TYPE= "contentType";
	public static final String BNS_PMC_MMS_RECV_SENDER = "sender";
	public static final String BNS_PMC_MMS_RECV_RECEIVER = "receiver";
	public static final String BNS_PMC_MMS_RECV_RESEND_COUNT = "resendCount";
	
	public static final String BNS_PMC_MMS_DATA = "data";
	public static final String BNS_PMC_MMS_SND = "snd";
	public static final String BNS_PMC_MMS_RCV = "rcv";
	public static final String BNS_PMC_MMS_STR = "str";
	public static final String BNS_PMC_MMS_URL = "url";
	public static final String BNS_PMC_MMS_IMG = "img";
	public static final String BNS_PMC_MMS_MP3 = "mp3";
	public static final String BNS_PMC_MMS_NAME = "name";
	public static final String BNS_PMC_MMS_FORMAT = "format";
	
	public static final String BNS_PMC_INTENT_EXTRA_TITLE = "title";
	public static final String BNS_PMC_INTENT_EXTRA_SENDER = "sender";
	public static final String BNS_PMC_INTENT_EXTRA_CONTENT = "content";
	public static final String BNS_PMC_INTENT_EXTRA_NUMBER = "number";
	public static final String BNS_PMC_INTENT_EXTRA_CONTROL = "control";
	public static final String BNS_PMC_INTENT_EXTRA_ID = "_id";
	public static final String BNS_PMC_INTENT_EXTRA_IDX = "idx";
	
	public static final String BNS_PMC_PMA_RESULT_RESULT = "result";
	public static final String BNS_PMC_PMA_RESULT_SUCCESS = "success";
	public static final String BNS_PMC_PMA_RESULT_DATA = "data";
	public static final String BNS_PMC_PMA_RESULT_INFO = "info";
	public static final String BNS_PMC_PMA_RESULT_VALIDATION = "validation";
	
	public static final int BNS_PMC_NOTI_ID = 0x1234;
	// [0-9]{1,}(\\*)[0-9]{1,}
	public static final String BNS_PMC_REGEX_PTT = "[0-9*]{3,}";
	public static final String BNS_PMC_REGEX_GROUP_PTT = "#\\d{1,}";
	
	public static final int BNS_PMC_KEY_CODE_PTT_CALL = 224;
	
	public static final String BNS_PMC_LINK_TEL = "tel";
	public static final String BNS_PMC_LINK_PTT = "ptt";
	public static final String BNS_PMC_LINK_GROUP = "group";
	public static final String BNS_PMC_LINK_URL = "http";
	
	// MQTT
	public static final String BNS_PMC_MQTT_INTENT_EXTRA_EVENTCODE = "eventCode";
	public static final String BNS_PMC_MQTT_INTENT_EXTRA_EVENTMSG = "eventMsg";
	public static final String BNS_PMC_MQTT_INTENT_EXTRA_EVENTINFO = "eventInfo";
	
	public static final int BNS_PMC_MQTT_EVENT_CODE_CONN_START_SUCCESS = 1000; // 연결성공(PMA 시작후 최초 연결 성공)
	public static final int BNS_PMC_MQTT_EVENT_CODE_CONN_SUCCESS = 1001; // 연결 성공
	public static final int BNS_PMC_MQTT_EVENT_CODE_CONN_FAIL = 1002; // 연결 유실
	public static final int BNS_PMC_MQTT_EVENT_CODE_SIM_DONT_EXIST = 1003; // 유심 없음
	public static final int BNS_PMC_MQTT_EVENT_CODE_SIM_CHG = 1004; // 유심 변경

	// PTT LOGOUT
	public static final String BNS_PMC_PTT_REG_STATE_ACTION_LOGOUT = "com.bns.pw.action.REG_STATE";
	public static final String BNS_PMC_PTT_REG_STATE_EXTRA_VALUE = "reg";
	
	// DIG Broadcast
	public static final String BNS_PMC_PUSH_DIG_ACTION = "com.bns.pmc.action.digmsg.forwarding";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_PTALK_TYPE = "tp";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_ID = "id";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_PASSWORD = "pw";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_URL = "url";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_GROUP_CNT = "gc";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_GROUP = "gr";
	public static final String BNS_PMC_PUSH_DIG_EXTRA_BUNCH = "bu";
	
	// BNS PTT Action Name
	public static final String BNS_PMC_PTT_CALL_START_END_MODE_ACTION = "com.bns.ptt.daehap.a1.action.PTT_CALL_START_END_MODE";
	public static final String BNS_PMC_PTT_OUTCOMING_ACTION = "com.bns.ptt.daehap.a1.action.PTT_OUTCOMING";
	// PTT Calling mode
    public static final String BNS_PMC_PTT_CALL_START_END_MODE_EXTRA_VALUE = "PTTcallingmode";
    
	// UniCair Action Name
	public static final String UNI_PMC_PTT_SET_PTT_NUMBER_ACTION = "com.android.settings.deviceinfo.Status.pptnumber";
	public static final String UNI_PMC_PTT_SET_PTT_NUMBER_INFO_SYNC_ACTION = "com.bns.ptt.daehap.a1.action.PTT_GET_INFORMATION";
	// UniCair extra Name
	public static final String UNI_PMC_PTT_PTTFULLNUMBER_EXTRA_VALUE = "pptfullnumber";
	public static final String UNI_PMC_PTT_VERSION_EXTRA_VALUE = "pptversion";
	public static final String UNI_PMC_PTT_PTTGROUP_EXTRA_VALUE = "PTTgrouplist";
	public static final String UNI_PMC_PTT_BUNCHID_EXTRA_VALUE = "PTTbunchid"; 
	
	// api Type
    public static final int PMC_PTT_MODE_NONE            = 0;
    public static final int PMC_PTT_MODE_3PAPI           = 1;
    public static final int PMC_PTT_MODE_PTALK           = 2;
    
    // QuickPTT
    public static final String BNS_PMC_PTT_QUICK_EXTRA_VALUE = "PTTquick";
    public static final String BNS_PMC_PTT_QUICK_NAME_EXTRA_VALUE = "PTTName";
    public static final String BNS_PMC_PTT_QUICK_NUMBER_EXTRA_VALUE = "PTTNumber";
    
    // PTT type in Contacts
    public static final int BNS_PMC_CONTACT_PTT_TYPE = 21;
}
