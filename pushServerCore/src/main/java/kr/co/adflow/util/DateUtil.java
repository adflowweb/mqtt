/*
 * 
 */
package kr.co.adflow.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DateUtil.
 */
public class DateUtil {

	/** The format date iso. */
	public static String FORMAT_DATE_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	// public static String FORMAT_DATE_ISO="yyyy-MM-dd'T'HH:mm:ss.sssZ";

	/**
	 * The main method.
	 *
	 * @param agrs
	 *            the arguments
	 */
	public static void main(String[] agrs) {
		// System.out.println(fromISODateString("2015-02-07T14:48:00.000Z")
		// .getTime());

		// System.out.println(getDate(new Date()));
	}

	/**
	 * After minute.
	 *
	 * @param minute
	 *            the minute
	 * @param millis
	 *            the millis
	 * @return the date
	 */
	public static Date afterMinute(int minute, long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);

		cal.add(Calendar.MINUTE, minute);

		return cal.getTime();
	}

	/**
	 * Gets the yyyymm.
	 *
	 * @return the yyyymm
	 */
	public static String getYYYYMM() {
		return DateUtil.getYYYYMM(0);
	}

	/**
	 * Gets the yyyymm.
	 *
	 * @param month
	 *            the month
	 * @return the yyyymm
	 */
	public static String getYYYYMM(int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

		cal.add(Calendar.MONTH, month);

		return DateUtil.getDate(cal.getTime(), "yyyyMM");
	}

	/**
	 * Gets the yyyymmdd.
	 *
	 * @param day
	 *            the day
	 * @return the yyyymmdd
	 */
	public static String getYYYYMMDD(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

		cal.add(Calendar.DATE, day);

		return DateUtil.getDate(cal.getTime(), "yyyyMMdd");
	}

	/**
	 * Gets the yyyymmdd.
	 *
	 * @param day
	 *            the day
	 * @return the yyyymmdd
	 */
	public static String getYYYYMMDD(String strDate, int day) {

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = transFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		cal.add(Calendar.DATE, day);
		return DateUtil.getDate(cal.getTime(), "yyyyMMdd");
	}

	/**
	 * Gets the date.
	 *
	 * @param date
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @return the date
	 */
	private static String getDate(Date date, String pattern) {

		SimpleDateFormat fmt = new SimpleDateFormat(pattern);

		return fmt.format(date);

	}

	public static String getDate(Date date) {

		SimpleDateFormat fmt = new SimpleDateFormat(FORMAT_DATE_ISO);

		return fmt.format(date);

	}

	/**
	 * Gets the yyyymmddhhmmss.
	 *
	 * @param date
	 *            the date
	 * @return the yyyymmddhhmmss
	 */
	public static String getDateTime(Date date) {
		return DateUtil.getDate(date, "yyyyMMddHHmmss");
	}

	/**
	 * Gets the yyyymmddhhmmss.
	 *
	 * @param date
	 *            the date
	 * @return the yyyymmddhhmmss
	 */
	public static String getDateTimeSvc(Date date) {
		return DateUtil.getDate(date, "yyyy.MM.dd. aa hh:mm:ss");
	}

	/**
	 * From iso date string.
	 *
	 * @param isoDateString
	 *            the iso date string
	 * @return the date
	 */
	public static Date fromISODateString(String isoDateString) {
		if (isoDateString == null) {
			return null;
		}

		Calendar calendar = javax.xml.bind.DatatypeConverter.parseDateTime(isoDateString);

		return calendar.getTime();
	}
	// {
	// if (isoDateString == null) {
	// return null;
	// }
	// Date result = null;
	// DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_ISO);
	// try {
	// result = dateFormat.parse(isoDateString);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new RuntimeException(e.toString());
	// }
	//
	// return result;
	// }

}
