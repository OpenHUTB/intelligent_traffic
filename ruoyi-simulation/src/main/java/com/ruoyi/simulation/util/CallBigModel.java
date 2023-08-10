package com.ruoyi.simulation.util;

import com.google.common.io.ByteStreams;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 大模型调用工具类
 */
@Component
public class CallBigModel {
    @Resource
    private Environment environment;

    /**
     * 调用大模型执行文本命令
     * @param command 文本命令
     * @return 像素流
     */
    public byte[] executeTrainingByProcess(String command){
        byte[] byteArray = null;
        Process process = null;
        InputStream ins = null;
        try {
            //获取python解释器在服务器中的绝对路径
            String interpreterLocation = environment.getProperty("bigModel.interpreterLocation");
            String scriptLocation = environment.getProperty("bigModel.scriptLocation");
            //获取python代码文件在服务器中的绝对路径
            String[] arguments = new String[]{interpreterLocation, scriptLocation, command};
            //执行服务器中的python脚本
            process = Runtime.getRuntime().exec(arguments);
            //获取执行结果
            ins = process.getInputStream();
            byteArray = ByteStreams.toByteArray(ins);
            process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return byteArray;
    }
    /**
     * 调用大模型执行文本命令
     * @param command 文本命令
     * @return 像素流
     */
    public AjaxResult executeTrainingByProcessBuilder(String command){
        byte[] byteArray = null;
        ProcessBuilder builder = null;
        InputStream ins = null;
        try {
            //获取python解释器在服务器中的绝对路径
            String interpreterLocation = environment.getProperty("bigModel.interpreterLocation");
            String scriptLocation = environment.getProperty("bigModel.scriptLocation");
            //获取python代码文件在服务器中的绝对路径
            String[] arguments = new String[]{interpreterLocation, scriptLocation, command};
            //执行服务器中的python脚本
            builder = new ProcessBuilder(arguments);
            //当执行脚本出现错误时，合并错误输出流到标准输出流
            //builder.redirectErrorStream(true);
            Process process = builder.start();
            //获取执行结果
            ins = process.getErrorStream();
            if(ins!=null&&ins.available()>0){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"utf-8"));
                StringBuffer error = new StringBuffer();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    error.append(str);
                }
                return AjaxResult.error(error.toString());
            }else{
                ins = process.getInputStream();
                byteArray = ByteStreams.toByteArray(ins);
                process.waitFor();
                return AjaxResult.success(byteArray);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
