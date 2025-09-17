package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 路段信息实体类
 */
@Data
@TableName("simulation_section")
public class Section {
    /**
     * 坐标id
     */
    @TableId(type = IdType.AUTO)
    private Integer sectionId;
    /**
     * 起始路口ID
     */
    private Integer startJunctionId;
    /**
     * 终止路口ID
     */
    private Integer endJunctionId;
    /**
     * 虚拟坐标
     */
    private String virtualPoints;
}
