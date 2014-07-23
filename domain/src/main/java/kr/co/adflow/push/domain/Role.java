package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * @date 2014. 7. 21.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Role {

	// +-------+-------------+------+-----+---------+-------+
	// | Field | Type | Null | Key | Default | Extra |
	// +-------+-------------+------+-----+---------+-------+
	// | role | varchar(20) | YES | | NULL | |
	// | menu | varchar(20) | YES | | NULL | |
	// | auth | varchar(5) | YES | | NULL | |
	// +-------+-------------+------+-----+---------+-------+

	private String role;
	private String menu;
	private String auth;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "Role [role=" + role + ", menu=" + menu + ", auth=" + auth + "]";
	}

}
