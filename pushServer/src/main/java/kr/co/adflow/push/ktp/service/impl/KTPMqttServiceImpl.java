/*
 * 
 */
package kr.co.adflow.push.ktp.service.impl;

// TODO: Auto-generated Javadoc
/**
 * The Class KTPMqttServiceImpl.
 *
 * @author nadir93
 * @date 2014. 3. 21.
 */

public class KTPMqttServiceImpl {

	// /** The Constant logger. */
	// private static final org.slf4j.Logger logger =
	// LoggerFactory.getLogger(KTPMqttServiceImpl.class);
	//
	// // @Autowired
	// // @Qualifier("bsBanksqlSession")
	// // private SqlSession bsBanksqlSession;
	//
	// // private GroupMapper grpMapper;
	//
	// /**
	// * initialize.
	// *
	// * @throws Exception
	// * the exception
	// */
	// @PostConstruct
	// public void initIt() throws Exception {
	// super.initialize();
	// logger.info("KTPMqttServiceImpl초기화시작()");
	// // grpMapper = bsBanksqlSession.getMapper(GroupMapper.class);
	// logger.info("KTPkMqttServiceImpl초기화종료()");
	// }
	//
	// /**
	// * 모든리소스정리.
	// *
	// * @throws Exception
	// * the exception
	// */
	// @PreDestroy
	// public void cleanUp() throws Exception {
	// logger.info("cleanUp시작()");
	// destroy();
	// logger.info("cleanUp종료()");
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * kr.co.adflow.push.service.impl.AbstractMqttServiceImpl#receiveAck(java.
	// * lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	// */
	// @Override
	// protected void receiveAck(String topic, MqttMessage message) {
	// logger.debug("receiveAck시작(topic=" + topic + ", message=" + message +
	// ")");
	// try {
	// // db insert ack
	// // convert json string to object
	// Acknowledge ack = objectMapper.readValue(message.getPayload(),
	// Acknowledge.class);
	// msgMapper.postAck(ack);
	// logger.debug("ack메시지를등록하였습니다.");
	// } catch (Exception e) {
	// logger.error("에러발생", e);
	// }
	// logger.debug("receiveAck종료()");
	// }

}
