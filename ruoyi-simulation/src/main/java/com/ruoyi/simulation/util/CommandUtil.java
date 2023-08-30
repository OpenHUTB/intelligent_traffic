package com.ruoyi.simulation.util;

<<<<<<< HEAD
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
=======
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

public abstract class CommandUtil {
=======

public abstract class CommandUtil<T> {
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
    private Logger logger = LoggerFactory.getLogger(CommandUtil.class);

    /**
     * 获取process信息
     * @return
     */
<<<<<<< HEAD
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
=======
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
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
        Process process = null;
        try {
            process = this.getProcess();
            //获取执行结果
<<<<<<< HEAD
            InputStream ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
=======
            ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
            }else{
                ins = process.getInputStream();
<<<<<<< HEAD
                this.processResult(ins);
                flag = true;
=======
                data = this.processResult(ins);
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        } finally {
            try {
<<<<<<< HEAD
                if(bufferedReader!=null) {
                    bufferedReader.close();
=======
                if(ins!=null) {
                    ins.close();
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
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
<<<<<<< HEAD
        return flag;
=======
        return data;
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
    }
}
