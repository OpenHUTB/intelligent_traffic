package com.ruoyi.traffic.domain.intersection;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classname: TrafficIntersection
 * @author: ouyangdelong
 * @description: 路口的类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Data
public class TrafficIntersection {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("路口名称")
    private String name;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;
}
