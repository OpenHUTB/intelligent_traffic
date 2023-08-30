package com.ruoyi.simulation.util;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandUtil {
    private Logger logger = LoggerFactory.getLogger(CommandUtil.class);

    /**
     * 获取process信息
     * @return
     */
    protected abstract Process getProcess();
    /**
     * 处理执行结果
     */
    protected abstract void processResult(InputStream ins) throws Exception;

    /**
     * 执行cmd命令
     * @param arguments
     * @return
     */
    public boolean executionCommand(String[] arguments){
        boolean flag = false;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            process = this.getProcess();
            //获取执行结果
            InputStream ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
            }else{
                ins = process.getInputStream();
                this.processResult(ins);
                flag = true;
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        } finally {
            try {
                if(bufferedReader!=null) {
                    bufferedReader.close();
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
        return flag;
    }
}
