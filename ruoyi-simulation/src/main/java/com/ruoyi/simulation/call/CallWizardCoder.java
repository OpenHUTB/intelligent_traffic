package com.ruoyi.simulation.call;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.CommandLineUtil;
import com.ruoyi.simulation.util.CustomizedException;
import com.ruoyi.simulation.util.FileOperatorUtil;
import com.ruoyi.simulation.util.ProcessOperationUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大模型matlab脚本调用工具类
 */
@Component
public class CallWizardCoder {
    private final Logger logger = LoggerFactory.getLogger(CallWizardCoder.class);
    @Resource
    private Environment environment;
    @Resource
    private FileOperatorUtil fileUtil;
    /**
     * 调用大模型生成执行脚本代码
     * @param command 文本命令
     * @return 像素流
     */
    public List<String> generateCode(String command)  {
        CommandLineUtil<List<String>> commandLine = new CommandLineUtil<List<String>>() {
            private Process process;
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.wizardCoder.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptLocation = environment.getProperty("simulation.wizardCoder.scriptLocation");
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
                        //检测到"gpt_end"则说明代码生成成功，否则说明发生了异常
                        break;
                    }
                    logger.info(str);
                    codeList.add(str);
                }
                return codeList;
            }
        };
        List<String> codeList = commandLine.executionCommand();
        if(codeList==null||codeList.isEmpty()){
            throw new CustomizedException("调用大模型生成Matlab代码失败!");
        }
        return codeList;
    }
    public String getMapName(String params){
        CommandLineUtil<String> commandLine = new CommandLineUtil<String>() {
            private Process process;
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
                String scriptLocation = scriptDirectory + params;
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                logger.info("cmd /k "+interpreterLocation+" "+scriptLocation);
                return process = runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation);
            }

            @Override
            protected String processResult(InputStream ins) throws Exception {
                String mapName = null;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    if(StringUtils.equals(str, "gpt_end")){
                        process.destroyForcibly();
                        //检测到"gpt_end"则说明场景名称获取成功，否则说明发生了异常
                        break;
                    }
                    mapName = str;
                    logger.info(str);
                }
                return mapName;
            }
        };
        return commandLine.executionCommand();
    }
    /**
     * 执行指令
     * @param params
     * @return
     */
    public void executeExample(String params) {//get_buildings.py
        CommandLineUtil<Process> commandLine = new CommandLineUtil<Process>() {
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
                String scriptLocation = scriptDirectory + params;
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                //>C:/Buffer/gpt/gpt-main/webui/python/python.exe C:/Buffer/gpt/gpt-main/webui/client.py  --prompt 创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1
                logger.info("cmd /k "+interpreterLocation+" "+scriptLocation);
                return runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation);
            }

            @Override
            protected Process processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while(!StringUtils.isEmpty(str = bufferedReader.readLine())){
                    logger.info(str);
                }
                return null;
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                commandLine.executionCommand();
            }
        }).start();
    }
}
