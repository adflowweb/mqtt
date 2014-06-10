package kr.co.adflow.push.domain;

import java.util.Arrays;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerInfo {

	private CPU cpu;
	private Memory memory;
	private Heap heap;
	private Disk[] disk;
	private double tps;
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

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public Heap getHeap() {
		return heap;
	}

	public void setHeap(Heap heap) {
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

	/**
	 * @return
	 */
	public Disk[] getDisk() {
		return disk;
	}

	/**
	 * @param disk
	 */
	public void setDisk(Disk[] disk) {
		this.disk = disk;
	}

	public double getTps() {
		return tps;
	}

	public void setTps(double tps) {
		this.tps = tps;
	}

	@Override
	public String toString() {
		return "ServerInfo [cpu=" + cpu + ", memory=" + memory + ", heap="
				+ heap + ", disk=" + Arrays.toString(disk) + ", tps=" + tps
				+ ", hostName=" + hostName + ", ipAddress=" + ipAddress
				+ ", available=" + available + ", message=" + message + "]";
	}

}
