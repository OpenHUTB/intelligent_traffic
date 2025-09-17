package com.ruoyi.simulation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.simulation.domain.*;
import com.ruoyi.simulation.listener.ProcessCommandListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 指数获取工具类
 */
public class IndirectionUtil {
    //每天定时获取历史平均速度
    public static final Map<String,Double> speedMap = new HashMap<>();
    public static final Map<Integer, String> plateMap = new HashMap<>();

    /**
     * 每天零点初始化speedMap
     * @param speedList
     */
    public static void resetSpeedMap(List<Speed> speedList){
        speedMap.clear();
        for(Speed speed: speedList){
            speedMap.put(speed.getRoadId(), speed.getValue());
        }
    }
    /**
     * 获取车辆平均车速
     * @return
     */
    public static void setAverageSpeed(RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        double averageSpeed = IndirectionTrendUtil.getAverageSpeed(redisTemplate);
        indirection.setAverageSpeed(averageSpeed);
        double averageSpeedRate = IndirectionTrendUtil.getAverageSpeedRate(averageSpeed);
        indirection.setAverageSpeedRate(Math.ceil(averageSpeedRate*10000)/100);
    }

    /**
     * 设置交通指数与拥堵指数
     * @param indirection
     */
    public static void setSpeedIndirection(RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        double averageSpeed = IndirectionTrendUtil.getAverageSpeed(redisTemplate);
        if(speedMap.get("ALL")==null||speedMap.get("ALL")==0){
            speedMap.put("ALL",averageSpeed);
        }
        //设置交通指数
        double speedIndirection = 1d;
        if(speedMap.get("ALL")!=0){
            speedIndirection = averageSpeed/speedMap.get("ALL");
        }
        indirection.setSpeedIndirection(Math.ceil(speedIndirection*100)/100);
        //设置拥堵指数
        double congestionIndirection = 1d;
        if(averageSpeed!=0){
            congestionIndirection = speedMap.get("ALL")/averageSpeed;
        }
        indirection.setCongestionIndirection(Math.ceil(congestionIndirection*100)/100);
    }

    /**
     * 设置拥堵警情数据
     * @param indirectionCollection 交通指数
     */
    public static void setAlarmIndirection(List<Road> roadList, TrafficIndirectionCollection indirectionCollection){
        List<Road> tempList = new ArrayList<>(roadList);
        if(tempList.isEmpty()){
            return;
        }
        Collections.sort(tempList);
        tempList.remove(0);
        tempList.remove(0);
        indirectionCollection.setAlarmIndirection(tempList);
    }
    /**
     * 设置总的平均延误时间
     */
    public static void setAverageDelay(RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        //设置总的平均延误时间
        double averageDelay = IndirectionTrendUtil.getAverageDelay(redisTemplate);
        indirection.setAverageDelay(averageDelay);
        //设置总的平均延误时间变化趋势
        double averageDelayRate = IndirectionTrendUtil.getAverageDelayRate(averageDelay);
        indirection.setAverageDelayRate(averageDelayRate);
    }


    /**
     * 设置总拥堵里程及总拥堵里程变化趋势
     * @param redisTemplate
     * @param indirection
     */
    public static void setCongestionMileage(RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        //设置拥堵里程
        Object value = redisTemplate.opsForValue().get("congestion_mileage");
        double congestionMileage = 0d;
        if(value!=null){
            congestionMileage = Double.parseDouble(String.valueOf(value));
        }
        indirection.setCongestionMileage(Math.ceil(congestionMileage * 100)/100);
        //设置总的拥堵里程变化趋势
        Double trend = IndirectionTrendUtil.trafficTrendMap.get("congestionMileageTrend");
        double congestionMileageRate = 0d;
        if(trend!=null && trend!=0){
            congestionMileageRate = (congestionMileage-trend)/trend;
        }
        indirection.setCongestionMileageRate(Math.ceil(congestionMileageRate * 10000)/100);
    }

