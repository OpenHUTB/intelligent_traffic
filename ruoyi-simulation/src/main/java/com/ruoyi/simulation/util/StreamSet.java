package com.ruoyi.simulation.util;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;

import java.util.Date;
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
        DIGITAL_SIMULATION("DIGITAL_SIMULATION"),JUNCTION_CONTROL("JUNCTION_CONTROL"),ORDINARY("ORDINARY"),TRAFFIC_INDIRECTION("TRAFFIC_INDIRECTION");
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
     * 路口id
     */
    @ApiModelProperty("路口id")
    private Integer junctionId;
    /**
     * 交通指数信息
     */
    @ApiModelProperty("交通指数信息")
    private Object data;
}
