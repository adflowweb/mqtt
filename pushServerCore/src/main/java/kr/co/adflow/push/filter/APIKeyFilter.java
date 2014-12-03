package kr.co.adflow.push.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import kr.co.adflow.push.service.impl.HAServiceImpl;

import org.slf4j.LoggerFactory;

/**
 * @author nadir93
 * @date 2014. 5. 20.
 * 
 */
public class APIKeyFilter implements Filter {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(APIKeyFilter.class);
	private DataSource ds = null;

	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.debug("init시작(config=" + config);
		InitialContext cxt = null;
		try {
			cxt = new InitialContext();
			ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/push");
			logger.debug("datasource=" + ds);
		} catch (Exception e) {
			logger.error("인증필터초기화중에러발생", e);
		}
		logger.debug("init종료()");
	}

	@Override
	public void destroy() {
		logger.debug("destroy시작()");
		logger.debug("destroy종료()");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.debug("doFilter시작(request=" + request + ",response=" + response
				+ ",chain=" + chain);

		long start = System.currentTimeMillis();

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;

		logger.debug("requestURI=" + httpRequest.getRequestURI());
		logger.debug("requestMethod=" + httpRequest.getMethod());
		logger.debug("contentType=" + httpRequest.getContentType());

		// req parameter
		for (Enumeration e = httpRequest.getParameterNames(); e
				.hasMoreElements();) {
			String param = (String) e.nextElement();
			logger.debug(param + ":" + httpRequest.getParameter(param));
		}

		// req header
		for (Enumeration e = httpRequest.getHeaderNames(); e.hasMoreElements();) {
			String headerNames = (String) e.nextElement();
			logger.debug(headerNames + ":" + httpRequest.getHeader(headerNames));
		}

		String appContext = httpRequest.getRequestURI().substring(
				httpRequest.getRequestURI().lastIndexOf("/") + 1);
		logger.debug("appContext=" + appContext);

		// L4존재하면 필요없음
		// if (!HAServiceImpl.active) {
		// logger.debug("마스터가아닙니다.");
		// httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		// logger.debug("doFilter종료(responseCode=" + httpResponse.getStatus()
		// + ")");
		// return;
		// }

		// /v1/auth 요청일 경우는 X-ApiKey를 체크하지 않음
		// 이유는 ApiKey를 발급받는 요청이므로 ...
		if (appContext.equals("auth") || appContext.equals("adminAuth")) {
			chain.doFilter(httpRequest, httpResponse);
			long stop = System.currentTimeMillis();
			logger.debug("걸린시간=" + (stop - start) + " ms");
			logger.debug("doFilter종료(responseCode=" + httpResponse.getStatus()
					+ ")");
			return;
		}

		final String auth = httpRequest.getHeader("X-ApiKey");
		logger.debug("X-ApiKey=" + auth);

		if (auth == null) {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.debug("doFilter종료(responseCode=" + httpResponse.getStatus()
					+ ")");
			return;
		}

		// check api key
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM token WHERE tokenid ='"
					+ auth + "'");
			if (!rs.next()) {
				logger.debug("해당토큰이존재하지않습니다.");
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				long stop = System.currentTimeMillis();
				logger.debug("걸린시간=" + (stop - start) + " ms");
				logger.debug("doFilter종료(responseCode="
						+ httpResponse.getStatus() + ")");

				return;
			}

		} catch (Exception e) {
			logger.error("API키체크중에러발생", e);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			long stop = System.currentTimeMillis();
			logger.debug("걸린시간=" + (stop - start) + " ms");
			logger.debug("doFilter종료(responseCode=" + httpResponse.getStatus()
					+ ")");
			return;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		chain.doFilter(httpRequest, httpResponse);
		long stop = System.currentTimeMillis();
		logger.debug("걸린시간=" + (stop - start) + " ms");
		logger.debug("doFilter종료(responseCode=" + httpResponse.getStatus()
				+ ")");

		// if (auth != null) {
		//
		// final int index = auth.indexOf(' ');
		// if (index > 0) {
		// final String[] credentials = StringUtils.split(new String(
		// Base64.decodeBase64(auth.substring(index)),
		// Charsets.UTF_8), ':');
		//
		// if (credentials.length == 2 && _user.equals(credentials[0])
		// && _password.equals(credentials[1])) {
		// chain.doFilter(httpRequest, httpResponse);
		// return;
		// }
		// }
		// }

		// httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"\"");
		// httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
