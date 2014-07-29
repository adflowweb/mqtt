package kr.co.adflow.push.bsbank.dao.impl;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

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

import kr.co.adflow.push.dao.LdapAuthDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
@Service
public class LdapAuthDaoImpl implements LdapAuthDao {

	private static final Logger logger = LoggerFactory
			.getLogger(LdapAuthDaoImpl.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(LdapAuthDaoImpl.class
					.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String domain = prop.getProperty("ldap.server.url");
	private boolean ldap = Boolean.parseBoolean(prop
			.getProperty("ldap.server.enable"));
	private String ldapUrl = "LDAP://" + domain;
	private String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	private String ldapSecurity = "simple";

	// /**
	// * @param id
	// * @param pw
	// * @return
	// */
	// public boolean ldapAuth(String id, String password) {
	// logger.debug("ldapAuth시작(id=" + id + ", password=" + password + ")");
	// boolean isAuth = false;
	// DirContext ctx = null;
	//
	// Hashtable<String, String> env = new Hashtable<String, String>();
	// env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
	// env.put(Context.SECURITY_AUTHENTICATION, ldapSecurity);
	// env.put(Context.PROVIDER_URL, ldapUrl);
	// env.put(Context.SECURITY_PRINCIPAL, id + "@" + domain);
	// env.put(Context.SECURITY_CREDENTIALS, password);
	//
	// // ////testCode
	// // Enable connection pooling
	// env.put("com.sun.jndi.ldap.connect.pool", "true");
	// // ////testCodeEnd
	//
	// try {
	//
	// ctx = new InitialDirContext(env);
	// logger.debug("ctx=" + ctx);
	// isAuth = true;
	// // "Login Success";
	// } catch (Exception e) {
	// logger.error("에러발생", e);
	// isAuth = false;
	// } finally {
	// if (ctx != null) {
	// try {
	// ctx.close();
	// } catch (Exception e) {
	// logger.error("에러발생", e);
	// }
	// }
	// }
	// logger.debug("ldapAuth종료(결과=" + isAuth + ")");
	// return isAuth;
	// }

	// LDAP ¿Œ¡ı (æ€ø°º≠ ¿Ø«¸∫∞ ø°∑Ø ∏ﬁΩ√¡ˆµµ √≥∏Æ ∫Œ≈πµÂ∏≥¥œ¥Ÿ)
	public boolean ldapAuth(String id, String pw) {

		boolean isAuth = false;

		if (pw != null && !pw.equals("")) {
			DirContext ctx = null;

			Hashtable<String, String> property = new Hashtable<String, String>();
			property.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
			// property.put("com.sun.jndi.ldap.read.timeout", "5000");
			property.put(Context.SECURITY_AUTHENTICATION, ldapSecurity);
			property.put(Context.PROVIDER_URL, ldapUrl);
			property.put(Context.SECURITY_PRINCIPAL, id + "@" + domain);
			property.put(Context.SECURITY_CREDENTIALS, pw);

			try {
				ctx = new InitialDirContext(property);

				isAuth = true;
				// "Login Success";

			} catch (AuthenticationException e) {
				// "Password Fail";
			} catch (InvalidNameException e) {
				// "User not Found";
			} catch (NamingException e) {
				// "Login Failure";
			} catch (Exception e) {
				// "Login Failure";
			}

			finally {
				if (ctx != null)
					try {
						ctx.close();
					} catch (Exception e) {
					}
			}
		} else {
			// "Password Fail";
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.LdapAuthDao#auth(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean auth(String id, String pw) throws Exception {
		logger.debug("auth시작(id=" + id + ", pw=" + pw + ")");
		boolean isAuth = false;

		if (!ldap) {
			return true;
		}

		if (pw != null && !pw.equals("")) {
			DirContext ctx = null;

			Hashtable<String, String> property = new Hashtable<String, String>();
			property.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
			property.put(Context.SECURITY_AUTHENTICATION, ldapSecurity);
			property.put(Context.PROVIDER_URL, ldapUrl);
			logger.debug("ldapUrl=" + ldapUrl);
			property.put(Context.SECURITY_PRINCIPAL, id + "@" + domain);
			property.put(Context.SECURITY_CREDENTIALS, pw);

			// ////testCode
			// // Enable connection pooling
			// property.put("com.sun.jndi.ldap.connect.pool", "true");
			// ////testCodeEnd

			try {
				ctx = new InitialDirContext(property);
				isAuth = true;
				// "Login Success";
				logger.debug("Login Success");
			} catch (Exception e) {
				logger.error("에러발생", e);
				// "Login Failure";
			} finally {
				if (ctx != null)
					try {
						ctx.close();
					} catch (Exception e) {
					}
			}
		} else {
			// "Password Fail";
		}
		logger.debug("auth종료(결과=" + isAuth + ")");
		return isAuth;
	}
}
