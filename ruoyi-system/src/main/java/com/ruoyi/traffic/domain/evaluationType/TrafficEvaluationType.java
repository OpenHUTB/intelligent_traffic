package com.ruoyi.traffic.domain.evaluationType;

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
 * @classname: TrafficEvaluationType
 * @author: ouyangdelong
 * @description: 评价指标类型的类(公共的)
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficEvaluationType extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("评价指标名称")
    private String name;

    @ApiModelProperty("评价指标类型: 1.路口，2.区域")
    private String type;


}
