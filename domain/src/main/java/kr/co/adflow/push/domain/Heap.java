package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 6. 3.
 */
public class Heap {
	private long heapInit;
	private long heapUsed;
	private long heapCommitted;
	private long heapMax;
	private long nonHeapInit;
	private long nonHeapUsed;
	private long nonHeapCommitted;
	private long nonHeapMax;

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

	public long getHeapInit() {
		return heapInit;
	}

	public void setHeapInit(long heapInit) {
		this.heapInit = heapInit;
	}

	public long getHeapUsed() {
		return heapUsed;
	}

	public void setHeapUsed(long heapUsed) {
		this.heapUsed = heapUsed;
	}

	public long getHeapCommitted() {
		return heapCommitted;
	}

	public void setHeapCommitted(long heapCommitted) {
		this.heapCommitted = heapCommitted;
	}

	public long getHeapMax() {
		return heapMax;
	}

	public void setHeapMax(long heapMax) {
		this.heapMax = heapMax;
	}

	public long getNonHeapInit() {
		return nonHeapInit;
	}

	public void setNonHeapInit(long nonHeapInit) {
		this.nonHeapInit = nonHeapInit;
	}

	public long getNonHeapUsed() {
		return nonHeapUsed;
	}

	public void setNonHeapUsed(long nonHeapUsed) {
		this.nonHeapUsed = nonHeapUsed;
	}

	public long getNonHeapCommitted() {
		return nonHeapCommitted;
	}

	public void setNonHeapCommitted(long nonHeapCommitted) {
		this.nonHeapCommitted = nonHeapCommitted;
	}

	public long getNonHeapMax() {
		return nonHeapMax;
	}

	public void setNonHeapMax(long nonHeapMax) {
		this.nonHeapMax = nonHeapMax;
	}

	@Override
	public String toString() {
		return "Heap [heapInit=" + heapInit + ", heapUsed=" + heapUsed
				+ ", heapCommitted=" + heapCommitted + ", heapMax=" + heapMax
				+ ", nonHeapInit=" + nonHeapInit + ", nonHeapUsed="
				+ nonHeapUsed + ", nonHeapCommitted=" + nonHeapCommitted
				+ ", nonHeapMax=" + nonHeapMax + "]";
	}

}
