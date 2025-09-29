package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 绿波
 */
@Data
@TableName("simulation_green_wave")
public class GreenWave {
    public static final double ROAD_AVERAGE_SPEED = 15.0;
    /**
     * 主键
     */
    private Integer waveId;
    /**
     * 绿波组包含的交通灯id
     */
    private Integer trafficLightId;
    /**
     * 交通灯所在路口id
     */
    @TableField(exist = false)
    private Integer junctionId;
    /**
     * 下一个交通灯id
     */
    private Integer nextId;
    /**
     * 与下一个路口的距离
     */
    private Double distance;
    /**
     * 绿波组id
     */
    private Integer groupId;
    /**
     * 相邻路口时间差
     */
    @TableField(exist = false)
    private Integer intervalSecond;
    /**
     * 上一个路口的绿波数据
     */
    @TableField(exist = false)
    private GreenWave prefix;
    /**
     * 下一个路口的绿波数据
     */
    @TableField(exist = false)
    private GreenWave next;

    @Override
    public String toString() {
        return "GreenWave{" +
                "waveId=" + waveId +
                ", trafficLightId=" + trafficLightId +
                ", junctionId=" + junctionId +
                ", nextId=" + nextId +
                ", distance=" + distance +
                ", intervalSecond=" + intervalSecond +
                ", groupId=" + groupId +
                '}';
    }
}
