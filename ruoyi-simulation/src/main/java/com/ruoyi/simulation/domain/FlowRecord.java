package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 不同红绿灯的平均流量统计
 */
@Data
@TableName("simulation_flow_record")
public class FlowRecord {
    /**
     * 主键
     */
    private Integer recordId;
    /**
     * 路口id
     */
    private Integer junctionId;
    /**
     * 相位
     */
    private Integer phase;
    /**
     * 所属时段
     */
    private Integer durationId;
    /**
     * 平均流量
     */
    private Integer averageFlow;
}
