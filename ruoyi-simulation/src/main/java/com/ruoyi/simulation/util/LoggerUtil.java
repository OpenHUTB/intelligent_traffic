package com.ruoyi.simulation.util;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * AOP工具类
 * @author alanYang
 *
 */
@Aspect
@Configuration
public class LoggerUtil {
	private Logger logger = LoggerFactory.getLogger(LoggerUtil.class);
	/**
	 * 获取异常栈信息
	 * @param e
	 * @return
	 */
	public static String getLoggerStace(Throwable e) {
		StringWriter stringWriter = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(stringWriter, true);
	    e.printStackTrace(printWriter);
	    return stringWriter.getBuffer().toString();
	}
}
