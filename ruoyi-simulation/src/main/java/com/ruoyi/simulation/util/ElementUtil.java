package com.ruoyi.simulation.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 声音存储信息队列
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElementUtil {
    /**
     * websocket会话sessionId
     */
    @ApiModelProperty("websocket会话sessionId")
    private String sessionId;
    /**
     * 文本命令
     */
    @ApiModelProperty("文本命令")
    private String command;
}
