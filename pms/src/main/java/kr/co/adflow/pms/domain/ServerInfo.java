/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Arrays;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerInfo.
 * 
 * @author nadir93
 * @date 2014. 4. 14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerInfo {

	/** The cpu. */
	private CPU cpu;

	/** The memory. */
	private Memory memory;

	/** The heap. */
	private Heap heap;

	/** The disk. */
	private Disk[] disk;

	/** The tps. */
	private double tps;

	/** The host name. */
	private String hostName;

	/** The ip address. */
	private String ipAddress;

	/** The available. */
	private boolean available;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new server info.
	 */
	public ServerInfo() {
	}

	/**
	 * Instantiates a new server info.
	 * 
	 * @param available
	 *            the available
	 * @param message
	 *            the message
	 */
	public ServerInfo(boolean available, String message) {
		this.available = available;
		this.message = message;
	}

	/**
	 * Gets the cpu.
	 * 
	 * @return the cpu
	 */
	public CPU getCpu() {
		return cpu;
	}

	/**
	 * Sets the cpu.
	 * 
	 * @param cpu
	 *            the new cpu
	 */
	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	/**
	 * Gets the memory.
	 * 
	 * @return the memory
	 */
	public Memory getMemory() {
		return memory;
	}

	/**
	 * Sets the memory.
	 * 
	 * @param memory
	 *            the new memory
	 */
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * Gets the heap.
	 * 
	 * @return the heap
	 */
	public Heap getHeap() {
		return heap;
	}

	/**
	 * Sets the heap.
	 * 
	 * @param heap
	 *            the new heap
	 */
	public void setHeap(Heap heap) {
		this.heap = heap;
	}

	/**
	 * Gets the host name.
	 * 
	 * @return the host name
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Sets the host name.
	 * 
	 * @param hostName
	 *            the new host name
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Gets the ip address.
	 * 
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the ip address.
	 * 
	 * @param ipAddress
	 *            the new ip address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Checks if is available.
	 * 
	 * @return true, if is available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * Sets the available.
	 * 
	 * @param available
	 *            the new available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the disk.
	 * 
	 * @return the disk
	 */
	public Disk[] getDisk() {
		return disk;
	}

	/**
	 * Sets the disk.
	 * 
	 * @param disk
	 *            the new disk
	 */
	public void setDisk(Disk[] disk) {
		this.disk = disk;
	}

	/**
	 * Gets the tps.
	 * 
	 * @return the tps
	 */
	public double getTps() {
		return tps;
	}

	/**
	 * Sets the tps.
	 * 
	 * @param tps
	 *            the new tps
	 */
	public void setTps(double tps) {
		this.tps = tps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerInfo [cpu=" + cpu + ", memory=" + memory + ", heap="
				+ heap + ", disk=" + Arrays.toString(disk) + ", tps=" + tps
				+ ", hostName=" + hostName + ", ipAddress=" + ipAddress
				+ ", available=" + available + ", message=" + message + "]";
	}

}
