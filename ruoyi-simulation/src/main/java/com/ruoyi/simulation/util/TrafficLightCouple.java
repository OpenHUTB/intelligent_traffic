package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.TrafficLight;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 对向交通灯组合
 */
@Data
public class TrafficLightCouple implements Comparable {
    /**
     * 交通灯所在方向：东西直行，南北直行，东西左转，南北左转
     */
    public enum Orientation{
        WEST_EAST_FORWARD,NORTH_SOUTH_FORWARD,WEST_EAST_LEFT,NORTH_SOUTH_LEFT
    }

    /**
     * 所在方向
     */
    private Orientation orientation;
    /**
     * 指数比例
     */
    private Double indirection;
    /**
     * 交通灯组
     */
    private List<TrafficLight> couple = new ArrayList<>();
    public TrafficLightCouple(Orientation orientation){
        this.orientation = orientation;
    }
    /**
     * 调整对向的红绿灯参数，使得对向的参数能够对齐
     */
    public void justifyIndirection(){
        if(couple.size()>1){
            if (couple.get(0).getWaitVehicle() > couple.get(1).getWaitVehicle()) {
                couple.get(1).setWaitVehicle(couple.get(0).getWaitVehicle());
            } else {
                couple.get(0).setWaitVehicle(couple.get(1).getWaitVehicle());
            }
        }
        indirection = couple.get(0).getWaitVehicle() * 1.0;
    }

    /**
     * 添加红绿灯到红绿灯组中
     * @param trafficLight
     */
    public void add(TrafficLight trafficLight){
        couple.add(trafficLight);
    }
    @Override
    public int compareTo(@NotNull Object o) {
        TrafficLightCouple c = (TrafficLightCouple) o;
        return (int)Math.ceil(this.indirection - c.indirection);
    }

    /**
     * 设置红绿灯组的红黄蓝三种灯光的时间
     * @param redTime
     * @param greenTime
     * @param yellowTime
     */
    public void setLightTime(int redTime, int greenTime, int yellowTime){
        for(TrafficLight trafficLight: couple) {
            trafficLight.setRedTime(redTime);
            trafficLight.setGreenTime(greenTime);
            trafficLight.setYellowTime(yellowTime);
        }
    }
}
