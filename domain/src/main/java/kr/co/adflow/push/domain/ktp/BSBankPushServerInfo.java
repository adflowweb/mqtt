/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

import kr.co.adflow.push.domain.ServerInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class BSBankPushServerInfo.
 *
 * @author nadir93
 * @date 2014. 7. 15.
 */
public class BSBankPushServerInfo extends ServerInfo {

	/** The sms. */
	private boolean sms;

	/**
	 * Instantiates a new BS bank push server info.
	 */
	public BSBankPushServerInfo() {
	}

	/**
	 * Instantiates a new BS bank push server info.
	 *
	 * @param available the available
	 * @param message the message
	 */
	public BSBankPushServerInfo(boolean available, String message) {
		super(available, message);
	}

	/**
	 * Instantiates a new BS bank push server info.
	 *
	 * @param info the info
	 */
	public BSBankPushServerInfo(ServerInfo info) {

		// private CPU cpu;
		// private Memory memory;
		// private Heap heap;
		// private Disk[] disk;
		// private double tps;
		// private String hostName;
		// private String ipAddress;
		//
		// private boolean available;
		// private String message;

		this.setCpu(info.getCpu());
		this.setMemory(info.getMemory());
		this.setHeap(info.getHeap());
		this.setDisk(info.getDisk());
		this.setTps(info.getTps());
		this.setHostName(info.getHostName());
		this.setIpAddress(info.getIpAddress());
		this.setAvailable(info.isAvailable());
		this.setMessage(info.getMessage());
	}

	/**
	 * Checks if is sms.
	 *
	 * @return true, if is sms
	 */
	public boolean isSms() {
		return sms;
	}

	/**
	 * Sets the sms.
	 *
	 * @param sms the new sms
	 */
	public void setSms(boolean sms) {
		this.sms = sms;
	}

}
