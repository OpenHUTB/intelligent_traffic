package com.ruoyi.traffic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classname: VillageDTO
 * @author: chengchangli
 * @description: 乡镇数据传输类
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Data
public class VillageDTO {

    @ApiModelProperty("乡镇名称")
    private String villageName;

    @ApiModelProperty("乡镇编码")
    private String villageCode;
}
