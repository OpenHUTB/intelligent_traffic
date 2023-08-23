package com.ruoyi.simulation.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图像和声音数据
 */
@Data
public class StreamSet {
    /**
     * 声音信息数据流
     */
    @ApiModelProperty("声音信息数据流")
    private String sound;
    /**
     * 像素流
     */
    @ApiModelProperty("像素流")
    private byte[] graph;
    /**
     * 提示消息
     */
    @ApiModelProperty("提示文本")
    private String message;
}
