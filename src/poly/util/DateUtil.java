package poly.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 날짜, 시간 출력하기
	 * @param fm 날짜 출력 형식
	 * @return
	 */
	public static String getDateTime(String fm) {
		
		Date today = new Date();
	    System.out.println(today);
	        
	    SimpleDateFormat date = new SimpleDateFormat(fm);
	    
	    return date.format(today);
	}

	/**
	 * 날짜, 시간 출력하기
	 * @return 기본값은 년.월.일
	 */
	public static String getDateTime() {
		
		return getDateTime("yyyy.MM.dd");
		
	}
	
	public static String addDate(int day) throws Exception {
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		
		Calendar cal = Calendar.getInstance();
		
		Date today = new Date();
		
		cal.setTime(today);
		
		cal.add(Calendar.DATE, day);
		cal.add(Calendar.MONTH , -1);

		
		return date.format(cal.getTime());

	}
	
	public static String endDayChg(String day) throws Exception {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd");
		Calendar cal = Calendar.getInstance();
		Date endday = date.parse(day);
		cal.setTime(endday);
		cal.add(Calendar.DATE, 1);
		
		return date.format(cal.getTime());
	}
}
