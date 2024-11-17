package com.ruoyi.simulation.config;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 负责获取carla指标的监听器
 */
@Component
public class CarlaInformationListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(CarlaInformationListener.class);
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    private Environment environment;
    @Resource
    private WebSocketConfig webSocketConfig;
    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
    /**
     * 开启Carla日志记录功能
     */
    private void startLogger(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String loggerPath = environment.getProperty("simulation.loggerPath");
        loggerPath = loggerPath+"recording" + sdf.format(new Date())+".log";
        String codeStr = "start_recoder.py --loggerPath "+ loggerPath;
        this.callUE4Engine.executeExample(codeStr);
    }

    /**
     * 获取交通参数
     */
    public void getTrafficInfo(){
        String sumoPath = this.environment.getProperty("filepath.sumoPath");
        try {
            FileInputStream inputStream = new FileInputStream(sumoPath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while((str=bufferedReader.readLine())!=null){
                buffer.append(str);
            }
            JSONObject result = JSONObject.parseObject(buffer.toString());

        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
}
