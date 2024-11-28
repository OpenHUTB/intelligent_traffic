package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 路口指标信息实体类
 */
@Data
@TableName("simulation_junction")
public class Junction {
    /**
     * 路口方向id
     */
    private Integer junctionId;
    /**
     * 路口方向名称
     */
    private String junctionName;
    /**
     * 交通灯id
     */
    @JsonIgnore
    private Integer trafficLightId;
    /**
     * 拥堵里程
     */
    @TableField(exist = false)
    private Double congestionMileage;
    /**
     * 拥堵里程趋势变化
     */
    @TableField(exist = false)
    private Double congestionMileageTrend;
}
