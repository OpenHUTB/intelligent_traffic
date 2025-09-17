package com.ruoyi.simulation.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.*;
import com.ruoyi.simulation.domain.*;
import com.ruoyi.simulation.service.GreenWaveService;
import com.ruoyi.simulation.util.*;
import com.ruoyi.simulation.websocket.WebSocketServer;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 获取交通参数的监听器
 */
@Component
public class TrafficIndirectionListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(TrafficIndirectionListener.class);
    public static TrafficIndirectionCollection indirectionCollection = new TrafficIndirectionCollection();
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    private Environment environment;
    @Resource
    private GreenWaveService greenWaveService;
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
    @Resource
    public PredictionMapper predictionMapper;
    @Autowired
    private GreenWaveMapper greenWaveMapper;
    @Autowired
    private TrackMapper trackMapper;
    //一天内的平均速度
    private final Map<String, List<Double>> daySpeedMap = new HashMap<>();
    private int acquiredCount = 0;
    private List<Road> roadList = new ArrayList<>();
    public static List<Junction> junctionList = new ArrayList<>();
    private List<TrafficLight> trafficLightList = new ArrayList<>();
    private static List<String> plateList;
    private static final Map<String, Double> hourSpeedMap = new HashMap<>();
    @Override
    public void contextInitialized(ServletContextEvent e){
        this.getMapBasicData();
        this.killIndirectionScript();
        this.executionIndirectionScript();
        //this.callUE4Engine.executeExample("walker_cross_road_test.py");
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
        //获得所有交通指数
        this.callUE4Engine.executeIndirection("traffic_indirection.py");
    }

    /**
     * 获取道路及路口的地图基础数据信息
     */
    private void getMapBasicData(){
        this.roadList = this.roadMapper.selectList(new QueryWrapper<>());
        //获取所在地图
        String area = this.environment.getProperty("simulation.world.map");
        LambdaQueryWrapper<Junction> junctionWrapper = new LambdaQueryWrapper<>();
        junctionWrapper.eq(Junction::getArea, area);
        //获取所有路口信息
        junctionList = this.junctionMapper.selectList(junctionWrapper);
        //获取所有交通灯信息
        trafficLightList = this.trafficLightMapper.selectList(new QueryWrapper<>());
        IndirectionTrendUtil.initialBasicData(roadList, redisTemplate);
        //初始化不同道路的平均车速
        for(Road road: roadList){
            hourSpeedMap.put(String.valueOf(road.getRoadId()),0d);
        }
        //获取所有车牌信息
        plateList = this.trackMapper.getPlateList();
    }
    /**
     * 每秒钟读取一次redis中的交通指数
     */
    @Scheduled(fixedRate = 1000)
    public void getRedisIndirection(){
        try {
            //获取实时的平均车速
            IndirectionUtil.setAverageSpeed(this.redisTemplate, indirectionCollection);
            //设置实时交通指数
            IndirectionUtil.setSpeedIndirection(this.redisTemplate, indirectionCollection);
            //设置不同道路的实时平均车速及拥堵指数
            IndirectionUtil.setRoadIndirection(this.roadList, this.redisTemplate, indirectionCollection);
            //设置拥堵警情数据
            IndirectionUtil.setAlarmIndirection(this.roadList, indirectionCollection);
            //设置平均延误时间
            IndirectionUtil.setAverageDelay(this.redisTemplate, indirectionCollection);
            //设置总拥堵里程
            IndirectionUtil.setCongestionMileage(this.redisTemplate, indirectionCollection);
            //获取不同红绿灯对应的拥堵里程
            Map<Integer,TrafficLight> trafficLightMap = TrafficLightUtil.getTrafficLightMap(this.trafficLightList);
            IndirectionUtil.getTrafficLightIndirection(trafficLightMap, redisTemplate);
            //获取不同路口的拥堵里程
            IndirectionUtil.setJunctionIndirection(trafficLightMap, junctionList, this.redisTemplate, indirectionCollection);
            //获取服务车次（万次）
            IndirectionUtil.setServicedVehicle(this.redisTemplate, indirectionCollection);
            //获取路网车辆（万辆）
            IndirectionUtil.setTotalVehicle(this.redisTemplate, indirectionCollection);
            //设置告警信息
            IndirectionUtil.setAlarming(plateList, this.redisTemplate, indirectionCollection);
            //获取交通流量预测数据
            getPredictionFlow();
            for(String sessionId: WebSocketServer.webSocketMap.keySet()){
                SendResponseUtil.sendJSONResponse(StreamSet.Signal.TRAFFIC_INDIRECTION, indirectionCollection, sessionId);
            }
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
    /**
     * 每隔5分钟获取一次趋势变化
     */
    @Scheduled(cron = "0 * * * * ?")
    private void setTrafficTrend(){
        Map<Integer,TrafficLight> trafficLightMap = TrafficLightUtil.getTrafficLightMap(this.trafficLightList);
        //保存不同道路的拥堵指数变化趋势
        IndirectionTrendUtil.setIndirectionTrend(this.roadList, this.redisTemplate);
        //保存不同路口的拥堵里程变化趋势
        IndirectionTrendUtil.setCongestionMileageTrend(trafficLightMap, junctionList, this.redisTemplate);
        //保存不同红绿灯的交通指数变化趋势
        IndirectionTrendUtil.setDelayTrend(trafficLightMap, junctionList, this.redisTemplate);
        //设置车辆通过不同交通灯的停车次数
        IndirectionTrendUtil.setStopTrend(trafficLightMap, junctionList, this.redisTemplate);
        //设置不同红绿灯下的车流量
        IndirectionTrendUtil.setFlowTrend(this.trafficLightList, this.redisTemplate);
        //设置整个地图的变化趋势数据
        IndirectionTrendUtil.setWholeTrend(this.redisTemplate);
    }
    /**
     * 获取交通流量预测数据
     */
    private void getPredictionFlow(){
        List<Axis> flowList = indirectionCollection.getFlowList();
        if(!flowList.isEmpty()){
            String x = flowList.get(0).getX();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss");
            LocalTime last = LocalDateTime.parse(x, formatter).toLocalTime();
            LocalTime now = LocalDateTime.now().toLocalTime();
            if(now.isBefore(last)){
                return;
            }
        }
        flowList.clear();
        List<Prediction> predictionList = this.predictionMapper.getNearestList();
        for(Prediction prediction: predictionList){
            Axis axis = new Axis();
            axis.setX(String.valueOf(prediction.getPredictionTime()));
            axis.setY((double)prediction.getFlow());
            flowList.add(axis);
        }
        System.out.println(JSON.toJSONString(flowList));
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
        double averageSpeed = IndirectionTrendUtil.getAverageSpeed(this.redisTemplate);
        if(averageSpeed!=0){
            hourSpeedMap.putIfAbsent("ALL", 0d);
            hourSpeedMap.put("ALL", this.hourSpeedMap.get("ALL") + averageSpeed);
        }
        //每采集一次平均速度，将采集次数加一
        this.acquiredCount ++;
    }

    /**
     * 每小时计算一次当前小时内的平均车速
     */
    @Scheduled(cron = "0 0 * * * ?")
    private void calculateHourSpeed(){
        for(String roadId: hourSpeedMap.keySet()){
            Double value = hourSpeedMap.get(roadId);
            if(this.acquiredCount!=0&&value!=null&&value!=0){
                this.daySpeedMap.putIfAbsent(roadId,new ArrayList<>());
                List<Double> valueList = this.daySpeedMap.get(roadId);
                valueList.add(value/this.acquiredCount);
            }
            //将每个小时的获取速度的次数重新设置为0
            this.acquiredCount = 0;
            hourSpeedMap.put(roadId, 0d);
        }
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
        this.daySpeedMap.clear();
        //获取不同道路的历史平均车速
        speedList = this.speedMapper.getAverageSpeedList();
        IndirectionUtil.resetSpeedMap(speedList);
    }
}