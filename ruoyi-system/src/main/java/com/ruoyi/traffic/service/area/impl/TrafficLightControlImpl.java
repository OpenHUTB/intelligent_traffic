package com.ruoyi.traffic.service.area.impl;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @classname: TrafficLightControlImpl
 * @author: ouyanghua
 * @description: 路口红绿灯控制
 * @date: 2023/11/24
 * @version: v1.0
 **/

@Service
//依据一定时间段的路口车流数据，计算下一个时间段的最优绿灯相位顺序及持续时长
public class TrafficLightControlImpl {
    //绿灯最大、最小时长
    private static final int MAX_TIME = 60;
    private static final int MIN_TIME = 10;

    // 排头车启动损失时间
    private static final int STARTUP_LOST_TIME = 2;

    // 饱和车头时距
    private static final int SATURATION_HEADWAY = 3;
    private int current_state;
    private int phase;
    private Map<String, Integer> traffic_light;
    private int light_cycle_time;
    public TrafficLightControlImpl() {
        this.current_state = -1;
        this.phase = -1;
        this.traffic_light = new LinkedHashMap<>();

         // 路口信号灯总周期时长
        this.light_cycle_time = 0;
    }

    //根据当前各相位的车流量，计算最优的各相位绿灯持续时间及亮绿灯顺序
    public void getTrafficLightTime(List<Integer> state) {
        Map<String, Integer> trafficLightTime = new HashMap<>();

        //计算每个相位的期望绿灯时间
        trafficLightTime.put("NS_S", calculateLightTime(state.get(0), false));
        trafficLightTime.put("NS_L", calculateLightTime(state.get(1), true));
        trafficLightTime.put("EW_S", calculateLightTime(state.get(2), false));
        trafficLightTime.put("EW_L", calculateLightTime(state.get(3), true));

        //信号灯周期总时长
        this.light_cycle_time = trafficLightTime.values().stream().mapToInt(Integer::intValue).sum();

        //按照期望绿灯时间降序排列
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(trafficLightTime.entrySet());
        entries.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));
        this.traffic_light.clear();
        for (Map.Entry<String, Integer> entry : entries) {
            this.traffic_light.put(entry.getKey(), entry.getValue());
        }
    }

    //计算每个相位的绿灯时间
    private int calculateLightTime(int vehicleNum, boolean turn) {
        int laneNum = turn ? 1 : 3;

        //单车道车辆数
        int avgVehicleNum = vehicleNum / laneNum;

        //期望绿灯时间公式: 排头车启动损失时间 + 平均单车道车辆数 * 饱和车头时距
        int expectGreenLightTime = STARTUP_LOST_TIME + avgVehicleNum * SATURATION_HEADWAY;
        return Math.min(Math.max(expectGreenLightTime, MIN_TIME), MAX_TIME);
    }

    //  模拟车流数据 ['南北直行', '南北左转', '东西直行', '东西左转'],先用List集合代表四个相位的车流量
   public Map<String, Object> getTrafficLightInfo(List<Integer> numList){
        TrafficLightControlImpl control= new TrafficLightControlImpl();

        // # 计算绿灯相位优先级及对应绿灯持续时间(s)
        control.getTrafficLightTime(numList);
        Map<String,Object> response =new HashMap<>();

        //信号灯总周期时间(s)
        response.put("cycleTime",control.light_cycle_time);

        //相位变换顺序
        response.put("phaseOrder",new ArrayList<>(control.traffic_light.keySet()));

        //相位绿灯时长(s)
        response.put("phaseDurations",new ArrayList<>(control.traffic_light.values()));
        return response;
   }
}