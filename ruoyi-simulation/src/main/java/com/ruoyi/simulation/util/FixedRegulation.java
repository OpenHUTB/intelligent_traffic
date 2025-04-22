package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.FlowRecord;
import com.ruoyi.simulation.domain.GreenGroup;
import com.ruoyi.simulation.domain.TrafficLight;
import org.intellij.lang.annotations.Flow;

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
        //将红绿灯按路口分组
        Map<Integer,List<TrafficLight>> junctionLightMap = TrafficLightUtil.getJunctionTrafficLightMap(trafficLightList);
        //将红绿灯按相位分组
        Map<Integer, Map<Integer, TrafficLightCouple>> junctionCoupleMap = TrafficLightUtil.mergeTrafficLight(junctionLightMap);
        //获取不同红绿灯对应的平均真实流量比
        Map<Integer,List<TrafficLightCouple>> junctionMap = setCoupleFlow(recordList, junctionCoupleMap);
        //利用韦伯斯特公式获取各路口最佳周期时间
        Map<Integer,Integer> junctionCycleMap = WebsterUtil.getOptimalCycleTime(junctionMap);
        //为绿波路段路口设置公共周期
        for(List<Integer> junctionIdList: commonJunctionList){
            setCommonCycleTime(junctionCycleMap, junctionIdList);
        }
        //根据公共周期设置各个相位的信控数据
        WebsterUtil.setJunctionSignal(junctionCycleMap, junctionMap);
        return trafficLightList;
    }

    /**
     * 为不同相位下的交通灯设置流量值
     * @param recordList
     * @param junctionCoupleMap
     */
    private static Map<Integer,List<TrafficLightCouple>> setCoupleFlow(List<FlowRecord> recordList, Map<Integer,Map<Integer, TrafficLightCouple>> junctionCoupleMap){
        for(FlowRecord record: recordList){
            int junctionId = record.getJunctionId();
            Map<Integer, TrafficLightCouple> coupleMap = junctionCoupleMap.get(junctionId);
            int phaseId = record.getPhase();
            TrafficLightCouple couple = coupleMap.get(phaseId);
            //设置相位流量比
            double trafficFlow  = record.getAverageFlow();
            double flowRate = trafficFlow/WebsterUtil.MAX_FLOW;
            couple.setFlowRate(flowRate);
            couple.justifyIndirection();
        }
        Map<Integer,List<TrafficLightCouple>> junctionMap = new HashMap<>();
        for(int junctionId: junctionCoupleMap.keySet()){
            Map<Integer, TrafficLightCouple> coupleMap = junctionCoupleMap.get(junctionId);
            List<TrafficLightCouple> coupleList = new ArrayList<>(coupleMap.values());
            junctionMap.put(junctionId, coupleList);
        }
        return junctionMap;
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