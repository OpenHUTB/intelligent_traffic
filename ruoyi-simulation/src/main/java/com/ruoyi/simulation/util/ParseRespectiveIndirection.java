package com.ruoyi.simulation.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.config.TrafficIndirectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.DecimalFormat;
/**
 * 抽象类：用来解析redis中的不同车道、不同道路、不同红绿灯的交通指数
 */
public abstract class ParseRespectiveIndirection {
    private static final Logger logger = LoggerFactory.getLogger(ParseRespectiveIndirection.class);
    /**
     * 获取redis中的不同车道、不同道路、不同红绿灯的交通指数
     * @param redisKey redis中的键
     */
    public void getRespectiveIndirection(RedisTemplate<String,Object> redisTemplate, String redisKey){
        Object redisValue = redisTemplate.opsForValue().get(redisKey);
        if(redisValue!=null) {
            JSONObject jsonObject = JSON.parseObject(String.valueOf(redisValue));
            for (String key : jsonObject.keySet()) {
                //获取不同道路的实时平均速度
                double value = Double.parseDouble(String.valueOf(jsonObject.get(key)));
                if (value != 0) {
                    value = Math.ceil(value * 100)/100;
                    this.setRespectiveIndirection(key, value);
                }
            }
        }
    }
    /**
     * 设置实时平均车速
     * @param key 指数键
     * @param value 指数值
     */
    protected abstract void setRespectiveIndirection(String key, double value);
}
