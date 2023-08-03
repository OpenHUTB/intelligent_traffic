package com.ruoyi.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @classname: TrafficStandardAdcode
 * @author: chengchangli
 * @description: 行政区划编码表
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Data
public class TrafficStandardAdcode implements Serializable {

    private static final long serialVersionUID = 606647902503896130L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("省份编码")
    private String provinceCode;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("城市编码")
    private String cityCode;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("区县编码")
    private String districtCode;

    @ApiModelProperty("乡镇名称")
    private String villageName;

    @ApiModelProperty("乡镇编码")
    private String villageCode;


}
