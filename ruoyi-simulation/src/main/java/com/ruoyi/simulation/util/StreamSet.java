package com.ruoyi.simulation.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 图像和声音数据
 */
@Data
public class StreamSet {
    public enum Status {
        AWAKENED("AWAKENED"),WAITING("WAITING");
        private final String status;
        Status(String status){
            this.status = status;
        }
        @Override
        public String toString() {
            return this.status;
        }
    }
    public enum Signal{
        DIGITAL_SIMULATION("DIGITAL_SIMULATION"),TRAFFIC_LIGHT_INSTRUCTION("TRAFFIC_LIGHT_INSTRUCTION"),ORDINARY("ORDINARY");
        private final String signal;
        Signal(String signal){
            this.signal = signal;
        }
        @Override
        public String toString() {
            return this.signal;
        }
    }
    /**
     * 提示消息
     */
    @ApiModelProperty("提示消息")
    private String message;
    /**
     * 声音信息数据流
     */
    @ApiModelProperty("声音信息数据流")
    private String sound;
    /**
     * READYING-预备状态，可以发送语音进行唤醒、AWAKENED-唤醒状态-可以发送命令进行执行、WAITING-执行状态-需要等待
     */
    @ApiModelProperty("唤醒状态")
    private String status;
    /**
     * 信号
     */
    @ApiModelProperty("提示信号")
    private String signal;
    /**
     * 设置红绿灯信息
     */
    @ApiModelProperty("红绿灯信息")
    private Map<String,Object> lightInfo;
}
