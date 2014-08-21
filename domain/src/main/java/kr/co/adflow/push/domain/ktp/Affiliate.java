package kr.co.adflow.push.domain.ktp;

/**
 * 부산은행 계열사 클래스
 * 
 * @author nadir93
 * @date 2014. 6. 30.
 */
public class Affiliate {

	// +--------------+--------------+------+-----+---------+-------+
	// | Field | Type | Null | Key | Default | Extra |
	// +--------------+--------------+------+-----+---------+-------+
	// | gw_sbsd_cdnm | varchar(50) | NO | PRI | NULL | |
	// | gw_sbsd_nm | varchar(200) | NO | | NULL | |
	// +--------------+--------------+------+-----+---------+-------+

	// 계열사 코드
	private String gw_sbsd_cdnm;
	// 계열사 이름
	private String gw_sbsd_nm;

	public String getGw_sbsd_cdnm() {
		return gw_sbsd_cdnm;
	}

	public void setGw_sbsd_cdnm(String gw_sbsd_cdnm) {
		this.gw_sbsd_cdnm = gw_sbsd_cdnm;
	}

	public String getGw_sbsd_nm() {
		return gw_sbsd_nm;
	}

	public void setGw_sbsd_nm(String gw_sbsd_nm) {
		this.gw_sbsd_nm = gw_sbsd_nm;
	}

	@Override
	public String toString() {
		return "Affiliate [gw_sbsd_cdnm=" + gw_sbsd_cdnm + ", gw_sbsd_nm="
				+ gw_sbsd_nm + "]";
	}

}
