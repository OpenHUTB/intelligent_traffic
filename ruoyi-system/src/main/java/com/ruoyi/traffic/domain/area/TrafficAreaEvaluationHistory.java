package com.ruoyi.traffic.domain.area;

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
 * @classname: TrafficArea_Evaluation_History
 * @author: ouyanghua
 * @description: 历史区域评价的实体类
 * @date: 2023/7/24
 * @version: v1.0
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficAreaEvaluationHistory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1916727955064789114L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("历史评价年份")
    private String year;

    @ApiModelProperty("历史评价月份")
    private String month;

    @ApiModelProperty("历史评价日期")
    private String day;

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

}
