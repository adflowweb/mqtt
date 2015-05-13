package kr.co.adflow.pms.core.util;

import kr.co.adflow.pms.domain.Message;

import org.apache.log4j.Logger;

public class MessageTRLog {

 public static Logger logger = Logger.getLogger("messageTrLogger");

 public static void log(Message msg) {
 logger.info(msg.toString());
 }
}
