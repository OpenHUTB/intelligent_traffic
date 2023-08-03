package com.ruoyi.traffic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classname: CityDTO
 * @author: chengchangli
 * @description: 城市传输对象
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Data
public class CityDTO {
    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("城市编码")
    private String cityCode;
}
