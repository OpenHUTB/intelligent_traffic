package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 预测信息实体类
 */
@Data
@TableName("simulation_prediction")
public class Prediction {
    /**
     * 预测id
     */
    private Integer predictionId;
    /**
     * 车流量
     */
    private Integer flow;
    /**
     * 天气编码
     */
    private Integer weatherCode;
    /**
     * 预测时间
     */
    private String predictionTime;
}
