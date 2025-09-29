package com.ruoyi.simulation.util;

import com.alibaba.fastjson.JSON;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.Road;
import com.ruoyi.simulation.domain.TrafficLight;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndirectionTrendUtil {
    public static final Map<String, Double> indirectionTrendMap = new HashMap<>();
    public static final Map<String, Double> trafficTrendMap = new HashMap<>();
    public static final Map<Integer, Double> mileageTrendMap = new HashMap<>();
    public static final Map<Integer, Double> delayTrendMap = new HashMap<>();
    public static final Map<Integer, Double> stopTrendMap = new HashMap<>();
    /**
     * 初始化基础数据
     * @param roadList
     * @param redisTemplate
     */
    public static void initialBasicData(List<Road> roadList, RedisTemplate<String,Object> redisTemplate){
        Map<String,Integer> roadMap = new HashMap<>();
        //设置carla中roadId与道路主键之间的对应关系
        for(Road road: roadList){
            String[] relatedList = road.getRelatedRoadId().split(",");
            for(String related: relatedList){
                roadMap.put(related, road.getRoadId());
            }
            //初始化拥堵指数趋势变化初始值
            indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
        }
        redisTemplate.opsForValue().set("road_map", JSON.toJSONString(roadMap));
        //初始化平均延误时间变化趋势初始值
        trafficTrendMap.put("averageDelayTrend", 0d);
        //初始化总拥堵里程变化趋势初始值
        trafficTrendMap.put("congestionMileageTrend", 0d);
        //初始化总的平均速度变化趋势初始值
        trafficTrendMap.put("averageSpeedTrend", 0d);
    }

    /**
     * 设置车辆平均速度变化趋势
     * @param averageSpeed 平均速度
     */
    public static double getAverageSpeedRate(double averageSpeed){
        Double trend = trafficTrendMap.get("averageSpeedTrend");
        //计算平均车速指数趋势变化
        double averageSpeedRate = 0d;
        if(trend!=null && trend!=0){
            averageSpeedRate = (averageSpeed-trend)/trend;
        }
        return Math.ceil(averageSpeedRate*10000)/100;
    }
    public static double getAverageDelayRate(double averageDelay){
        Double trend = trafficTrendMap.get("averageDelayTrend");
        if(trend==null){
            trend = 0d;
        }
        //计算平均车速指数趋势变化(分钟)
        return Math.ceil((averageDelay - trend)/trend * 10000)/100;
    }
    /**
     * 保存不同道路的拥堵指数变化趋势
     */
    public static void setIndirectionTrend(List<Road> roadList, RedisTemplate<String,Object> redisTemplate){
        for(Road road: roadList){
            indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String roadId, double averageSpeed) {
                averageSpeed = Math.ceil(averageSpeed * 360)/100;
                //保存不同道路的实时平均车速
                IndirectionUtil.speedMap.putIfAbsent(roadId, averageSpeed);
                double congestionIndirection = IndirectionUtil.speedMap.get(roadId)/averageSpeed;
                indirectionTrendMap.put(roadId, congestionIndirection);
            }
        }.getRespectiveIndirection(redisTemplate,"road_average_speed");
    }
    /**
     * 保存不同路口的拥堵里程变化趋势
     */
    public static void setCongestionMileageTrend(Map<Integer, TrafficLight> trafficLightMap, List<Junction> junctionList, RedisTemplate<String,Object> redisTemplate){
        for(Junction junction: junctionList){
            mileageTrendMap.put(junction.getJunctionId(), 0d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String key, double congestionMileage) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    return;
                }
                int junctionId = trafficLight.getJunctionId();
                if(mileageTrendMap.get(junctionId)<congestionMileage){
                    mileageTrendMap.put(junctionId, congestionMileage);
                }
            }
        }.getRespectiveIndirection(redisTemplate,"junction_congestion_mileage");
    }
    /**
     * 保存不同红绿灯的交通指数变化趋势
     */
    public static void setDelayTrend(Map<Integer,TrafficLight> trafficLightMap, List<Junction> junctionList, RedisTemplate<String,Object> redisTemplate){
        for(Junction junction: junctionList){
            delayTrendMap.put(junction.getJunctionId(), 0d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String key, double averageDelay) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                int junctionId = trafficLight.getJunctionId();
                if(delayTrendMap.get(junctionId)<averageDelay){
                    delayTrendMap.put(junctionId, averageDelay);
                }
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_delay");
    }
    /**
     * 设置车辆通过不同交通灯的停车次数
     */
    public static void setStopTrend(Map<Integer,TrafficLight> trafficLightMap, List<Junction> junctionList, RedisTemplate<String,Object> redisTemplate){
        for(Junction junction: junctionList){
            stopTrendMap.put(junction.getJunctionId(), 0d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String key, double stopTimes) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                int junctionId = trafficLight.getJunctionId();
                if(stopTrendMap.get(junctionId)<stopTimes){
                    stopTrendMap.put(junctionId, stopTimes);
                }
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_stop");
    }
    /**
     * 设置过去一段时间不同交通灯的车流量
     */
    public static void setFlowTrend(Map<Integer,TrafficLight> trafficLightMap, RedisTemplate<String,Object> redisTemplate){
        for(int trafficLightId: trafficLightMap.keySet()) {
            String key = "traffic_light_flow"+trafficLightId;
            Object value = redisTemplate.opsForValue().get(key);
            int flow = 0;
            if(value!=null){
                flow = (int)Math.ceil(Double.parseDouble(String.valueOf(value)));
            }
            TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
            trafficLight.setFlowTrend(flow);
        }
    }
    /**
     * 设置整个地图的变化趋势
     * @param redisTemplate
     */
    public static void setWholeTrend(RedisTemplate<String, Object> redisTemplate){
        //保存平均延误时间变化趋势
        trafficTrendMap.put("averageDelayTrend", getAverageDelay(redisTemplate));
        //保存总拥堵里程变化趋势
        trafficTrendMap.put("congestionMileageTrend", getCongestionMileage(redisTemplate));
        //保存总的平均速度变化趋势
        trafficTrendMap.put("averageSpeedTrend", getAverageSpeed(redisTemplate));
    }

    /**
     * 获取平均延迟时间
     * @param redisTemplate
     * @return
     */
    public static double getAverageDelay(RedisTemplate<String,Object> redisTemplate){
        Object value = redisTemplate.opsForValue().get("average_delay");
        double averageDelay = 0d;
        if(value!=null){
            averageDelay = Double.parseDouble(String.valueOf(value))/60;
        }
        return Math.ceil(averageDelay * 100)/100;
    }

    /**
     * 获取总拥堵里程
     * @param redisTemplate
     * @return
     */
    public static double getCongestionMileage(RedisTemplate<String,Object> redisTemplate){
        Object value = redisTemplate.opsForValue().get("congestion_mileage");
        double congestionMileage = 0d;
        if(value!=null){
            congestionMileage = Double.parseDouble(String.valueOf(value))/60;
        }
        return Math.ceil(congestionMileage * 100)/100;
    }
    /**
     * 获取车辆平均速度
     * @param redisTemplate
     * @return
     */
    public static double getAverageSpeed(RedisTemplate<String,Object> redisTemplate){
        Object value = redisTemplate.opsForValue().get("average_speed");
        double averageSpeed = 0d;
        if(value!=null){
            averageSpeed = Double.parseDouble(String.valueOf(value));
        }
        return Math.ceil(averageSpeed*360)/100;
    }
}