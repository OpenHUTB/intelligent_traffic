package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * 路口指标信息实体类
 */
@Data
@TableName("simulation_junction")
public class Junction {
    /**
     * 路口id
     */
    private Integer junctionId;
    /**
     * 路口方向名称
     */
    private String junctionName;
    /**
     * 东西方向的道路名称
     */
    private String transverse;
    /**
     * 南北方向的道路名称
     */
    private String portrait;
    /**
     * 拥堵里程
     */
    @TableField(exist = false)
    private Double congestionMileage = 0d;
    /**
     * 拥堵里程趋势变化
     */
    @TableField(exist = false)
    private Double congestionMileageRate;
    /**
     * 交通灯对应的指数集合
     */
    @TableField(exist = false)
    private List<TrafficLight> trafficLightList;
}
