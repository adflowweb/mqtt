package kr.co.adflow.push.domain.bsbank;

/**
 * 부서정보
 * 
 * @author nadir93
 * @date 2014. 6. 30.
 */
public class Department {

	/**
	 * +------------------------+--------------+------+-----+---------+-------+
	 * | Field | Type | Null | Key | Default | Extra |
	 * +------------------------+--------------+------+-----+---------+-------+
	 * | gw_sbsd_cdnm | varchar(50) | no | mul | null | |
	 * | gw_deptmt_cdnm | varchar(50) | no | pri | null | |
	 * | gw_dpnm | varchar(200) | yes | | null | |
	 * | gw_upper_deptmt_cdnm | varchar(50) | yes | | null | |
	 * | gw_deptmt_lnup_cntn | varchar(500) | no | | null | |
	 * | gw_deptmt_path_cntn | varchar(500) | no | | null | |
	 * | gw_deptmt_lnup_key_val | int(11) | yes | | null | |
	 * +------------------------+--------------+------+-----+---------+-------+
	 */

	private String gw_sbsd_cdnm;
	private String gw_deptmt_cdnm;
	private String gw_dpnm;
	private String gw_upper_deptmt_cdnm;
	private String gw_deptmt_lnup_cntn;
	private String gw_deptmt_path_cntn;
	private int gw_deptmt_lnup_key_val;

	public String getGw_sbsd_cdnm() {
		return gw_sbsd_cdnm;
	}

	public void setGw_sbsd_cdnm(String gw_sbsd_cdnm) {
		this.gw_sbsd_cdnm = gw_sbsd_cdnm;
	}

	public String getGw_deptmt_cdnm() {
		return gw_deptmt_cdnm;
	}

	public void setGw_deptmt_cdnm(String gw_deptmt_cdnm) {
		this.gw_deptmt_cdnm = gw_deptmt_cdnm;
	}

	public String getGw_dpnm() {
		return gw_dpnm;
	}

	public void setGw_dpnm(String gw_dpnm) {
		this.gw_dpnm = gw_dpnm;
	}

	public String getGw_upper_deptmt_cdnm() {
		return gw_upper_deptmt_cdnm;
	}

	public void setGw_upper_deptmt_cdnm(String gw_upper_deptmt_cdnm) {
		this.gw_upper_deptmt_cdnm = gw_upper_deptmt_cdnm;
	}

	public String getGw_deptmt_lnup_cntn() {
		return gw_deptmt_lnup_cntn;
	}

	public void setGw_deptmt_lnup_cntn(String gw_deptmt_lnup_cntn) {
		this.gw_deptmt_lnup_cntn = gw_deptmt_lnup_cntn;
	}

	public String getGw_deptmt_path_cntn() {
		return gw_deptmt_path_cntn;
	}

	public void setGw_deptmt_path_cntn(String gw_deptmt_path_cntn) {
		this.gw_deptmt_path_cntn = gw_deptmt_path_cntn;
	}

	public int getGw_deptmt_lnup_key_val() {
		return gw_deptmt_lnup_key_val;
	}

	public void setGw_deptmt_lnup_key_val(int gw_deptmt_lnup_key_val) {
		this.gw_deptmt_lnup_key_val = gw_deptmt_lnup_key_val;
	}

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
