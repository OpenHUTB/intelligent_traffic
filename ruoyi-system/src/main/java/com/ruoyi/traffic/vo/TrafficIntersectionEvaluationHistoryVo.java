package com.ruoyi.traffic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname: TrafficIntersectionEvaluationHistoryVo
 * @author: ouyangdelong
 * @description: 路口历史数据Vo类
 * @date: 2023/8/9
 * @version: v1.0
 **/
@Data
public class TrafficIntersectionEvaluationHistoryVo implements Serializable {

    @ApiModelProperty("历史评价年份")
    private String year;

    @ApiModelProperty("历史评价月份")
    private String month;

    @ApiModelProperty("历史评价日期")
    private String day;

    @ApiModelProperty("路口名称")
    private String intersectionName;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("评价指标名称")
    private String evaluationName;

    @ApiModelProperty("评价指标类型: 1.路口，2.区域")
    private String type;

    @ApiModelProperty("评价指标的值")
    private BigDecimal value;

    @ApiModelProperty("数据采集时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectTime;

}
