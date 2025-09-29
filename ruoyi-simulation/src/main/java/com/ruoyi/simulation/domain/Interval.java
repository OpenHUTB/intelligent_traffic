package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 不同红绿灯之间的间隔时间
 */
@TableName("simulation_interval")
public class Interval {
    private Integer intervalId;
    private Integer startId;
    private Integer endId;
    private Integer intervalSecond;
}
