package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.Signalbase.TrafficLightState;

/**
 * 不同红绿灯状态对应的时长
 */
public class StateStage {
    private TrafficLightState state;
    private int length;

    public TrafficLightState getState() {
        return state;
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
