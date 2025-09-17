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
    private final Logger logger = LoggerFactory.getLogger(CallUE4Engine.class);
    @Resource
    private Environment environment;

    /**
     * 执行examples文件
     * @param params
     * @return
     */
    public void executeExamples(String params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
                executeSynchronized(interpreterLocation, scriptDirectory, params);
            }
        }).start();
    }

    /**
     * 获取交通指数
     * @param params
     */
    public void executeIndirection(String params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.indirection.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptDirectory = environment.getProperty("simulation.indirection.scriptDirectory");
                executeSynchronized(interpreterLocation, scriptDirectory, params);
            }
        }).start();
    }

    /**
     * 获取carla中的数据
     * @param params
     * @return
     */
    public String getInformation(String params){
        //获取python解释器在服务器中的绝对路径
        String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
        //获取python代码文件在服务器中的绝对路径
        String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
        return executeSynchronized(interpreterLocation, scriptDirectory, params);
    }
    /**
     * 通过Java调用cmd命令同步执行python脚本
     * @param interpreterLocation
     * @param scriptDirectory
     * @param params
     * @return
     */
    private String executeSynchronized(String interpreterLocation, String scriptDirectory, String params){
        CommandLineUtil<String> commandLine = new CommandLineUtil<String>() {
            private Process process;
            @Override
            protected Process getProcess() throws Exception {
                String scriptLocation = scriptDirectory + params;
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                logger.info("cmd /k "+interpreterLocation+" "+scriptLocation);
                return process = runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation);
            }

            @Override
            protected String processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    if(StringUtils.equals(str, "gpt_end")){
                        process.destroyForcibly();
                        //检测到"gpt_end"则说明场景名称获取成功，否则说明发生了异常
                        break;
                    }
                    logger.info(str);
                }
                return str;
            }
        };
        return commandLine.executionCommand();
    }
}
