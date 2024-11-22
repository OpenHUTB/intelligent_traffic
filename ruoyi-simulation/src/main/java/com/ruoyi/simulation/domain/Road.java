package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 道路指标信息实体类
 */
@Data
@TableName("simulation_road")
public class Road {
    /**
     * 道路id
     */
    private Integer roadId;
    /**
     * 道路名称
     */
    private String roadName;
    /**
     * 地图中相关联的roadId
     */
    @JsonIgnore
    private String relatedRoadId;
    /**
     * 平均速度
     */
    private Double averageSpeed;
    /**
     * 拥堵指数
     */
    private Double congestionIndirection;
    /**
     * 拥堵趋势
     */
    private Double congestionIndirectionTrend;
}
