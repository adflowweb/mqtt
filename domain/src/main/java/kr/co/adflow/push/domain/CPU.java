/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class CPU.
 *
 * @author nadir93
 * @date 2014. 6. 2.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CPU {

	/** The User. */
	private double User;
	
	/** The Sys. */
	private double Sys;
	
	/** The Idle. */
	private double Idle;
	
	/** The Wait. */
	private double Wait;
	
	/** The Nice. */
	private double Nice;
	
	/** The Combined. */
	private double Combined;
	
	/** The Irq. */
	private double Irq;
	
	/** The Soft irq. */
	private double SoftIrq; // for linux
	
	/** The Stolen. */
	private double Stolen; // for linux

	/**
	 * Instantiates a new cpu.
	 *
	 * @param user the user
	 * @param sys the sys
	 * @param idle the idle
	 * @param wait the wait
	 * @param nice the nice
	 * @param combined the combined
	 * @param irq the irq
	 */
	public CPU(double user, double sys, double idle, double wait, double nice,
			double combined, double irq) {
		super();
		User = user;
		Sys = sys;
		Idle = idle;
		Wait = wait;
		Nice = nice;
		Combined = combined;
		Irq = irq;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public double getUser() {
		return User;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(double user) {
		User = user;
	}

	/**
	 * Gets the sys.
	 *
	 * @return the sys
	 */
	public double getSys() {
		return Sys;
	}

	/**
	 * Sets the sys.
	 *
	 * @param sys the new sys
	 */
	public void setSys(double sys) {
		Sys = sys;
	}

	/**
	 * Gets the idle.
	 *
	 * @return the idle
	 */
	public double getIdle() {
		return Idle;
	}

	/**
	 * Sets the idle.
	 *
	 * @param idle the new idle
	 */
	public void setIdle(double idle) {
		Idle = idle;
	}

	/**
	 * Gets the wait.
	 *
	 * @return the wait
	 */
	public double getWait() {
		return Wait;
	}

	/**
	 * Sets the wait.
	 *
	 * @param wait the new wait
	 */
	public void setWait(double wait) {
		Wait = wait;
	}

	/**
	 * Gets the nice.
	 *
	 * @return the nice
	 */
	public double getNice() {
		return Nice;
	}

	/**
	 * Sets the nice.
	 *
	 * @param nice the new nice
	 */
	public void setNice(double nice) {
		Nice = nice;
	}

	/**
	 * Gets the combined.
	 *
	 * @return the combined
	 */
	public double getCombined() {
		return Combined;
	}

	/**
	 * Sets the combined.
	 *
	 * @param combined the new combined
	 */
	public void setCombined(double combined) {
		Combined = combined;
	}

	/**
	 * Gets the irq.
	 *
	 * @return the irq
	 */
	public double getIrq() {
		return Irq;
	}

	/**
	 * Sets the irq.
	 *
	 * @param irq the new irq
	 */
	public void setIrq(double irq) {
		Irq = irq;
	}

	/**
	 * Gets the soft irq.
	 *
	 * @return the soft irq
	 */
	public double getSoftIrq() {
		return SoftIrq;
	}

	/**
	 * Sets the soft irq.
	 *
	 * @param softIrq the new soft irq
	 */
	public void setSoftIrq(double softIrq) {
		SoftIrq = softIrq;
	}

	/**
	 * Gets the stolen.
	 *
	 * @return the stolen
	 */
	public double getStolen() {
		return Stolen;
	}

	/**
	 * Sets the stolen.
	 *
	 * @param stolen the new stolen
	 */
	public void setStolen(double stolen) {
		Stolen = stolen;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CPU [User=" + User + ", Sys=" + Sys + ", Idle=" + Idle
				+ ", Wait=" + Wait + ", Nice=" + Nice + ", Combined="
				+ Combined + ", Irq=" + Irq + ", SoftIrq=" + SoftIrq
				+ ", Stolen=" + Stolen + "]";
	}

}
