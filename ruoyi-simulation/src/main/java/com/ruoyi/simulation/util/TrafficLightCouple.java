package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.TrafficLight;
import lombok.Data;
import java.util.*;

/**
 * 同一个相位中的交通灯组合
 */
@Data
public class TrafficLightCouple {
    /**
     * 所在相位
     */
    private Integer phase;
    /**
     * 流量比
     */
    private Double flowRate = 0d;
    /**
     * 清空损失时间
     */
    private Integer clearingLostTime = 0;
    /**
     * 绿灯时间
     */
    private Integer greenTime;
    /**
     * 相位前缀时间
     */
    private Integer prefixTime;
    /**
     * 行人通过斑马线的时间
     */
    private Integer walkTime = 0;
    /**
     * 交通灯组
     */
    private List<TrafficLight> trafficLightList = new ArrayList<>();
    public TrafficLightCouple(int phase, int prefixTime){
        this.phase = phase;
        this.prefixTime = prefixTime;
    }
    /**
     * 添加红绿灯到红绿灯组中
     * @param trafficLight
     */
    public void add(TrafficLight trafficLight){
        trafficLightList.add(trafficLight);
    }

    /**
     * 调整不同红绿灯对应的清空距离和行人过街时间
     */
    public void justifyIndirection(){
        for(TrafficLight trafficLight: trafficLightList){
            //设置相位清空损失时间：清空损失时间=启动损失时间+清空距离/车辆速度+安全余量
            double lowerSpeed = getLowerSpeed(flowRate);
            double clearanceDistance = trafficLight.getClearanceDistance() + 5;
            int clearingLostTime = WebsterUtil.VEHICLE_START_LOSS + (int)Math.round(clearanceDistance/lowerSpeed) + TrafficLight.SAFETY_MARGIN_TIME;
            if(this.clearingLostTime<clearingLostTime){
                this.clearingLostTime = clearingLostTime;
            }
            //设置行人通过斑马线的时间
            double walkDistance = trafficLight.getWalkDistance();
            if(walkDistance!=0){
                int walkTime = WebsterUtil.WALK_START_LOSS + (int)Math.round(walkDistance/WebsterUtil.WALK_SPEED) + TrafficLight.SAFETY_MARGIN_TIME;
                if(this.walkTime<walkTime){
                    this.walkTime = walkTime;
                }
            }
        }
    }
    /**
     * 获取流量对应的通过路口的最低速度
     * @param averageFlow
     * @return
     */
    private double getLowerSpeed(double averageFlow){
        List<Double> flowList = new ArrayList<>();
        for(Double flow : WebsterUtil.flowSpeedMap.keySet()){
            flowList.add(flow);
        }
        Collections.sort(flowList);
        double flowUpper = 0d;
        for(double flow: flowList){
            if(flow > averageFlow){
                flowUpper = flow;
                break;
            }
        }
        return WebsterUtil.flowSpeedMap.get(flowUpper);
    }

    /**
     * 设置红绿灯组的红黄蓝三种灯光的时间
     * @param redTime
     * @param greenTime
     * @param yellowTime
     * @param prefixTime
     */
    public void setLightTime(int redTime, int greenTime, int yellowTime, int prefixTime){
        for(TrafficLight trafficLight: trafficLightList){
            trafficLight.setRedTime(redTime);
            trafficLight.setGreenTime(greenTime);
            trafficLight.setYellowTime(yellowTime);
            trafficLight.setPrefixTime(prefixTime);
        }
    }
}
