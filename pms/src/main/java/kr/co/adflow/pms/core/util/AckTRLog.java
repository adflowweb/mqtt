package kr.co.adflow.pms.core.util;

import kr.co.adflow.pms.domain.Ack;

import org.apache.log4j.Logger;

public class AckTRLog {

	public static Logger logger = Logger.getLogger("ackLogger");

	public static void log(Ack ack) {
		logger.info(ack.toString());
	}
}
