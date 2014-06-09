package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 6. 2.
 */
public class Memory {

	// Actual total free system memory
	private long ActualFree;
	// Actual total used system memory
	private long ActualUsed;
	// Total free system memory
	private long Free;
	// System Random Access Memory
	private long Ram;
	// Total system memory
	private long Total;
	// Total used system memory
	private long Used;

	public Memory(long actualFree, long actualUsed, long free, long ram,
			long total, long used) {
		super();
		ActualFree = actualFree;
		ActualUsed = actualUsed;
		Free = free;
		Ram = ram;
		Total = total;
		Used = used;
	}

	public long getActualFree() {
		return ActualFree;
	}

	public void setActualFree(long actualFree) {
		ActualFree = actualFree;
	}

	public long getActualUsed() {
		return ActualUsed;
	}

	public void setActualUsed(long actualUsed) {
		ActualUsed = actualUsed;
	}

	public long getFree() {
		return Free;
	}

	public void setFree(long free) {
		Free = free;
	}

	public long getRam() {
		return Ram;
	}

	public void setRam(long ram) {
		Ram = ram;
	}

	public long getTotal() {
		return Total;
	}

	public void setTotal(long total) {
		Total = total;
	}

	public long getUsed() {
		return Used;
	}

	public void setUsed(long used) {
		Used = used;
	}

	@Override
	public String toString() {
		return "Memory [ActualFree=" + ActualFree + ", ActualUsed="
				+ ActualUsed + ", Free=" + Free + ", Ram=" + Ram + ", Total="
				+ Total + ", Used=" + Used + "]";
	}

}
