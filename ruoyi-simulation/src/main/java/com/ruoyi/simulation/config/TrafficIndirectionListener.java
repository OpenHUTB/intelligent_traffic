package com.ruoyi.simulation.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.SpeedMapper;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.Road;
import com.ruoyi.simulation.domain.Speed;
import com.ruoyi.simulation.util.LoggerUtil;
import com.ruoyi.simulation.util.SendResponseUtil;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
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
    public RedisTemplate<String,String> redisTemplate;
    @Resource
    public SpeedMapper speedMapper;
    //每天定时获取历史平均速度
    private Map<String,Double> speedMap = new HashMap<>();
    //一天内的平均速度
    private Map<String, List<Double>> daySpeedMap = new HashMap<>();
    private Map<String, Double> hourSpeedMap = new HashMap<>();
    private Map<String, Double> indirectionTrendMap = new HashMap<>();
    private Map<String, Double> mileageTrendMap = new HashMap<>();
    private Map<String, Double> trafficTrendMap = new HashMap<>();
    private int acquiredCount = 0;
    private List<Road> roadList = new ArrayList<>();
    private List<Junction> junctionList = new ArrayList<>();
    @Override
    public void onApplicationEvent(ApplicationStartedEvent e) {
        executionIndirectionScript();
        getJunctionRoad();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getRedisIndirection();
            }
        }).start();
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
        this.callUE4Engine.getTrafficIndirection("road_congestion_mileage.py");
        //获得服务车次
        this.callUE4Engine.getTrafficIndirection("serviced_vehicle.py");
        //获得路网车辆
        this.callUE4Engine.getTrafficIndirection("total_vehicle.py");
    }

    /**
     * 获取道路及路口的字典信息
     */
    private void getJunctionRoad(){
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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LoggerUtil.printLoggerStace(e);
            }
            Map<String,Object> indirectionMap = new HashMap<>();
            //获取实时的平均车速
            double averageSpeed = this.getAverageSpeed();
            indirectionMap.put("averageSpeed", averageSpeed);
            //设置实时交通指数
            this.setTrafficIndirection(averageSpeed, indirectionMap);
            //设置不同道路的实时平均车速及拥堵指数
            this.setRoadIndirection(indirectionMap);
            //设置平均延误时间
            double averageDelay = this.getAverageDelay();
            indirectionMap.put("averageDelay", averageDelay);
            //设置总拥堵里程
            double congestionMileage = this.getCongestionMileage();
            indirectionMap.put("congestionMileage", congestionMileage);
            //获取不同路口的拥堵里程
            this.setJunctionCongestionMileage(indirectionMap);
            //获取服务车次
            this.setServicedVehicle(indirectionMap);
            //获取路网车辆
            this.setTotalVehicle(indirectionMap);
            //设置交通趋势变化数据
            this.setTrafficTrend();
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
        String averageSpeedStr = this.redisTemplate.opsForValue().get("average_speed");
        double averageSpeed = 0d;
        if(!StringUtils.isEmpty(averageSpeedStr)){
            averageSpeed = Double.parseDouble(averageSpeedStr);
        }
        return averageSpeed;
    }
    /**
     * 设置交通指数
     * @param indirectionMap
     */
    public void setTrafficIndirection(double averageSpeed, Map<String,Object> indirectionMap){
        this.speedMap.putIfAbsent("ALL", averageSpeed);
        double trafficIndirection = averageSpeed/this.speedMap.get("ALL");
        indirectionMap.put("trafficIndirection", trafficIndirection);
    }

    /**
     * 设置道路的平均速度和拥堵指数
     * @param indirectionMap
     */
    private void setRoadIndirection(Map<String,Object> indirectionMap){
        Map<String, Road> roadMap = new HashMap<>();
        for(Road road: this.roadList){
            roadMap.put(String.valueOf(road.getRoadId()),road);
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
            }
        }.getRoadAverageSpeed();
        indirectionMap.put("roadIndirection", this.roadList);
        //将不同道路的平均速度保存到数据库
        this.setSpeedToHistory();
    }
    /**
     * 设置总的平均延误时间
     */
    public double getAverageDelay(){
        String averageDelayStr = this.redisTemplate.opsForValue().get("average_delay");
        double averageDelay = 0d;
        if(!StringUtils.isEmpty(averageDelayStr)){
            averageDelay = Double.parseDouble(averageDelayStr);
        }
        return averageDelay;
    }

    /**
     * 设置总拥堵里程
     */
    public double getCongestionMileage(){
        String congestionMileageStr = this.redisTemplate.opsForValue().get("congestion_mileage");
        //获取总拥堵里程
        double congestionMileage = 0d;
        if(!StringUtils.isEmpty(congestionMileageStr)){
            congestionMileage = Double.parseDouble(congestionMileageStr);
        }
        return congestionMileage;
    }

    /**
     * 设置不同路口的拥堵里程
     * @param indirectionMap
     */
    public void setJunctionCongestionMileage(Map<String,Object> indirectionMap){
        Map<String, Junction> junctionMap = new HashMap<>();
        for(Junction junction: this.junctionList){
            junctionMap.put(String.valueOf(junction.getJunctionRoadId()), junction);
            //初始化所有的道路拥堵里程
            junction.setCongestionMileage(0d);
            //初始化拥堵里程趋势变化
            junction.setCongestionMileageTrend(0d);
        }
        //保存不同路口拥堵里程数据
        new ParseJunctionIndirection(){
            @Override
            protected void setJunctionCongestionMileage(String junctionRoadId, double congestionMileage) {
                Junction junction = junctionMap.get(junctionRoadId);
                //设置拥堵里程
                junction.setCongestionMileage(congestionMileage);
            }
        }.getJunctionCongestionMileage();
        indirectionMap.put("junctionCongestionMileage", junctionList);
    }
    /**
     * 保存交通趋势变化数据
     */
    private void setTrafficTrend(){
        long millis = System.currentTimeMillis();
        //每隔5分钟获取一次趋势变化
        if(millis%30000==0){
            for(Road road: this.roadList){
                this.indirectionTrendMap.put(String.valueOf(road.getRoadId()), 1d);
            }
            for(Junction junction: this.junctionList){
                this.mileageTrendMap.put(String.valueOf(junction.getJunctionRoadId()), 0d);
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
                public void setJunctionCongestionMileage(String junctionRoadId, double congestionMileage) {
                    mileageTrendMap.put(junctionRoadId, congestionMileage);
                }
            }.getJunctionCongestionMileage();

            //保存平均延误时间变化趋势
            trafficTrendMap.put("averageDelay", this.getAverageDelay());
            //保存总拥堵里程变化趋势
            trafficTrendMap.put("congestionMileage", this.getCongestionMileage());
            //保存总的平均速度变化趋势
            trafficTrendMap.put("averageSpeed", this.getAverageSpeed());
        }
    }

    /**
     * 设置实时的服务车次
     * @param indirectionMap
     */
    public void setServicedVehicle(Map<String,Object> indirectionMap){
        String servicedVehicleStr = this.redisTemplate.opsForValue().get("serviced_vehicle");
        double servicedVehicle = 0d;
        if(!StringUtils.isEmpty(servicedVehicleStr)){
            servicedVehicle = Double.parseDouble(servicedVehicleStr);
        }
        indirectionMap.put("servicedVehicle", servicedVehicle);
    }

    /**
     * 设置实时的总车量
     * @param indirectionMap
     */
    public void setTotalVehicle(Map<String,Object> indirectionMap){
        String totalVehicleStr = this.redisTemplate.opsForValue().get("total_vehicle");
        double totalVehicle = 0d;
        if(!StringUtils.isEmpty(totalVehicleStr)){
            totalVehicle = Double.parseDouble(totalVehicleStr);
        }
        indirectionMap.put("totalVehicle", totalVehicle);
    }
    /**
     * 采集平均速度作为历史数据
     */
    private void setSpeedToHistory(){
        long millis = System.currentTimeMillis();
        //每隔5秒取一次数据进行统计
        if(millis%5000==0) {
            new ParseRoadIndirection(){
                @Override
                public void setRoadAverageSpeed(String roadId, double averageSpeed) {
                    hourSpeedMap.put(roadId, hourSpeedMap.get(roadId) + averageSpeed);
                }
            }.getRoadAverageSpeed();
            double averageSpeed = this.getAverageSpeed();
            if(averageSpeed!=0){
                this.hourSpeedMap.put("ALL", this.hourSpeedMap.get(averageSpeed) + averageSpeed);
            }
            //每采集一次平均速度，将采集次数加一
            this.acquiredCount ++;
        }
        //每隔1小时计算一次统计数据
        if(millis%3600000==0){
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
    }
    /**
     * 内部类，用来解析不同道路的平均速度数据
     */
    private abstract class ParseRoadIndirection {
        /**
         * 获取redis中的不同车道的实时平均速度
         */
        public void getRoadAverageSpeed(){
            String roadAverageSpeedStr = redisTemplate.opsForValue().get("road_average_speed");
            if(!StringUtils.isEmpty(roadAverageSpeedStr)) {
                JSONObject jsonObject = JSON.parseObject(roadAverageSpeedStr);
                for (String roadId : jsonObject.keySet()) {
                    //获取不同道路的实时平均速度
                    double averageSpeed = Double.valueOf(String.valueOf(jsonObject.get(roadId)));
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
            String roadCongestionMileageStr = redisTemplate.opsForValue().get("road_congestion_mileage");
            if(!StringUtils.isEmpty(roadCongestionMileageStr)) {
                JSONObject jsonObject = JSON.parseObject(roadCongestionMileageStr);
                for (String junctionRoadId : jsonObject.keySet()) {
                    //获取不同路口的拥堵里程
                    double congestionMileage = Double.valueOf(String.valueOf(jsonObject.get(junctionRoadId)));
                    this.setJunctionCongestionMileage(junctionRoadId, congestionMileage);
                }
            }
        }
        /**
         * 设置实时拥堵里程数据
         * @param junctionRoadId
         * @param congestionMileage
         */
        protected abstract void setJunctionCongestionMileage(String junctionRoadId, double congestionMileage);
    }
}
