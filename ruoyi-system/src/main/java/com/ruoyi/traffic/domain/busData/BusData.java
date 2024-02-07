package com.ruoyi.traffic.domain.busData;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusData implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("线路名称")
    private String lineName;

    @ApiModelProperty("yy线路名称")
    private String yyLineName;

    @ApiModelProperty("车牌号")
    private String busNumber;

    @ApiModelProperty("编号")
    private String number;

    @ApiModelProperty("gps数据时间")
    private String gpsTime;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("速度")
    private Double speed;

    @ApiModelProperty("无用id")
    private String busId;
}
