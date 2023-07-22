package com.ruoyi.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @classname: TrafficArea
 * @author: chengchangli
 * @description: 路网面的类
 * @date: 2023/7/17
 * @version: v1.0
 **/
@Data
public class TrafficArea extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("大区编号+小区编号")
    private String code;

    @ApiModelProperty("面积大小")
    private BigDecimal area;

    @ApiModelProperty("用地类型")
    private String landUseType;

}
