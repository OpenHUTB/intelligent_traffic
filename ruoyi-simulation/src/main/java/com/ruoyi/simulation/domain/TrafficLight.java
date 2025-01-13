package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 交通灯信息实体类
 */
@Data
@TableName("simulation_traffic_light")
public class TrafficLight implements Comparable {
    /**
     * 交通灯id
     */
    private Integer trafficLightId;
    /**
     * 交通灯名称
     */
    private String trafficLightName;
    /**
     * 路口id
     */
    private Integer junctionId;
    /**
     * 起始方位
     */
    private String fromDirection;
    /**
     * 行驶方向：left-左转，forward-直行
     */
    private String turnDirection;
    /**
     * 红灯时间
     */
    @TableField(exist = false)
    private Integer redTime = 0;
    /**
     * 黄灯时间
     */
    @TableField(exist = false)
    private Integer yellowTime = 0;
    /**
     * 绿灯时间
     */
    @TableField(exist = false)
    private Integer greenTime = 0;
    /**
     * 起始交通灯
     */
    @TableField(exist = false)
    private SignalBase.LightStatus startLight;
    /**
     * 拥堵里程
     */
    @TableField(exist = false)
    @JsonIgnore
    private Double congestionMileage = 0d;
    /**
     * 等候红灯的车辆数量
     */
    @TableField(exist = false)
    @JsonIgnore
    private Integer waitVehicle;
    /**
     * 平均通行时间
     */
    @TableField(exist = false)
    private Double averageDelay;
    /**
     * 平均通行时间变化率
     */
    @TableField(exist = false)
    private Double averageDelayRate;
    /**
     * 停车次数
     */
    @TableField(exist = false)
    private Double stopTimes;
    /**
     * 停车次数变化率
     */
    @TableField(exist = false)
    private Double stopTimesRate;
    public TrafficLight(Integer trafficLightId){
        this.trafficLightId = trafficLightId;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        TrafficLight trafficLight = (TrafficLight)o;
        return this.waitVehicle-trafficLight.getWaitVehicle();
    }
}
