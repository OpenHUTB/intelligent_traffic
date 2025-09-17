package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.TrafficLight;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public class JunctionIndirectionUtil {
    public static void setCongestionMileage(Map<Integer, TrafficLight> trafficLightMap, Map<Integer, Junction> junctionMap, RedisTemplate<String,Object> redisTemplate){
        for(Junction junction: junctionMap.values()) {
            //初始化所有的道路拥堵里程
            junction.setCongestionMileage(0d);
            //初始化拥堵里程趋势变化
            junction.setCongestionMileageRate(0d);
        }
        //根据每个红绿灯对应的拥堵里程信息来设置不同路口拥堵里程数据
        final boolean[] flag = new boolean[1];
        String response = new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String key, double congestionMileage) {
                //设置红绿灯对应的拥堵里程信息
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    flag[0] = true;
                    return;
                }
                //设置路口拥堵里程信息，如果某个红绿灯对应的拥堵里程大于路口拥堵里程，则其为该路口的实际最大拥堵里程
                int junctionId = trafficLight.getJunctionId();
                Junction junction = junctionMap.get(junctionId);
                if(congestionMileage<=junction.getCongestionMileage()){
                    return;
                }
                junction.setCongestionMileage(Math.ceil(congestionMileage * 100)/100);
                if(IndirectionTrendUtil.mileageTrendMap.get(junctionId)==null||IndirectionTrendUtil.mileageTrendMap.get(junctionId)==0d){
                    IndirectionTrendUtil.mileageTrendMap.put(junctionId, congestionMileage);
                }
                double mileageTrend = IndirectionTrendUtil.mileageTrendMap.get(junctionId);
                //计算拥堵里程趋势变化
                double congestionTrendRate = 0d;
                if(mileageTrend!=0){
                    congestionTrendRate = (congestionMileage-mileageTrend)/mileageTrend;
                }
                while(congestionTrendRate>3){
                    congestionTrendRate/=2;
                }
                junction.setCongestionMileageRate(Math.ceil(congestionTrendRate * 10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate, "traffic_light_congestion_mileage");
        if(flag[0]){
            System.out.println("不存在的trafficLightId:"+response);
        }
    }
    public static void setAverageDelay(Map<Integer, TrafficLight> trafficLightMap, Map<Integer, Junction> junctionMap, RedisTemplate<String,Object> redisTemplate){
        for(Junction junction: junctionMap.values()) {
            //初始化平均延迟时间
            junction.setAverageDelay(0d);
            junction.setAverageDelayRate(0d);
        }
        //设置红绿灯的平均等待时间指数
        new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String key, double averageDelay) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    return;
                }
                int junctionId = trafficLight.getJunctionId();
                Junction junction = junctionMap.get(junctionId);
                if(averageDelay<junction.getAverageDelay()){
                    return;
                }
                junction.setAverageDelay(Math.ceil(averageDelay * 100)/100);
                if(IndirectionTrendUtil.delayTrendMap.get(junctionId)==null||IndirectionTrendUtil.delayTrendMap.get(junctionId)==0d){
                    IndirectionTrendUtil.delayTrendMap.put(junctionId, averageDelay);
                }
                double delayTrend = IndirectionTrendUtil.delayTrendMap.get(junctionId);
                //计算拥堵里程趋势变化
                double delayTrendRate = 0d;
                if(delayTrend!=0){
                    delayTrendRate = (averageDelay-delayTrend)/delayTrend;
                }
                while(delayTrendRate>3){
                    delayTrendRate/=2;
                }
                junction.setAverageDelayRate(Math.ceil(delayTrendRate * 10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_delay");
    }
    public static void setStopTimes(Map<Integer, TrafficLight> trafficLightMap, Map<Integer, Junction> junctionMap, RedisTemplate<String,Object> redisTemplate){
        for(Junction junction: junctionMap.values()){
            //初始化平均等待次数
            junction.setStopTimes(0d);
            junction.setStopTimesRate(0d);
        }

        //获取每个红绿灯对应的停车次数信息
        new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String key, double stopTimes) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    return;
                }
                int junctionId = trafficLight.getJunctionId();
                Junction junction = junctionMap.get(junctionId);
                if(stopTimes<junction.getStopTimes()){
                    return;
                }
                junction.setStopTimes(stopTimes);
                if(IndirectionTrendUtil.stopTrendMap.get(junctionId)==null||IndirectionTrendUtil.stopTrendMap.get(junctionId)==0d){
                    IndirectionTrendUtil.stopTrendMap.put(junctionId, stopTimes);
                }
                double stopTrend = IndirectionTrendUtil.stopTrendMap.get(junctionId);
                //计算拥堵里程趋势变化
                double stopTrendRate = 0d;
                if(stopTrend!=0){
                    stopTrendRate = (stopTimes-stopTrend)/stopTrend;
                }
                while(stopTrendRate>3){
                    stopTrendRate/=2;
                }
                junction.setStopTimesRate(Math.ceil(stopTrendRate * 10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_stop");
    }

    /**
     * 设置交通优化对比数据
     * @param trafficLightMap
     * @param junctionMap
     * @param redisTemplate
     */
    public static void setOptimizationComparison(Map<Integer, TrafficLight> trafficLightMap, Map<Integer, Junction> junctionMap, RedisTemplate<String,Object> redisTemplate){

    }
}
