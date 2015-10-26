/*
 * 
 */
package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

// TODO: Auto-generated Javadoc
/**
 * The Class Role.
 *
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

	/** The role. */
	private String role;
	
	/** The menu. */
	private String menu;
	
	/** The auth. */
	private String auth;

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the menu.
	 *
	 * @return the menu
	 */
	public String getMenu() {
		return menu;
	}

	/**
	 * Sets the menu.
	 *
	 * @param menu the new menu
	 */
	public void setMenu(String menu) {
		this.menu = menu;
	}

	/**
	 * Gets the auth.
	 *
	 * @return the auth
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * Sets the auth.
	 *
	 * @param auth the new auth
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Role [role=" + role + ", menu=" + menu + ", auth=" + auth + "]";
	}

}
