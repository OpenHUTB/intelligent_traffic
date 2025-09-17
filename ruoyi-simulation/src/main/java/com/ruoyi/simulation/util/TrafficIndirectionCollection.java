package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.Road;
import lombok.Data;

import java.util.*;

/**
 * 交通指数信息实体类
 */
@Data
public class TrafficIndirectionCollection {
    /**
     * 速度指数
     */
    private Double speedIndirection;
    /**
     * 车辆平均速度
     */
    private Double averageSpeed;
    /**
     * 平均车速变化率
     */
    private Double averageSpeedRate;
    /**
     * 拥堵指数
     */
    private Double congestionIndirection;
    /**
     * 拥堵里程
     */
    private Double congestionMileage;
    /**
     * 拥堵里程变化率
     */
    private Double congestionMileageRate;
    /**
     * 拥堵路段数量
     */
    private Double congestionRoad;
    /**
     * 平均延误时间
     */
    private Double averageDelay;
    /**
     * 平均延误时间变化
     */
    private Double averageDelayRate;
    /**
     * 拥堵警情
     */
    private List<Road> alarmIndirection;
    /**
     * 不同道路的交通指数
     */
    private List<Road> roadIndirection;
    /**
     * 不同路口的交通指数
     */
    private List<Junction> junctionIndirection;
    /**
     * 服务车次
     */
    private Double servicedVehicle;
    /**
     * 总车次
     */
    private Double totalVehicle;
    /**
     * 当前所在路口
     */
    private Junction currentJunction;
    /**
     * 流量预测数据
     */
    private List<Axis> flowList = new ArrayList<>();
    /**
     * 告警信息列表
     */
    private List<Alarming> alarmingList;
    /**
     * 优化前数据
     */
    private Map<String,List<Axis>> beforeOptimizationList;
    /**
     * 优化后数据
     */
    private Map<String,List<Axis>> afterOptimizationList;
}
