package com.ruoyi.simulation.call;

import com.google.common.io.ByteStreams;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.CommandUtil;
import com.ruoyi.simulation.util.FileUtil;
import com.ruoyi.simulation.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 大模型matlab脚本调用工具类
 */
@Component
public class CallBigModel {
    private Logger logger = LoggerFactory.getLogger(CallPaddleSpeech.class);
    @Resource
    private Environment environment;
    @Resource
    private FileUtil fileUtil;
    /**
     * 调用大模型生成执行脚本代码
     * @param command 文本命令
     * @return 像素流
     */
    public List<String> generateCode(String command)  {
        CommandUtil<List<String>> commandUtil = new CommandUtil<List<String>>() {
            private Process process;
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.generateCode.interpreterLocation");
                String scriptLocation = environment.getProperty("simulation.generateCode.scriptLocation");
                //获取python代码文件在服务器中的绝对路径
                String[] arguments = new String[]{interpreterLocation, scriptLocation, command};
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                //>C:/Buffer/gpt/gpt-main/webui/python/python.exe C:/Buffer/gpt/gpt-main/webui/client.py  --prompt 创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1
                process = runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation+" --prompt "+command);
                return process;
            }
            @Override
            protected List<String> processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                List<String> codeList = new ArrayList<>();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    if(StringUtils.equals(str, "gpt_end")){
                        process.destroyForcibly();
                        break;
                    }
                    logger.info(str);
                    codeList.add(str);
                }
                return codeList;
            }
        };
        List<String> codeList = commandUtil.executionCommand();
        if(codeList==null||codeList.isEmpty()){
            throw new RuntimeException("调用大模型生成Matlab代码失败!");
        }
        return codeList;
    }
    /**
     * 生成三维场景像素流
     * @param codeList 代码
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized byte[] generatePixelStream(List<String> codeList) {
        CommandUtil<byte[]> commandUtil = new CommandUtil<byte[]>() {
            @Override
            protected Process getProcess() throws Exception {
                //将脚本代码存储到matlab项目下的code.m文件中，等待被matlab调用
                String targetPath = environment.getProperty("simulation.matlab.scriptLocation")+ File.separator+"code.m";
                fileUtil.writeText(targetPath,codeList);
                //调用matlab命令执行code.m文件
                String interpreterLocation = environment.getProperty("simulation.matlab.interpreterLocation");
                String scriptLocation = environment.getProperty("simulation.matlab.scriptLocation");//
                String autoVrtlEnvLocation = environment.getProperty("simulation.matlab.autoVrtlEnvLocation");//autoVrtlEnvLocation
                Runtime runtime = Runtime.getRuntime();
                //C:Buffer/gpt/matlab/bin>matlab -nojvm -nodesktop -nodisplay -r "cd C:/Buffer/gpt/gpt-main/sim; main('C:/Buffer/gpt/WindowsNoEditor/AutoVrtlEnv.exe')"
                Process process = runtime.exec("cmd /k "+interpreterLocation+" -nojvm -nodesktop -nodisplay -r \"cd "+scriptLocation+"; main('"+autoVrtlEnvLocation+"')\"");
                return process;
            }

            @Override
            protected byte[] processResult(InputStream ins) throws Exception {
                byte[] byteArray = ByteStreams.toByteArray(ins);
                return byteArray;
            }
        };
        byte[] byteArray = commandUtil.executionCommand();
        if(codeList==null||codeList.isEmpty()){
            throw new RuntimeException("根据matlab代码生成三维场景像素流失败!");
        }
        return byteArray;
    }
}
