package com.ruoyi.traffic.domain.intersection;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @classname: TrafficIntersectionEvaluationHistory
 * @author: chengchangli
 * @description: 区域指标历史值
 * @date: 2023/8/7
 * @version: v1.0
 **/
@Data
public class TrafficIntersectionEvaluationHistory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4287177392609425762L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("历史评价年份")
    private String Year;

    @ApiModelProperty("历史评价月份")
    private String Month;

    @ApiModelProperty("历史评价日期")
    private String Day;

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
