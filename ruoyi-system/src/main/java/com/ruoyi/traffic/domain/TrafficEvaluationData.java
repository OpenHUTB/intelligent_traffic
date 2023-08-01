package com.ruoyi.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @classname: TrafficEvaluationData
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据的类
 * @date: 2023/7/25
 * @version: v1.0
 **/
@Data
public class TrafficEvaluationData extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("交通路口ID")
    private Long intersectionId;

    @ApiModelProperty("评价指标类型ID")
    private Long evaluationTypeId;

    @ApiModelProperty("评价指标的值")
    private BigDecimal value;
}
