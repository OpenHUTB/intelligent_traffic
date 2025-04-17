package com.ruoyi.simulation.util;

import com.ruoyi.common.utils.StringUtils;
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
     * @param trafficLightList
     * @return
     */
    public static Map<Integer, List<TrafficLightCouple>> mergeTrafficLight(List<TrafficLight> trafficLightList){
        Map<Integer, List<TrafficLightCouple>> junctionCoupleMap = new HashMap<>();
        Map<Integer,List<TrafficLight>> junctionLightMap = getJunctionTrafficLightMap(trafficLightList);
        for(int junctionId:junctionLightMap.keySet()){
            trafficLightList = junctionLightMap.get(junctionId);
            //将交通灯根据所在的方位加入到组合中
            Map<Integer, TrafficLightCouple> coupleMap = new HashMap<>();
            for(TrafficLight trafficLight: trafficLightList) {
                int phase = trafficLight.getPhase();
                coupleMap.putIfAbsent(phase, new TrafficLightCouple(phase, trafficLight.getPrefixTime()));
                TrafficLightCouple couple = coupleMap.get(phase);
                couple.add(trafficLight);
            }
            List<TrafficLightCouple> coupleList = new ArrayList<>();
            for(int phase: coupleMap.keySet()){
                TrafficLightCouple couple = coupleMap.get(phase);
                couple.justifyIndirection();
                coupleList.add(couple);
            }
            junctionCoupleMap.put(junctionId, coupleList);
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
        int redTime = cycle - current.getGreenTime() - TrafficLight.YELLOW_TIME;
        current.setLightTime(redTime, current.getGreenTime(), TrafficLight.YELLOW_TIME, current.getPrefixTime());
        for(int i=1;i<length;i++) {
            TrafficLightCouple prefix = coupleList.get(i-1);
            current = coupleList.get(i);
            redTime = cycle - current.getGreenTime() - TrafficLight.YELLOW_TIME;
            //前缀时间=前一个相位的绿灯时间+前一相位黄灯时间+清空损失时间
            int prefixTime = prefix.getPrefixTime() + prefix.getGreenTime() + prefix.getClearingLostTime();
            current.setPrefixTime(prefixTime);
            current.setLightTime(redTime, current.getGreenTime(), TrafficLight.YELLOW_TIME, prefixTime);
        }
    }
    /**
     * 设置carla中的部分红绿灯参数
     * @param trafficLightList
     */
    public static void setCarlaTrafficLight(RedisTemplate<String,Object> redisTemplate, List<TrafficLight> trafficLightList){
        for(TrafficLight trafficLight: trafficLightList){
            int trafficLightId = trafficLight.getTrafficLightId();
            redisTemplate.opsForValue().set("prefix_time_"+trafficLightId, trafficLight.getPrefixTime());
            //保存每个红绿灯信控数据到Redis
            redisTemplate.opsForValue().set("signal_control_"+trafficLightId, trafficLight);
        }
    }
}
