package kr.co.adflow.fdk.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestInvokeWorkLightAdapterByHTTP {

	private final static String USER_AGENT = "Mozilla/5.0";
	private static int callCount = 0;

	public static void main(String[] args) {

		for (int i = 0; i < 100000; i++) {
			try {
				// sendGet();
				sendPost();
				callCount++;
				System.out.println("callCount : " + callCount);
				Thread.sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// HTTP GET request
	private static void sendGet() throws Exception {

		String url = "http://testm.moneyon.com/FirstStore/invoke?adapter=mini_WidgetAdapter&procedure=getDailyReport&parameters=[\"20141127\",\"20141127\",\"mobiletest1\",\"mobiletest1\"]";

		// [\"cal_date_fr\":\"20141127\",\"cal_date_to\":\"20141127\",\"password\":\"mobiletest1\",\"user_id\":\"mobiletest1\"}";

		// String url =
		// "http://testm.moneyon.com/FirstStore/apps/services/api/FirstStore/android/init";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

	// HTTP POST request
	private static void sendPost() throws Exception {
		BufferedReader in = null;
		try {
			String url = "http://testm.moneyon.com/FirstStore/invoke";

			long start = System.currentTimeMillis();

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("WL-Instance-Id",
					"tt3kct1lt7kitbu1a5cd8hljsj");

			String urlParameters = "adapter=mini_WidgetAdapter&procedure=getDailyReport&parameters=[{'cal_date_fr':'20141125','cal_date_to':'20141127','password':'mobiletest1','user_id':'mobiletest1'}]";

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			long stop = System.currentTimeMillis();
			System.out.println("elapsedTime : " + (stop - start) + " ms");

			// print result
			System.out.println(response.toString());
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}
}
