package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 道路指标信息实体类
 */
@Data
@TableName("simulation_road")
public class Road implements Comparable {
    /**
     * 道路id
     */
    private Integer roadId;
    /**
     * 道路名称
     */
    private String roadName;
    /**
     * 地图中相关联的roadId，与主键中的roadId并非同一概念。由于carla中一条道路也会包含多个roadId，因此需要进行关联
     */
    @JsonIgnore
    private String relatedRoadId;
    /**
     * 平均速度
     */
    @TableField(exist = false)
    private Double averageSpeed;
    /**
     * 拥堵指数
     */
    @TableField(exist = false)
    private Double congestionIndirection;
    /**
     * 拥堵趋势变化率
     */
    @TableField(exist = false)
    private Double congestionIndirectionRate;

    @Override
    public int compareTo(@NotNull Object o) {
        Road road = (Road)o;
        return (int)(this.congestionIndirection - road.getCongestionIndirection());
    }
}
