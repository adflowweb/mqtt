/*
 * 
 */
package kr.co.adflow.util;

import java.security.Provider;

// TODO: Auto-generated Javadoc
/**
 * The Class AcceptAllProvider.
 */
public class AcceptAllProvider extends Provider {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new accept all provider.
	 */
	public AcceptAllProvider() {
		super("AcceptAllProvider", 1.0, "Trust all X509 certificates");
		put("TrustManagerFactory.TrustAllCertificates",
				AcceptAllTrustManagerFactory.class.getName());
	}

	/**
	 * A factory for creating AcceptAllTrustManager objects.
	 */
	protected static class AcceptAllTrustManagerFactory extends
			javax.net.ssl.TrustManagerFactorySpi {
		
		/**
		 * Instantiates a new accept all trust manager factory.
		 */
		public AcceptAllTrustManagerFactory() {
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.TrustManagerFactorySpi#engineInit(java.security.KeyStore)
		 */
		protected void engineInit(java.security.KeyStore keystore) {
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.TrustManagerFactorySpi#engineInit(javax.net.ssl.ManagerFactoryParameters)
		 */
		protected void engineInit(
				javax.net.ssl.ManagerFactoryParameters parameters) {
		}

		/* (non-Javadoc)
		 * @see javax.net.ssl.TrustManagerFactorySpi#engineGetTrustManagers()
		 */
		protected javax.net.ssl.TrustManager[] engineGetTrustManagers() {
			return new javax.net.ssl.TrustManager[] { new AcceptAllX509TrustManager() };
		}

		/**
		 * The Class AcceptAllX509TrustManager.
		 */
		protected static class AcceptAllX509TrustManager implements
				javax.net.ssl.X509TrustManager {
			
			/* (non-Javadoc)
			 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
			 */
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certificateChain,
					String authType)
					throws java.security.cert.CertificateException {
				report("Client authtype=" + authType);
				for (java.security.cert.X509Certificate certificate : certificateChain) {
					report("Accepting:" + certificate);
				}
			}

			/* (non-Javadoc)
			 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
			 */
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certificateChain,
					String authType)
					throws java.security.cert.CertificateException {
				report("Server authtype=" + authType);
				for (java.security.cert.X509Certificate certificate : certificateChain) {
					report("Accepting:" + certificate);
				}
			}

			/* (non-Javadoc)
			 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
			 */
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[0];
			}

			/**
			 * Report.
			 *
			 * @param string the string
			 */
			private static void report(String string) {
				System.out.println(string);
			}
		}

	}
}
