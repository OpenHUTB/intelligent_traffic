package com.ruoyi.simulation.util;

import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 大模型python脚本调用工具类
 */
@Component
public class CallPython {
    private Logger logger = LoggerFactory.getLogger(CallPython.class);
    @Resource
    private Environment environment;

    /**
     * 调用PaddleSpeech将声音转为文字
     * @param location 语音文件地址
     * @return
     */
    public AjaxResult generateText(String location) {
        AjaxResult result = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        String errorMsg = null;
        String text = null;
        try {
            //获取python解释器在服务器中的绝对路径
            String interpreterLocation = environment.getProperty("simulation.python.interpreterLocation.paddleSpeech");
            String scriptLocation = environment.getProperty("simulation.python.scriptLocation.paddleSpeech.generateText");
            String[] arguments = new String[]{interpreterLocation,scriptLocation, location};
            ProcessBuilder builder = new ProcessBuilder(arguments);
            process = builder.start();
            //获取执行结果
            InputStream ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
                errorMsg = "调用PaddleSpeech将声音转为文字执行失败!";
            }else{
                ins = process.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                List<String> outputList = new ArrayList<String>();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.info(str);
                    outputList.add(str);
                }
                text = outputList.get(outputList.size()-1);
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
            return AjaxResult.error("调用PaddleSpeech将声音转为文字执行失败!");
        } finally {
            try {
                if(bufferedReader!=null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                int flag = process.waitFor();
                logger.info("执行结果是否为0："+flag);
                if(flag==0){
                    result = AjaxResult.success("解析成功",text);
                }else{
                    result = AjaxResult.error(errorMsg);
                }
            } catch (InterruptedException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
        }
        return result;
    }
    /**
     * 调用PaddleSpeech将文字转为声音
     * @param text 文本
     * @return 像素流
     */
    public synchronized AjaxResult generateVoice(String text) {
        AjaxResult result = null;
        BufferedReader  bufferedReader  = null;
        Process process = null;
        String errorMsg = null;
        String soundName = null;
        try{
            //指定语音文件的生成位置
            String targetPath = environment.getProperty("simulation.filepath")+ File.separator+System.currentTimeMillis()+".wav";
            File targetFile = new File(targetPath);
            //判断父目录是否存在，若不存在则创建父目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            //获取python解释器在服务器中的绝对路径
            String interpreterLocation = environment.getProperty("simulation.python.interpreterLocation.paddleSpeech");
            String scriptLocation = environment.getProperty("simulation.python.scriptLocation.paddleSpeech.generateVoice");
            String[] arguments = new String[]{interpreterLocation,scriptLocation, text, targetPath};
            ProcessBuilder builder = new ProcessBuilder(arguments);
            process = builder.start();
            //获取执行结果
            InputStream ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
                errorMsg = "调用PaddleSpeech将文字转为声音执行失败!";
            }else{
                //读取已经生成好的语音文件
                soundName = targetFile.getName();
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
            return AjaxResult.error("调用PaddleSpeech将文字转为声音执行失败!");
        } finally {
            try {
                if(bufferedReader!=null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                if(process!=null){
                    int flag = process.waitFor();
                    logger.info("执行结果是否为0："+flag);
                    if(flag==0){
                        result = AjaxResult.success("执行成功",soundName);
                    }else{
                        result = AjaxResult.error(errorMsg);
                    }
                }
            } catch (InterruptedException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
        }
        return result;
    }
    /**
     * 调用大模型生成执行脚本代码
     * @param command 文本命令
     * @return 像素流
     */
    public AjaxResult generateCode(String command)  {
        AjaxResult result = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        String errorMsg = null;
        List<String> codeList = null;
        try {
            //获取python解释器在服务器中的绝对路径
            String interpreterLocation = environment.getProperty("simulation.python.interpreterLocation.generateCode");
            String scriptLocation = environment.getProperty("simulation.python.scriptLocation.generateCode.clientScript");
            //获取python代码文件在服务器中的绝对路径
            String[] arguments = new String[]{interpreterLocation, scriptLocation, command};
            //执行服务器中的python脚本
            Runtime runtime = Runtime.getRuntime();
            //>C:/Buffer/gpt/gpt-main/webui/python/python.exe C:/Buffer/gpt/gpt-main/webui/client.py  --prompt 创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1
            process = runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation+" --prompt "+command);
            //获取执行结果
            InputStream ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"utf-8"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.error(str);
                }
                errorMsg = "调用大模型生成执行脚本代码失败!";
            }else{
                ins = process.getInputStream();
                logger.info("代码生成数据流大小："+ins.available());
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"utf-8"));
                codeList = new ArrayList<>();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    codeList.add(str);
                }
            }
        }catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
            result = AjaxResult.error("调用大模型生成执行脚本代码失败!");
        } finally {
            try {
                if(bufferedReader!=null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                if(process!=null){
                    int flag = process.waitFor();
                    logger.info("执行结果是否为0："+flag);
                    if(flag==0){
                        result = AjaxResult.success(codeList);
                    }else{
                        result = AjaxResult.error(errorMsg);
                    }
                }
            } catch (InterruptedException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
        }
        return result;
    }
}
