package com.ruoyi.simulation.util;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 告警信息
 */
@Data
public class Alarming {
    /**
     * 告警类型
     */
    public enum AlarmingType{
        WRONG_WAY,REVERSE,ABNORMAL_STOP,LOWER_SPEED
    }
    public enum AlarmingLevel{
        MINOR,GENERAL,SERIOUS,FATAL
    }
    private Integer vehicleId;
    /**
     * 车道id
     */
    private Integer roadId;
    /**
     * 位置
     */
    private Location location;
    /**
     * 车速
     */
    private Double speed;
    /**
     * 告警类型
     */
    private AlarmingType type;
    /**
     * 车牌
     */
    private String plate;
    /**
     * 是否上报
     */
    private Boolean reported = false;
    /**
     * 是否处理
     */
    private Boolean operated = false;
    /**
     * 发生时间
     */
    private LocalDateTime time;
    /**
     * 告警级别
     */
    private AlarmingLevel level;
}
