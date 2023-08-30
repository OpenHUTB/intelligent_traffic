package com.ruoyi.simulation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class CommandUtil<T> {
    private Logger logger = LoggerFactory.getLogger(CommandUtil.class);

    /**
     * 获取process信息
     * @return
     */
    protected abstract Process getProcess() throws Exception;
    /**
     * 处理执行结果
     */
    protected abstract T processResult(InputStream ins) throws Exception;

    /**
     * 执行cmd命令
     * @return
     */
    public T executionCommand(){
        T data = null;
        InputStream ins = null;
        Process process = null;
        try {
            process = this.getProcess();
            //判断是否有异常信息，若有异常信息，则打印异常信息
            ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
            }else{
                //获取执行结果
                ins = process.getInputStream();
                data = this.processResult(ins);
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        } finally {
            try {
                if(ins!=null) {
                    ins.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                logger.info("执行结果是否为0："+process.waitFor());
            } catch (InterruptedException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
        }
        return data;
    }
}
