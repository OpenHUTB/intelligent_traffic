package com.ruoyi.traffic.domain.point;

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
 * @classname: TrafficPoint
 * @author: chengchangli
 * @description: 交通点的类
 * @date: 2023/7/17
 * @version: v1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficPoint extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("路网节点名称")
    private String name;

    @ApiModelProperty("路网节点编码")
    private String code;

    @ApiModelProperty("节点的X坐标")
    private String xValue;

    @ApiModelProperty("节点的Y坐标")
    private String yValue;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("类型")
    private String type;










}
