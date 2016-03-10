package com.xyzh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author 作者： 周卫东
 * @E-mail 邮箱： zhouweidong@gochinatv.com
 * @version 创建时间：2015年12月16日 下午12:11:45 类说明 时间工具类
 */
public class TimeUtil {
	static final String propertiesName = "urlForReptile.properties";
	public static int parse(){
			Properties p = new Properties();
				File file = new File(propertiesName);
				InputStream in;
				try {
					in = new FileInputStream(file);
					p.load(in);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return Integer.parseInt(p.getProperty("tableIfNull"));
	}
	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期属于一年中的哪一天
	 * @throws ParseException
	 */
	public static int getDayOfYear(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期属于一年中的哪一月
	 * @throws ParseException
	 */
	public static int getMonthOfYear(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		System.out.println(sdf.format(calendar.getTime()));
		return calendar.get(Calendar.MONTH)+1;
	}

	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期的年份
	 * @throws ParseException
	 */
	public static int getYear(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期属于一年中的哪一周
	 * @throws ParseException
	 */
	public static int getWeekOfYear(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期属于一周中的哪一天
	 * @throws ParseException
	 */
	public static int getDayOfWeek(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		return (calendar.get(Calendar.DAY_OF_WEEK)-1);
	}

	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期属于本月的第几天
	 * @throws ParseException
	 */
	public static int getDayOfMonth(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取当前日期属于本月的第几周
	 * @throws ParseException
	 */
	public static int getWeekOfMonth(String dateTime) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}
	/**
	 * 判断某个月有多少天
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthTotalDay(String dateTime) throws ParseException{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.clear();
		calendar2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	/**
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * 获取动态表明后面的数字
	 * @return
	 * @throws ParseException 
	 */
	public static String getTableName(String dateTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		dateTime = sdf.format(calendar.getTime()).replace("-", "_");
		return dateTime;
	}
	/**
	 * 获取前一天的表名
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	public static String getTableName2(String dateTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = sdf.parse(dateTime);
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -2);
		dateTime = sdf.format(calendar.getTime()).replace("-", "_");
		return dateTime;
	}

/**
 * 获取本周的第一天和最后一天
 * @return
 * @throws ParseException 
 */
	public static Map getWeekFirstDayAndLastDay(String dateTime) throws ParseException {
		Map<String,String> map = new HashMap<String,String>();
		Calendar cal =Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(sdf.parse(dateTime));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
		map.put("mon", sdf.format(cal.getTime()));
//		System.out.println("********得到本周一的日期*******"+sdf.format(cal.getTime()));
		//这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		//增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		map.put("sun", sdf.format(cal.getTime()));
//		System.out.println("********得到本周天的日期*******"+sdf.format(cal.getTime()));
		return map;
	}
	/**
	 * 获取当前月的第一天和最后一天
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	public static Map getMonthFirstDayAndLastDay(String dateTime) throws ParseException{
		  Map<String,String> map = new HashMap<String,String>();
		  // 获取Calendar  
		  Calendar calendar = Calendar.getInstance(); 
		   DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		  // 设置时间,当前时间不用设置  
		   calendar.setTime(sdf.parse(dateTime));  
		   calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE)); 
		   map.put("monthF", sdf.format(calendar.getTime()));
		   System.out.println("*********得到本月的最小日期**********"+sdf.format(calendar.getTime()));
		 // 设置日期为本月最大日期  
		   calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));  
		 // 打印  
		   map.put("monthL", sdf.format(calendar.getTime()));
		   System.out.println("*********得到本月的最大日期**********"+sdf.format(calendar.getTime()));
		   return map;
		 }
	public static void main(String[] args) throws ParseException {
//		System.out.println(getMonthDate("2015-02-12"));
		String date = "2016-03-01";
		System.out.println(getMonthOfYear(date));
		
	}
}
