package com.ruoyi.simulation.util;

import com.ruoyi.simulation.listener.TrafficIndirectionListener;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.TrafficLight;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class AutoRegulation {
    /**
     * 使用自动调控进行信控优化
     * @param trafficLightList
     */
    public static void assignSignalControl(List<TrafficLight> trafficLightList){
        //将红绿灯按路口分组
        Map<Integer,List<TrafficLight>> junctionLightMap = TrafficLightUtil.getJunctionTrafficLightMap(trafficLightList);
        //将红绿灯按相位分组
        Map<Integer, Map<Integer, TrafficLightCouple>> junctionCoupleMap = TrafficLightUtil.mergeTrafficLight(junctionLightMap);
        //设置不同相位红绿灯的流量值
        Map<Integer, List<TrafficLightCouple>> junctionMap = setCoupleFlow(junctionCoupleMap);
        //利用韦伯斯特公式获取各路口最佳周期时间
        Map<Integer,Integer> junctionCycleMap = WebsterUtil.getOptimalCycleTime(junctionMap);
        //设置各个路口的信控时间
        WebsterUtil.setJunctionSignal(junctionCycleMap, junctionMap);
    }

    /**
     * 为红绿灯设置交通指数
     * @param junctionCoupleMap
     * @return
     */
    private static Map<Integer, List<TrafficLightCouple>> setCoupleFlow(Map<Integer, Map<Integer, TrafficLightCouple>> junctionCoupleMap){
        Map<Integer,Integer> flowMap = new HashMap<>();
        List<Junction> junctionList = TrafficIndirectionListener.indirectionCollection.getJunctionIndirection();
        for(Junction junction: junctionList){
            for(TrafficLight trafficLight: junction.getTrafficLightList()){
                flowMap.put(trafficLight.getTrafficLightId(), trafficLight.getFlowTrend());
            }
        }
        Map<Integer, List<TrafficLightCouple>> coupleMap = new HashMap<>();
        for(int junctionId: junctionCoupleMap.keySet()){
            List<TrafficLightCouple> coupleList = new ArrayList<>(junctionCoupleMap.get(junctionId).values());
            for(TrafficLightCouple couple:coupleList){
                setPhaseFlow(couple, flowMap);
            }
            coupleMap.put(junctionId, coupleList);
        }
        return coupleMap;
    }

    /**
     * 判断指定相位是否为同一侧道路的执行和左转
     * @param trafficLightList
     * @return
     */
    private static boolean isSameStartRoad(List<TrafficLight> trafficLightList){
        String turnDirection = null;
        for(TrafficLight trafficLight: trafficLightList){
            if(StringUtils.isEmpty(turnDirection)){
                turnDirection = trafficLight.getTurnDirection();
            }else if(!StringUtils.equals(turnDirection, trafficLight.getTurnDirection())){
                return true;
            }
        }
        return false;
    }

    /**
     * 设置单个相位的流量比
     * @param couple
     * @param flowMap
     */
    private static void setPhaseFlow(TrafficLightCouple couple, Map<Integer,Integer> flowMap){
        List<TrafficLight> trafficLightList = couple.getTrafficLightList();
        boolean flag = isSameStartRoad(trafficLightList);
        double trafficFlow = 0;
        if(flag == true){
            for(TrafficLight trafficLight: trafficLightList){
                trafficFlow += trafficLight.getFlowTrend();
            }
        }else{
            for(TrafficLight trafficLight: trafficLightList){
                if(trafficLight.getFlowTrend()>trafficFlow){
                    trafficFlow = trafficLight.getFlowTrend();
                }
            }
        }
        double flowRate = trafficFlow/WebsterUtil.MAX_FLOW;
        couple.setFlowRate(flowRate);
    }
}
