/*
 * 
 */
package kr.co.adflow.pms.domain;

// TODO: Auto-generated Javadoc
/**
 * The Class Heap.
 *
 * @author nadir93
 * @date 2014. 6. 3.
 */
public class Heap {
	
	/** The heap init. */
	private long heapInit;
	
	/** The heap used. */
	private long heapUsed;
	
	/** The heap committed. */
	private long heapCommitted;
	
	/** The heap max. */
	private long heapMax;
	
	/** The non heap init. */
	private long nonHeapInit;
	
	/** The non heap used. */
	private long nonHeapUsed;
	
	/** The non heap committed. */
	private long nonHeapCommitted;
	
	/** The non heap max. */
	private long nonHeapMax;

	/**
	 * Instantiates a new heap.
	 *
	 * @param heapInit the heap init
	 * @param heapUsed the heap used
	 * @param heapCommitted the heap committed
	 * @param heapMax the heap max
	 * @param nonHeapInit the non heap init
	 * @param nonHeapUsed the non heap used
	 * @param nonHeapCommitted the non heap committed
	 * @param nonHeapMax the non heap max
	 */
	public Heap(long heapInit, long heapUsed, long heapCommitted, long heapMax,
			long nonHeapInit, long nonHeapUsed, long nonHeapCommitted,
			long nonHeapMax) {
		super();
		this.heapInit = heapInit;
		this.heapUsed = heapUsed;
		this.heapCommitted = heapCommitted;
		this.heapMax = heapMax;
		this.nonHeapInit = nonHeapInit;
		this.nonHeapUsed = nonHeapUsed;
		this.nonHeapCommitted = nonHeapCommitted;
		this.nonHeapMax = nonHeapMax;
	}

	/**
	 * Gets the heap init.
	 *
	 * @return the heap init
	 */
	public long getHeapInit() {
		return heapInit;
	}

	/**
	 * Sets the heap init.
	 *
	 * @param heapInit the new heap init
	 */
	public void setHeapInit(long heapInit) {
		this.heapInit = heapInit;
	}

	/**
	 * Gets the heap used.
	 *
	 * @return the heap used
	 */
	public long getHeapUsed() {
		return heapUsed;
	}

	/**
	 * Sets the heap used.
	 *
	 * @param heapUsed the new heap used
	 */
	public void setHeapUsed(long heapUsed) {
		this.heapUsed = heapUsed;
	}

	/**
	 * Gets the heap committed.
	 *
	 * @return the heap committed
	 */
	public long getHeapCommitted() {
		return heapCommitted;
	}

	/**
	 * Sets the heap committed.
	 *
	 * @param heapCommitted the new heap committed
	 */
	public void setHeapCommitted(long heapCommitted) {
		this.heapCommitted = heapCommitted;
	}

	/**
	 * Gets the heap max.
	 *
	 * @return the heap max
	 */
	public long getHeapMax() {
		return heapMax;
	}

	/**
	 * Sets the heap max.
	 *
	 * @param heapMax the new heap max
	 */
	public void setHeapMax(long heapMax) {
		this.heapMax = heapMax;
	}

	/**
	 * Gets the non heap init.
	 *
	 * @return the non heap init
	 */
	public long getNonHeapInit() {
		return nonHeapInit;
	}

	/**
	 * Sets the non heap init.
	 *
	 * @param nonHeapInit the new non heap init
	 */
	public void setNonHeapInit(long nonHeapInit) {
		this.nonHeapInit = nonHeapInit;
	}

	/**
	 * Gets the non heap used.
	 *
	 * @return the non heap used
	 */
	public long getNonHeapUsed() {
		return nonHeapUsed;
	}

	/**
	 * Sets the non heap used.
	 *
	 * @param nonHeapUsed the new non heap used
	 */
	public void setNonHeapUsed(long nonHeapUsed) {
		this.nonHeapUsed = nonHeapUsed;
	}

	/**
	 * Gets the non heap committed.
	 *
	 * @return the non heap committed
	 */
	public long getNonHeapCommitted() {
		return nonHeapCommitted;
	}

	/**
	 * Sets the non heap committed.
	 *
	 * @param nonHeapCommitted the new non heap committed
	 */
	public void setNonHeapCommitted(long nonHeapCommitted) {
		this.nonHeapCommitted = nonHeapCommitted;
	}

	/**
	 * Gets the non heap max.
	 *
	 * @return the non heap max
	 */
	public long getNonHeapMax() {
		return nonHeapMax;
	}

	/**
	 * Sets the non heap max.
	 *
	 * @param nonHeapMax the new non heap max
	 */
	public void setNonHeapMax(long nonHeapMax) {
		this.nonHeapMax = nonHeapMax;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Heap [heapInit=" + heapInit + ", heapUsed=" + heapUsed
				+ ", heapCommitted=" + heapCommitted + ", heapMax=" + heapMax
				+ ", nonHeapInit=" + nonHeapInit + ", nonHeapUsed="
				+ nonHeapUsed + ", nonHeapCommitted=" + nonHeapCommitted
				+ ", nonHeapMax=" + nonHeapMax + "]";
	}

}
