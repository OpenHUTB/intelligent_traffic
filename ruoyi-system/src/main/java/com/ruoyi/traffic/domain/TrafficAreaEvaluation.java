package com.ruoyi.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @classname: TrafficArea_Evaluation
 * @author: ouyanghua
 * @description: 区域评价的实体类
 * @date: 2023/7/24
 * @version: v1.0
 **/

@Data
public class TrafficAreaEvaluation extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("对应的区域ID")
    private long intersectionId;

    @ApiModelProperty("区域名称")
    private String name;

    @ApiModelProperty("区域平均速度")
    private double averageSpeed;

    @ApiModelProperty("区域车均延误")
    private  double averageDelay;

    @ApiModelProperty("区域拥堵指数")
    private double congestionIndex;

    @ApiModelProperty("评价年份")
    private String Year;

    @ApiModelProperty("评价月份")
    private  String Month;

    @ApiModelProperty("评价日期")
    private String Day;


}
