package kr.co.adflow.pms.core.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static Date afterMinute(int minute,long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		
		cal.add(Calendar.MINUTE, minute);
		
		return cal.getTime();
	}

}
