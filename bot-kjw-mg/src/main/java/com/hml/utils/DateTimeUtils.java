package com.hml.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 日期时间相关工具
 * @author hml
 * @date Jan 14, 2019
 */
public class DateTimeUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 获取当前标准格式化日期时间
	 * @param date
	 * @return
	 */
	public static String getDateTime() {
		return getDateTime(new Date());
	}
	
	/**
	 * 标准格式化日期时间
	 * @param date
	 * @return
	 */
	public static String getDateTime(Date date) {
		return (new SimpleDateFormat(DATE_FORMAT)).format(date);
	}
	
	
	public static String getCurrentDate(String format) {
		if(StringUtils.isBlank(format)) {
			format = "yyyyMMdd";
		}
		String date = new SimpleDateFormat(format).format(new Date());
		return date;
	}
	
	public static String addDate(String fdate,String format,int days) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(fdate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		Date time = cal.getTime();
		return sdf.format(time);
	}
	
	public static String addMinute(int minutes) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		Date time = cal.getTime();
		return sdf.format(time);
	}
	
	public static String lastDayOfMonth(String month,String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(month);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date time = cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	
	public static List<String> getDates(String fdate,int nums,String format)throws Exception{
		List<String> list = new LinkedList<String>();
		for(int i=nums; i > 0 ;i--) {
			list.add(addDate(fdate, format, -1 * i));
		}
		list.add(fdate);
		return list;
	}
	
	public static LocalDateTime parseDate(String date) {
		 // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串解析为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        return localDateTime;
	}
	
	public static long getSeconds(LocalDateTime dateTime) {
    	Duration duration = Duration.between(LocalDateTime.now(), dateTime);
         // 获取相差的秒数
        long secondsBetween = duration.getSeconds();
        if(secondsBetween < 0) {
        	secondsBetween = 0l;
        }
        return secondsBetween;
	}
}
