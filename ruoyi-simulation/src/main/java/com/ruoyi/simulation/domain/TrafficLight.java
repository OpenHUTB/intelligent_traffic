package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.simulation.util.StateStage;
import com.ruoyi.simulation.domain.Signalbase.TrafficLightState;
import lombok.Data;

import java.util.*;

/**
 * 交通灯信息实体类
 */
@Data
@TableName("simulation_traffic_light")
public class TrafficLight {
    /**
     * 黄灯时间
     */
    public static final int YELLOW_TIME = 3;
    /**
     * 安全余量
     */
    public static final int SAFETY_MARGIN_TIME = 1;
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
     * 清空距离
     */
    private Double clearanceDistance;
    /**
     * 行人通过斑马线的最短行驶距离
     */
    private Double walkDistance;
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
    /**
     * 流量
     */
    @TableField(exist = false)
    private Integer flowTrend = 0;
    /**
     * 绿灯所在相位
     */
    @TableField(exist = false)
    private Integer greenPhase;
    /**
     * 红绿灯对应的状态及持续时间
     */
    @TableField(exist = false)
    private List<StateStage> stageList = new ArrayList<>();
    /**
     * 一个周期内每秒钟对应的红绿灯状态
     */
    @TableField(exist = false)
    private TrafficLightState[] stateArr;
    /**
     * 添加一个红绿灯状态
     * @param state
     * @param length
     */
    public void addState(Signalbase.TrafficLightState state, int length){
        StateStage stage = new StateStage();
        stage.setState(state);
        stage.setLength(length);
        this.stageList.add(stage);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficLight that = (TrafficLight) o;
        return Objects.equals(trafficLightId, that.trafficLightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trafficLightId);
    }
}
