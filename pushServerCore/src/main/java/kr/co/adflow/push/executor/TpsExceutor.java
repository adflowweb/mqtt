package kr.co.adflow.push.executor;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//TODO: Auto-generated Javadoc
/**
 * The Class TpsExceutor.
 */
@Component("tpsExceutor")
public class TpsExceutor {
	/** The Constant logger. */
	// private static final org.slf4j.Logger logger =
	// LoggerFactory.getLogger(TpsExceutor.class);

	public static double digCnt = 0;
	public static double preCnt = 0;
	public static double digTps = 0;
	public static double preTps = 0;

	/**
	 * checkTPS.
	 */
	public void checkTPS() {

		// logger.debug("digCnt::"+digCnt);
		// logger.debug("preCnt::"+preCnt);
		digTps = digCnt;
		preTps = preCnt;
		// logger.debug("digTps::"+digTps);
		// logger.debug("preTps::"+preTps);
		// logger.debug("chananananan");
		digCnt = 0;
		preCnt = 0;
	}

}
