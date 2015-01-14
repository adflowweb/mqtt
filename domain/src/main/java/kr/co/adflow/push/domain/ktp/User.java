/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
public class User {
	// +---------------------+---------------+------+-----+---------+-------+
	// | field | type | null | key | default | extra |
	// +---------------------+---------------+------+-----+---------+-------+
	// | gw_sbsd_cdnm | varchar(50) | no | | null | |
	// | gw_stf_cdnm | varchar(20) | no | pri | null | |
	// | gw_deptmt_cdnm | varchar(50) | no | | null | |
	// | gw_user_nm | varchar(100) | no | | null | |
	// | gw_psit_cdnm | varchar(50) | yes | | null | |
	// | gw_psinm | varchar(200) | yes | | null | |
	// | gw_jgd_cdnm | varchar(50) | yes | | null | |
	// | gw_jgd_nm | varchar(200) | yes | | null | |
	// | gw_rsb_cdnm | varchar(50) | yes | | null | |
	// | gw_rsb_nm | varchar(200) | yes | | null | |
	// | gw_pho_path_nm | varchar(500) | yes | | null | |
	// | etco_dt | char(8) | yes | | null | |
	// | slr_dvcd | char(1) | yes | | null | |
	// | bird | char(8) | yes | | null | |
	// | gw_emladr | varchar(100) | yes | | null | |
	// | gw_cg_biz_nm | varchar(1000) | yes | | null | |
	// | stf_iwno | varchar(50) | yes | | null | |
	// | home_tlno | varchar(20) | yes | | null | |
	// | mpno | varchar(20) | yes | | null | |
	// | faxno | varchar(20) | yes | | null | |
	// | gw_clwr_altm_nm | varchar(50) | yes | | null | |
	// | tsn_dt | varchar(10) | yes | | null | |
	// | gw_bnhof_cdnm | varchar(50) | no | | null | |
	// | gw_bnhof_nm | varchar(2000) | yes | | null | |
	// | gw_stf_lnup_key_val | varchar(200) | yes | | null | |
	// +---------------------+---------------+------+-----+---------+-------+

	/** The gw_sbsd_cdnm. */
	private String gw_sbsd_cdnm;
	
	/** The gw_stf_cdnm. */
	private String gw_stf_cdnm;
	
	/** The gw_deptmt_cdnm. */
	private String gw_deptmt_cdnm;
	
	/** The gw_user_nm. */
	private String gw_user_nm;
	
	/** The gw_psit_cdnm. */
	private String gw_psit_cdnm;
	
	/** The gw_psinm. */
	private String gw_psinm;
	
	/** The gw_jgd_cdnm. */
	private String gw_jgd_cdnm;
	
	/** The gw_jgd_nm. */
	private String gw_jgd_nm;
	
	/** The gw_rsb_cdnm. */
	private String gw_rsb_cdnm;
	
	/** The gw_rsb_nm. */
	private String gw_rsb_nm;
	
	/** The mpno. */
	private String mpno;

	/**
	 * Gets the gw_sbsd_cdnm.
	 *
	 * @return the gw_sbsd_cdnm
	 */
	public String getGw_sbsd_cdnm() {
		return gw_sbsd_cdnm;
	}

	/**
	 * Sets the gw_sbsd_cdnm.
	 *
	 * @param gw_sbsd_cdnm the new gw_sbsd_cdnm
	 */
	public void setGw_sbsd_cdnm(String gw_sbsd_cdnm) {
		this.gw_sbsd_cdnm = gw_sbsd_cdnm;
	}

	/**
	 * Gets the gw_stf_cdnm.
	 *
	 * @return the gw_stf_cdnm
	 */
	public String getGw_stf_cdnm() {
		return gw_stf_cdnm;
	}

	/**
	 * Sets the gw_stf_cdnm.
	 *
	 * @param gw_stf_cdnm the new gw_stf_cdnm
	 */
	public void setGw_stf_cdnm(String gw_stf_cdnm) {
		this.gw_stf_cdnm = gw_stf_cdnm;
	}

	/**
	 * Gets the gw_deptmt_cdnm.
	 *
	 * @return the gw_deptmt_cdnm
	 */
	public String getGw_deptmt_cdnm() {
		return gw_deptmt_cdnm;
	}

	/**
	 * Sets the gw_deptmt_cdnm.
	 *
	 * @param gw_deptmt_cdnm the new gw_deptmt_cdnm
	 */
	public void setGw_deptmt_cdnm(String gw_deptmt_cdnm) {
		this.gw_deptmt_cdnm = gw_deptmt_cdnm;
	}

