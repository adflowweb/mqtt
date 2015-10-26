/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

// TODO: Auto-generated Javadoc
/**
 * 부서정보.
 *
 * @author nadir93
 * @date 2014. 6. 30.
 */
public class Department {

	/** +------------------------+--------------+------+-----+---------+-------+ | Field | Type | Null | Key | Default | Extra | +------------------------+--------------+------+-----+---------+-------+ | gw_sbsd_cdnm | varchar(50) | no | mul | null | | | gw_deptmt_cdnm | varchar(50) | no | pri | null | | | gw_dpnm | varchar(200) | yes | | null | | | gw_upper_deptmt_cdnm | varchar(50) | yes | | null | | | gw_deptmt_lnup_cntn | varchar(500) | no | | null | | | gw_deptmt_path_cntn | varchar(500) | no | | null | | | gw_deptmt_lnup_key_val | int(11) | yes | | null | | +------------------------+--------------+------+-----+---------+-------+. */

	private String gw_sbsd_cdnm;
	
	/** The gw_deptmt_cdnm. */
	private String gw_deptmt_cdnm;
	
	/** The gw_dpnm. */
	private String gw_dpnm;
	
	/** The gw_upper_deptmt_cdnm. */
	private String gw_upper_deptmt_cdnm;
	
	/** The gw_deptmt_lnup_cntn. */
	private String gw_deptmt_lnup_cntn;
	
	/** The gw_deptmt_path_cntn. */
	private String gw_deptmt_path_cntn;
	
	/** The gw_deptmt_lnup_key_val. */
	private int gw_deptmt_lnup_key_val;

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
	 * Gets the gw_dpnm.
	 *
	 * @return the gw_dpnm
	 */
	public String getGw_dpnm() {
		return gw_dpnm;
	}

	/**
	 * Sets the gw_dpnm.
	 *
	 * @param gw_dpnm the new gw_dpnm
	 */
	public void setGw_dpnm(String gw_dpnm) {
		this.gw_dpnm = gw_dpnm;
	}

	/**
	 * Gets the gw_upper_deptmt_cdnm.
	 *
	 * @return the gw_upper_deptmt_cdnm
	 */
	public String getGw_upper_deptmt_cdnm() {
		return gw_upper_deptmt_cdnm;
	}

	/**
	 * Sets the gw_upper_deptmt_cdnm.
	 *
	 * @param gw_upper_deptmt_cdnm the new gw_upper_deptmt_cdnm
	 */
	public void setGw_upper_deptmt_cdnm(String gw_upper_deptmt_cdnm) {
		this.gw_upper_deptmt_cdnm = gw_upper_deptmt_cdnm;
	}

	/**
	 * Gets the gw_deptmt_lnup_cntn.
	 *
	 * @return the gw_deptmt_lnup_cntn
	 */
	public String getGw_deptmt_lnup_cntn() {
		return gw_deptmt_lnup_cntn;
	}

	/**
	 * Sets the gw_deptmt_lnup_cntn.
	 *
	 * @param gw_deptmt_lnup_cntn the new gw_deptmt_lnup_cntn
	 */
	public void setGw_deptmt_lnup_cntn(String gw_deptmt_lnup_cntn) {
		this.gw_deptmt_lnup_cntn = gw_deptmt_lnup_cntn;
	}

	/**
	 * Gets the gw_deptmt_path_cntn.
	 *
	 * @return the gw_deptmt_path_cntn
	 */
	public String getGw_deptmt_path_cntn() {
		return gw_deptmt_path_cntn;
	}

	/**
	 * Sets the gw_deptmt_path_cntn.
	 *
	 * @param gw_deptmt_path_cntn the new gw_deptmt_path_cntn
	 */
	public void setGw_deptmt_path_cntn(String gw_deptmt_path_cntn) {
		this.gw_deptmt_path_cntn = gw_deptmt_path_cntn;
	}

	/**
	 * Gets the gw_deptmt_lnup_key_val.
	 *
	 * @return the gw_deptmt_lnup_key_val
	 */
	public int getGw_deptmt_lnup_key_val() {
		return gw_deptmt_lnup_key_val;
	}

	/**
	 * Sets the gw_deptmt_lnup_key_val.
	 *
	 * @param gw_deptmt_lnup_key_val the new gw_deptmt_lnup_key_val
	 */
	public void setGw_deptmt_lnup_key_val(int gw_deptmt_lnup_key_val) {
		this.gw_deptmt_lnup_key_val = gw_deptmt_lnup_key_val;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Department [gw_sbsd_cdnm=" + gw_sbsd_cdnm + ", gw_deptmt_cdnm="
				+ gw_deptmt_cdnm + ", gw_dpnm=" + gw_dpnm
				+ ", gw_upper_deptmt_cdnm=" + gw_upper_deptmt_cdnm
				+ ", gw_deptmt_lnup_cntn=" + gw_deptmt_lnup_cntn
				+ ", gw_deptmt_path_cntn=" + gw_deptmt_path_cntn
				+ ", gw_deptmt_lnup_key_val=" + gw_deptmt_lnup_key_val + "]";
	}

}
