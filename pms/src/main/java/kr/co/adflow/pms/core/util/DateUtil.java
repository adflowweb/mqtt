package kr.co.adflow.pms.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	
	public static Date afterMinute(int minute,long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		
		cal.add(Calendar.MINUTE, minute);
		
		return cal.getTime();
	}
	
	public static String getYYYYMM() {
		return DateUtil.getYYYYMM(0);
	}
	
	public static String getYYYYMM(int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		cal.add(Calendar.MONTH, month);

		return DateUtil.getDate(cal.getTime(), "YYYYMM");
	}

	private static String getDate(Date date, String pattern) {

		SimpleDateFormat fmt = new SimpleDateFormat(pattern);

		return fmt.format(date);

	}

}
