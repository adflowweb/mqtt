package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerInfo {

	private int cpu;
	private int memory;
	private int heap;
	private String hostName;
	private String ipAddress;

	private boolean available;
	private String message;

	/**
	 * @param available
	 * @param message
	 */
	public ServerInfo(boolean available, String message) {
		this.available = available;
		this.message = message;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getHeap() {
		return heap;
	}

	public void setHeap(int heap) {
		this.heap = heap;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ServerInfoResponseData [cpu=" + cpu + ", memory=" + memory
				+ ", heap=" + heap + ", hostName=" + hostName + ", ipAddress="
				+ ipAddress + ", available=" + available + ", message="
				+ message + "]";
	}

}
