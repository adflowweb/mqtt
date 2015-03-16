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
	@Value("#{pms['executor.server.id1']}")
	public String EXECUTOR_SERVER_ID1; // = "S01";
	
	/** The executor server id. */
	@Value("#{pms['executor.server.id2']}")
	public String EXECUTOR_SERVER_ID2;

	@Value("#{pms['executor.server.id3']}")
	public String EXECUTOR_SERVER_ID3;

	@Value("#{pms['executor.server.id4']}")
	public String EXECUTOR_SERVER_ID4;

	@Value("#{pms['executor.server.id5']}")
	public String EXECUTOR_SERVER_ID5;

	@Value("#{pms['executor.server.id6']}")
	public String EXECUTOR_SERVER_ID6;
	
	/** The executor delay time. */
	@Value("#{pms['executor.delay.time']}")
	public int EXECUTOR_DELAY_TIME;// = 10000;

	/** The executor send limit. */
	@Value("#{pms['executor.send.limit']}")
	public int EXECUTOR_SEND_LIMIT;// = 100;

	/** The header application token expired. */
	@Value("#{pms['header.application.token.expired']}")
	public int HEADER_APPLICATION_TOKEN_EXPIRED; // = 30;

	/** The message header type default. */
	@Value("#{pms['message.header.type.default']}")
	public int MESSAGE_HEADER_TYPE_DEFAULT;// = 10;

	/** The message header expiry default. */
	@Value("#{pms['message.header.expiry.default']}")
	public int MESSAGE_HEADER_EXPIRY_DEFAULT;// = 0;

	/** The message header qos default. */
	@Value("#{pms['message.header.qos.default']}")
	public int MESSAGE_HEADER_QOS_DEFAULT;// = 2;

	/** The message size limit default. */
	@Value("#{pms['message.size.limit.default']}")
	public int MESSAGE_SIZE_LIMIT_DEFAULT;// = 1024*500; //500kb

	/** The message service id default. */
	@Value("#{pms['message.service.id.default']}")
	public String MESSAGE_SERVICE_ID_DEFAULT;// = "kr.co.ktpowertel.push.mms";

	/** The message ack default. */
	@Value("#{pms['message.ack.default']}")
	public boolean MESSAGE_ACK_DEFAULT;// = true;

	
	@Value("#{pms['message.csv.limit.default']}")
	public int MESSAGE_CSV_LIMIT_DEFAULT;// = 100000
	
	// public static final String EXECUTOR_MESSAGE_CRON =
	// "0,10,20,30,40,50 * * * * *";
	// public static final String EXECUTOR_RESERVATION_CRON =
	// "3,13,23,33,43,53 * * * * *";
	// public static final String EXECUTOR_CALLBACK_CRON =
	// "6,16,26,36,46,56 * * * * *";
	// public static final String EXECUTOR_CREATE_TABLE_CRON = "0 0 12 * * *";

}
