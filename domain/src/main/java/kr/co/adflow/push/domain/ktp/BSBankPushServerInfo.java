package kr.co.adflow.push.domain.ktp;

import kr.co.adflow.push.domain.ServerInfo;

/**
 * @author nadir93
 * @date 2014. 7. 15.
 */
public class BSBankPushServerInfo extends ServerInfo {

	private boolean sms;

	public BSBankPushServerInfo() {
	}

	public BSBankPushServerInfo(boolean available, String message) {
		super(available, message);
	}

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

	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

}
