package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 绿波组
 */
@Data
@TableName("simulation_green_wave")
public class GreenGroup {
    /**
     * 绿波组id
     */
    private Integer groupId;
    /**
     * 绿波组名
     */
    @TableField(exist = false)
    private String groupName;
    /**
     * 绿波组包含的交通灯id
     */
    @TableField(exist = false)
    private List<TrafficLight> trafficLightList;
}
