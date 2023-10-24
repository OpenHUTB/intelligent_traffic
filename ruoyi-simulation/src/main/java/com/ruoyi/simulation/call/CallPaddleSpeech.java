package com.ruoyi.simulation.call;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.CommandUtil;
import com.ruoyi.simulation.util.Constant.Status;
import com.ruoyi.simulation.util.FileOperatorUtil;
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
public class CallPaddleSpeech {
    private Logger logger = LoggerFactory.getLogger(CallPaddleSpeech.class);
    @Resource
    private Environment environment;
    @Resource
    private FileOperatorUtil fileUtil;
    public AjaxResult awakenCheck(String location){
        CommandUtil<AjaxResult> commandUtil = new CommandUtil<AjaxResult>(){
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.paddleSpeech.interpreterLocation");
                String scriptLocation = environment.getProperty("simulation.paddleSpeech.scriptLocation.awaken");
                String[] arguments = new String[]{interpreterLocation,scriptLocation, location};
                ProcessBuilder builder = new ProcessBuilder(arguments);
                Process process = builder.start();
                return process;
            }

            @Override
            protected AjaxResult processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                List<String> outputList = new ArrayList<String>();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.info(str);
                    outputList.add(str);
                }
                String text = outputList.get(outputList.size()-1);
                String[] arr = text.split(",");
                if(StringUtils.equals(arr[0], Status.SUCCESS)){
                    return AjaxResult.success("唤醒成功!",arr[1]);
                }else{
                    return AjaxResult.error("唤醒失败!",arr[1]);
                }
            }
        };
        return commandUtil.executionCommand();
    }
    /**
     * 调用PaddleSpeech将声音转为文字
     * @param location 语音文件地址
     * @return
     */
    public String generateText(String location) {
        CommandUtil<String> commandUtil = new CommandUtil<String>() {
            @Override
            protected Process getProcess() throws IOException {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.paddleSpeech.interpreterLocation");
                String scriptLocation = environment.getProperty("simulation.paddleSpeech.scriptLocation.generateText");
                String[] arguments = new String[]{interpreterLocation,scriptLocation, location};
                ProcessBuilder builder = new ProcessBuilder(arguments);
                Process process = builder.start();
                return process;
            }
            @Override
            protected String processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                List<String> outputList = new ArrayList<String>();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    logger.info(str);
                    outputList.add(str);
                }
                String text = outputList.get(outputList.size()-1);
                return text;
            }
        };
        String text = commandUtil.executionCommand();
        if(StringUtils.isEmpty(text)){
            throw new RuntimeException("调用PaddleSpeech将语音转为文字执行失败!");
        }
        return text;
    }
    /**
     * 调用PaddleSpeech将文字转为声音
     * @param text 文本
     * @return 像素流
     */
    public synchronized String generateVoice(String text) {
        File targetFile = fileUtil.createDefaultFile(".wav");
        CommandUtil<String> commandUtil = new CommandUtil<String>() {
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.paddleSpeech.interpreterLocation");
                String scriptLocation = environment.getProperty("simulation.paddleSpeech.scriptLocation.generateVoice");
                String[] arguments = new String[]{interpreterLocation,scriptLocation, text, targetFile.getAbsolutePath()};
                ProcessBuilder builder = new ProcessBuilder(arguments);
                Process process = builder.start();
                return process;
            }
            @Override
            protected String processResult(InputStream ins) throws Exception {
                //合成语音时，由于没有输出，所以不用处理执行结果
                return targetFile.getName();
            }
        };
        String fileName = commandUtil.executionCommand();
        if(fileName==null){
            throw new RuntimeException("调用PaddleSpeech将文字转为语音执行失败!");
        }
        return fileName;
    }
}
