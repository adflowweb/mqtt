package kr.co.adflow.push.auth.wmq;

import java.util.logging.Logger;

/* @start_prolog@
 * -----------------------------------------------------------------
 * IBM Websphere MQ MQTT Run MQTTV5 Sample Utility - Run script
 * Version: @(#) MQMBID sn=p750-001-130308 su=_h5hlwIgCEeKYL-Plq9baPw pn=com.ibm.mq.mqxr.listener/listenerSamplesSource/samples/JAASPrincipal.java  
 *
 *   <copyright 
 *   notice="lm-source-program" 
 *   pids="5724-H72," 
 *   years="2009,2012" 
 *   crc="317364299" > 
 *   Licensed Materials - Property of IBM  
 *    Ï
 *   5724-H72, 
 *    
 *   (C) Copyright IBM Corp. 2009, 2012 All Rights Reserved.  
 *    
 *   US Government Users Restricted Rights - Use, duplication or  
 *   disclosure restricted by GSA ADP Schedule Contract with  
 *   IBM Corp.  
 *   </copyright> 
 * -----------------------------------------------------------------
 */

public class JAASPrincipal implements java.security.Principal,
		java.io.Serializable {
	private static Logger logger = Logger.getLogger("JAASPrincipal");
	private static final long serialVersionUID = 1L;

	String name;

	public JAASPrincipal(String name) {
		logger.info("JAASPrincipal시작(name=" + name + ")");
		logger.info("thread=" + Thread.currentThread());
		this.name = name;
		//logger.info("JAASPrincipal초기화되었습니다." + this.hashCode());
		logger.info("JAASPrincipal종료");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		logger.info("getName시작()");
		logger.info("thread=" + Thread.currentThread());
		logger.info("getName종료(name=" + name + ")");
		return name;

	}

	public String toString() {
		logger.info("toString시작()");
		logger.info("thread=" + Thread.currentThread());
		logger.info("toString종료(name=" + name + ")");
		return (name);
	}

	public boolean equals(Object object) {
		logger.info("equals시작(object=" + object + ")");
		logger.info("thread=" + Thread.currentThread());
		if (object != null && object instanceof JAASPrincipal
				&& name.equals(((JAASPrincipal) object).getName())) {
			logger.info("toString종료(true)");
			return true;
		} else {
			logger.info("toString종료(false)");
			return false;
		}
	}

	public int hashCode() {
		logger.info("hashCode시작()");
		logger.info("thread=" + Thread.currentThread());
		logger.info("hashCode종료(name.hashCode=" + name.hashCode() + ")");
		return name.hashCode();
	}
}
