package com.ruoyi.traffic.domain.intersection;

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
 * @classname: TrafficIntersection
 * @author: ouyangdelong
 * @description: 路口的类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficIntersection extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("路口名称")
    private String name;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("方向")
    private String direction;
}
