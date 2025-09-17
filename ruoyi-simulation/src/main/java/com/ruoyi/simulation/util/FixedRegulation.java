package com.ruoyi.simulation.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.simulation.domain.FlowRecord;
import com.ruoyi.simulation.domain.GreenWave;
import com.ruoyi.simulation.domain.TrafficLight;
import com.ruoyi.simulation.listener.SignalControlListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * 固定周期信控优化方案
 */
public class FixedRegulation {
    private static GreenWaveRegulation regulation = null;
    /**
     * 设置信控数据
     * @param trafficLightList
     * @param recordList
     * @param waveList
     * @return
     */
    public static void assignSignalControl(List<TrafficLight> trafficLightList, List<FlowRecord> recordList, List<GreenWave> waveList){
        //将红绿灯按路口分组
        Map<Integer,List<TrafficLight>> junctionLightMap = TrafficLightUtil.getJunctionTrafficLightMap(trafficLightList);
        //将红绿灯按相位分组 路口-相位-红绿灯组
        Map<Integer, Map<Integer, TrafficLightCouple>> junctionCoupleMap = TrafficLightUtil.mergeTrafficLight(junctionLightMap);
        Map<Integer, TrafficLight> trafficLightMap = TrafficLightUtil.getTrafficLightMap(trafficLightList);
        //获取不同红绿灯对应的平均真实流量比
        Map<Integer,List<TrafficLightCouple>> junctionMap = setCoupleFlow(recordList, junctionCoupleMap);
        //利用韦伯斯特公式获取各路口最佳周期时间 路口-周期
        Map<Integer,Integer> junctionCycleMap = WebsterUtil.getOptimalCycleTime(junctionMap);
        //获取绿波组中周期相同的红绿灯组合
        List<List<Integer>> commonJunctionList = FixedRegulation.getCommonJunctionList(waveList, trafficLightMap);
        //为绿波路段路口设置公共周期
        for(List<Integer> junctionIdList: commonJunctionList){
            setCommonCycleTime(junctionCycleMap, junctionIdList);
        }
        //根据公共周期设置各个相位的信控数据
        WebsterUtil.setJunctionSignal(junctionCycleMap, junctionMap);
        TrafficLightUtil.loggerSignal(trafficLightList,"优化后信控方案");
        //绿波协同控制
        regulation = new GreenWaveRegulation(junctionCoupleMap, junctionCycleMap, trafficLightMap);
        regulation.setGreenWave(waveList);
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
            couple.adjustIndirection();
        }
        Map<Integer,List<TrafficLightCouple>> junctionMap = new HashMap<>();
        for(int junctionId: junctionCoupleMap.keySet()){
            Map<Integer, TrafficLightCouple> coupleMap = junctionCoupleMap.get(junctionId);
            List<TrafficLightCouple> coupleList = new ArrayList<>(coupleMap.values());
            junctionMap.put(junctionId, coupleList);
            justifyWalkTime(coupleList);
        }
        return junctionMap;
    }
    private static void justifyWalkTime(List<TrafficLightCouple> coupleList){
        int totalWalkTime = 0;
        for(TrafficLightCouple couple: coupleList){
            totalWalkTime += couple.getWalkTime();
        }
        //设置行人过街总时间上限
        if(totalWalkTime>WebsterUtil.MAX_WALK_TIME){
            for(TrafficLightCouple couple: coupleList){
                int walkTime = (int)Math.round(WebsterUtil.MAX_WALK_TIME * couple.getWalkTime() * 1.0 /totalWalkTime);
                couple.setWalkTime(walkTime);
            }
        }
    }
    /**
     * 获取具有相同周期的红绿灯组合
     * @param waveList
     * @return
     */
    private static List<List<Integer>> getCommonJunctionList(List<GreenWave> waveList, Map<Integer, TrafficLight> trafficLightMap){
        for(GreenWave wave: waveList){
            TrafficLight trafficLight = trafficLightMap.get(wave.getTrafficLightId());
            if(trafficLight==null){
                continue;
            }
            wave.setJunctionId(trafficLight.getJunctionId());
        }
        //临时存储每一组绿波组中的红绿灯信息
        List<List<Integer>> groupJunctionIdList = getGroupJunctionList(waveList);
        //获取所有具有公共周期的红绿灯信息
        int length = groupJunctionIdList.size();
        for(int i=0;i<length;i++){
            List<Integer> list1 = groupJunctionIdList.get(i);
            if(list1.isEmpty()){
                continue;
            }
            for(int j=i+1;j<length;j++){
                List<Integer> list2 = groupJunctionIdList.get(j);
                if(ListUtil.anyIntersection(list1, list2)){
                    list1.addAll(list2);
                    list2.clear();
                    //如果有相同的路口，则i回退一位之后继续循环
                    i--;
                    break;
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
    /**
     * 获取所有的绿波组合
     * @param waveList
     * @return
     */
    private static List<List<Integer>> getGroupJunctionList(List<GreenWave> waveList){
        LinkedHashMap<Integer,List<Integer>> groupJunctionIdMap = new LinkedHashMap<>();
        for(GreenWave wave: waveList){
            groupJunctionIdMap.putIfAbsent(wave.getGroupId(), new ArrayList<>());
            List<Integer> junctionIdList = groupJunctionIdMap.get(wave.getGroupId());
            junctionIdList.add(wave.getJunctionId());
        }
        List<List<Integer>> groupJunctionIdList = new ArrayList<>();
        for(int groupId: groupJunctionIdMap.keySet()){
            List<Integer> junctionIdList = groupJunctionIdMap.get(groupId);
            groupJunctionIdList.add(junctionIdList);
        }
        return groupJunctionIdList;
    }

    /**
     * 重新更新绿波路线下的信控数据
     * @param waveList
     * @param redisTemplate
     */
    public static void setGreenWave(List<GreenWave> waveList, RedisTemplate<String,Object> redisTemplate){
        if(regulation==null){
            throw new RuntimeException("基本信控数据缺失，请先进行固定信控调优！");
        }
        List<TrafficLight> trafficLightList = regulation.setGreenWave(waveList);
        TrafficLightUtil.initialStateArr(trafficLightList);
        SignalControlListener.setTemporarySignal(trafficLightList);
        TrafficLightUtil.setCarlaTrafficLight(redisTemplate, trafficLightList);
    }
}