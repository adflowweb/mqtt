/*
 * 
 */
package kr.co.adflow.pms.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Disk.
 * 
 * @author nadir93
 * @date 2014. 6. 9.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Disk {

	/** The File system. */
	private String FileSystem;

	/** The size. */
	private String size;

	/** The used. */
	private String used;

	/** The avail. */
	private String avail;

	/** The use pct. */
	private String usePct;

	/** The mounted. */
	private String mounted;

	/** The type. */
	private String type;

	/**
	 * Gets the file system.
	 * 
	 * @return the file system
	 */
	public String getFileSystem() {
		return FileSystem;
	}

	/**
	 * Sets the file system.
	 * 
	 * @param fileSystem
	 *            the new file system
	 */
	public void setFileSystem(String fileSystem) {
		FileSystem = fileSystem;
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 * 
	 * @param size
	 *            the new size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Gets the used.
	 * 
	 * @return the used
	 */
	public String getUsed() {
		return used;
	}

	/**
	 * Sets the used.
	 * 
	 * @param used
	 *            the new used
	 */
	public void setUsed(String used) {
		this.used = used;
	}

	/**
	 * Gets the avail.
	 * 
	 * @return the avail
	 */
	public String getAvail() {
		return avail;
	}

	/**
	 * Sets the avail.
	 * 
	 * @param avail
	 *            the new avail
	 */
	public void setAvail(String avail) {
		this.avail = avail;
	}

	/**
	 * Gets the use pct.
	 * 
	 * @return the use pct
	 */
	public String getUsePct() {
		return usePct;
	}

	/**
	 * Sets the use pct.
	 * 
	 * @param usePct
	 *            the new use pct
	 */
	public void setUsePct(String usePct) {
		this.usePct = usePct;
	}

	/**
	 * Gets the mounted.
	 * 
	 * @return the mounted
	 */
	public String getMounted() {
		return mounted;
	}

	/**
	 * Sets the mounted.
	 * 
	 * @param mounted
	 *            the new mounted
	 */
	public void setMounted(String mounted) {
		this.mounted = mounted;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Disk [FileSystem=" + FileSystem + ", size=" + size + ", used="
				+ used + ", avail=" + avail + ", usePct=" + usePct
				+ ", mounted=" + mounted + ", type=" + type + "]";
	}

}
