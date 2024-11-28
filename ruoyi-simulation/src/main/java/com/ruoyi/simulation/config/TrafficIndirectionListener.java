package com.ruoyi.simulation.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.JunctionMapper;
import com.ruoyi.simulation.dao.RoadMapper;
import com.ruoyi.simulation.dao.SpeedMapper;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.Road;
import com.ruoyi.simulation.domain.Speed;
import com.ruoyi.simulation.util.LoggerUtil;
import com.ruoyi.simulation.util.ProcessOperationUtil;
import com.ruoyi.simulation.util.SendResponseUtil;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 获取交通参数的监听器
 */
@Component
public class TrafficIndirectionListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
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
    public JunctionMapper junctionMapper;
    //每天定时获取历史平均速度
    private final Map<String,Double> speedMap = new HashMap<>();
    //一天内的平均速度
    private final Map<String, List<Double>> daySpeedMap = new HashMap<>();
    private final Map<String, Double> hourSpeedMap = new HashMap<>();
    private final Map<String, Double> indirectionTrendMap = new HashMap<>();
    private final Map<Integer, Double> mileageTrendMap = new HashMap<>();
    private final Map<String, Double> trafficTrendMap = new HashMap<>();
    private int acquiredCount = 0;
    private List<Road> roadList = new ArrayList<>();
    private List<Junction> junctionList = new ArrayList<>();
    @Override
    public void onApplicationEvent(ApplicationStartedEvent e) {
        this.getMapBasicData();
        this.killIndirectionScript();
        this.executionIndirectionScript();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getRedisIndirection();
            }
        }).start();
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
        ProcessOperationUtil.killProcess(processIdList);
    }
    /**
     * 执行获取交通指数的脚本
     */
    private void executionIndirectionScript(){
        //获得车辆平均速度
        this.callUE4Engine.getTrafficIndirection("average_speed.py");
        //获得不同道路的车辆平均速度
        this.callUE4Engine.getTrafficIndirection("road_average_speed.py");
        //获得平均延误时间
        this.callUE4Engine.getTrafficIndirection("average_delay.py");
        //获得总拥堵里程
        this.callUE4Engine.getTrafficIndirection("congestion_mileage.py");
        //获得不同道路的拥堵里程
        this.callUE4Engine.getTrafficIndirection("junction_congestion_mileage.py");
        //获得服务车次
        this.callUE4Engine.getTrafficIndirection("serviced_vehicle.py");
        //获得路网车辆
        this.callUE4Engine.getTrafficIndirection("total_vehicle.py");
    }

    /**
     * 获取道路及路口的地图基础数据信息
     */
    private void getMapBasicData(){
        this.roadList = this.roadMapper.selectList(new QueryWrapper<>());
        Map<String,Integer> roadMap = new HashMap<>();
        //设置carla中roadId与道路主键之间的对应关系
        for(Road road: this.roadList){
            List<String> relatedList = Arrays.asList(road.getRelatedRoadId().split(","));
            for(String related: relatedList){
                roadMap.put(related, road.getRoadId());
            }
            //初始化拥堵指数趋势变化初始值
            this.indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
            //初始化不同道路的平均车速
            this.hourSpeedMap.put(String.valueOf(road.getRoadId()),0d);
        }
        this.redisTemplate.opsForValue().set("road_map", JSON.toJSONString(roadMap));
        this.junctionList = this.junctionMapper.selectList(new QueryWrapper<>());
        for(Junction junction: this.junctionList){
            //初始化拥堵里程趋势变化初始值
            this.mileageTrendMap.put(junction.getTrafficLightId(), 0d);
        }
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
            }
            Speed speed = new Speed();
            speed.setValue(averageSpeed/count);
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
     * 读取redis中的交通指数
     */
    public void getRedisIndirection(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LoggerUtil.printLoggerStace(e);
            }
            Map<String,Object> indirectionMap = new HashMap<>();
            //获取实时的平均车速
            double averageSpeed = this.getAverageSpeed();
            indirectionMap.put("averageSpeed", averageSpeed);
            //设置平均车速变化趋势
            this.setAverageSpeedTrend(averageSpeed,indirectionMap);
            //设置实时交通指数
            this.setTrafficIndirection(averageSpeed, indirectionMap);
            //设置不同道路的实时平均车速及拥堵指数
            this.setRoadIndirection(indirectionMap);
            //设置平均延误时间
            double averageDelay = this.getAverageDelay();
            indirectionMap.put("averageDelay", averageDelay);
            //设置平均延误时间变化趋势
            this.setAverageDelayTrend(averageDelay,indirectionMap);
            //设置总拥堵里程
            double congestionMileage = this.getCongestionMileage();
            indirectionMap.put("congestionMileage", congestionMileage);
            //设置总拥堵里程变化趋势
            this.setCongestionMileageTrend(congestionMileage, indirectionMap);
            //获取不同路口的拥堵里程
            this.setJunctionIndirection(indirectionMap);
            //获取服务车次
            this.setServicedVehicle(indirectionMap);
            //获取路网车辆
            this.setTotalVehicle(indirectionMap);
            for(String sessionId: WebSocketServer.webSocketMap.keySet()){
                SendResponseUtil.sendIndirectionResponse(indirectionMap, sessionId);
            }
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
        return averageSpeed;
    }

    /**
     * 设置车辆平均速度变化趋势
     * @param averageSpeed
     * @param indirectionMap
     */
    public void setAverageSpeedTrend(double averageSpeed, Map<String,Object> indirectionMap){
        double trend = this.trafficTrendMap.get("averageSpeedTrend");
        //计算平均车速指数趋势变化
        double averageSpeedTrend = 1d;
        if(trend!=0){
            averageSpeedTrend = (averageSpeed-trend)/trend;
        }
        indirectionMap.put("averageSpeedTrend", averageSpeedTrend);
    }
    /**
     * 设置交通指数
     * @param indirectionMap
     */
    public void setTrafficIndirection(double averageSpeed, Map<String,Object> indirectionMap){
        this.speedMap.putIfAbsent("ALL",averageSpeed);
        double trafficIndirection = 1d;
        if(this.speedMap.get("ALL")!=0){
            trafficIndirection = averageSpeed/this.speedMap.get("ALL");
        }
        indirectionMap.put("trafficIndirection", trafficIndirection);
    }

    /**
     * 设置道路的平均速度和拥堵指数
     * @param indirectionMap
     */
    private void setRoadIndirection(Map<String,Object> indirectionMap){
        Map<String, Road> roadMap = new HashMap<>();
        for(Road road: this.roadList){
            roadMap.put(String.valueOf(road.getRoadId()), road);
            //初始化所有的道路平均速度
            road.setAverageSpeed(0d);
            //初始化所有的道路拥堵指数
            road.setCongestionIndirection(1d);
            //初始化拥堵趋势
            road.setCongestionIndirectionTrend(0d);
        }
        new ParseRoadIndirection(){
            @Override
            public void setRoadAverageSpeed(String roadId, double averageSpeed) {
                //保存不同道路的实时平均速度
                speedMap.putIfAbsent(roadId, averageSpeed);
                //记录不同道路的实时拥堵指数
                Road road = roadMap.get(roadId);
                road.setAverageSpeed(averageSpeed);
                double congestionIndirection = speedMap.get(roadId)/averageSpeed;
                road.setCongestionIndirection(congestionIndirection);
                double trend = indirectionTrendMap.get(roadId);
                //计算拥堵指数趋势变化
                double IndirectionTrendRate = 1d;
                if(trend!=1){
                    IndirectionTrendRate = (congestionIndirection-trend)/trend;
                }
                road.setCongestionIndirectionTrend(IndirectionTrendRate);
            }
        }.getRoadAverageSpeed();
        indirectionMap.put("roadIndirection", this.roadList);
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
        return averageDelay;
    }
    /**
     * 设置总的平均延误时间变化趋势
     * @param averageDelay
     * @param indirectionMap
     */
    public void setAverageDelayTrend(double averageDelay, Map<String,Object> indirectionMap){
        double trend = this.trafficTrendMap.get("averageDelayTrend");
        //计算平均车速指数趋势变化
        double averageDelayTrend = 1d;
        if(trend!=0){
            averageDelayTrend = (averageDelay-trend)/trend;
        }
        indirectionMap.put("averageDelayTrend", averageDelayTrend);
    }
    /**
     * 设置总拥堵里程
     */
    public double getCongestionMileage(){
        Object value = this.redisTemplate.opsForValue().get("congestion_mileage");
        //获取总拥堵里程
        double congestionMileage = 0d;
        if(value!=null){
            congestionMileage = Double.parseDouble(String.valueOf(value));
        }
        return congestionMileage;
    }
    /**
     * 设置总的平均延误时间变化趋势
     * @param congestionMileage
     * @param indirectionMap
     */
    public void setCongestionMileageTrend(double congestionMileage, Map<String,Object> indirectionMap){
        double trend = this.trafficTrendMap.get("congestionMileageTrend");
        //计算平均车速指数趋势变化
        double congestionMileageTrend = 1d;
        if(trend!=0){
            congestionMileageTrend = (congestionMileage-trend)/trend;
        }
        indirectionMap.put("congestionMileageTrend", congestionMileageTrend);
    }
    /**
     * 设置不同路口的拥堵里程
     * @param indirectionMap
     */
    public void setJunctionIndirection(Map<String,Object> indirectionMap){
        Map<Integer, Junction> junctionMap = new HashMap<>();
        for(Junction junction: this.junctionList){
            junctionMap.put(junction.getTrafficLightId(), junction);
            //初始化所有的道路拥堵里程
            junction.setCongestionMileage(0d);
            //初始化拥堵里程趋势变化
            junction.setCongestionMileageTrend(0d);
        }
        //保存不同路口拥堵里程数据
        new ParseJunctionIndirection(){
            @Override
            protected void setJunctionCongestionMileage(int trafficLightId, double congestionMileage) {
                Junction junction = junctionMap.get(trafficLightId);
                //设置拥堵里程
                junction.setCongestionMileage(congestionMileage);
                double trend = mileageTrendMap.get(trafficLightId);
                //计算拥堵里程趋势变化
                double trendRate = 1d;
                if(trend!=0){
                    trendRate = (congestionMileage-trend)/trend;
                }
                junction.setCongestionMileageTrend(trendRate);
            }
        }.getJunctionCongestionMileage();
        indirectionMap.put("junctionIndirection", junctionList);
    }
    /**
     * 每隔5分钟获取一次趋势变化
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    private void setTrafficTrend(){
        for(Road road: this.roadList){
            this.indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
        }
        for(Junction junction: this.junctionList){
            this.mileageTrendMap.put(junction.getTrafficLightId(), 0d);
        }
        //保存不同道路的拥堵指数变化趋势
        new ParseRoadIndirection(){
            @Override
            public void setRoadAverageSpeed(String roadId, double averageSpeed) {
                //保存不同道路的实时拥堵指数
                speedMap.putIfAbsent(roadId, averageSpeed);
                double congestionIndirection = speedMap.get(roadId)/averageSpeed;
                indirectionTrendMap.put(roadId, congestionIndirection);
            }
        }.getRoadAverageSpeed();

        //保存不同路口的拥堵里程变化趋势
        new ParseJunctionIndirection(){
            @Override
            public void setJunctionCongestionMileage(int trafficLightId, double congestionMileage) {
                mileageTrendMap.put(trafficLightId, congestionMileage);
            }
        }.getJunctionCongestionMileage();
        //保存平均延误时间变化趋势
        trafficTrendMap.put("averageDelayTrend", this.getAverageDelay());
        //保存总拥堵里程变化趋势
        trafficTrendMap.put("congestionMileageTrend", this.getCongestionMileage());
        //保存总的平均速度变化趋势
        trafficTrendMap.put("averageSpeedTrend", this.getAverageSpeed());
    }

    /**
     * 设置实时的服务车次
     * @param indirectionMap
     */
    public void setServicedVehicle(Map<String,Object> indirectionMap){
        Object value = this.redisTemplate.opsForValue().get("serviced_vehicle");
        double servicedVehicle = 0d;
        if(value!=null){
            servicedVehicle = Double.parseDouble(String.valueOf(value));
        }
        indirectionMap.put("servicedVehicle", servicedVehicle);
    }

    /**
     * 设置实时的总车量
     * @param indirectionMap
     */
    public void setTotalVehicle(Map<String,Object> indirectionMap){
        Object value = this.redisTemplate.opsForValue().get("total_vehicle");
        double totalVehicle = 0d;
        if(value!=null){
            totalVehicle = Double.parseDouble(String.valueOf(value));
        }
        indirectionMap.put("totalVehicle", totalVehicle);
    }
    /**
     * 采集平均速度作为历史数据
     * 每5秒钟采集一次平均速度数据
     */
    @Scheduled(fixedRate = 5000)
    private void getHourSpeedMap(){
        new ParseRoadIndirection(){
            @Override
            public void setRoadAverageSpeed(String roadId, double averageSpeed) {
                hourSpeedMap.put(roadId, hourSpeedMap.get(roadId) + averageSpeed);
            }
        }.getRoadAverageSpeed();
        double averageSpeed = this.getAverageSpeed();
        if(averageSpeed!=0){
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
                List<Double> valueList = this.daySpeedMap.get(roadId);
                valueList.add(value/this.acquiredCount);
            }
            //将每个小时的获取速度的次数重新设置为0
            this.acquiredCount = 0;
            this.hourSpeedMap.put(roadId, 0d);
        }
    }
    /**
     * 内部类，用来解析不同道路的平均速度数据
     */
    private abstract class ParseRoadIndirection {
        /**
         * 获取redis中的不同车道的实时平均速度
         */
        public void getRoadAverageSpeed(){
            Object value = redisTemplate.opsForValue().get("road_average_speed");
            if(value!=null) {
                JSONObject jsonObject = JSON.parseObject(String.valueOf(value));
                for (String roadId : jsonObject.keySet()) {
                    //获取不同道路的实时平均速度
                    double averageSpeed = Double.parseDouble(String.valueOf(jsonObject.get(roadId)));
                    if (averageSpeed != 0) {
                        this.setRoadAverageSpeed(roadId, averageSpeed);
                    }
                }
            }
        }
        /**
         * 设置实时平均车速
         * @param roadId
         * @param averageSpeed
         */
        protected abstract void setRoadAverageSpeed(String roadId, double averageSpeed);
    }
    /**
     * 内部类，用来解析不同路口的拥堵里程
     */
    private abstract class ParseJunctionIndirection {
        /**
         * 获取redis中的不同路口的拥堵里程数据
         */
        public void getJunctionCongestionMileage(){
            Object value = redisTemplate.opsForValue().get("road_congestion_mileage");
            if(value!=null) {
                JSONObject jsonObject = JSON.parseObject(String.valueOf(value));
                for (String trafficLightId : jsonObject.keySet()) {
                    //获取不同路口的拥堵里程
                    double congestionMileage = Double.parseDouble(String.valueOf(jsonObject.get(trafficLightId)));
                    this.setJunctionCongestionMileage(Integer.parseInt(trafficLightId), congestionMileage);
                }
            }
        }
        /**
         * 设置实时拥堵里程数据
         * @param trafficLightId
         * @param congestionMileage
         */
        protected abstract void setJunctionCongestionMileage(int trafficLightId, double congestionMileage);
    }
}
