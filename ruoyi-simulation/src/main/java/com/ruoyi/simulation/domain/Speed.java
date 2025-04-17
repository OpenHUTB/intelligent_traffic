package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 车辆平均速度实体类
 */
@Data
@TableName("simulation_indirection_speed")
public class Speed {
    /**
     * 指数id
     */
    private Integer indirectionId;
    /**
     * 道路id，若统计的是所有道路的平均速度，则其值为ALL
     */
    private String roadId;
    /**
     * 指数值
     */
    private Double value;
    /**
     * 更新时间
     */
    private Date createTime;
}
