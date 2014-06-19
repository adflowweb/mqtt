package kr.co.adflow.ldap;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class LdapAuth {

	private static final Logger logger = LoggerFactory
			.getLogger(LdapAuth.class);

	private String domain = "adpusan.co.kr";
	private String ldapUrl = "LDAP://" + domain;
	private String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	private String ldapSecurity = "simple";

	/**
	 * @param id
	 * @param pw
	 * @return
	 */
	public boolean ldapAuth(String id, String password) {
		logger.debug("ldapAuth시작(id=" + id + ", password=" + password + ")");
		boolean isAuth = false;
		DirContext ctx = null;

		Hashtable<String, String> property = new Hashtable<String, String>();
		property.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
		property.put(Context.SECURITY_AUTHENTICATION, ldapSecurity);
		property.put(Context.PROVIDER_URL, ldapUrl);
		property.put(Context.SECURITY_PRINCIPAL, id + "@" + domain);
		property.put(Context.SECURITY_CREDENTIALS, password);

		try {
			ctx = new InitialDirContext(property);
			isAuth = true;
			// "Login Success";
		} catch (Exception e) {
			logger.error("에러발생", e);
			isAuth = false;
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception e) {
					logger.error("에러발생", e);
				}
			}
		}
		logger.debug("ldapAuth종료(결과=" + isAuth + ")");
		return isAuth;
	}

	private void getUserInfo(String id, String pw) {
		String info = "DC=adpusan,DC=co,DC=kr";
		String filterString = String.format("(cn=%s)", id);

		DirContext ctx = null;

		Hashtable<String, String> property = new Hashtable<String, String>();
		property.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
		property.put(Context.SECURITY_AUTHENTICATION, ldapSecurity);
		property.put(Context.PROVIDER_URL, ldapUrl);
		property.put(Context.SECURITY_PRINCIPAL, id + "@" + domain);
		property.put(Context.SECURITY_CREDENTIALS, pw);

		SearchControls searcher = new SearchControls();
		searcher.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> results;
		try {
			ctx = new InitialDirContext(property);
			results = ctx.search(info, filterString, searcher);

			SearchResult result = results.next();
			Attributes attrs = result.getAttributes();

			/**
			 * displayName ¿Ã∏ß mail ∏ﬁ¿œ extensionAttribute1 ∫Œº≠ƒ⁄µÂ ..
			 */

			String userName = "";
			String eMail = "";
			String deptCode = "";

			if (attrs.get("displayName") != null)
				userName = attrs.get("displayName").get().toString();
			if (attrs.get("mail") != null)
				eMail = attrs.get("mail").get().toString();
			if (attrs.get("extensionAttribute1") != null)
				deptCode = attrs.get("extensionAttribute1").get().toString();

		} catch (NamingException e) {
		}
	}
}
