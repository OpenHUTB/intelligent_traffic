package com.ruoyi.simulation.util;

import lombok.Data;

import java.util.Date;

/**
 * 获取时间戳工具类，将当前时间转换为从0开始的秒数
 */
@Data
public class TimestampUtil {
    private static Long timestamp;
    private TimestampUtil(){
    }
    public static double getCustomerTime(Date currentTime){
        if(timestamp==null){
            timestamp = currentTime.getTime();
        }
        long current = currentTime.getTime();
        return Math.round((current-timestamp)*2/100.0)/20.0;
    }
}
