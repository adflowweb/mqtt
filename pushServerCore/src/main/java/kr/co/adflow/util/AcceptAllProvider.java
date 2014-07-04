package kr.co.adflow.util;

import java.security.Provider;

public class AcceptAllProvider extends Provider {

	private static final long serialVersionUID = 1L;

	public AcceptAllProvider() {
		super("AcceptAllProvider", 1.0, "Trust all X509 certificates");
		put("TrustManagerFactory.TrustAllCertificates",
				AcceptAllTrustManagerFactory.class.getName());
	}

	protected static class AcceptAllTrustManagerFactory extends
			javax.net.ssl.TrustManagerFactorySpi {
		public AcceptAllTrustManagerFactory() {
		}

		protected void engineInit(java.security.KeyStore keystore) {
		}

		protected void engineInit(
				javax.net.ssl.ManagerFactoryParameters parameters) {
		}

		protected javax.net.ssl.TrustManager[] engineGetTrustManagers() {
			return new javax.net.ssl.TrustManager[] { new AcceptAllX509TrustManager() };
		}

		protected static class AcceptAllX509TrustManager implements
				javax.net.ssl.X509TrustManager {
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certificateChain,
					String authType)
					throws java.security.cert.CertificateException {
				report("Client authtype=" + authType);
				for (java.security.cert.X509Certificate certificate : certificateChain) {
					report("Accepting:" + certificate);
				}
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certificateChain,
					String authType)
					throws java.security.cert.CertificateException {
				report("Server authtype=" + authType);
				for (java.security.cert.X509Certificate certificate : certificateChain) {
					report("Accepting:" + certificate);
				}
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[0];
			}

			private static void report(String string) {
				System.out.println(string);
			}
		}

	}
}
