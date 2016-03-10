package com.xyzh.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.xyzh.facebook.FacebookGetFinalData;
import com.xyzh.ggplus.GgplusGetFinalData;
import com.xyzh.twitter.TwitterGetFinalData;
import com.xyzh.youtube.YtbGetFinalData;

/**
 * @author 作者： 周卫东
 * @E-mail 邮箱： zhouweidong@gochinatv.com
 * @version 创建时间：2015年12月17日 上午11:24:03 类说明
 */
public class testMain {
	static TwitterGetFinalData twitter = new TwitterGetFinalData();
	static GgplusGetFinalData ggplus = new GgplusGetFinalData();
	static FacebookGetFinalData facebook = new FacebookGetFinalData();
	static YtbGetFinalData ytb = new YtbGetFinalData();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	static Logger logger = Logger.getLogger(testMain.class);

	public static void main() {
		try {

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			 String dateTime = sdf.format(date);
			String dateTime = "2016-03-04";
			while(sdf.parse(dateTime).before(new Date())){
				Calendar calendar = Calendar.getInstance();
				Date date_time = sdf.parse(dateTime);
				calendar.setTime(date_time);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			logger.info("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");

			// 总榜
			twitter.twitterToFinalTotal(dateTime);
			ggplus.ggplusToFinalTotal(dateTime);
			ytb.ytbToFinalTotal(dateTime);
			facebook.facebookToFinalTotal(dateTime);
			// 日榜
			ytb.ytbToFinalDay(dateTime);
			ggplus.ggplusToFinalDay(dateTime);
			twitter.twitterToFinalDay(dateTime);
			facebook.facebookToFinalDay(dateTime);
			// 周榜
			ytb.ytbToFinalWeek(dateTime);
			twitter.twitterToFinalWeek(dateTime);
			ggplus.ggplusToFinalWeek(dateTime);
			facebook.facebookToFinalWeek(dateTime);
			// 月榜
			ytb.ytbToFinalMonth(dateTime);
			twitter.twitterToFinalMonth(dateTime);
			ggplus.ggplusToFinalMonth(dateTime);
			facebook.facebookToFinalMonth(dateTime);

			 dateTime = sdf.format(calendar.getTime());
			 System.out.println(dateTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	public static void main(String[] args) {
		// while (true) {
		// try {
		// Thread.sleep(1000 * 3600 * 24);
		// } catch (InterruptedException e) {
		// logger.error("在:::" + sdf.format(new Date()) +
		// ";;;EntranceMain这个类出现了" + e.toString());
		// e.printStackTrace();
		// }
		main();
	}
	// }
}
