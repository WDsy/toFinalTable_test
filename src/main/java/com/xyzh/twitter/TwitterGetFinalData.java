package com.xyzh.twitter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.xyzh.util.DBLinkPoll;
import com.xyzh.util.DBLinkPollForAdd;
import com.xyzh.util.OperateDatabase;
import com.xyzh.util.TimeUtil;
import com.xyzh.util.TableWhetherExist;

/**
 * @author jiyangnan
 * @since 2015-12-16 14:46:29
 * @desribe 从model表生成数据到final表
 */
public class TwitterGetFinalData {
	static final String propertiesName = "urlForReptile.properties";
	Connection connForReptile = null;
	Connection connForProduct = null;
	String id = null;
	String name = null;
	String fileName = "Database.properties";

	/**
	 * 处理twitter总榜数据
	 * 
	 * @author 作者： 周卫东
	 * @E-mail 邮箱： zhouweidong@gochinatv.com
	 * @param tableName
	 * @throws ParseException
	 * @throws SQLException
	 */
	public void twitterToFinalTotal(String dateTime_str) throws ParseException,
			SQLException {
		OperateDatabase odb = new OperateDatabase();
		String dateTime_tableName = TimeUtil.getTableName(dateTime_str);
		int week = TimeUtil.getWeekOfYear(dateTime_str);
		int month = TimeUtil.getMonthOfYear(dateTime_str);
		DBLinkPoll.setFileName(propertiesName);
		// 判断表是否存在
		TableWhetherExist tableWhetherExist = new TableWhetherExist();
		boolean flag = tableWhetherExist.whetherExist("t_twitter_total_"
				+ dateTime_tableName, "baseForReptile");
		if (flag) {
			String sql = "select  c.Id as columnId,c.parentId as parentId,c.cnName as title,c.level as level, "
					+ "sum(a.contentCount) as contentCount, "
					+ "sum(a.viewCount) as viewCount, "
					+ "sum(a.goodCount) as goodCount, "
					+ "sum(a.badCount) as badCount, "
					+ "sum(a.shareCount) as shareCount, "
					+ "sum(a.commentCount) as commentCount, "
					+ "a.dateTime as dateTime,a.languageType as language  "
					+ "FROM  t_twitter_total_"+dateTime_tableName+"   a  "
					+ " JOIN t_oper_keywords k ON a.keyWordId = k.Id JOIN platformbackstage.t_oper_columns c ON k.columnId = c.id "
					+ "  GROUP BY c.Id  order by viewCount desc";
			System.out.println(sql);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			resultList = DBLinkPoll.getQueryRunner().query(sql,
					new MapListHandler());
			if (CollectionUtils.isNotEmpty(resultList)) {
				Map<String, Object> myMap = new HashMap<String, Object>();
				for (int i = 0; i < resultList.size(); i++) {
					myMap = resultList.get(i);
					myMap.put("weekNumber", week);
					myMap.put("monthNumber", month);
					myMap.put("language", "CN");
				}
			}
			
			System.out.println("开始插入+++++++++++++++++++++++++++++++++++++++++++++++++++++");
			odb.addToProductBase(resultList, "t_twitter_total_final");
			System.out.println("插入完成+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} else {
			System.out
					.println("t_twitter_total_" + dateTime_tableName + "表不存在");
		}

	}

	/**
	 * Twitter Day Data and Interval
	 * 
	 * @param dateTime_str
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void twitterToFinalDay(String dateTime_str) throws SQLException,
			ParseException {
		OperateDatabase odb = new OperateDatabase();
		String dateTime_tableName = TimeUtil.getTableName(dateTime_str);
		String dateTime_tableName2 = TimeUtil.getTableName2(dateTime_str);
		int week = TimeUtil.getWeekOfYear(dateTime_str);
		int month = TimeUtil.getMonthOfYear(dateTime_str);
		DBLinkPoll.setFileName(propertiesName);
		boolean flag = new TableWhetherExist().whetherExist("t_twitter_day_"
				+ dateTime_tableName, "baseForReptile");
		if (flag) {
			boolean flag2 = new TableWhetherExist().whetherExist(
					"t_twitter_day_" + dateTime_tableName2, "baseForReptile");
			if (!flag2) {
				while (!flag2) {
					System.out.println("t_twitter_day_" + dateTime_tableName2
							+ "表不存在，往前推一天比较增量");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(dateTime_tableName2));
					calendar.add(Calendar.DATE, -1);
					Date date = calendar.getTime();
					calendar.clear();
					calendar.setTime(date);
					dateTime_tableName2 = sdf.format(calendar.getTime());
					flag2 = new TableWhetherExist().whetherExist(
							"t_twitter_day_" + dateTime_tableName2,
							"baseForReptile");
				}
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			String sql = "select  c.Id as columnId,c.parentId as parentId,c.cnName as title,c.level as level,"
					+ "sum(a.contentCount) as contentCount,"
					+ "cast(ifnull( ( sum(a.contentCount)  -ifnull(  sum(d.contentCount)  ,0)  )/sum(d.contentCount)  ,0)*100 as decimal(18,2))  as contentCountInterval, "
					+ "sum(a.viewCount) as viewCount,"
					+ "cast(ifnull((sum(a.viewCount)-ifnull(sum(d.viewCount),0))/sum(d.viewCount),0)*100 as decimal(18,2))  as viewCountInterval, "
					+ "sum(a.goodCount) as goodCount,"
					+ "cast(ifnull((sum(a.goodCount)-ifnull(sum(d.goodCount),0))/sum(d.goodCount),0)*100  as decimal(18,2))  as goodCountInterval, "
					+ "sum(a.badCount) as badCount, "
					+ "cast(ifnull((sum(a.badCount)-ifnull(sum(d.badCount),0))/sum(d.badCount),0)*100 as decimal(18,2))  as badCountInterval, "
					+ "sum(a.shareCount) as shareCount, "
					+ "cast(ifnull((sum(a.shareCount)-ifnull(sum(d.shareCount),0))/sum(d.shareCount),0)*100 as decimal(18,2))  as shareCountInterval, "
					+ "sum(a.commentCount) as commentCount, "
					+ "cast(ifnull((sum(a.commentCount)-ifnull(sum(d.commentCount),0))/sum(d.commentCount),0)*100 as decimal(18,2))  as commentCountInterval, "
					+ "a.dateTime as dateTime,a.languageType as language  "
					+ "FROM  t_twitter_day_"+dateTime_tableName+"   a  "
					+ "JOIN t_twitter_day_"+dateTime_tableName2+"  d ON a.keyWordId = d.keyWordId  "
					+ " JOIN t_oper_keywords k ON d.keyWordId = k.Id JOIN platformbackstage.t_oper_columns c ON k.columnId = c.id GROUP BY c.Id  ";
			System.out.println(sql);
			resultList = DBLinkPoll.getQueryRunner().query(sql,
					new MapListHandler());

			if (CollectionUtils.isNotEmpty(resultList)) {
				Map<String, Object> myMap = new HashMap<String, Object>();
				for (int i = 0; i < resultList.size(); i++) {
					myMap = resultList.get(i);
					myMap.put("weekNumber", week);
					myMap.put("monthNumber", month);
					myMap.put("language", "CN");
				}
			}
			System.out
					.println("开始插入+++++++++++++++++++++++++++++++++++++++++++++++++++++");
			odb.addToProductBase(resultList, "t_twitter_day_final");
			System.out
					.println("插入完成+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} else {
			System.out.println("t_twitter_day_" + dateTime_tableName + "表不存在");
		}
	}

	/**
	 * 根据已经生成的日榜 生成周榜
	 * 
	 * @param dateTime
	 * @throws ParseException
	 * @throws SQLException
	 */
	public void twitterToFinalWeek(String dateTime) throws ParseException,
			SQLException {
		OperateDatabase odb = new OperateDatabase();
		// 周
		int week = TimeUtil.getWeekOfYear(dateTime);
		// 月
		// 查看今天是周几
		int weekDay = TimeUtil.getDayOfWeek(dateTime);
		int week1HaveDay = new TableWhetherExist().getRecordNumber(
				" t_twitter_day_final ", " and weekNumber = " + (week-1));
		int week2HaveDay = new TableWhetherExist().getRecordNumber(
				" t_twitter_day_final ", " and weekNumber = " + (week-2));
//		int week1HaveDay = new TableWhetherExist().getRecordNumber(
//				" t_ggplus_day_final ", " and weekNumber = 1");
//		int week2HaveDay = new TableWhetherExist().getRecordNumber(
//				" t_ggplus_day_final ", " and weekNumber = 52");
		if (weekDay == 1) {
			String sql = "select a.columnId,a.parentId,a.title,a.level, a.dateTime, a.language,"
					+ "cast(ifnull(a.contentCount,0) as decimal(18,0)) as contentCount, "
					+ "cast(ifnull((a.contentCount-b.contentCount ) /b.contentCount*100,0) as decimal(18,2)) as contentCountInterval"
					+ ",cast(ifnull(a.viewCount,0) as decimal(18,0)) as viewCount, "
					+ "cast(ifnull((a.viewCount-b.viewCount ) /b.viewCount*100,0) as decimal(18,2)) as viewCountInterval"
					+ ",cast(ifnull(a.goodCount,0) as decimal(18,0)) as goodCount, "
					+ "cast(ifnull((a.goodCount-b.goodCount ) /b.goodCount*100,0) as decimal(18,2)) as goodCountInterval"
					+ ",cast(ifnull(a.badCount,0) as decimal(18,0)) as badCount, "
					+ "cast(ifnull((a.badCount-b.badCount) /b.badCount*100,0) as decimal(18,2)) as badCountInterval"
					+ ",cast(ifnull(a.shareCount,0) as decimal(18,0)) as shareCount, "
					+ "cast(ifnull((a.shareCount-b.shareCount) /b.shareCount*100,0) as decimal(18,2)) as shareCountInterval"
					+ ",cast(ifnull(a.commentCount,0) as decimal(18,0)) as commentCount, "
					+ "cast(ifnull((a.commentCount-b.commentCount) /b.commentCount*100,0) as decimal(18,2)) as commentCountInterval"
					+ " from  (select columnId,parentId, title,level, dateTime, language,"
					+ "sum(contentCount)/"+week1HaveDay+" as contentCount"
					+ ",sum(viewCount)/"+week1HaveDay+" as viewCount"
					+ ",sum(goodCount)/"+week1HaveDay+" as goodCount"
					+ ",sum(badCount)/"+week1HaveDay+" as badCount"
					+ ",sum(shareCount)/"+week1HaveDay+" as shareCount"
					+ ",sum(commentCount)/"+week1HaveDay+" as commentCount"
					+ " from t_twitter_day_final where weekNumber="+(week-1)+" group by title) as  b,  "
					+ "(select  columnId,parentId,title,level , dateTime, language ,  "
					+ "sum(contentCount)/"+week2HaveDay+" as contentCount"
					+ ",sum(viewCount)/"+week2HaveDay+" as viewCount"
					+ ",sum(goodCount)/"+week2HaveDay+" as goodCount"
					+ ",sum(badCount)/"+week2HaveDay+" as badCount"
					+ ",sum(shareCount)/"+week2HaveDay+" as shareCount"
					+ ",sum(commentCount)/"+week2HaveDay+" as commentCount"
					+ " from t_twitter_day_final where weekNumber="+(week-2)+" group by title) as a  "
					+ "where b.title=a.title ";
			DBLinkPoll.setFileName(propertiesName);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			System.out.println(sql);
			resultList = DBLinkPollForAdd.getQueryRunner().query(sql,
					new MapListHandler());
			if (CollectionUtils.isNotEmpty(resultList)) {
				Map<String, Object> myMap = new HashMap<String, Object>();
				for (int i = 0; i < resultList.size(); i++) {
					myMap = resultList.get(i);
					myMap.put("language", "CN");
				}
			}
			System.out.println(resultList.size());
			odb.addToProductBase(resultList, "t_twitter_week_final_copy");
		} else {
			System.out.println("今天不是周一，不用生成周榜");
		}
	}

	/**
	 * 根据已有的日榜数据生成月榜
	 * 
	 * @param dateTime
	 * @throws ParseException
	 * @throws SQLException
	 */
	public void twitterToFinalMonth(String dateTime) throws ParseException,
			SQLException {
		OperateDatabase odb = new OperateDatabase();
		// 月
		int month = TimeUtil.getMonthOfYear(dateTime);
		/**
		 * 求表中有多少天关于这个月的数据
		 */
		int monthHaveDay1 = new TableWhetherExist().getRecordNumber(
				" t_twitter_day_final ", " and monthNumber = " + (month-1));
		int monthHaveDay2 = new TableWhetherExist().getRecordNumber(
				" t_twitter_day_final ", " and monthNumber = " + (month-2));
		
		// 查看今天是几号
		int monthDay = TimeUtil.getDayOfMonth(dateTime);
		if (monthDay == 1) {
			String sql = "select a.columnId,a.parentId,a.title,a.level, a.dateTime, a.language,a.monthNumber,"
					+ "cast(ifnull(a.contentCount,0) as decimal(18,0)) as contentCount, "
					+ "cast(ifnull((a.contentCount-b.contentCount ) /b.contentCount*100,0) as decimal(18,2)) as contentCountInterval"
					+ ",cast(ifnull(a.viewCount,0) as decimal(18,0)) as viewCount, "
					+ "cast(ifnull((a.viewCount-b.viewCount ) /b.viewCount*100,0) as decimal(18,2)) as viewCountInterval"
					+ ",cast(ifnull(a.goodCount,0) as decimal(18,0)) as goodCount, "
					+ "cast(ifnull((a.goodCount-b.goodCount ) /b.goodCount*100,0) as decimal(18,2)) as goodCountInterval"
					+ ",cast(ifnull(a.badCount,0) as decimal(18,0)) as badCount, "
					+ "cast(ifnull((a.badCount-b.badCount) /b.badCount*100,0) as decimal(18,2)) as badCountInterval"
					+ ",cast(ifnull(a.shareCount,0) as decimal(18,0)) as shareCount, "
					+ "cast(ifnull((a.shareCount-b.shareCount) /b.shareCount*100,0) as decimal(18,2)) as shareCountInterval"
					+ ",cast(ifnull(a.commentCount,0) as decimal(18,0)) as commentCount, "
					+ "cast(ifnull((a.commentCount-b.commentCount) /b.commentCount*100,0) as decimal(18,2)) as commentCountInterval"
					+ " from  (select columnId,parentId, title,level, dateTime, language ,monthNumber,"
					+ "sum(contentCount)/"+monthHaveDay1+" as contentCount"
					+ ",sum(viewCount)/"+monthHaveDay1+" as viewCount"
					+ ",sum(goodCount)/"+monthHaveDay1+" as goodCount"
					+ ",sum(badCount)/"+monthHaveDay1+" as badCount"
					+ ",sum(shareCount)/"+monthHaveDay1+" as shareCount"
					+ ",sum(commentCount)/"+monthHaveDay1+" as commentCount"
					+ " from t_twitter_day_final where monthNumber="+(month-1)+" group by title) as  b,  "
					+ "(select  columnId,parentId,title,level , dateTime, language,monthNumber ,  "
					+ "sum(contentCount)/"+monthHaveDay2+" as contentCount"
					+ ",sum(viewCount)/"+monthHaveDay2+" as viewCount"
					+ ",sum(goodCount)/"+monthHaveDay2+" as goodCount"
					+ ",sum(badCount)/"+monthHaveDay2+" as badCount"
					+ ",sum(shareCount)/"+monthHaveDay2+" as shareCount"
					+ ",sum(commentCount)/"+monthHaveDay2+" as commentCount"
					+ " from t_twitter_day_final where monthNumber="+(month-2)+" group by title) as a  "
					+ "where b.title=a.title ";
			DBLinkPoll.setFileName(propertiesName);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			System.out.println(sql);
			resultList = DBLinkPollForAdd.getQueryRunner().query(sql,
					new MapListHandler());
			if (CollectionUtils.isNotEmpty(resultList)) {
				Map<String, Object> myMap = new HashMap<String, Object>();
				for (int i = 0; i < resultList.size(); i++) {
					myMap = resultList.get(i);
					myMap.put("language", "CN");
				}
			}
			System.out.println(resultList.size());
			odb.addToProductBase(resultList, "t_twitter_month_final");
		}else {
			System.out.println("今天不是一号，不用生成月榜");
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		try {
			new TwitterGetFinalData().twitterToFinalMonth("2016-03-01");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
