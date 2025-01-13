package com.ruoyi.simulation.domain;

import lombok.Data;

import java.util.List;

/**
 * 交通指数信息实体类
 */
@Data
public class TrafficIndirectionCollection {
    /**
     * 车辆平均速度
     */
    private Double averageSpeed;
    /**
     * 平均车速变化率
     */
    private Double averageSpeedRate;
    /**
     * 平均延误时间
     */
    private Double averageDelay;
    /**
     * 平均延误时间变化
     */
    private Double averageDelayChange;
    /**
     * 拥堵里程
     */
    private Double congestionMileage;
    /**
     * 拥堵里程变化率
     */
    private Double congestionMileageRate;
    /**
     * 速度指数
     */
    private Double speedIndirection;
    /**
     * 拥堵指数
     */
    private Double congestionIndirection;
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
}
