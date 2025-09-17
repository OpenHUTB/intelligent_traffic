package com.ruoyi.simulation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public abstract class CommandLineUtil<T> {
    private final Logger logger = LoggerFactory.getLogger(CommandLineUtil.class);
    private Process process = null;
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
        try {
            process = this.getProcess();
            //获取执行结果
            ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
            }else{
                ins = process.getInputStream();
                data = this.processResult(ins);
            }
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        } finally {
            try {
                if(ins!=null) {
                    ins.close();
                }
            } catch (IOException e) {
                LoggerUtil.printLoggerStace(e);
            }
            try {
                process.waitFor();
                //logger.info("执行结果是否为0："+process.waitFor());
            } catch (InterruptedException e) {
                LoggerUtil.printLoggerStace(e);
            }
        }
        return data;
    }

    /**
     * 强制终止当前线程
     * @return
     */
    protected void destroyForcibly(){
        process.destroyForcibly();
    }
}
