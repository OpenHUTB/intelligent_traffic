package com.ruoyi.traffic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname:  TrafficAreaEvaluationHistoryRankVo
 * @author: ouyanghua
 * @description: 区域指标历史排名vo
 * @date: 2023/8/9
 * @version: v1.0
 **/
@Data
public class TrafficAreaEvaluationHistoryRankVo {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("区域ID")
    private Long areaId;

    @ApiModelProperty("评价指标类型ID")
    private Long evaluationTypeId;

    @ApiModelProperty("评价指标的值")
    private BigDecimal value;

    // 不确定接收到数据的实时性，所以数据可能带有采集时间字段
    @ApiModelProperty("数据采集时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectTime;

    @ApiModelProperty("指标类型名称")
    private String evaluationTypeName;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("历史评价年份")
    private String Year;

    @ApiModelProperty("历史评价月份")
    private String Month;

    @ApiModelProperty("历史评价日期")
    private String Day;


}
