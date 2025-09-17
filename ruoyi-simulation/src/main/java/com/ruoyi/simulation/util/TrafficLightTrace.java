package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.TrafficLight;
import lombok.Data;

import java.util.List;

@Data
public class TrafficLightTrace {
    private List<TrafficLight> startList;
    private List<TrafficLight> endList;
}
