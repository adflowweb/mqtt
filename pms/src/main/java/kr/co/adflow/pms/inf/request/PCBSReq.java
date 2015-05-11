/*
 * 
 */
package kr.co.adflow.pms.inf.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class PCBSReq.
 */
public class PCBSReq {

	/** The CERT_KEY. */
	@NotEmpty
	private String certKey;
	
	/** The SA_ID. */
	@NotEmpty
	private String saId;
	
	/** The D_SVC_CD. */
	@NotEmpty
	private String dSvcCd;
	
	/** The STATUS_CD. */
	@NotEmpty
	private String statusCd;
	
	
	/** The PTALK20_BUNCH. */
	@NotEmpty
	private String ptalk20Bunch;
	
	/** The SOLUTION_ID. */
	@NotEmpty
	private String solutionId;
	
	/** The SOLUTION_PW. */
	@NotEmpty
	private String solutionPw;
	
	/** The USEGROUP_NM. */
	@NotEmpty
	private String usegroupNm;
	
	
	/**
	 * Gets the cert Key.
	 *
	 * @return the certKey
	 */
	public String getCertKey() {
		return certKey;
	}

	/**
	 * Sets the cert Key.
	 *
	 * @param certKey the new cert Key
	 */
	public void setCertKey(String certKey) {
		this.certKey = certKey;
	}

	/**
	 * Gets the sa Id.
	 *
	 * @return the saId
	 */
	public String getSaId() {
		return saId;
	}

	/**
	 * Sets the sa Id.
	 *
	 * @param saId the new sa Id
	 */
	public void setSaId(String saId) {
		this.saId = saId;
	}

	/**
	 * Gets the dSvcCd.
	 *
	 * @return the dSvcCd
	 */
	public String getdSvcCd() {
		return dSvcCd;
	}

	/**
	 * Sets the dSvcCd.
	 *
	 * @param dSvcCd the new dSvcCd
	 */
	public void setdSvcCd(String dSvcCd) {
		this.dSvcCd = dSvcCd;
	}

	/**
	 * Gets the status Cd.
	 *
	 * @return the statusCd
	 */
	public String getStatusCd() {
		return statusCd;
	}

	/**
	 * Sets the status Cd.
	 *
	 * @param statusCd the new status Cd
	 */
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	/**
	 * Gets the ptalk20 Bunch.
	 *
	 * @return the ptalk20Bunch
	 */
	public String getPtalk20Bunch() {
		return ptalk20Bunch;
	}

	/**
	 * Sets the ptalk20 Bunch.
	 *
	 * @param ptalk20Bunch the new ptalk20 Bunch
	 */
	public void setPtalk20Bunch(String ptalk20Bunch) {
		this.ptalk20Bunch = ptalk20Bunch;
	}

	/**
	 * Gets the solution Id.
	 *
	 * @return the solutionId
	 */
	public String getSolutionId() {
		return solutionId;
	}

	/**
	 * Sets the solution Id.
	 *
	 * @param solutionId the new solution Id
	 */
	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	/**
	 * Gets the solution Pw.
	 *
	 * @return the solutionPw
	 */
	public String getSolutionPw() {
		return solutionPw;
	}

	/**
	 * Sets the solution Pw.
	 *
	 * @param solutionPw the new solution Pw
	 */
	public void setSolutionPw(String solutionPw) {
		this.solutionPw = solutionPw;
	}

	/**
	 * Gets the usegroup Nm.
	 *
	 * @return the usegroupNm
	 */
	public String getUsegroupNm() {
		return usegroupNm;
	}

	/**
	 * Sets the usegroup Nm.
	 *
	 * @param usegroupNm the new usegroup Nm
	 */
	public void setUsegroupNm(String usegroupNm) {
		this.usegroupNm = usegroupNm;
	}



}
