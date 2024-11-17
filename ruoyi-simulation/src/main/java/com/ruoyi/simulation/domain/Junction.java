package com.ruoyi.simulation.domain;

import lombok.Data;

/**
 * 路口指标信息实体类
 */
@Data
public class Junction {
    /**
     * 路口方向id
     */
    private Integer junctionRoadId;
    /**
     * 路口方向名称
     */
    private String junctionRoadName;
    /**
     * 拥堵里程
     */
    private Double congestionMileage;
    /**
     * 拥堵里程趋势变化
     */
    private Double congestionMileageTrend;

    public Integer getJunctionRoadId() {
        return junctionRoadId;
    }

    public void setJunctionRoadId(Integer junctionRoadId) {
        this.junctionRoadId = junctionRoadId;
    }

    public String getJunctionRoadName() {
        return junctionRoadName;
    }

    public void setJunctionRoadName(String junctionRoadName) {
        this.junctionRoadName = junctionRoadName;
    }

    public Double getCongestionMileage() {
        return congestionMileage;
    }

    public void setCongestionMileage(Double congestionMileage) {
        this.congestionMileage = congestionMileage;
    }

    public Double getCongestionMileageTrend() {
        return congestionMileageTrend;
    }

    public void setCongestionMileageTrend(Double congestionMileageTrend) {
        this.congestionMileageTrend = congestionMileageTrend;
    }
}
