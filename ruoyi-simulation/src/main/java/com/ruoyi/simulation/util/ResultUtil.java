package com.ruoyi.simulation.util;

import lombok.Data;

/**
 *  返回工具类
 * @param <T>
 */
@Data
public class ResultUtil <T>{
    public enum Status{
        SUCCESS,FAILED;
    }

    /**
     * 返回状态
     */
    private Status status = Status.FAILED;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 提示消息
     */
    private String message;
}
