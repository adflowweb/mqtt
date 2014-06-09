package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 6. 2.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CPU {

	private double User;
	private double Sys;
	private double Idle;
	private double Wait;
	private double Nice;
	private double Combined;
	private double Irq;
	private double SoftIrq; // for linux
	private double Stolen; // for linux

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

	public double getUser() {
		return User;
	}

	public void setUser(double user) {
		User = user;
	}

	public double getSys() {
		return Sys;
	}

	public void setSys(double sys) {
		Sys = sys;
	}

	public double getIdle() {
		return Idle;
	}

	public void setIdle(double idle) {
		Idle = idle;
	}

	public double getWait() {
		return Wait;
	}

	public void setWait(double wait) {
		Wait = wait;
	}

	public double getNice() {
		return Nice;
	}

	public void setNice(double nice) {
		Nice = nice;
	}

	public double getCombined() {
		return Combined;
	}

	public void setCombined(double combined) {
		Combined = combined;
	}

	public double getIrq() {
		return Irq;
	}

	public void setIrq(double irq) {
		Irq = irq;
	}

	public double getSoftIrq() {
		return SoftIrq;
	}

	public void setSoftIrq(double softIrq) {
		SoftIrq = softIrq;
	}

	public double getStolen() {
		return Stolen;
	}

	public void setStolen(double stolen) {
		Stolen = stolen;
	}

	@Override
	public String toString() {
		return "CPU [User=" + User + ", Sys=" + Sys + ", Idle=" + Idle
				+ ", Wait=" + Wait + ", Nice=" + Nice + ", Combined="
				+ Combined + ", Irq=" + Irq + ", SoftIrq=" + SoftIrq
				+ ", Stolen=" + Stolen + "]";
	}

}
