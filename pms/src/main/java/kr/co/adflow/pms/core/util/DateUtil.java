package kr.co.adflow.pms.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	
	public static String FORMAT_DATE_ISO="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	//public static String FORMAT_DATE_ISO="yyyy-MM-dd'T'HH:mm:ss.sssZ";
		
	
	public static void main(String[] agrs) {
		System.out.println(fromISODateString("2015-02-07T14:48:00.000Z").getTime());
	}
	
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
	
	public static Date fromISODateString(String isoDateString) {
		if (isoDateString == null) {
		return null;
	}
		
	Calendar calendar = javax.xml.bind.DatatypeConverter.parseDateTime(isoDateString);
	
	return calendar.getTime();
	}
//	{	
//		if (isoDateString == null) {
//			return null;
//		}
//		Date result = null;
//		DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_ISO);
//		try {
//			result = dateFormat.parse(isoDateString);
//	} catch (Exception e) {
//		e.printStackTrace();
//		throw new RuntimeException(e.toString());
//	}
//		
//		return result;
//	}

}
