/*
 * 
 */
package kr.co.adflow.pms.domain;

// TODO: Auto-generated Javadoc
/**
 * The Class Memory.
 * 
 * @author nadir93
 * @date 2014. 6. 2.
 */
public class Memory {

	// Actual total free system memory
	/** The Actual free. */
	private long ActualFree;
	// Actual total used system memory
	/** The Actual used. */
	private long ActualUsed;
	// Total free system memory
	/** The Free. */
	private long Free;
	// System Random Access Memory
	/** The Ram. */
	private long Ram;
	// Total system memory
	/** The Total. */
	private long Total;
	// Total used system memory
	/** The Used. */
	private long Used;

	/**
	 * Instantiates a new memory.
	 * 
	 * @param actualFree
	 *            the actual free
	 * @param actualUsed
	 *            the actual used
	 * @param free
	 *            the free
	 * @param ram
	 *            the ram
	 * @param total
	 *            the total
	 * @param used
	 *            the used
	 */
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

	/**
	 * Gets the actual free.
	 * 
	 * @return the actual free
	 */
	public long getActualFree() {
		return ActualFree;
	}

	/**
	 * Sets the actual free.
	 * 
	 * @param actualFree
	 *            the new actual free
	 */
	public void setActualFree(long actualFree) {
		ActualFree = actualFree;
	}

	/**
	 * Gets the actual used.
	 * 
	 * @return the actual used
	 */
	public long getActualUsed() {
		return ActualUsed;
	}

	/**
	 * Sets the actual used.
	 * 
	 * @param actualUsed
	 *            the new actual used
	 */
	public void setActualUsed(long actualUsed) {
		ActualUsed = actualUsed;
	}

	/**
	 * Gets the free.
	 * 
	 * @return the free
	 */
	public long getFree() {
		return Free;
	}

	/**
	 * Sets the free.
	 * 
	 * @param free
	 *            the new free
	 */
	public void setFree(long free) {
		Free = free;
	}

	/**
	 * Gets the ram.
	 * 
	 * @return the ram
	 */
	public long getRam() {
		return Ram;
	}

	/**
	 * Sets the ram.
	 * 
	 * @param ram
	 *            the new ram
	 */
	public void setRam(long ram) {
		Ram = ram;
	}

	/**
	 * Gets the total.
	 * 
	 * @return the total
	 */
	public long getTotal() {
		return Total;
	}

	/**
	 * Sets the total.
	 * 
	 * @param total
	 *            the new total
	 */
	public void setTotal(long total) {
		Total = total;
	}

	/**
	 * Gets the used.
	 * 
	 * @return the used
	 */
	public long getUsed() {
		return Used;
	}

	/**
	 * Sets the used.
	 * 
	 * @param used
	 *            the new used
	 */
	public void setUsed(long used) {
		Used = used;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Memory [ActualFree=" + ActualFree + ", ActualUsed="
				+ ActualUsed + ", Free=" + Free + ", Ram=" + Ram + ", Total="
				+ Total + ", Used=" + Used + "]";
	}

}
