package com.ruoyi.simulation.util;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.simulation.domain.GreenWave;
import com.ruoyi.simulation.domain.TrafficLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * 绿波优化方案
 */
public class GreenWaveRegulation {
    private static Logger logger = LoggerFactory.getLogger(GreenWaveRegulation.class);
    /**
     * 交通灯id-交通灯
     */
    private static Map<Integer, TrafficLight> trafficLightMap;
    /**
     * 路口-相位-红绿灯组
     */
    private static Map<Integer, Map<Integer, TrafficLightCouple>> junctionCoupleMap;
    /**
     * 路口-周期
     */
    private static Map<Integer,Integer> junctionCycleMap;
    public static void  initialData(Map<Integer, Map<Integer, TrafficLightCouple>> coupleMap, Map<Integer,Integer> cycleMap, Map<Integer, TrafficLight> lightMap){
        junctionCoupleMap = coupleMap;
        junctionCycleMap = cycleMap;
        trafficLightMap = lightMap;
    }
    /**
     * 设置绿波路段
     * @param waveList 绿波组合
     */
    public static List<TrafficLight> setGreenWave(List<GreenWave> waveList){
        if(junctionCycleMap==null||junctionCycleMap==null||trafficLightMap==null){
            throw new RuntimeException("未设置初始化数据!");
        }
        //存储已经进行了绿波调整的路口id
        Set<Integer> adjustedJunctionIdSet = new HashSet<>();
        //存储每个绿波组下的绿波红绿灯信息
        Map<Integer,List<GreenWave>> groupWaveMap = new HashMap<>();
        for(GreenWave wave: waveList){
            groupWaveMap.putIfAbsent(wave.getGroupId(), new ArrayList<>());
            List<GreenWave> tempList = groupWaveMap.get(wave.getGroupId());
            tempList.add(wave);
        }
        //协调绿波路段中的每一个路口
        while(!groupWaveMap.isEmpty()){
            List<GreenWave> adjustableList = getAdjustableGroup(groupWaveMap, adjustedJunctionIdSet);
            doubleLink(adjustableList, adjustedJunctionIdSet);
        }
        for(Map<Integer, TrafficLightCouple> coupleMap: junctionCoupleMap.values()) {
            for(TrafficLightCouple couple: coupleMap.values()){
                //合并红绿灯中相邻且具有相同state的stage
                List<StateStage> stageList = TrafficLightUtil.mergeStage(couple.getStageList());
                couple.setStageList(stageList);
                //将每一个相位下的信控数据设置到交通灯中
                setTrafficLightStage(couple);
            }
        }
        return new ArrayList<TrafficLight>(trafficLightMap.values());
    }
    /**
     * 将相位中红绿灯的信控数据设置到红绿灯中
     */
    private static void setTrafficLightStage(TrafficLightCouple couple){
        for(TrafficLight trafficLight: couple.getTrafficLightList()){
            trafficLight.setStageList(couple.getStageList());
        }
    }
    /**
     * 优先获取包含已经调整过的路口的绿波组
     * @param groupWaveMap
     * @return
     */
    private static List<GreenWave> getAdjustableGroup(Map<Integer,List<GreenWave>> groupWaveMap, Set<Integer> adjustedJunctionIdSet){
        for(int groupId: groupWaveMap.keySet()){
            List<GreenWave> waveList = groupWaveMap.get(groupId);
            for(GreenWave wave: waveList){
                if(adjustedJunctionIdSet.contains(wave.getJunctionId())){
                    return groupWaveMap.remove(groupId);
                }
            }
        }
        List<GreenWave> adjustableList = null;
        for(int groupId: groupWaveMap.keySet()) {
            adjustableList = groupWaveMap.remove(groupId);
            break;
        }
        return adjustableList;
    }
    /**
     * 为绿波信息制作双向链接
     * @param waveList
     */
    private static void doubleLink(List<GreenWave> waveList, Set<Integer> adjustedJunctionIdSet){
        for(GreenWave wave: waveList){
            if(wave.getNextId()==null){
                continue;
            }
            for(GreenWave next: waveList){
                if(wave.getNextId() == next.getTrafficLightId()){
                    wave.setNext(next);
                    next.setPrefix(wave);
                }
            }
        }
        int index = 0;
        for(int i=0;i<waveList.size();i++){
            GreenWave wave = waveList.get(i);
            if(adjustedJunctionIdSet.contains(wave.getJunctionId())){
                index = i;
                break;
            }
        }
        GreenWave wave = waveList.get(index);
        adjustedJunctionIdSet.add(wave.getJunctionId());
        nextLink(wave, adjustedJunctionIdSet);
        prefixLink(wave, adjustedJunctionIdSet);
    }
    /**
     * 调整下一个路口的绿波数据
     * @param wave
     */
    private static void nextLink(GreenWave wave, Set<Integer> adjustedJunctionIdSet){
        //获取下一个红绿灯对应的绿波信息
        GreenWave next = wave.getNext();
        if(next==null){
            return;
        }
        int nextJunctionId = next.getJunctionId();
        if(adjustedJunctionIdSet.contains(nextJunctionId)){
            return;
        }
        int interval = wave.getIntervalSecond();
        //获取当前红绿灯对应的相位组
        TrafficLightCouple currentCouple = getWaveCouple(wave);
        TrafficLightCouple nextCouple = getWaveCouple(next);
        //获取红绿灯周期
        int cycle = junctionCycleMap.get(nextJunctionId);
        //获取原定前缀时间与绿波需要的实际前缀时间的差值
        int prefixTime = (currentCouple.getPrefixTime() + interval) % cycle;
        int gap = (nextCouple.getPrefixTime() - prefixTime + cycle) % cycle;
        Map<Integer, TrafficLightCouple> nextCoupleMap = junctionCoupleMap.get(nextJunctionId);
        adjustTrafficLight(gap, nextCoupleMap);
        //记录下当前路口已经进行了调整
        adjustedJunctionIdSet.add(nextJunctionId);
        //递归调整下一个绿波路口
        nextLink(next, adjustedJunctionIdSet);
    }
    /**
     * 调整上一个路口的绿波数据
     * @param wave
     */
    private static void prefixLink(GreenWave wave, Set<Integer> adjustedJunctionIdSet){
        GreenWave prefix = wave.getPrefix();
        if(prefix==null){
            return;
        }
        int prefixJunctionId = prefix.getJunctionId();
        if(adjustedJunctionIdSet.contains(prefixJunctionId)){
            return;
        }
        int interval = wave.getIntervalSecond();
        //获得前一路口相位组信息
        TrafficLightCouple prefixCouple = getWaveCouple(prefix);
        //获取当前相位组信息
        TrafficLightCouple currentCouple = getWaveCouple(wave);
        //获取前一路口的周期
        int cycle = junctionCycleMap.get(prefixJunctionId);
        int prefixTime = currentCouple.getPrefixTime() - interval;
        while(prefixTime<0){
            prefixTime += cycle;
        }
        int gap = (prefixCouple.getPrefixTime()-prefixTime+cycle) % cycle;
        Map<Integer, TrafficLightCouple> prefixCoupleMap = junctionCoupleMap.get(prefixJunctionId);
        adjustTrafficLight(gap, prefixCoupleMap);
        //记录下当前路口已经进行了调整
        adjustedJunctionIdSet.add(prefix.getJunctionId());
        //递归调整下一个绿波路口
        nextLink(prefix, adjustedJunctionIdSet);
    }

