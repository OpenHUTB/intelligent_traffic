package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 信号控制信息实体类
 */
@Data
public class Signalbase {
    /**
     * 交通灯状态
     */
    public enum LightStatus{
        RED,GREEN,YELLOW,GREEN_YELLOW
    }
    private Integer signalControlId;
    /**
     * 用于区分不同的路口(如序号1表示尖山路-望青路)
     */
    private Integer junctionId;
    /**
     * 路口名称
     */
    private String junctionName;
    /**
     * 用于区分同一路口的不同红绿灯控制方案(如1表示方案1，2表示方案2)
     */
    private Integer schemeId;
    /**
     * 周期ID，用于标识一个完整的周期(现在用1表示，以后可拓展)。
     */
    private Integer cycleId;
    /**
     * 车辆大方向阶段，以车辆在路口开始行驶方向为准划分，分为"North-South"和"East-West"(表示车辆开始时的南北走向和东西走向)，North-South(B)后面括号加了(B)标识的时非机动车道。
     */
    private String direction;
    /**
     * 一个红绿灯周期四个阶段变化，分别用1、2、3、4代替
     */
    private Integer phase;
    /**
     * 起始方向（如 "South"）
     */
    private String fromDirection;
    /**
     * 目的方向（如 "East"） (起始方向和目的方向就决定了车辆形式的方向)
     */
    private String toDirection;
    /**
     * 机动车道信号灯状态，表示该方向阶段内的绿灯、黄灯、红灯变化。
     */
    private LightStatus lightStatus;
    /**
     * 每个阶段的开始时间。
     */
    private Timestamp startTime;
    /**
     * 每个阶段的结束时间。
     */
    private Timestamp endTime;
    /**
     * 机动车道的阶段持续时间（以秒为单位）
     */
    private Integer duration;
    /**
     * 表示南北方向人行道的信号灯状态
     */
    private String pedestrianNSLightStatus;
    /**
     * 表示南北方向人行道的信号灯持续时间
     */
    private Integer pedestrianNSDuration;
    /**
     * 表示东西方向人行道的信号灯状态及其持续时间。
     */
    private String pedestrianEWLightStatus;
    /**
     * 表示东西方向人行道的信号灯持续时间。
     */
    private Integer pedestrianEWDuration;
    /**
     * 交通灯id
     */
    @TableField(exist = false)
    private Integer trafficLightId;
    /**
     * 行驶方向：left-左转，forward-直行
     */
    @TableField(exist = false)
    private String turnDirection;
    /**
     * 清空距离
     */
    @TableField(exist = false)
    private Double clearanceDistance;
    /**
     * 行人通过斑马线的最短行驶距离
     */
    @TableField(exist = false)
    private Double walkDistance;
}
