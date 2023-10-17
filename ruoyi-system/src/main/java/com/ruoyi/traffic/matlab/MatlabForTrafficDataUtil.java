package com.ruoyi.traffic.matlab;

import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import com.ruoyi.traffic.enums.EvaluationTypeEnum;
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
    // TODO 先暂时将各路径在这以用来测试数据的连通
    public JSONArray dataFromMatlab() throws Exception {
        TrafficData2 process = new TrafficData2();
        // roadrunner的project路径
        String rrProjectPath = properties.getRrProjectPath();
        // roadrunner中动态场景文件名
        String rrScenarioPath = properties.getRrScenarioPath();
        // roadrunner软件启动路径
        String workPath = properties.getWorkPath();
        //运行matlab中获取交通数据的函数
        Object[] objects = process.trafficData2(2, rrProjectPath, rrScenarioPath, workPath);
        // 先将matlab传来的数据转换成string类型
        String s = objects[0].toString();

        // 转换成Json数组
        JSONArray jsonArray = new JSONArray(s);
        return jsonArray;
    }

    public static void main(String[] args) throws Exception {
        TrafficData2 process = new TrafficData2();
        // roadrunner的project路径
        String rrProjectPath = "E:/trafficProject/Roadrunner";
        // roadrunner中动态场景文件名
        String rrScenarioPath = "E:/trafficProject/Roadrunner/Scenarios/桐梓坡-谷丰-岳麓大道-望岳路.rrscenario";
        // roadrunner软件启动路径
        String workPath = "D:/software/Roadrunner/RoadRunner_2022b/bin/win64";
        //运行matlab中获取交通数据的函数
        Object[] objects = process.trafficData2(2, rrProjectPath, rrScenarioPath, workPath);
        // 先将matlab传来的数据转换成string类型
        String s = objects[0].toString();

        // 转换成Json数组
        JSONArray jsonArray = new JSONArray(s);
        System.out.println(jsonArray);
        String typeId = jsonArray.getJSONObject(0).getString("id");
        System.out.println(EvaluationTypeEnum.getEnumByType(typeId).getDesc());
//        for (int i = 0; i < jsonArray.length(); i++) {
//            // 路口名称
//            String name = jsonArray.getJSONObject(i).getString("name");
//
//            // 路口指标
//            String evaluation = jsonArray.getJSONObject(i).getString("datatype");
//
//            // 数据
//            Object value = jsonArray.getJSONObject(i).get("volume");
//
//            // 路口相对坐标x, y
//            String x = jsonArray.getJSONObject(i).getJSONObject("Coordinate").getString("lat");
//            String y = jsonArray.getJSONObject(i).getJSONObject("Coordinate").getString("lon");
//
//            // 指标类型
//            String type = jsonArray.getJSONObject(i).getString("type");
//
//            System.out.println("name: " + name + " evaluation: " + evaluation + " value: " + value + " type: " + type
//            );
    }

}