    /**
     * 设置实时的服务车次
     * @param indirection 交通指数
     */
    public static void setServicedVehicle(RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        Object value = redisTemplate.opsForValue().get("serviced_vehicle");
        double servicedVehicle = 0;
        if(value!=null){
            servicedVehicle = Double.parseDouble(String.valueOf(value)) /10000;
        }
        indirection.setServicedVehicle(servicedVehicle);
    }
    /**
     * 设置实时的总车量
     * @param indirection 交通指数
     */
    public static void setTotalVehicle(RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        Object value = redisTemplate.opsForValue().get("total_vehicle");
        double totalVehicle = 0;
        if(value!=null){
            totalVehicle = Double.parseDouble(String.valueOf(value)) /10000;
        }
        indirection.setTotalVehicle(totalVehicle);
    }
    /**
     * 设置道路的平均速度和拥堵指数
     * @param indirection 交通指数
     */
    public static void setRoadIndirection(List<Road> roadList, RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection) {
        if(roadList.isEmpty()){
            return;
        }
        Map<String, Road> roadMap = new HashMap<>();
        for (Road road : roadList) {
            roadMap.put(String.valueOf(road.getRoadId()), road);
            //初始化所有的道路平均速度
            road.setAverageSpeed(0d);
            //初始化所有的道路拥堵指数
            road.setCongestionIndirection(1d);
            //初始化拥堵趋势
            road.setCongestionIndirectionRate(1d);
        }
        new ParseRespectiveIndirection() {
            @Override
            public void setRespectiveIndirection(String roadId, double averageSpeed) {
                averageSpeed = Math.ceil(averageSpeed * 360)/100;
                //保存不同道路的实时平均速度
                IndirectionUtil.speedMap.putIfAbsent(roadId, averageSpeed);
                //记录不同道路的实时拥堵指数
                Road road = roadMap.get(roadId);
                road.setAverageSpeed(averageSpeed);
                double congestionIndirection = 1d;
                if(averageSpeed!=0){
                    congestionIndirection = IndirectionUtil.speedMap.get(roadId) / averageSpeed;
                }
                road.setCongestionIndirection(Math.ceil(congestionIndirection*100)/100);
                double trend = IndirectionTrendUtil.indirectionTrendMap.get(roadId);
                //计算拥堵指数趋势变化
                double indirectionTrendRate = 0d;
                if (trend != 0) {
                    indirectionTrendRate = (congestionIndirection - trend) / trend;
                }
                while(indirectionTrendRate>3){
                    indirectionTrendRate/=2;
                }
                road.setCongestionIndirectionRate(Math.ceil(indirectionTrendRate*10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate,"road_average_speed");
        indirection.setRoadIndirection(roadList);
    }
    /**
     * 设置不同红绿灯的停车次数
     * @return 红绿灯信息
     */
    public static void getTrafficLightIndirection(Map<Integer, TrafficLight> trafficLightMap, RedisTemplate<String,Object> redisTemplate){
        //将红绿灯信息添加到对应的路口下
        for(TrafficLight trafficLight: trafficLightMap.values()) {
            //初始化红绿灯对应的车辆数量
            trafficLight.setWaitVehicle(0);
        }
        //获取每个红绿灯对应的车辆数量
        new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String key, double waitVehicle) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    return;
                }
                trafficLight.setWaitVehicle((int)waitVehicle);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_vehicle_count");
    }
    /**
     * 设置不同路口的拥堵里程
     * @param indirection 交通指数
     */
    public static void setJunctionIndirection(Map<Integer, TrafficLight> trafficLightMap, List<Junction> junctionList, RedisTemplate<String,Object> redisTemplate, TrafficIndirectionCollection indirection){
        if(junctionList.isEmpty()){
            return;
        }
        //初始化路口信息
        Map<Integer, Junction> junctionMap = new HashMap<>();
        for(Junction junction: junctionList){
            junctionMap.put(junction.getJunctionId(), junction);
        }
        JunctionIndirectionUtil.setCongestionMileage(trafficLightMap, junctionMap, redisTemplate);
        JunctionIndirectionUtil.setAverageDelay(trafficLightMap, junctionMap, redisTemplate);
        JunctionIndirectionUtil.setStopTimes(trafficLightMap, junctionMap, redisTemplate);
        indirection.setJunctionIndirection(junctionList);
        //设置当前所在路口
        indirection.setCurrentJunction(junctionMap.get(ProcessCommandListener.junctionId));
    }
	/**
     * 设置告警信息
     * @param redisTemplate
     * @return
     */
    public static void setAlarming(List<String> plateList, RedisTemplate<String, Object> redisTemplate, TrafficIndirectionCollection indirection){
        List<Alarming> alarmingList = null;
        Object redisValue = redisTemplate.opsForValue().get("abnormal_vehicle_list");
        if(redisValue!=null) {
            alarmingList = com.alibaba.fastjson2.JSON.parseArray(String.valueOf(redisValue),Alarming.class);
            for(Alarming alarming: alarmingList){
                alarming.setTime(LocalDateTime.now());
                String plate = plateMap.get(alarming.getVehicleId());
                if(StringUtils.isBlank(plate)&&!plateList.isEmpty()){
                    plate = plateList.remove(0);
                    plateMap.put(alarming.getVehicleId(), plate);
                }
                alarming.setPlate(plate);
                double speed = alarming.getSpeed();
                alarming.setSpeed(Math.floor(speed * 100)/100);
                if(alarming.getType()== Alarming.AlarmingType.LOWER_SPEED){
                    alarming.setLevel(Alarming.AlarmingLevel.MINOR);
                }else if(alarming.getType() == Alarming.AlarmingType.ABNORMAL_STOP){
                    alarming.setLevel(Alarming.AlarmingLevel.GENERAL);
                }else if(alarming.getType() == Alarming.AlarmingType.REVERSE){
                    alarming.setLevel(Alarming.AlarmingLevel.SERIOUS);
                }else if(alarming.getType() == Alarming.AlarmingType.WRONG_WAY){
                    alarming.setLevel(Alarming.AlarmingLevel.FATAL);
                }
            }
        }
        indirection.setAlarmingList(alarmingList);
    }
}
