package com.ruoyi.simulation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AOP工具类
 * @author alanYang
 *
 */
public class LoggerUtil {
	private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);
	private static final ConcurrentHashMap<String,Long> exceptionMap = new ConcurrentHashMap<String,Long>();
	/**
	 * 获取异常栈信息
	 * @param e
	 * @return
	 */
	public static void printLoggerStace(Throwable e) {
		String error = null;
		StackTraceElement element = e.getStackTrace()[0];
		String key = element.getClassName()+":"+element.getLineNumber();
		Long millis = exceptionMap.get(key);
		long currentMillis = System.currentTimeMillis();
		if(millis==null||currentMillis-millis>=60*1000){
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter, true);
			e.printStackTrace(printWriter);
			error = stringWriter.getBuffer().toString();
			logger.error(error);
			exceptionMap.put(key,currentMillis);
		}
	}
}
