/*
 * 
 */
package kr.co.adflow.push.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.HADao;
import kr.co.adflow.push.domain.HA;
import kr.co.adflow.push.service.HAService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class HAServiceImpl.
 *
 * @author nadir93
 * @date 2014. 7. 24.
 */
// @Service
public class HAServiceImpl {

	// /** The Constant logger. */
	// private static final org.slf4j.Logger logger = LoggerFactory
	// .getLogger(HAServiceImpl.class);
	//
	// /** The Constant CONFIG_PROPERTIES. */
	// private static final String CONFIG_PROPERTIES = "/config.properties";
	//
	// /** The prop. */
	// private static Properties prop = new Properties();
	//
	// static {
	// try {
	// prop.load(HAServiceImpl.class
	// .getResourceAsStream(CONFIG_PROPERTIES));
	// logger.debug("속성값=" + prop);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// /** The takeover time. */
	// private int takeoverTime = Integer.parseInt(prop
	// .getProperty("ha.takeover.interval"));
	//
	// /** The cal. */
	// private Calendar cal = Calendar.getInstance();
	//
	// /** The ha dao. */
	// @Resource
	// private HADao haDao;
	//
	// /** The active. */
	// public static boolean active;
	//
	// /* (non-Javadoc)
	// * @see kr.co.adflow.push.service.HAService#isActive()
	// */
	// @Override
	// public boolean isActive() {
	// return active;
	// }
	//
	// /* (non-Javadoc)
	// * @see kr.co.adflow.push.service.HAService#check()
	// */
	// @Override
	// public void check() throws Exception {
	// logger.debug("check시작()");
	// String hostName = InetAddress.getLocalHost().getHostName();
	// logger.debug("hostName=" + hostName + ")");
	// HA ha = haDao.get();
	// logger.debug("ha=" + ha + ")");
	//
	// cal.setTime(new Date());
	// cal.add(cal.MINUTE, -takeoverTime); // 3분전
	// Date time = cal.getTime();
	// logger.debug("테이크오버기준시간=" + time + ")");
	//
	// if (ha == null) {
	// logger.info("최초마스터등록합니다.");
	// // 최초 인서트
	// active = true;
	// // insert
	// HA h = new HA();
	// h.setHostname(hostName);
	// h.setLastime(new Date());
	// h.setStatus(0);
	// int rst = haDao.post(h);
	// logger.debug("insert=" + rst + ")");
	// } else if (hostName.equals(ha.getHostname())) {
	// logger.info("이미마스터입니다.");
	// // 이미 마스터인경우
	// active = true;
	// // update
	// HA h = new HA();
	// h.setHostname(hostName);
	// h.setLastime(new Date());
	// h.setStatus(0);
	// int rst = haDao.put(h);
	// logger.debug("update=" + rst + ")");
	// } else if (time.before(ha.getLastime())) {
	// logger.info("상대서버가마스터입니다.");
	// // 마스터가 아니고 마스터가 살아있는경우
	// active = false;
	// } else {
	// logger.info("마스터서버가죽어takeOver합니다.");
	// // 마스터가 죽은경우(failOver)
	// active = true;
	// // update
	// HA h = new HA();
	// h.setHostname(hostName);
	// h.setLastime(new Date());
	// h.setStatus(0);
	// int rst = haDao.put(h);
	// logger.debug("update=" + rst + ")");
	// }
	// logger.debug("check종료()");
	// }
}
