package com.ruoyi.traffic.service.area.impl;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrafficLightControlImpl {
    private static final int MAX_TIME = 60;
    private static final int MIN_TIME = 10;
    private static final int STARTUP_LOST_TIME = 2;
    private static final int SATURATION_HEADWAY = 3;
    private int current_state;
    private int phase;
    private Map<String, Integer> traffic_light;
    private int light_cycle_time;
    public TrafficLightControlImpl() {
        this.current_state = -1;
        this.phase = -1;
        this.traffic_light = new LinkedHashMap<>();
        this.light_cycle_time = 0;
    }
    public void getTrafficLightTime(List<Integer> state) {
        Map<String, Integer> trafficLightTime = new HashMap<>();
        trafficLightTime.put("NS_S", calculateLightTime(state.get(0), false));
        trafficLightTime.put("NS_L", calculateLightTime(state.get(1), true));
        trafficLightTime.put("EW_S", calculateLightTime(state.get(2), false));
        trafficLightTime.put("EW_L", calculateLightTime(state.get(3), true));
        this.light_cycle_time = trafficLightTime.values().stream().mapToInt(Integer::intValue).sum();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(trafficLightTime.entrySet());
        entries.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));
        this.traffic_light.clear();
        for (Map.Entry<String, Integer> entry : entries) {
            this.traffic_light.put(entry.getKey(), entry.getValue());
        }
    }
    private int calculateLightTime(int vehicleNum, boolean turn) {
        int laneNum = turn ? 1 : 3;
        int avgVehicleNum = vehicleNum / laneNum;
        int expectGreenLightTime = STARTUP_LOST_TIME + avgVehicleNum * SATURATION_HEADWAY;
        return Math.min(Math.max(expectGreenLightTime, MIN_TIME), MAX_TIME);
    }
   public Map<String, Object> getTrafficLightInfo(List<Integer> numList){
        TrafficLightControlImpl control= new TrafficLightControlImpl();
        control.getTrafficLightTime(numList);
        Map<String,Object> response =new HashMap<>();
        response.put("cycleTime",control.light_cycle_time);
        response.put("phaseOrder",new ArrayList<>(control.traffic_light.keySet()));
        response.put("phaseDurations",new ArrayList<>(control.traffic_light.values()));
        return response;
   }
}