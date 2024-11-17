package com.ruoyi.simulation.util;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * 时间桶
 */
public class TimeBucket {
    /**
     * 时间周期
     */
    public static final int period = 5000;
    /**
     * 时间域-为当前时间戳除以3000得到的余数，以便得到不同周期的时间
     */
    private Long timeDomain;
    /**
     * Mqtt响应数据
     */
    private List<JSONObject> responseList;
    public static long getTimeDomain(long timestamp){
        return timestamp/period;
    }
    public Long getTimeDomain() {
        return timeDomain;
    }

    public void setTimeDomain(Long timeDomain) {
        this.timeDomain = timeDomain;
    }

    public List<JSONObject> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<JSONObject> responseList) {
        this.responseList = responseList;
    }
}
