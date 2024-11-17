package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 雷达信息实体类
 */
@Data
@TableName("radar")
public class Radar {
    /**
     * 设备编号
     */
    private Integer deviceId;
    /**
     * 雷达设备编号
     */
    private String deviceNum;
    /**
     * 安装位置，比如“XX路-YY路口”或者“K121+456”
     */
    private String location;
    /**
     * 雷达经度
     */
    private Double longitude;
    /**
     * 雷达维度
     */
    private Double latitude;
    /**
     * 雷达海拔
     */
    private Double altitude;
    /**
     * 雷达在carla坐标系中的x坐标
     */
    private double carlaLocationX;
    /**
     * 雷达在carla坐标系中的y坐标
     */
    private double carlaLocationY;
    /**
     * 雷达检测到的车辆信息
     */
    private Map<Long, Vehicle> vehicleMap = new HashMap<>();
}
