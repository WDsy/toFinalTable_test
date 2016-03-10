package com.xyzh.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapListHandler;
 /**
 * @author 作者：       周卫东
 * @E-mail 邮箱：       zhouweidong@gochinatv.com
 * @version 创建时间：2015年12月25日 上午10:38:24
 * 类说明
 */
public class TableWhetherExist {
	public boolean whetherExist(String tableName,String databaseName) throws SQLException{
		boolean flag = false;
		String sql = "select table_name from `INFORMATION_SCHEMA`.`TABLES`"
				+ " where table_name ='"+tableName+"' and TABLE_SCHEMA='"+databaseName+"'";
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		System.out.println(sql);
		resultList = DBLinkPollForAdd.getQueryRunner().query(sql,
				new MapListHandler());
		System.out.println();
		if (resultList.size()>0) {
			flag = true;
		}
		return flag;
	}
	
	
	
	public int getRecordNumber(String tableName,String items) throws SQLException{
		String sql = "select count(*) from "+tableName+" where 1=1 "+items+" group by dateTime";
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		System.out.println(sql);
		resultList = DBLinkPollForAdd.getQueryRunner().query(sql,
				new MapListHandler());
		System.out.println(resultList.size());
		return resultList.size();
	}
	public static void main(String[] args) throws SQLException {
		int i = 12;
		System.out.println(new TableWhetherExist().getRecordNumber("t_facebook_day_final", "and monthNumber =  "+i));
	}
}
