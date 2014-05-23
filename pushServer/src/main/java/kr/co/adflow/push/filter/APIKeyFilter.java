package kr.co.adflow.push.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

		final String auth = httpRequest.getHeader("X-ApiKey");
		logger.debug("X-ApiKey=" + auth);

		if (auth == null) {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.debug("doFilter종료()");
			return;
		}

		// check api key
		Connection conn = null;
		try {
			conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM token WHERE tokenid ='" + auth
							+ "'");
			if (!rs.next()) {
				logger.debug("해당토큰이존재하지않습니다.");
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				long stop = System.currentTimeMillis();
				logger.debug("걸린시간=" + (stop - start) + " ms");
				logger.debug("doFilter종료()");

				return;
			}

		} catch (Exception e) {
			logger.error("API키체크중에러발생", e);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			long stop = System.currentTimeMillis();
			logger.debug("걸린시간=" + (stop - start) + " ms");
			logger.debug("doFilter종료()");
			return;
		} finally {
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
		logger.debug("doFilter종료()");

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
