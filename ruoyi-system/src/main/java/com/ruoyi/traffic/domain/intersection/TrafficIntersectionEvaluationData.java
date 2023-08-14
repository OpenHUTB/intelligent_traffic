package com.ruoyi.traffic.domain.intersection;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname: TrafficEvaluationData
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据的类
 * @date: 2023/7/25
 * @version: v1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficIntersectionEvaluationData extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("交通路口ID")
    private Long intersectionId;

    @ApiModelProperty("评价指标类型ID")
    private Long evaluationTypeId;

    @ApiModelProperty("评价指标的值")
    private BigDecimal value;

    // 不确定接收到数据的实时性，所以数据可能带有采集时间字段
    @ApiModelProperty("数据采集时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectTime;


}
