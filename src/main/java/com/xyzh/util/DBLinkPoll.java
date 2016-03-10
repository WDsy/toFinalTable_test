package com.xyzh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
 /**
 * @author 作者：       周卫东
 * @E-mail 邮箱：       zhouweidong@gochinatv.com
 * @version 创建时间：2015年12月21日 下午12:15:16
 * 类说明
 */
public class DBLinkPoll {
	 static String fileName;
	 static DataSource ds;
	static String username;
	 static String password;
	 static String url;
	 static String driver;
	static Logger logger = Logger.getLogger(DBLinkPoll.class);
	private DBLinkPoll(){
		 
	}
	
	public static void setFileName(String fileName) {
		DBLinkPoll.fileName = fileName;
	}

	public static void parse(){
		if(StringUtils.isNotEmpty(fileName)){
			Properties p = new Properties();
			try {
				File file = new File(fileName);
				InputStream in = new FileInputStream(file);
				p.load(in);
			//	p.load(DBHelper.class.getClassLoader().getResourceAsStream(fileName));
				username = p.getProperty("username");
				password = p.getProperty("password");
				url = p.getProperty("url");
				driver = p.getProperty("driver");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				logger.debug("未指定数据库配置");
				throw new Exception("未指定数据库配置");
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			}
		}
	}
	
	public static QueryRunner getQueryRunner(){
		if(null == ds){
			//解析
			parse();
			//配置dbcp数据�?
			BasicDataSource dbcpDataSource = new BasicDataSource();
			
			dbcpDataSource.setUsername(username);
			dbcpDataSource.setPassword(password);
			dbcpDataSource.setUrl(url);
			dbcpDataSource.setDriverClassName(driver);
//			dbcpDataSource.setUsername("root");
//			dbcpDataSource.setPassword("root");
//			dbcpDataSource.setUrl("jdbc:mysql://localhost:3306/bigdataplatform?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
//			dbcpDataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dbcpDataSource.setDefaultAutoCommit(true);
			//连接池最大数据库连接�?
			dbcpDataSource.setMaxActive(100);
			//�?��空闲数，数据库连接的�?��空闲时间。超过空闲时间，数据库连接将被标记为不可用，然后被释放�?设为0表示无限�?
			dbcpDataSource.setMaxIdle(30);
			//�?��建立连接等待时间。如果超过此时间将接到异常�?设为-1表示无限�?
			dbcpDataSource.setMaxWait(500);
			
			DBLinkPoll.ds = dbcpDataSource;
			System.out.println("Initialize dbcp...");
		}
		return new QueryRunner(ds);
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws SQLException {
		QueryRunner qr = DBLinkPoll.getQueryRunner();
		Object value = qr.query("select pro_name,pro_price from zwd_test", new ScalarHandler());
		System.out.println(value);
	}

}
