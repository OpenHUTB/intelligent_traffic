package com.ruoyi.simulation.call;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.CommandLineUtil;
import com.ruoyi.simulation.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CallUE4Engine {
    private final Logger logger = LoggerFactory.getLogger(CallWizardCoder.class);
    @Resource
    private Environment environment;
    /**
     * 执行指令
     * @param params
     * @return
     */
    public void executeExample(String params) {//get_buildings.py
        CommandLineUtil<Process> commandLine = new CommandLineUtil<Process>() {
            private Process process;
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
                String scriptLocation = scriptDirectory + params;
                //执行服务器中的python脚本
                ProcessBuilder builder = new ProcessBuilder("cmd.exe","/k", interpreterLocation, scriptLocation);
                return builder.start();
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
