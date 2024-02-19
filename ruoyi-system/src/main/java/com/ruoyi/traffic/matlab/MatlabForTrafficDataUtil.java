package com.ruoyi.traffic.matlab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import trafficData2.TrafficData2;


/**
 * 从matlab获取交通相关数据的工具类
 */
@Component
public class MatlabForTrafficDataUtil {

    @Autowired
    private RoadRunnerProperties properties;
    /**
     * 获取交通数据并转为Json数组
     * @return
     * @throws Exception
     */
    public Object[] dataFromMatlab() throws Exception {
        TrafficData2 process = new TrafficData2();
        // roadrunner的project路径
        String rrProjectPath = properties.getRrProjectPath();
        // roadrunner中动态场景文件名
        String rrScenarioPath = properties.getRrScenarioPath();
        // roadrunner软件启动路径
        String workPath = properties.getWorkPath();
        //运行matlab中获取交通数据的函数
        Object[] objects = process.trafficData2(2, rrProjectPath, rrScenarioPath, workPath);
        return objects;
    }

}
