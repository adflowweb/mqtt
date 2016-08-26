/*
 * 
 */
package kr.co.adflow.pms.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class PmsConfig.
 */
@Component("pmsConfig")
public class PmsConfig {

	/** The executor server id. */
	@Value("#{pms['executor.server.old.pms.id']}")
	public String EXECUTOR_SERVER_OLD_PMS_ID; // = "올드버전의 아이디를 자신의 아이디로 사용 config 공유문";

	@Value("#{pms['executor.server.id1']}")
	public String EXECUTOR_SERVER_ID1; // = "new pms 1";

	@Value("#{pms['executor.server.id2']}")
	public String EXECUTOR_SERVER_ID2; // = "new pms 2";

	/** The executor delay time. */
	@Value("#{pms['executor.delay.time']}")
	public int EXECUTOR_DELAY_TIME;// = 10000;

	/** The executor send limit. */
	@Value("#{pms['executor.send.limit']}")
	public int EXECUTOR_SEND_LIMIT;// = 100;

	/** The header application token expired. */
	@Value("#{pms['header.application.token.expired']}")
	public int HEADER_APPLICATION_TOKEN_EXPIRED; // = 180;

	/** The message header type default. */
	@Value("#{pms['message.header.type.default']}")
	public int MESSAGE_HEADER_TYPE_DEFAULT;// = 10;

	/** The message header expiry default. */
	@Value("#{pms['message.header.expiry.default']}")
	public int MESSAGE_HEADER_EXPIRY_DEFAULT;// = 600000;

	/** The message header qos default. */
	@Value("#{pms['message.header.qos.default']}")
	public int MESSAGE_HEADER_QOS_DEFAULT;// = 2;

	/** The message size limit default. */
	@Value("#{pms['message.size.limit.default']}")
	public int MESSAGE_SIZE_LIMIT_DEFAULT;// = 1024*500; //500kb

	/** The message service id default. */
	@Value("#{pms['message.service.id.default']}")
	public String MESSAGE_SERVICE_ID_DEFAULT;// = "kr.co.ktpowertel.push.mms";

	/** The message service id userMessage. */
	@Value("#{pms['message.service.id.userMessage']}")
	public String MESSAGE_SERVICE_ID_USERMESSAGE;// =
													// "kr.co.ktpowertel.push.userMessage";

	/** The message service id userMessage. */
	@Value("#{pms['message.user.message.expiry.default']}")
	public int MESSAGE_USER_MESSAGE_EXPIRY_DEFAULT;// = 600000;

	/** The message ack default. */
	@Value("#{pms['message.ack.default']}")
	public boolean MESSAGE_ACK_DEFAULT;// = true;

	@Value("#{pms['message.csv.limit.default']}")
	public int MESSAGE_CSV_LIMIT_DEFAULT;// = 100000

	/** The cdr.file.path. */
	@Value("#{pms['cdr.file.path']}")
	public String CDR_FILE_PATH;// = /cdrData/;

	/** The cdr.targetfile.path. */
	@Value("#{pms['cdr.targetfile.path']}")
	public String CDR_TARGETFILE_PATH;// = /root/;

	/** The cdr.file.max.row. */
	@Value("#{pms['cdr.file.max.row']}")
	public int CDR_FILE_MAX_ROW;// = 30000;

	/** The cdr.db.max.row. */
	@Value("#{pms['cdr.db.max.row']}")
	public int CDR_DB_MAX_ROW;// = 1000;

	@Value("#{pms['zookeeper.url']}")
	public String ZOOKEEPER_URL;

	@Value("#{pms['zookeeper.node']}")
	public String ZOOKEEPER_NODE;

	@Value("#{pms['zookeeper.old.id']}") //구버전의경우 올드아이디를 사용
	public String ZOOKEEPER_ID;

	// /** The mq.pcf.hostname. */
	// @Value("#{pms['mq.pcf.hostname']}")
	// public String MQ_PCF_HOSTNAME;// = "14.63.217.141;
	//
	// /** The mq.pcf.port. */
	// @Value("#{pms['mq.pcf.port']}")
	// public int MQ_PCF_PORT;// = 1414;
	//
	// /** The mq.pcf.channel. */
	// @Value("#{pms['mq.pcf.channel']}")
	// public String MQ_PCF_CHANNEL;// = "ADFlowPCF";
	//
	// /** The mq.pcf.userID. */
	// @Value("#{pms['mq.pcf.userID']}")
	// public String MQ_PCF_USERID;// = "adflow";
	//
	// /** The mq.pcf.password. */
	// @Value("#{pms['mq.pcf.password']}")
	// public String MQ_PCF_PASSWORD;// = "!ADFlow@";

	// public static final String EXECUTOR_MESSAGE_CRON =
	// "0,10,20,30,40,50 * * * * *";
	// public static final String EXECUTOR_RESERVATION_CRON =
	// "3,13,23,33,43,53 * * * * *";
	// public static final String EXECUTOR_CALLBACK_CRON =
	// "6,16,26,36,46,56 * * * * *";
	// public static final String EXECUTOR_CREATE_TABLE_CRON = "0 0 12 * * *";

}
