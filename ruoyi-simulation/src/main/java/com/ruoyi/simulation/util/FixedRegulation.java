package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.FlowRecord;
import com.ruoyi.simulation.domain.GreenGroup;
import com.ruoyi.simulation.domain.TrafficLight;

import java.util.*;

/**
 * 固定周期信控优化方案
 */
public class FixedRegulation {
    /**
     * 设置信控数据
     * @param trafficLightList
     * @param recordList
     * @param groupList
     * @return
     */
    public static List<TrafficLight> assignSignalControl(List<TrafficLight> trafficLightList, List<FlowRecord> recordList, List<GreenGroup> groupList){
        //获取绿波组中周期相同的红绿灯组合
        List<List<Integer>> commonJunctionList = FixedRegulation.getCommonJunctionList(groupList);
        //获取不同红绿灯对应的平均真实流量比
        trafficLightList = setTrafficLightFlow(recordList, trafficLightList);
        //将红绿灯按相位分组
        Map<Integer,List<TrafficLightCouple>> junctionCoupleMap = TrafficLightUtil.mergeTrafficLight(trafficLightList);
        //利用韦伯斯特公式获取各路口最佳周期时间
        Map<Integer,Integer> junctionCycleMap = WebsterUtil.getOptimalCycleTime(junctionCoupleMap);
        //为绿波路段路口设置公共周期
        for(List<Integer> junctionIdList: commonJunctionList){
            setCommonCycleTime(junctionCycleMap, junctionIdList);
        }
        //根据公共周期设置各个相位的信控数据
        WebsterUtil.setJunctionSignal(junctionCycleMap,junctionCoupleMap);
        return trafficLightList;
    }
    /**
     * 获取不同交通灯下的平均流量
     * @param recordList
     * @return
     */
    private static List<TrafficLight> setTrafficLightFlow(List<FlowRecord> recordList, List<TrafficLight> trafficLightList){
        Map<Integer, TrafficLight> trafficLightMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList){
            trafficLightMap.put(trafficLight.getTrafficLightId(), trafficLight);
        }
        List<TrafficLight> tempList = new ArrayList<>();
        //获取指定时段下的流量数据，并存储具有历史流量数据的红绿灯集合
        for(FlowRecord record: recordList){
            int trafficLightId = record.getTrafficLightId();
            TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
            trafficLight.setFlowTrend(record.getAverageFlow());
            tempList.add(trafficLight);
        }
        return tempList;
    }
    /**
     * 获取具有相同周期的红绿灯组合
     * @param groupList
     * @return
     */
    private static List<List<Integer>> getCommonJunctionList(List<GreenGroup> groupList){
        //临时存储每一组绿波组中的红绿灯信息
        List<List<Integer>> groupJunctionIdList = new ArrayList<>();
        for(GreenGroup greenGroup: groupList){
            List<Integer> junctionIdList = new ArrayList<>();
            for(TrafficLight trafficLight: greenGroup.getTrafficLightList()){
                junctionIdList.add(trafficLight.getJunctionId());
            }
            groupJunctionIdList.add(junctionIdList);
        }
        //获取所有具有公共周期的红绿灯信息
        int length = groupJunctionIdList.size();
        for(int i=0;i<length;i++){
            List<Integer> list1 = groupJunctionIdList.get(i);
            if(list1.isEmpty()){
                continue;
            }
            for(int j=i+1;i<length;j++){
                List<Integer> list2 = groupJunctionIdList.get(j);
                if(ListUtil.anyIntersection(list1, list2)){
                    list1.addAll(list2);
                    list2.clear();
                }
            }
        }
        List<List<Integer>> commonList = new ArrayList<>();
        for(List<Integer> tempList: groupJunctionIdList){
            if(!tempList.isEmpty()){
                commonList.add(tempList);
            }
        }
        return commonList;
    }

    /**
     * 获取同一个绿波组下的公共周期
     * @param junctionCycleMap 不同路口对应的最佳周期
     * @param junctionIdList 在同一个绿波组下的路口集合
     */
    private static void setCommonCycleTime(Map<Integer,Integer> junctionCycleMap, List<Integer> junctionIdList){
        Map<Integer, Integer> tempCycleMap = new HashMap<>();
        for(int junctionId: junctionCycleMap.keySet()){
            if(junctionIdList.contains(junctionId)){
                int optimalCycle = junctionCycleMap.get(junctionId);
                tempCycleMap.put(junctionId, optimalCycle);
            }
        }
        int commonCycle = 0;
        for(int junctionId: tempCycleMap.keySet()){
            int optimalCycle = tempCycleMap.get(junctionId);
            if(commonCycle<optimalCycle) {
                commonCycle = optimalCycle;
            }
        }
        //为红绿灯设置公共周期
        for(int junctionId: tempCycleMap.keySet()){
            junctionCycleMap.put(junctionId, commonCycle);
        }
    }
}