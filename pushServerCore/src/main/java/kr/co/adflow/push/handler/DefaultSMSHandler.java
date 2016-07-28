/*
 * 
 */
package kr.co.adflow.push.handler;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import kr.co.adflow.push.service.HAService;

import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultSMSHandler.
 *
 * @author nadir93
 * @date 2014. 7. 8.
 */
public abstract class DefaultSMSHandler {

	// /** The Constant logger. */
	// private static final org.slf4j.Logger logger =
	// LoggerFactory.getLogger(DefaultSMSHandler.class);
	//
	// /** The Constant CONFIG_PROPERTIES. */
	// private static final String CONFIG_PROPERTIES = "/config.properties";
	//
	// /** The prop. */
	// private static Properties prop = new Properties();
	//
	// static {
	// try {
	// prop.load(DefaultSMSHandler.class.getResourceAsStream(CONFIG_PROPERTIES));
	// logger.debug("속성값=" + prop);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// /** The sms looper. */
	// protected ScheduledExecutorService smsLooper;
	//
	// // SMS처리 유무
	// /** The sms. */
	// protected boolean sms =
	// Boolean.parseBoolean(prop.getProperty("sms.enable"));
	// // sms처리주기
	// /** The sms interval. */
	// protected int smsInterval =
	// Integer.parseInt(prop.getProperty("sms.process.interval"));
	//
	// /** The first. */
	// private static boolean first = true; // thread name 세팅용
	//
	// /** The ha service. */
	// @Resource
	// HAService haService;
	//
	// /**
	// * initialize.
	// *
	// * @throws Exception
	// * the exception
	// */
	// @PostConstruct
	// public void initIt() throws Exception {
	// logger.info("DefaultSMSHandler초기화시작()");
	// logger.info("SMS처리유무=" + sms);
	// if (sms) {
	// smsLooper = Executors.newScheduledThreadPool(1);
	// smsLooper.scheduleWithFixedDelay(this, smsInterval, smsInterval,
	// TimeUnit.SECONDS);
	// logger.info("SMS핸들러가시작되었습니다.");
	// }
	// logger.info("DefaultSMSHandler초기화종료()");
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
	// if (sms) {
	// smsLooper.shutdown();
	// logger.info("SMS핸들러가종료되었습니다.");
	// }
	// logger.info("cleanUp종료()");
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Runnable#run()
	// */
	// @Override
	// public void run() {
	// if (first) {
	// final String orgName = Thread.currentThread().getName();
	// Thread.currentThread().setName("SMSPrecessing " + orgName);
	// first = !first;
	// }
	//
	// // 이중화요건
	// // 마스터이면 SMS처리
	// if (!haService.isActive()) {
	// logger.debug("마스터가아닙니다.");
	// return;
	// }
	//
	// doProcess();
	// }
	//
	// /**
	// * Do process.
	// */
	// protected abstract void doProcess();

}
