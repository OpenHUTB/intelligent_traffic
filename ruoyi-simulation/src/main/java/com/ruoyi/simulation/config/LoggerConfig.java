package com.ruoyi.simulation.config;

import com.ruoyi.simulation.util.ResultUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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
public class LoggerConfig {
	private Logger logger = LoggerFactory.getLogger(LoggerConfig.class);
	/**
	 * 环绕增强
	 * @param point
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Around("execution(public * com.ruoyi.simulation.controller.*.*(..))")
	public Object aroundMethod(ProceedingJoinPoint point) throws InstantiationException, IllegalAccessException {
		Object result = null;
		try {
			result = point.proceed();
		} catch (Throwable e) {
			logger.error(getLoggerStace(e));
			ResultUtil<?> resultUtil = ResultUtil.class.newInstance();
			resultUtil.setStatus(ResultUtil.Status.FAILED);
			resultUtil.setMessage(e.getMessage());
			result = resultUtil;
		}
		return result;
	}
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
