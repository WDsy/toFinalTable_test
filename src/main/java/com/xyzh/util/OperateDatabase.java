package com.xyzh.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author jiyangnan
 * @since 2015-12-16 14:46:29
 * @desribe 从model表生成数据到final�?
 *
 */

public class OperateDatabase {
	Connection connForReptile = null;
	Connection connForProduct = null;
	String id = null;
	String name = null;
	String fileName = "Database.properties";
	static Logger logger = Logger.getLogger(DBLinkPoll.class);
	/**
	 * @author jiyangnan
	 * @since 2015-12-16 19:06:09
	 * @describe 从爬虫库获取数据
	 * @param String
	 *            tableName,String sqlStr 若需要sqlStr，则拼写，eg.
	 *            "uid = xxx and uname = xxx"
	 */
	public List<Map<String, Object>> getDataFromReptile(String sqlStr) {
		PropertyConfigurator.configure("log4j.properties");
		Logger logger = Logger.getLogger(OperateDatabase.class);
		String method = "getDataFromReptile";
		DatabaseUtil dbu = new DatabaseUtil(fileName, method);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			System.out.println(sqlStr);
			logger.info(sqlStr);
			connForReptile = dbu.getConnForReptile();
			Statement s = connForReptile.createStatement();
			ResultSet rs = s.executeQuery(sqlStr);
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(md.getColumnName(i), rs.getObject(i));
				}
				resultList.add(map);
			}
			rs.close();
			s.close();
			connForReptile.close();
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * @author jiyangnan
	 * @since 2015-12-16 19:07:15
	 * @describe 从中心库获取数据
	 * @param String
	 *            tableName,String sqlStr 若需要sqlStr，则拼写，eg.
	 */
	public List<Map<String, Object>> getDataFromProduct(String tableName, String sqlStr) {
		PropertyConfigurator.configure("log4j.properties");
		Logger logger = Logger.getLogger(OperateDatabase.class);
		String method = "getConnForProduct";
		DatabaseUtil dbu = new DatabaseUtil(fileName, method);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String sql = null;
		try {
			System.out.println(sql);
			logger.info(sql);
			connForProduct = dbu.getConnForProduct();
			PreparedStatement pstm = connForProduct.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(md.getColumnName(i), rs.getObject(i));
				}
				resultList.add(map);
			}
			rs.close();
			pstm.close();
			connForProduct.close();
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * @author jiyangnan
	 * @since 2015-12-16 21:25:27
	 * @describe 向中心库获插入数据
	 * @param
	 */
	public int addToProductBase(List<Map<String, Object>> resultList, String dataTableName) {
		Logger logger = Logger.getLogger(OperateDatabase.class);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		logger.info("执行日期" + sdf.format(date));
		logger.info("执行日期" + sdf.format(date));
		int count = resultList.size();
		if (CollectionUtils.isNotEmpty(resultList)) {
			try {
				Map<String, Object> map = resultList.get(0);
				StringBuffer keyStr = new StringBuffer();
				StringBuffer valueStr = new StringBuffer();
				List<String> list = new ArrayList<String>();
				Thread.sleep(2000);
				System.out.println("map大小为：" + map.size());
				logger.debug("map大小为" + map.size());
				System.out.println("ok!!");
				for (Entry<String, Object> entry : map.entrySet()) {
					keyStr.append("`");
					keyStr.append(entry.getKey());
					keyStr.append("`,");
					valueStr.append("?,");
					list.add(entry.getKey());
				}
				if (keyStr.length() > 0 || valueStr.length() > 0) {
					keyStr.deleteCharAt(keyStr.length() - 1);
					valueStr.deleteCharAt(valueStr.length() - 1);
				}
				String sql = "INSERT ignore INTO " + dataTableName + "(" + keyStr.toString() + ") VALUES("
						+ valueStr.toString() + ")";

				Object[][] param = new Object[count][map.size()];
		  		for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> bean = resultList.get(i);
					for (int j = 0; j < list.size(); j++) {
						param[i][j] = bean.get(list.get(j));
						// if("platFormIds".equals(list.get(j))){
						// param[i][j] = "wo";
						// }
					}
				}
				logger.info(sql);
				System.out.println(sql);
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				DBLinkPollForAdd.getQueryRunner().batch(sql, param);
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
			} catch (InterruptedException e) {
				logger.error(e.toString());
				e.printStackTrace();
			}
		}
		return count;
	}
}