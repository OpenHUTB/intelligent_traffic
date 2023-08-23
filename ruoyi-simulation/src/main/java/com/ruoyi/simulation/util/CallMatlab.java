package com.ruoyi.simulation.util;

import com.google.common.io.ByteStreams;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * 大模型matlab脚本调用工具类
 */
@Component
public class CallMatlab {
    private Logger logger = LoggerFactory.getLogger(CallPython.class);
    @Resource
    private Environment environment;
    /**
     * 生成三维场景像素流
     * @param codeList 代码
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized AjaxResult generatePixelStream(List<String> codeList) {
        AjaxResult result = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        Process process = null;
        String errorMsg = null;
        byte[] byteArray = null;
        try{
            //将脚本代码存储到matlab项目下的code.m文件中，等待被matlab调用
            String targetPath = environment.getProperty("simulation.matlab.scriptLocation")+ File.separator+"code.m";
            File file = new File(targetPath);
            FileOutputStream ous = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(ous,"utf-8"));
            for(String line: codeList){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            //调用matlab命令执行code.m文件
            String interpreterLocation = environment.getProperty("simulation.matlab.interpreterLocation");
            String scriptLocation = environment.getProperty("simulation.matlab.scriptLocation");//
            String autoVrtlEnvLocation = environment.getProperty("simulation.matlab.autoVrtlEnvLocation");//autoVrtlEnvLocation
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("cmd /k cd "+interpreterLocation);
            //C:Buffer/gpt/matlab/bin>matlab -nojvm -nodesktop -nodisplay -r "cd C:/Buffer/gpt/gpt-main/sim; main('C:/Buffer/gpt/WindowsNoEditor/AutoVrtlEnv.exe')"
            process = runtime.exec("cmd /k matlab -nojvm -nodesktop -nodisplay -r \"cd "+scriptLocation+"; main('"+autoVrtlEnvLocation+"')\"");
            //获取执行结果
            InputStream ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"utf-8"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
                errorMsg = "生成三维场景像素流失败!";
            }else{
                ins = process.getInputStream();
                byteArray = ByteStreams.toByteArray(ins);
            }
        } catch (Exception e){
            logger.error(LoggerUtil.getLoggerStace(e));
            result = AjaxResult.error("生成三维场景像素流失败!");
        } finally {
            try {
                if(bufferedReader!=null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                if(bufferedWriter!=null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                int flag = process.waitFor();
                logger.info("执行结果是否为0："+flag);
                if(flag==0){
                    result = AjaxResult.success("执行成功",byteArray);
                }else{
                    result = AjaxResult.error(errorMsg);
                }
            } catch (InterruptedException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
        }
        return result;
    }
}