    /**
     * 获取绿波信息对应的红绿灯相位组
     * @param wave
     * @return
     */
    private static TrafficLightCouple getWaveCouple(GreenWave wave){
        int trafficLightId = wave.getTrafficLightId();
        TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
        int greenPhase = trafficLight.getGreenPhase();
        int junctionId = wave.getJunctionId();
        Map<Integer, TrafficLightCouple> coupleMap = junctionCoupleMap.get(junctionId);
        TrafficLightCouple couple = coupleMap.get(greenPhase);
        return couple;
    }
    /**
     * 设置相邻绿波路口的信控数据
     * @param gap
     * @param coupleMap
     */
    private static void adjustTrafficLight(int gap, Map<Integer, TrafficLightCouple> coupleMap){
        for(int phase: coupleMap.keySet()){
            TrafficLightCouple couple = coupleMap.get(phase);
            List<StateStage> stageList = couple.getStageList();
            List<StateStage> suffixList = new ArrayList<>();
            //红绿灯周期往前移动，直到移动到与绿波时间相匹配为止
            int index = moveForward(gap, stageList, suffixList);
            //将移动后的周期重新组合
            List<StateStage> prefixList = new ArrayList<>();
            for(int i=index;i<stageList.size();i++){
                StateStage stage = stageList.get(i);
                prefixList.add(stage);
            }
            for(int i=0;i<suffixList.size();i++){
                StateStage stage = suffixList.get(i);
                prefixList.add(stage);
            }
            couple.setStageList(prefixList);
            //计算调整后的prefix值
            couple.refreshPrefixTime();
        }
    }

    /**
     * 根据绿波时差让红绿灯状态往前移动
     * @param gap
     * @param stageList
     * @param suffixList
     * @return
     */
    public static int moveForward(int gap, List<StateStage> stageList, List<StateStage> suffixList){
        int index = 0;
        for(int i=0;i<stageList.size();i++){
            StateStage stage = stageList.get(i);
            int length = stage.getLength();
            if(gap>length){
                gap -= length;
                suffixList.add(stage);
            }else{
                stage.setLength(length-gap);
                StateStage temp = new StateStage();
                temp.setLength(gap);
                temp.setState(stage.getState());
                suffixList.add(temp);
                index = i;
                break;
            }
        }
        return index;
    }
    public static GreenWave getNext(List<GreenWave> waveList, GreenWave current) {
        GreenWave next = null;
        for(GreenWave wave: waveList){
            if(current.getNextId()==wave.getTrafficLightId()){
                next = wave;
                break;
            }
        }
        return next;
    }
    public static GreenWave getPrefix(List<GreenWave> waveList, GreenWave current) {
        GreenWave prefix = null;
        for(GreenWave wave: waveList){
            if(wave.getNextId()==current.getTrafficLightId()){
                prefix = wave;
                break;
            }
        }
        return prefix;
    }
}
