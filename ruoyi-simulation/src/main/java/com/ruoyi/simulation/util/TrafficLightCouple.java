package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.Signalbase;
import com.ruoyi.simulation.domain.Signalbase.TrafficLightState;
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
     * 行人通过斑马线的时间
     */
    private Integer walkTime = 0;
    /**
     * 前缀时间
     */
    private Integer prefixTime = 0;
    /**
     * 红绿灯对应的状态及持续时间
     */
    private List<StateStage> stageList = new ArrayList<>();
    /**
     * 交通灯组
     */
    private List<TrafficLight> trafficLightList = new ArrayList<>();
    public TrafficLightCouple(int phase){
        this.phase = phase;
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
    public void adjustIndirection(){
        for(TrafficLight trafficLight: trafficLightList){
            //设置相位清空损失时间：清空损失时间=启动损失时间+清空距离/车辆速度+安全余量
            double lowerSpeed = getLowerSpeed(flowRate);
            double clearanceDistance = trafficLight.getClearanceDistance() + 5;
            int clearingLostTime = (int)Math.round(clearanceDistance/lowerSpeed) + TrafficLight.SAFETY_MARGIN_TIME;
            //不同相位的红绿灯切换时间至少要有3秒，即3秒黄灯时间
            if(clearingLostTime<3){
                clearingLostTime = 3;
            }
            if(this.clearingLostTime<clearingLostTime){
                this.clearingLostTime = clearingLostTime;
            }
            //设置行人通过斑马线的时间
            double walkDistance = trafficLight.getWalkDistance();
            if(walkDistance==0){
                continue;
            }
            int walkTime = (int)Math.round(walkDistance/WebsterUtil.WALKER_SPEED) + TrafficLight.SAFETY_MARGIN_TIME;
            if(this.walkTime<walkTime){
                this.walkTime = walkTime;
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
     * @param cycle
     * @param prefixTime
     */
    public void setLightTime(int cycle, int prefixTime){
        this.prefixTime = prefixTime;
        int redTime = cycle - this.getGreenTime() - TrafficLight.YELLOW_TIME;
        int suffixTime = redTime - prefixTime;
        //设置相位组中的红绿灯stage
        if(this.prefixTime!=0){
            this.addState(TrafficLightState.RED, this.getPrefixTime());
        }
        this.addState(TrafficLightState.GREEN, this.greenTime);
        this.addState(TrafficLightState.YELLOW, TrafficLight.YELLOW_TIME);
        this.addState(TrafficLightState.RED, suffixTime);
        //设置每个交通灯的红绿黄时长
        for(TrafficLight trafficLight: trafficLightList){
            trafficLight.setRedTime(redTime);
            trafficLight.setGreenTime(this.greenTime);
            trafficLight.setYellowTime(TrafficLight.YELLOW_TIME);
            trafficLight.setStageList(this.getStageList());
        }
    }
    /**
     * 添加一个红绿灯状态
     * @param state
     * @param length
     */
    public void addState(Signalbase.TrafficLightState state, int length){
        StateStage stage = new StateStage();
        stage.setState(state);
        stage.setLength(length);
        this.stageList.add(stage);
    }

    /**
     * 重新计算该相位组经过绿波调整后的prefixTime
     */
    public void refreshPrefixTime(){
        int prefixTime = 0;
        int length = stageList.size();
        StateStage firstStage = stageList.get(0);
        StateStage lastStage = stageList.get(length-1);
        if(firstStage.getState()==TrafficLightState.GREEN){
            if(lastStage.getState()!=TrafficLightState.GREEN){
                this.prefixTime = 0;
            }else{
                this.prefixTime = 0 - lastStage.getLength();
            }
            return;
        }
        for(int i=0;i<stageList.size();i++){
            StateStage stage = stageList.get(i);
            if(stage.getState()== Signalbase.TrafficLightState.GREEN){
                break;
            }
            prefixTime += stage.getLength();
        }
        this.prefixTime = prefixTime;
    }
}
