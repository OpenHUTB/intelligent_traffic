package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 不同红绿灯对应的时段划分
 */
@Data
@TableName("simulation_duration")
public class Duration {
    /**
     * 主键
     */
    private Integer durationId;
    /**
     * 时段名称
     */
    private String durationName;
    /**
     * 时段起始时间（预设）
     */
    private Date startTime;
    /**
     * 时段起始时间（预设）
     */
    private Date endTime;
}
