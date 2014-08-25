package kr.co.adflow.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class ADFSSLSocketFactory {

	public static final String TAG = "에스에스엘소켓팩토리";
	static SSLSocketFactory sslSocketFactory;

	static {
		// Imports: javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(final X509Certificate[] chain,
						final String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] chain,
						final String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} };

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts,
					new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			Log.e(TAG, "에러발생", e);
		}
	}

	public static SSLSocketFactory getSSLSokcetFactory() {
		return sslSocketFactory;
	}
}
