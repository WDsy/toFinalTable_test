package com.xyzh.testLog;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author 作者：       周卫东
 * @E-mail 邮箱：       zhouweidong@gochinatv.com
 * @version 创建时间：2016年2月17日 下午3:13:15
 * 类说明
*/
public class TestLog4j {
	 public   static   void  main(String[] args)  {
		 Logger logger  =  Logger.getLogger(TestLog4j. class );
//	        PropertyConfigurator.configure( "log4j.properties " );
	        logger.info("这是输出的信息内容");
	        logger.debug( " debug " );
	        logger.error( " error " );
	    } 
}
