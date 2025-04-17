package com.ruoyi.simulation.util;

import com.ruoyi.simulation.listener.TrafficIndirectionListener;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.TrafficLight;

import java.util.*;

public class AutoRegulation {
    /**
     * 使用自动调控进行信控优化
     * @param trafficLightList
     */
    public static void assignSignalControl(List<TrafficLight> trafficLightList){
        setTrafficLightFlow(trafficLightList);
        //将红绿灯按相位分组
        Map<Integer, List<TrafficLightCouple>> junctionCoupleMap = TrafficLightUtil.mergeTrafficLight(trafficLightList);
        //利用韦伯斯特公式获取各路口最佳周期时间
        Map<Integer,Integer> junctionCycleMap = WebsterUtil.getOptimalCycleTime(junctionCoupleMap);
        //设置各个路口的信控时间
        WebsterUtil.setJunctionSignal(junctionCycleMap,junctionCoupleMap);
    }

    /**
     * 为红绿灯设置交通指数
     * @param trafficLightList
     * @return
     */
    private static void setTrafficLightFlow(List<TrafficLight> trafficLightList){
        Map<Integer,Integer> flowMap = new HashMap<>();
        List<Junction> junctionList = TrafficIndirectionListener.indirectionCollection.getJunctionIndirection();
        for(Junction junction: junctionList){
            for(TrafficLight trafficLight: junction.getTrafficLightList()){
                flowMap.put(trafficLight.getTrafficLightId(), trafficLight.getFlowTrend());
            }
        }
        for(TrafficLight trafficLight: trafficLightList){
            int flow = flowMap.get(trafficLight.getTrafficLightId());
            trafficLight.setFlowTrend(flow);
        }
    }
}
