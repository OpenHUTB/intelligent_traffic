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
public class VoiceUtil {
    /**
     * 允许语音叠加的最大数量
     */
    public static final Integer MAX_COUNT = 6;
    /**
     * websocket会话sessionId
     */
    @ApiModelProperty("websocket会话sessionId")
    private String sessionId;
    /**
     * 声音文件存储绝对路径
     */
    @ApiModelProperty("声音文件存储绝对路径")
    private String voice;
    /**
     * 计数器，初始值为0，每叠加一次语音，count加一
     */
    @ApiModelProperty("计数器")
    private int count;
    /**
     * 声音对应的文本
     */
    @ApiModelProperty("声音对应的文本")
    private String command;
}
