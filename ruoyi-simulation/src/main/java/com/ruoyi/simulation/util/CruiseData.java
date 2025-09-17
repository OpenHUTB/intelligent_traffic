package com.ruoyi.simulation.util;

import lombok.Data;

import java.util.List;

/**
 * 导航数据
 */
@Data
public class CruiseData {
    /**
     * 巡航虚拟坐标集合
     */
    private String route;
    /**
     * 角度(平视为 0,俯视为负数仰望为正数)
     */
    private Double pitch = -10.0;
    /**
     * 速度
     */
    private Double speed = 50.0;
    /**
     * 天气
     */
    private String weather = "ClearSunset";
    /**
     * 待巡游的路口
     */
    private List<Integer> junctionIdList;
}
