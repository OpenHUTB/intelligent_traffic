package com.ruoyi.traffic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classname: DistrictDTO
 * @author: chengchangli
 * @description: 区县数据传输对象
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Data
public class DistrictDTO {

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("区县编码")
    private String districtCode;
}
