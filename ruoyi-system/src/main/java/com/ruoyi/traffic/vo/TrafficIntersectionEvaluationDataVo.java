package com.ruoyi.traffic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class TrafficIntersectionEvaluationDataVo implements Serializable {

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
