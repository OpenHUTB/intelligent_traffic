package com.ruoyi.traffic.matlab;

import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import simulateAndPlotRoadRunner.simulateAndPlotRoadRunner;

/**
 * 从matlab获取交通相关数据的工具类
 */
public class MatlabForTrafficDataUtil {
    /**
     * 获取交通数据并转为Json数组
     * @return
     * @throws Exception
     */
    // TODO 先暂时将各路径在这以用来测试数据的连通
    public JSONArray dataFromMatlab() throws Exception {
        simulateAndPlotRoadRunner process = new simulateAndPlotRoadRunner();
        // roadrunner的project路径
        String rrProjectPath = "E:/trafficProject/Roadrunner";
        // roadrunner中动态场景文件名
        String rrScenarioPath = "E:/trafficProject/Roadrunner/Scenarios/baigezui.rrscenario";
        // roadrunner软件启动路径
        String workPath = "D:/software/Roadrunner/RoadRunner_2022b/bin/win64";
        //运行matlab中获取交通数据的函数
        Object[] objects = process.simulateAndPlotRoadRunnerScenario(1, rrProjectPath, rrScenarioPath, workPath);
        // 先将matlab传来的数据转换成string类型
        String s = objects[0].toString();

        // 转换成Json数组
        JSONArray jsonArray = new JSONArray(s);
        return jsonArray;
    }

}
