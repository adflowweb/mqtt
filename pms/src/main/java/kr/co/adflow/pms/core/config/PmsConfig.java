package kr.co.adflow.pms.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pmsConfig")
public class PmsConfig {

	@Value("#{pms['executor.server.id']}")
	public String EXECUTOR_SERVER_ID; // = "S01";
	
	@Value("#{pms['executor.delay.time']}")
	public int EXECUTOR_DELAY_TIME;// = 10000;
	
	@Value("#{pms['executor.send.limit']}")
	public int EXECUTOR_SEND_LIMIT ;//= 100;
	
	@Value("#{pms['header.application.token.expired']}")
	public int HEADER_APPLICATION_TOKEN_EXPIRED; // = 30;

	@Value("#{pms['message.header.type.default']}")
	public int MESSAGE_HEADER_TYPE_DEFAULT;// = 10;
	
	@Value("#{pms['message.header.expiry.default']}")
	public int MESSAGE_HEADER_EXPIRY_DEFAULT;// = 0;
	
	@Value("#{pms['message.header.qos.default']}")
	public int MESSAGE_HEADER_QOS_DEFAULT;// = 2;
	
	@Value("#{pms['message.size.limit.default']}")
	public int MESSAGE_SIZE_LIMIT_DEFAULT;// = 1024*500; //500kb
	
	@Value("#{pms['message.service.id.default']}")
	public String MESSAGE_SERVICE_ID_DEFAULT;// = "kr.co.ktpowertel.push.mms";
	
	@Value("#{pms['message.ack.default']}")
	public boolean MESSAGE_ACK_DEFAULT;// = true;

	//public static final String EXECUTOR_MESSAGE_CRON = "0,10,20,30,40,50 * * * * *";
	//public static final String EXECUTOR_RESERVATION_CRON = "3,13,23,33,43,53 * * * * *";
	//public static final String EXECUTOR_CALLBACK_CRON = "6,16,26,36,46,56 * * * * *";
	//public static final String EXECUTOR_CREATE_TABLE_CRON = "0 0 12 * * *";



}
