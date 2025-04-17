package com.ruoyi.simulation.listener;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.JunctionMapper;
import com.ruoyi.simulation.dao.RoadMapper;
import com.ruoyi.simulation.dao.SpeedMapper;
import com.ruoyi.simulation.dao.TrafficLightMapper;
import com.ruoyi.simulation.domain.*;
import com.ruoyi.simulation.util.*;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 获取交通参数的监听器
 */
@Component
public class TrafficIndirectionListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(TrafficIndirectionListener.class);
    public static TrafficIndirectionCollection indirectionCollection;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    private Environment environment;
    @Resource
    public RedisTemplate<String,Object> redisTemplate;
    @Resource
    public SpeedMapper speedMapper;
    @Resource
    public RoadMapper roadMapper;
    @Resource
    public TrafficLightMapper trafficLightMapper;
    @Resource
    public JunctionMapper junctionMapper;
    //每天定时获取历史平均速度
    private final Map<String,Double> speedMap = new HashMap<>();
    //一天内的平均速度
    private final Map<String, List<Double>> daySpeedMap = new HashMap<>();
    private final Map<String, Double> hourSpeedMap = new HashMap<>();
    private final Map<String, Double> indirectionTrendMap = new HashMap<>();
    private final Map<Integer, Double> mileageTrendMap = new HashMap<>();
    private final Map<Integer, Double> delayTrendMap = new HashMap<>();
    private final Map<Integer, Double> stopTrendMap = new HashMap<>();
    private final Map<String, Double> trafficTrendMap = new HashMap<>();
    private int acquiredCount = 0;
    private List<Road> roadList = new ArrayList<>();
    public static List<Junction> junctionList = new ArrayList<>();
    public static List<TrafficLight> trafficLightList = new ArrayList<>();
    @Override
    public void contextInitialized(ServletContextEvent e){
        this.getMapBasicData();
        this.killIndirectionScript();
        this.executionIndirectionScript();
        this.callUE4Engine.executeExample("walker_cross_road_test.py");
    }
    /**
     * 杀死之前已经执行的交通指数脚本进程
     */
    private void killIndirectionScript(){
        //获取python.exe安装路径
        String interpreterLocation = this.environment.getProperty("simulation.indirection.interpreterLocation");
        //获取python代码文件在服务器中的绝对路径
        String scriptDirectory = this.environment.getProperty("simulation.indirection.scriptDirectory");
        Map<String, List<Integer>> processIdMap = ProcessOperationUtil.getProcessIdMap(interpreterLocation, scriptDirectory);
        List<Integer> processIdList = new ArrayList<>();
        for(String scriptName: processIdMap.keySet()){
            processIdList.addAll(processIdMap.get(scriptName));
        }
        ProcessOperationUtil.killProcessList(processIdList);
    }
    /**
     * 执行获取交通指数的脚本
     */
    private void executionIndirectionScript(){
        //获得总的平均车速以及不同道路的平均车速
        this.callUE4Engine.getTrafficIndirection("road_average_speed.py");
        //获得车辆通过不同红绿灯的延迟时间
        this.callUE4Engine.getTrafficIndirection("traffic_light_average_delay.py");
        //获得车辆通过不同红绿灯的停车次数
        this.callUE4Engine.getTrafficIndirection("traffic_light_average_stop.py");
        //获得总的拥堵里程以及不同路口的拥堵里程
        this.callUE4Engine.getTrafficIndirection("traffic_light_vehicle.py");
        //获得服务车次及获得路网车辆
        this.callUE4Engine.getTrafficIndirection("vehicle_count_statistics.py");
    }

    /**
     * 获取道路及路口的地图基础数据信息
     */
    private void getMapBasicData(){
        this.roadList = this.roadMapper.selectList(new QueryWrapper<>());
        Map<String,Integer> roadMap = new HashMap<>();
        //设置carla中roadId与道路主键之间的对应关系
        for(Road road: this.roadList){
            String[] relatedList = road.getRelatedRoadId().split(",");
            for(String related: relatedList){
                roadMap.put(related, road.getRoadId());
            }
            //初始化拥堵指数趋势变化初始值
            this.indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
            //初始化不同道路的平均车速
            this.hourSpeedMap.put(String.valueOf(road.getRoadId()),0d);
        }
        this.redisTemplate.opsForValue().set("road_map", JSON.toJSONString(roadMap));
        //获取所有路口信息
        junctionList = this.junctionMapper.selectList(new QueryWrapper<>());
        //获取所有交通灯信息
        trafficLightList = this.trafficLightMapper.selectList(new QueryWrapper<>());
        //初始化平均延误时间变化趋势初始值
        trafficTrendMap.put("averageDelayTrend", 0d);
        //初始化总拥堵里程变化趋势初始值
        trafficTrendMap.put("congestionMileageTrend", 0d);
        //初始化总的平均速度变化趋势初始值
        trafficTrendMap.put("averageSpeedTrend", 0d);
    }
    /**
     * 每天零点整执行读取数据和写入数据的操作
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void getHistoryIndirection(){
        Date current = new Date();
        List<Speed> speedList = new ArrayList<>();
        for(String roadId: this.daySpeedMap.keySet()){
            List<Double> valueList = this.daySpeedMap.get(roadId);
            double averageSpeed = 0d;
            int count = 0;
            for(Double value: valueList){
                averageSpeed += value;
                count++;
            }
            Speed speed = new Speed();
            if(count!=0){
                speed.setValue(averageSpeed/count);
            }
            speed.setRoadId(roadId);
            speed.setCreateTime(current);
            speedList.add(speed);
        }
        if(!speedList.isEmpty()){
            this.speedMapper.addAverageSpeedList(speedList);
        }
        this.speedMap.clear();
        this.daySpeedMap.clear();
        //获取不同道路的历史平均车速
        speedList = this.speedMapper.getAverageSpeedList();
        for(Speed speed: speedList){
            speedMap.put(speed.getRoadId(), speed.getValue());
        }
    }
    /**
     * 每秒钟读取一次redis中的交通指数
     */
    @Scheduled(fixedRate = 1000)
    public void getRedisIndirection(){
        try {
            TrafficIndirectionCollection indirection = new TrafficIndirectionCollection();
            //获取实时的平均车速
            double averageSpeed = this.getAverageSpeed();
            indirection.setAverageSpeed(averageSpeed);
            //设置平均车速变化率
            this.setAverageSpeedRate(averageSpeed, indirection);
            //设置实时交通指数
            this.setSpeedIndirection(averageSpeed, indirection);
            //设置不同道路的实时平均车速及拥堵指数
            this.setRoadIndirection(indirection);
            //设置拥堵警情数据
            this.setAlarmIndirection(indirection);
            //设置平均延误时间
            double averageDelay = this.getAverageDelay();
            indirection.setAverageDelay(averageDelay);
            //设置平均延误时间变化情况（分钟）
            this.setAverageDelayChange(averageDelay, indirection);
            //获取不同红绿灯对应的拥堵里程
            Map<Integer,TrafficLight> trafficLightMap = this.getTrafficLightIndirection();
            //获取不同路口的拥堵里程
            this.setJunctionIndirection(trafficLightMap, indirection);
            //设置总拥堵里程
            double congestionMileage = this.getCongestionMileage();
            indirection.setCongestionMileage(congestionMileage);
            //设置总拥堵里程变化趋势
            this.setCongestionMileageRate(congestionMileage, indirection);
            //获取服务车次（万次）
            this.setServicedVehicle(indirection);
            //获取路网车辆（万辆）
            this.setTotalVehicle(indirection);
            for(String sessionId: WebSocketServer.webSocketMap.keySet()){
                SendResponseUtil.sendJSONResponse(StreamSet.Signal.TRAFFIC_INDIRECTION, indirection, sessionId);
            }
            //将交通指数数据传递给ProcessCommandListener
            indirectionCollection = indirection;
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }

    /**
     * 获取车辆平均车速
     * @return
     */
    public double getAverageSpeed(){
        Object value = this.redisTemplate.opsForValue().get("average_speed");
        double averageSpeed = 0d;
        if(value!=null){
            averageSpeed = Double.parseDouble(String.valueOf(value));
        }
        return Math.ceil(averageSpeed*360)/100;
    }

    /**
     * 设置车辆平均速度变化趋势
     * @param averageSpeed 平均速度
     * @param indirection 交通指数
     */
    public void setAverageSpeedRate(double averageSpeed, TrafficIndirectionCollection indirection){
        Double trend = this.trafficTrendMap.get("averageSpeedTrend");
        //计算平均车速指数趋势变化
        double averageSpeedRate = 0d;
        if(trend!=null && trend!=0){
            averageSpeedRate = (averageSpeed-trend)/trend;
        }
        indirection.setAverageSpeedRate(Math.ceil(averageSpeedRate*10000)/100);
    }
    /**
     * 设置交通指数与拥堵指数
     * @param indirection
     */
    public void setSpeedIndirection(double averageSpeed, TrafficIndirectionCollection indirection){
        if(speedMap.get("ALL")==null||speedMap.get("ALL")==0){
            this.speedMap.put("ALL",averageSpeed);
        }
        //设置交通指数
        double speedIndirection = 1d;
        if(this.speedMap.get("ALL")!=0){
            speedIndirection = averageSpeed/this.speedMap.get("ALL");
        }
        indirection.setSpeedIndirection(Math.ceil(speedIndirection*100)/100);
        //设置拥堵指数
        double congestionIndirection = 1d;
        if(averageSpeed!=0){
            congestionIndirection = this.speedMap.get("ALL")/averageSpeed;
        }
        indirection.setCongestionIndirection(Math.ceil(congestionIndirection*100)/100);
    }

    /**
     * 设置道路的平均速度和拥堵指数
     * @param indirection 交通指数
     */
    private void setRoadIndirection(TrafficIndirectionCollection indirection) {
        if(this.roadList.isEmpty()){
            return;
        }
        Map<String, Road> roadMap = new HashMap<>();
        for (Road road : this.roadList) {
            roadMap.put(String.valueOf(road.getRoadId()), road);
            //初始化所有的道路平均速度
            road.setAverageSpeed(0d);
            //初始化所有的道路拥堵指数
            road.setCongestionIndirection(0d);
            //初始化拥堵趋势
            road.setCongestionIndirectionRate(0d);
        }
        new ParseRespectiveIndirection() {
            @Override
            public void setRespectiveIndirection(String roadId, double averageSpeed) {
                averageSpeed = Math.ceil(averageSpeed * 360)/100;
                //保存不同道路的实时平均速度
                speedMap.putIfAbsent(roadId, averageSpeed);
                //记录不同道路的实时拥堵指数
                Road road = roadMap.get(roadId);
                road.setAverageSpeed(averageSpeed);
                double congestionIndirection = 1d;
                if(averageSpeed!=0){
                    congestionIndirection = speedMap.get(roadId) / averageSpeed;
                }
                road.setCongestionIndirection(Math.ceil(congestionIndirection*100)/100);
                double trend = indirectionTrendMap.get(roadId);
                //计算拥堵指数趋势变化
                double IndirectionTrendRate = 0d;
                if (trend != 0) {
                    IndirectionTrendRate = (congestionIndirection - trend) / trend;
                }
                road.setCongestionIndirectionRate(Math.ceil(IndirectionTrendRate*10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate,"road_average_speed");
        indirection.setRoadIndirection(this.roadList);
    }
    /**
     * 设置拥堵警情数据
     * @param indirectionCollection 交通指数
     */
    public void setAlarmIndirection(TrafficIndirectionCollection indirectionCollection){
        List<Road> tempList = new ArrayList<>(this.roadList);
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
    public double getAverageDelay(){
        Object value = this.redisTemplate.opsForValue().get("average_delay");
        double averageDelay = 0d;
        if(value!=null){
            averageDelay = Double.parseDouble(String.valueOf(value));
        }
        return Math.ceil(averageDelay * 100)/100;
    }
    /**
     * 设置总的平均延误时间变化趋势
     * @param averageDelay 平均延误时间
     * @param indirection 交通指数
     */
    public void setAverageDelayChange(double averageDelay, TrafficIndirectionCollection indirection){
        Double trend = this.trafficTrendMap.get("averageDelayTrend");
        if(trend==null){
            trend = 0d;
        }
        //计算平均车速指数趋势变化(分钟)
        double averageDelayChange = Math.ceil((averageDelay - trend)/60 * 100)/100;
        indirection.setAverageDelayChange(averageDelayChange);
    }
    /**
     * 设置总拥堵里程
     */
    public double getCongestionMileage(){
        double congestionMileage = 0d;
        for(Junction junction: junctionList){
            congestionMileage += junction.getCongestionMileage();
        }
        return Math.ceil(congestionMileage * 100)/100;
    }
    /**
     * 设置总的拥堵里程变化趋势
     * @param congestionMileage 总拥堵里程
     * @param indirectionCollection 交通指数
     */
    public void setCongestionMileageRate(double congestionMileage, TrafficIndirectionCollection indirectionCollection){
        Double trend = this.trafficTrendMap.get("congestionMileageTrend");
        //计算平均车速指数趋势变化
        double congestionMileageRate = 0d;
        if(trend!=null && trend!=0){
            congestionMileageRate = (congestionMileage-trend)/trend;
        }
        indirectionCollection.setCongestionMileageRate(Math.ceil(congestionMileageRate * 10000)/100);
    }
    /**
     * 设置不同路口的拥堵里程
     * @param indirectionCollection 交通指数
     */
    public void setJunctionIndirection(Map<Integer,TrafficLight> trafficLightMap, TrafficIndirectionCollection indirectionCollection){
        if(junctionList.isEmpty()){
            return;
        }
        //初始化路口信息
        Map<Integer, Junction> junctionMap = new HashMap<>();
        for(Junction junction: junctionList){
            junctionMap.put(junction.getJunctionId(), junction);
            //初始化所有的道路拥堵里程
            junction.setCongestionMileage(0d);
            //初始化拥堵里程趋势变化
            junction.setCongestionMileageRate(0d);
            //初始化每个路口的交通灯列表，用来存储交通灯相关的指数信息
            junction.setTrafficLightList(new ArrayList<>());
        }
        //将红绿灯信息添加到对应的路口下
        for(TrafficLight trafficLight: trafficLightMap.values()){
            //初始化红绿灯对应的拥堵里程
            trafficLight.setCongestionMileage(0d);
            //初始化红绿灯对应的车辆数量
            trafficLight.setWaitVehicle(0);
            //将红绿灯信息添加到对应的路口下
            int junctionId = trafficLight.getJunctionId();
            Junction junction = junctionMap.get(junctionId);
            List<TrafficLight> trafficLightList = junction.getTrafficLightList();
            trafficLightList.add(trafficLight);
        }
        //根据每个红绿灯对应的拥堵里程信息来设置不同路口拥堵里程数据
        new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String key, double congestionMileage) {
                //设置红绿灯对应的拥堵里程信息
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    return;
                }
                trafficLight.setCongestionMileage(congestionMileage);
                //设置路口拥堵里程信息，如果某个红绿灯对应的拥堵里程大于路口拥堵里程，则其为该路口的实际最大拥堵里程
                int junctionId = trafficLight.getJunctionId();
                Junction junction = junctionMap.get(junctionId);
                if(trafficLight.getCongestionMileage()<=junction.getCongestionMileage()){
                    return;
                }
                junction.setCongestionMileage(trafficLight.getCongestionMileage());
                if(mileageTrendMap.get(junctionId)==null||mileageTrendMap.get(junctionId)==0d){
                    mileageTrendMap.put(junctionId, congestionMileage);
                }
                double mileageTrend = mileageTrendMap.get(junctionId);
                //计算拥堵里程趋势变化
                double congestionTrendRate = 0d;
                if(mileageTrend!=0){
                    congestionTrendRate = (congestionMileage-mileageTrend)/mileageTrend;
                }
                junction.setCongestionMileageRate(Math.ceil(congestionTrendRate * 10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate, "traffic_light_congestion_mileage");
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
        indirectionCollection.setJunctionIndirection(junctionList);
        //设置当前所在路口
        indirectionCollection.setCurrentJunction(junctionMap.get(ProcessCommandListener.junctionId));
    }
    /**
     * 设置不同红绿灯对应的交通指数
     * @return 红绿灯信息
     */
    public Map<Integer,TrafficLight> getTrafficLightIndirection(){
        Map<Integer,TrafficLight> trafficLightMap = new HashMap<>();
        if(trafficLightList.isEmpty()){
            return trafficLightMap;
        }
        for(TrafficLight trafficLight: trafficLightList){
            trafficLightMap.put(trafficLight.getTrafficLightId(), trafficLight);
            //初始化平均延迟时间
            trafficLight.setAverageDelay(0d);
            trafficLight.setAverageDelayRate(0d);
            //初始化平均等待次数
            trafficLight.setStopTimes(0d);
            trafficLight.setStopTimesRate(0d);
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
                trafficLight.setAverageDelay(averageDelay);
                if(delayTrendMap.get(trafficLightId)==null||delayTrendMap.get(trafficLightId)==0d){
                    delayTrendMap.put(trafficLightId, averageDelay);
                }
                double delayTrend = delayTrendMap.get(trafficLightId);
                //计算拥堵里程趋势变化
                double delayTrendRate = 0d;
                if(delayTrend!=0){
                    delayTrendRate = (averageDelay-delayTrend)/delayTrend;
                }
                trafficLight.setAverageDelayRate(Math.ceil(delayTrendRate * 10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_delay");
        //获取每个红绿灯对应的停车次数信息
        new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String key, double stopTimes) {
                int trafficLightId = Integer.parseInt(key);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                if(trafficLight==null){
                    return;
                }
                trafficLight.setStopTimes(stopTimes);
                if(stopTrendMap.get(trafficLightId)==null||stopTrendMap.get(trafficLightId)==0d){
                    stopTrendMap.put(trafficLightId, stopTimes);
                }
                double stopTrend = stopTrendMap.get(trafficLightId);
                //计算拥堵里程趋势变化
                double stopTrendRate = 0d;
                if(stopTrend!=0){
                    stopTrendRate = (stopTimes-stopTrend)/stopTrend;
                }
                trafficLight.setStopTimesRate(Math.ceil(stopTrendRate * 10000)/100);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_stop");
        return trafficLightMap;
    }

    /**
     * 每隔5分钟获取一次趋势变化
     */
    @Scheduled(cron = "0 * * * * ?")
    private void setTrafficTrend(){
        //保存不同道路的拥堵指数变化趋势
        this.setIndirectionTrend();
        //保存不同路口的拥堵里程变化趋势
        this.setCongestionMileageTrend();
        //保存不同红绿灯的交通指数变化趋势
        this.setDelayTrend();
        //设置车辆通过不同交通灯的停车次数
        this.setStopTrend();
        //设置不同红绿灯下的车流量
        this.setFlowTrend();
        //保存平均延误时间变化趋势
        trafficTrendMap.put("averageDelayTrend", this.getAverageDelay());
        //保存总拥堵里程变化趋势
        trafficTrendMap.put("congestionMileageTrend", this.getCongestionMileage());
        //保存总的平均速度变化趋势
        trafficTrendMap.put("averageSpeedTrend", this.getAverageSpeed());
    }

    /**
     * 保存不同道路的拥堵指数变化趋势
     */
    private void setIndirectionTrend(){
        for(Road road: this.roadList){
            this.indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String roadId, double averageSpeed) {
                averageSpeed = Math.ceil(averageSpeed * 360)/100;
                //保存不同道路的实时平均车速
                speedMap.putIfAbsent(roadId, averageSpeed);
                double congestionIndirection = speedMap.get(roadId)/averageSpeed;
                indirectionTrendMap.put(roadId, congestionIndirection);
            }
        }.getRespectiveIndirection(redisTemplate,"road_average_speed");
    }

    /**
     * 保存不同路口的拥堵里程变化趋势
     */
    private void setCongestionMileageTrend(){
        for(Junction junction: junctionList){
            this.mileageTrendMap.put(junction.getJunctionId(), 0d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String junctionId, double congestionMileage) {
                mileageTrendMap.put(Integer.parseInt(junctionId), congestionMileage);
            }
        }.getRespectiveIndirection(redisTemplate,"junction_congestion_mileage");
    }
    /**
     * 保存不同红绿灯的交通指数变化趋势
     */
    private void setDelayTrend(){
        for(TrafficLight trafficLight: trafficLightList){
            this.delayTrendMap.put(trafficLight.getTrafficLightId(), 0d);
            this.stopTrendMap.put(trafficLight.getTrafficLightId(), 0d);
        }
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String trafficLightId, double averageDelay) {
                delayTrendMap.put(Integer.parseInt(trafficLightId), averageDelay);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_delay");
    }
    /**
     * 设置车辆通过不同交通灯的停车次数
     */
    private void setStopTrend(){
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String trafficLightId, double stopTimes) {
                stopTrendMap.put(Integer.parseInt(trafficLightId), stopTimes);
            }
        }.getRespectiveIndirection(redisTemplate,"traffic_light_stop");
    }

    /**
     * 设置过去一段时间不同交通灯的车流量
     */
    private void setFlowTrend(){
        Map<Integer,TrafficLight> trafficLightMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList) {
            trafficLightMap.put(trafficLight.getTrafficLightId(), trafficLight);
        }
        new ParseRespectiveIndirection(){
            @Override
            protected void setRespectiveIndirection(String trafficLightId, double flow) {
                int flowTrend = (int)Math.round(flow*60);
                TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
                trafficLight.setFlowTrend(flowTrend);
            }
        }.getRespectiveIndirection(redisTemplate, "traffic_light_flow");
        redisTemplate.delete("traffic_light_flow");
    }
    /**
     * 设置实时的服务车次
     * @param indirectionCollection 交通指数
     */
    public void setServicedVehicle(TrafficIndirectionCollection indirectionCollection){
        Object value = this.redisTemplate.opsForValue().get("serviced_vehicle");
        double servicedVehicle = 0;
        if(value!=null){
            servicedVehicle = Double.parseDouble(String.valueOf(value)) /10000;
        }
        indirectionCollection.setServicedVehicle(servicedVehicle);
    }
    /**
     * 设置实时的总车量
     * @param indirectionCollection 交通指数
     */
    public void setTotalVehicle(TrafficIndirectionCollection indirectionCollection){
        Object value = this.redisTemplate.opsForValue().get("total_vehicle");
        double totalVehicle = 0;
        if(value!=null){
            totalVehicle = Double.parseDouble(String.valueOf(value)) /10000;
        }
        indirectionCollection.setTotalVehicle(totalVehicle);
    }
    /**
     * 采集平均速度作为历史数据
     * 每5秒钟采集一次平均速度数据
     */
    @Scheduled(fixedRate = 5000)
    private void getHourSpeedMap(){
        new ParseRespectiveIndirection(){
            @Override
            public void setRespectiveIndirection(String roadId, double averageSpeed) {
                averageSpeed = Math.ceil(averageSpeed * 360)/100;
                hourSpeedMap.putIfAbsent(roadId, 0d);
                hourSpeedMap.put(roadId, hourSpeedMap.get(roadId) + averageSpeed);
            }
        }.getRespectiveIndirection(redisTemplate,"road_average_speed");
        double averageSpeed = this.getAverageSpeed();
        if(averageSpeed!=0){
            hourSpeedMap.putIfAbsent("ALL", 0d);
            this.hourSpeedMap.put("ALL", this.hourSpeedMap.get("ALL") + averageSpeed);
        }
        //每采集一次平均速度，将采集次数加一
        this.acquiredCount ++;
    }

    /**
     * 每小时计算一次当前小时内的平均车速
     */
    @Scheduled(cron = "0 0 * * * ?")
    private void calculateHourSpeed(){
        for(String roadId: this.hourSpeedMap.keySet()){
            Double value = this.hourSpeedMap.get(roadId);
            if(this.acquiredCount!=0&&value!=null&&value!=0){
                this.daySpeedMap.putIfAbsent(roadId,new ArrayList<>());
                List<Double> valueList = this.daySpeedMap.get(roadId);
                valueList.add(value/this.acquiredCount);
            }
            //将每个小时的获取速度的次数重新设置为0
            this.acquiredCount = 0;
            this.hourSpeedMap.put(roadId, 0d);
        }
    }
}