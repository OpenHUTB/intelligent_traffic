package com.ruoyi.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @classname: TrafficEvaluation
 * @author: ouyangdelong
 * @description: 路口评价指标的类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Data
public class TrafficEvaluation extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("路口交通流量")
    private Integer trafficFlow;

    @ApiModelProperty("路口车均延误")
    private BigDecimal averageVehicleDelay;

    @ApiModelProperty("路口车均停车次数")
    private BigDecimal averageParkingCount;

    @ApiModelProperty("路口饱和度")
    private BigDecimal congestionLevel;

    @ApiModelProperty("路口排队长度")
    private Integer queueLength;

    @ApiModelProperty("交通路口ID")
    private Long trafficIntersectionId;
}