	/**
	 * Gets the gw_user_nm.
	 *
	 * @return the gw_user_nm
	 */
	public String getGw_user_nm() {
		return gw_user_nm;
	}

	/**
	 * Sets the gw_user_nm.
	 *
	 * @param gw_user_nm the new gw_user_nm
	 */
	public void setGw_user_nm(String gw_user_nm) {
		this.gw_user_nm = gw_user_nm;
	}

	/**
	 * Gets the gw_psit_cdnm.
	 *
	 * @return the gw_psit_cdnm
	 */
	public String getGw_psit_cdnm() {
		return gw_psit_cdnm;
	}

	/**
	 * Sets the gw_psit_cdnm.
	 *
	 * @param gw_psit_cdnm the new gw_psit_cdnm
	 */
	public void setGw_psit_cdnm(String gw_psit_cdnm) {
		this.gw_psit_cdnm = gw_psit_cdnm;
	}

	/**
	 * Gets the gw_psinm.
	 *
	 * @return the gw_psinm
	 */
	public String getGw_psinm() {
		return gw_psinm;
	}

	/**
	 * Sets the gw_psinm.
	 *
	 * @param gw_psinm the new gw_psinm
	 */
	public void setGw_psinm(String gw_psinm) {
		this.gw_psinm = gw_psinm;
	}

	/**
	 * Gets the gw_jgd_cdnm.
	 *
	 * @return the gw_jgd_cdnm
	 */
	public String getGw_jgd_cdnm() {
		return gw_jgd_cdnm;
	}

	/**
	 * Sets the gw_jgd_cdnm.
	 *
	 * @param gw_jgd_cdnm the new gw_jgd_cdnm
	 */
	public void setGw_jgd_cdnm(String gw_jgd_cdnm) {
		this.gw_jgd_cdnm = gw_jgd_cdnm;
	}

	/**
	 * Gets the gw_jgd_nm.
	 *
	 * @return the gw_jgd_nm
	 */
	public String getGw_jgd_nm() {
		return gw_jgd_nm;
	}

	/**
	 * Sets the gw_jgd_nm.
	 *
	 * @param gw_jgd_nm the new gw_jgd_nm
	 */
	public void setGw_jgd_nm(String gw_jgd_nm) {
		this.gw_jgd_nm = gw_jgd_nm;
	}

	/**
	 * Gets the gw_rsb_cdnm.
	 *
	 * @return the gw_rsb_cdnm
	 */
	public String getGw_rsb_cdnm() {
		return gw_rsb_cdnm;
	}

	/**
	 * Sets the gw_rsb_cdnm.
	 *
	 * @param gw_rsb_cdnm the new gw_rsb_cdnm
	 */
	public void setGw_rsb_cdnm(String gw_rsb_cdnm) {
		this.gw_rsb_cdnm = gw_rsb_cdnm;
	}

	/**
	 * Gets the gw_rsb_nm.
	 *
	 * @return the gw_rsb_nm
	 */
	public String getGw_rsb_nm() {
		return gw_rsb_nm;
	}

	/**
	 * Sets the gw_rsb_nm.
	 *
	 * @param gw_rsb_nm the new gw_rsb_nm
	 */
	public void setGw_rsb_nm(String gw_rsb_nm) {
		this.gw_rsb_nm = gw_rsb_nm;
	}

	/**
	 * Gets the mpno.
	 *
	 * @return the mpno
	 */
	public String getMpno() {
		return mpno;
	}

	/**
	 * Sets the mpno.
	 *
	 * @param mpno the new mpno
	 */
	public void setMpno(String mpno) {
		this.mpno = mpno;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [gw_sbsd_cdnm=" + gw_sbsd_cdnm + ", gw_stf_cdnm="
				+ gw_stf_cdnm + ", gw_deptmt_cdnm=" + gw_deptmt_cdnm
				+ ", gw_user_nm=" + gw_user_nm + ", gw_psit_cdnm="
				+ gw_psit_cdnm + ", gw_psinm=" + gw_psinm + ", gw_jgd_cdnm="
				+ gw_jgd_cdnm + ", gw_jgd_nm=" + gw_jgd_nm + ", gw_rsb_cdnm="
				+ gw_rsb_cdnm + ", gw_rsb_nm=" + gw_rsb_nm + ", mpno=" + mpno
				+ "]";
	}

}
