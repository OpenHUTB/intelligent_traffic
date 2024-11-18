package com.ruoyi.simulation.call;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.CommandLineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;

@Component
public class CallUE4Engine {
    private final Logger logger = LoggerFactory.getLogger(CallWizardCoder.class);
    @Resource
    private Environment environment;
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
    /**
     * 获取交通数据
     * @param params
     * @return
     */
    public void getTrafficIndirection(String params) {//get_buildings.py
        CommandLineUtil<Void> commandLine = new CommandLineUtil<Void>() {
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.indirection.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptDirectory = environment.getProperty("simulation.indirection.scriptDirectory");
                String scriptLocation = scriptDirectory + params;
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                //>C:/ProgramData/Python37/python.exe D:/project/gpt/webui/indirectionscript/
                logger.info("cmd /k "+interpreterLocation+" "+scriptLocation);
                return runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation);
            }

            @Override
            protected Void processResult(InputStream ins) throws Exception {
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
