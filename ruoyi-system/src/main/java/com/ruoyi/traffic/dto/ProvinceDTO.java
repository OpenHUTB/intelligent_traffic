package com.ruoyi.traffic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classname: ProvinceDTO
 * @author: chengchangli
 * @description: 省份数据传输对象
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Data
public class ProvinceDTO {

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("省份编码")
    private String provinceCode;
}
