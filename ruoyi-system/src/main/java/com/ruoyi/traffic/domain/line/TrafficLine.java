package com.ruoyi.traffic.domain.line;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @classname: TrafficLine
 * @author: chengchangli
 * @description: 路网线的类
 * @date: 2023/7/17
 * @version: v1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficLine extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("道路名称")
    private String name;

    @ApiModelProperty("道路起点ID")
    private Long startPointId;

    @ApiModelProperty("道路终点ID")
    private Long endPointId;


}
