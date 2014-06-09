package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 6. 9.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Disk {

	private String FileSystem;
	private String size;
	private String used;
	private String avail;
	private String usePct;
	private String mounted;
	private String type;

	public String getFileSystem() {
		return FileSystem;
	}

	public void setFileSystem(String fileSystem) {
		FileSystem = fileSystem;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getAvail() {
		return avail;
	}

	public void setAvail(String avail) {
		this.avail = avail;
	}

	public String getUsePct() {
		return usePct;
	}

	public void setUsePct(String usePct) {
		this.usePct = usePct;
	}

	public String getMounted() {
		return mounted;
	}

	public void setMounted(String mounted) {
		this.mounted = mounted;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Disk [FileSystem=" + FileSystem + ", size=" + size + ", used="
				+ used + ", avail=" + avail + ", usePct=" + usePct
				+ ", mounted=" + mounted + ", type=" + type + "]";
	}

}
