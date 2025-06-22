package com.ruoyi.simulation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.domain.Signalbase.TrafficLightState;
import com.ruoyi.simulation.domain.TrafficLight;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * 红绿灯工具类
 */
public class TrafficLightUtil {
    /**
     * 获取不同路口对应的红绿灯集合
     * @return
     */
    public static Map<Integer,List<TrafficLight>> getJunctionTrafficLightMap(List<TrafficLight> trafficLightList){
        Map<Integer,List<TrafficLight>> junctionLightMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList){
            junctionLightMap.putIfAbsent(trafficLight.getJunctionId(),new ArrayList<>());
            junctionLightMap.get(trafficLight.getJunctionId()).add(trafficLight);
        }
        return junctionLightMap;
    }
    /**
     * 将红绿灯按相位分组
     * @param junctionLightMap
     * @return
     */
    public static Map<Integer, Map<Integer, TrafficLightCouple>> mergeTrafficLight(Map<Integer,List<TrafficLight>> junctionLightMap){
        Map<Integer, Map<Integer, TrafficLightCouple>> junctionCoupleMap = new HashMap<>();
        for(int junctionId : junctionLightMap.keySet()){
            List<TrafficLight> trafficLightList = junctionLightMap.get(junctionId);
            //将交通灯根据所在的方位加入到组合中(key-相位，value-红绿灯组合)
            Map<Integer, TrafficLightCouple> coupleMap = new HashMap<>();
            for(TrafficLight trafficLight: trafficLightList) {
                int phase = trafficLight.getGreenPhase();
                coupleMap.putIfAbsent(phase, new TrafficLightCouple(phase));
                TrafficLightCouple couple = coupleMap.get(phase);
                couple.add(trafficLight);
            }
            junctionCoupleMap.put(junctionId, coupleMap);
        }
        return junctionCoupleMap;
    }

    /**
     * 设置交通灯信控方案
     * @param cycle
     * @param coupleList
     */
    public static void setTrafficLight(int cycle, List<TrafficLightCouple> coupleList){
        coupleList.sort(new Comparator<TrafficLightCouple>() {
            @Override
            public int compare(TrafficLightCouple o1, TrafficLightCouple o2) {
                return o1.getPhase()-o2.getPhase();
            }
        });
        int length = coupleList.size();
        TrafficLightCouple current = coupleList.get(0);
        current.setLightTime(cycle, 0);
        for(int i=1;i<length;i++) {
            TrafficLightCouple prefix = coupleList.get(i-1);
            current = coupleList.get(i);
            //前缀时间=前一个相位的绿灯时间+前一相位黄灯时间+清空损失时间
            int prefixTime = prefix.getPrefixTime() + prefix.getGreenTime() + prefix.getClearingLostTime();
            current.setLightTime(cycle, prefixTime);
        }
    }
    /**
     * 获取交通灯id与交通灯的映射
     * @param trafficLightList
     * @return
     */
    public static Map<Integer, TrafficLight> getTrafficLightMap(List<TrafficLight> trafficLightList){
        Map<Integer, TrafficLight> trafficLightMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList){
            trafficLightMap.put(trafficLight.getTrafficLightId(), trafficLight);
        }
        return trafficLightMap;
    }

    /**
     * 为红绿灯一个周期内，每秒钟对应的状态赋值
     * @param trafficLightList
     */
    public static void initialStateArr(List<TrafficLight> trafficLightList){
        for(TrafficLight trafficLight: trafficLightList){
            //计算红绿灯信控周期
            int cycle = 0;
            List<StateStage> stateList = trafficLight.getStageList();
            for(StateStage stage : stateList){
                cycle+=stage.getLength();
            }
            TrafficLightState[] stateArr = new TrafficLightState[cycle];
            trafficLight.setStateArr(stateArr);
            //计算红绿灯每个周期内每秒钟对应的状态
            int index = 0;
            for(StateStage stage: stateList){
                int length = stage.getLength();
                TrafficLightState state = stage.getState();
                for(int i=0;i<length;i++){
                    stateArr[index+i] = state;
                }
                index += stage.getLength();
            }
        }
    }
    /**
     * 设置carla中的部分红绿灯参数
     * @param trafficLightList
     */
    public static void setCarlaTrafficLight(RedisTemplate<String,Object> redisTemplate, List<TrafficLight> trafficLightList){
        Map<Integer, TrafficLightState[]> stateMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList){
            //保存每个红绿灯信控数据到Redis
            int trafficLightId = trafficLight.getTrafficLightId();
            TrafficLightState[] stateArr = trafficLight.getStateArr();
            redisTemplate.opsForValue().set("signal_control_"+trafficLightId, JSONObject.toJSONString(stateArr));
            stateMap.put(trafficLightId, stateArr);
        }
    }
    /**
     * 合并红绿灯中相邻且具有相同state的stage
     * @param stageList
     */
    public static List<StateStage> mergeStage(List<StateStage> stageList){
        List<StateStage> tempList = new ArrayList<>();
        for(StateStage stage: stageList){
            if(tempList.isEmpty()){
                tempList.add(stage);
                continue;
            }
            StateStage temp = tempList.get(tempList.size()-1);
            if(temp.getState()!=stage.getState()){
                tempList.add(stage);
                continue;
            }
            temp.setLength(temp.getLength()+stage.getLength());
        }
        return tempList;
    }
}
