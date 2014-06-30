package kr.co.adflow.push.domain.bsbank;

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

	private String gw_sbsd_cdnm;
	private String gw_stf_cdnm;
	private String gw_deptmt_cdnm;
	private String gw_user_nm;
	private String gw_psit_cdnm;
	private String gw_psinm;
	private String gw_jgd_cdnm;
	private String gw_jgd_nm;
	private String gw_rsb_cdnm;
	private String gw_rsb_nm;

	public String getGw_sbsd_cdnm() {
		return gw_sbsd_cdnm;
	}

	public void setGw_sbsd_cdnm(String gw_sbsd_cdnm) {
		this.gw_sbsd_cdnm = gw_sbsd_cdnm;
	}

	public String getGw_stf_cdnm() {
		return gw_stf_cdnm;
	}

	public void setGw_stf_cdnm(String gw_stf_cdnm) {
		this.gw_stf_cdnm = gw_stf_cdnm;
	}

	public String getGw_deptmt_cdnm() {
		return gw_deptmt_cdnm;
	}

	public void setGw_deptmt_cdnm(String gw_deptmt_cdnm) {
		this.gw_deptmt_cdnm = gw_deptmt_cdnm;
	}

	public String getGw_user_nm() {
		return gw_user_nm;
	}

	public void setGw_user_nm(String gw_user_nm) {
		this.gw_user_nm = gw_user_nm;
	}

	public String getGw_psit_cdnm() {
		return gw_psit_cdnm;
	}

	public void setGw_psit_cdnm(String gw_psit_cdnm) {
		this.gw_psit_cdnm = gw_psit_cdnm;
	}

	public String getGw_psinm() {
		return gw_psinm;
	}

	public void setGw_psinm(String gw_psinm) {
		this.gw_psinm = gw_psinm;
	}

	public String getGw_jgd_cdnm() {
		return gw_jgd_cdnm;
	}

	public void setGw_jgd_cdnm(String gw_jgd_cdnm) {
		this.gw_jgd_cdnm = gw_jgd_cdnm;
	}

	public String getGw_jgd_nm() {
		return gw_jgd_nm;
	}

	public void setGw_jgd_nm(String gw_jgd_nm) {
		this.gw_jgd_nm = gw_jgd_nm;
	}

	public String getGw_rsb_cdnm() {
		return gw_rsb_cdnm;
	}

	public void setGw_rsb_cdnm(String gw_rsb_cdnm) {
		this.gw_rsb_cdnm = gw_rsb_cdnm;
	}

	public String getGw_rsb_nm() {
		return gw_rsb_nm;
	}

	public void setGw_rsb_nm(String gw_rsb_nm) {
		this.gw_rsb_nm = gw_rsb_nm;
	}

	@Override
	public String toString() {
		return "User [gw_sbsd_cdnm=" + gw_sbsd_cdnm + ", gw_stf_cdnm="
				+ gw_stf_cdnm + ", gw_deptmt_cdnm=" + gw_deptmt_cdnm
				+ ", gw_user_nm=" + gw_user_nm + ", gw_psit_cdnm="
				+ gw_psit_cdnm + ", gw_psinm=" + gw_psinm + ", gw_jgd_cdnm="
				+ gw_jgd_cdnm + ", gw_jgd_nm=" + gw_jgd_nm + ", gw_rsb_cdnm="
				+ gw_rsb_cdnm + ", gw_rsb_nm=" + gw_rsb_nm + "]";
	}

}
