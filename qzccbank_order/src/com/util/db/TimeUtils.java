package com.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.autodata.SpecialDay;

public class TimeUtils {
	private static ResourceBundle rb = ResourceBundle.getBundle("com.util.db.db-config");
	private static Calendar now = Calendar.getInstance();
	private static int year     = now.get(Calendar.YEAR);
	private static int month    = (now.get(Calendar.MONTH) + 1);
	private static int day      = now.get(Calendar.DAY_OF_MONTH);
	private static int hour     = now.get(Calendar.HOUR_OF_DAY);
	private static int min      = now.get(Calendar.MINUTE);
	private static int sec      = now.get(Calendar.SECOND);
	private TimeUtils(){}
	
	public static int getYear() {
		return year;
	}

	public static int getMonth() {
		return month;
	}

	public static int getDay() {
		return day;
	}
	@SuppressWarnings("unused")
	public static int getTomDay() {
		Calendar tom = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
			tom.add(Calendar.DAY_OF_MONTH, +3);
		}else{
			tom.add(Calendar.DAY_OF_MONTH, +1);
		}
		return tom.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour() {
		return hour;
	}

	public static int getMin() {
		return min;
	}

	public static int getSec() {
		return sec;
	}

	public  static String getNowTime(){
		String nowtime = year + "-" + month + "-" + day + "|" + hour + ":" + min + ":" + sec;
		return nowtime;
	}
	
	public static String getNowTime2Int(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
	    String dateString = sdf.format(new Date());  
		return dateString;
	}
	
	public static int getNowTimeyMdInt(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
	    String dateString = sdf.format(new Date());  
		return Integer.parseInt(dateString);
	}
	
	public static int getNowTimeyLMInt(int month){
		String ZlastMonth2 = String.format("%02d", month);
		String Ym = "";
		if(TimeUtils.getMonth() == 1){
			Ym = String.valueOf(TimeUtils.getYear()-1)+"12";
		}else{
			Ym = String.valueOf(TimeUtils.getYear())+ZlastMonth2;
		}
		return Integer.parseInt(Ym);
	}
	
	public static String getNowTimeHms(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
	    String dateString = sdf.format(new Date());  
		return dateString;
	}
	public static String getNowTimeH(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH");  
	    String dateString = sdf.format(new Date());  
		return dateString;
	}




	public static List<Date> getWeekDates() {
		int year = getYear();
		int month  = getMonth();
		List<Date> dates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);

		while (cal.get(Calendar.YEAR) == year
				&& cal.get(Calendar.MONTH) < month) {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			for (int i = 0; i < dates.size(); i++) {
				if(delday().get(dates.get(i).getDate()) != null){
					if(delday().get(dates.get(i).getDate()) == 0){
						dates.remove(i);
						continue;
					}
				}
			}
			Date tmp = (Date) cal.getTime().clone();
			//特殊日期添加
			if(delday().get(tmp.getDate()) != null && delday().get(tmp.getDate()) == 1){
				dates.add(tmp);
			}
			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY) ) {
				dates.add(tmp);
			}
			cal.add(Calendar.DATE, 1);
		}
		return dates;
	}
	
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static Map<Integer, Integer> delday(){
		/* 过滤开始 */
		String file_address   = rb.getString("file_address_product")+"day.data";
		String whole_content = SpecialDay.readToString(file_address);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String[] rowBuf = whole_content.split("\r\n");
		String[] colBuf = new String[8];
		for (int i = 0; i < rowBuf.length; i++) {
			colBuf = rowBuf[i].split("\\,", -1);
				String months = colBuf[0].substring(0, 2);
				if(TimeUtils.getMonth() == Integer.parseInt(months)){
					String days = colBuf[0].substring(2, 4);
					String status = colBuf[1];
					map.put(Integer.parseInt(days), Integer.parseInt(status));
				}
		}
		return map;
		/* 过滤开始 */
	}
	public static void main(String[] args) {
//		System.out.println(TimeUtils.delday());
//		System.out.println(TimeUtils.getWeekDates());
		for (int i = 0; i < TimeUtils.getWeekDates().size(); i++) {
			System.out.println(TimeUtils.getWeekDates().get(i).getDate());
		}
	}
}
