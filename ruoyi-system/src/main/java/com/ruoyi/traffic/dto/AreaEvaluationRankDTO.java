package com.ruoyi.traffic.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classname: areaEvaluationRankDTO
 * @author: chengchangli
 * @description: 区域指标数据的DTO
 * @date: 2023/8/9
 * @version: v1.0
 **/
@Data
public class AreaEvaluationRankDTO {

    // 因为不确定后期是否会与前端有更多参数交互，所以用一个dto来传参

    @ApiModelProperty("指标类别ID")
    private Long evaluationTypeId;

    @ApiModelProperty("限制数")
    private Integer limit;
}
