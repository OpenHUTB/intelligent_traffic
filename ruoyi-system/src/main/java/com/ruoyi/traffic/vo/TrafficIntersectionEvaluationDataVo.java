package com.ruoyi.traffic.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.domain.intersection.TrafficIntersection;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class TrafficIntersectionEvaluationDataVo implements Serializable {

    @JsonIgnore
    @ApiModelProperty("路口对象")
    TrafficIntersection trafficIntersection;

    @JsonIgnore
    @ApiModelProperty("路口数据对象")
    TrafficIntersectionEvaluationData trafficIntersectionEvaluationData;

    @JsonIgnore
    @ApiModelProperty("指标类型对象")
    TrafficEvaluationType trafficEvaluationType;

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

    public void result() {
        this.intersectionName = trafficIntersection.getName();
        this.longitude = trafficIntersection.getLongitude();
        this.latitude = trafficIntersection.getLatitude();
        this.evaluationName = trafficEvaluationType.getName();
        if (trafficEvaluationType.getType().equals("1"))
            this.type = "路口";
        else
            this.type = "区域";
        this.value = trafficIntersectionEvaluationData.getValue();
    }
}
