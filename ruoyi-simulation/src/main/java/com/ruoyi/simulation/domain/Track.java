package com.ruoyi.simulation.domain;

import lombok.Data;

import java.util.Date;

/**
 * 车辆运行轨迹
 */
@Data
public class Track {
    private Integer trackId;
    /**
     * 车牌
     */
    private String plate;
    /**
     * 记录时间
     */
    private Date recordTime;
    /**
     * 监测设备编码
     */
    private String deviceNo;
    /**
     * 路口名称
     */
    private String junctionName;
    /**
     * 车道编码
     */
    private Integer laneNo;
    /**
     * 方向编码
     */
    private Integer directionNo;
}
