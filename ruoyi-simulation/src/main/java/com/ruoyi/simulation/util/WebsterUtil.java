package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.TrafficLight;

import java.util.*;

/**
 * 韦伯斯特红绿灯优化方式工具类
 */
public class WebsterUtil {
    public static final Map<Double, Double> flowSpeedMap = new HashMap<>();
    /**
     * 设置流量比喻速度的对应关系
     * @param phaseList
     */
    static {
        flowSpeedMap.put(0.3, 11.11);
        flowSpeedMap.put(0.6, 8.33);
        flowSpeedMap.put(0.8, 5.56);
        flowSpeedMap.put(1d, 0d);
    }
    /**
     * 预设车道的最大流量
     */
    public final static int MAX_FLOW = 1800;
    /**
     * 所有相位的最大过街时间
     */
    public final static int MAX_WALK_TIME = 75;
    /**
     * 车辆启动损失时间
     */
    public final static int VEHICLE_START_LOSS = 0;
    /**
     * 行人通过斑马线的速度
     */
    public final static double WALKER_SPEED = 1.2d;
    /**
     * 行人启动损失时间
     */
    public final static int WALKER_START_LOSS = 0;
    /**
     * 单个相位的最短绿灯时间
     */
    public final static int MIN_GREEN_TIME = 10;
    /**
     * 单个相位的最长绿灯时间
     */
    public final static int MAX_GREEN_TIME = 45;
    /**
     * 用韦伯斯特公式获取每个路口最佳周期时间
     * @param junctionCoupleMap
     * @return
     */
    public static Map<Integer,Integer> getOptimalCycleTime(Map<Integer, List<TrafficLightCouple>> junctionCoupleMap){
        Map<Integer, Integer> junctionCycleMap = new HashMap<>();
        for(int junctionId: junctionCoupleMap.keySet()) {
            List<TrafficLightCouple> coupleList = junctionCoupleMap.get(junctionId);
            int totalLostTime = getLostTime(coupleList);
            double totalFlow = getTotalFlow(coupleList);
            int totalWalkTime = getWalkTime(coupleList);
            int cycle = (int)Math.round((1.5 * totalLostTime +5 + totalWalkTime)/(1-totalFlow));
            int min = coupleList.size()*WebsterUtil.MIN_GREEN_TIME;
            int max = coupleList.size()*WebsterUtil.MAX_GREEN_TIME;
            if(cycle<min){
                cycle = min;
            }else if(cycle>max){
                cycle = max;
            }
            junctionCycleMap.put(junctionId, cycle);
        }
        return junctionCycleMap;
    }
    /**
     * 获取每个路口所有相位的总损失时间
     * @param coupleList
     * @return
     */
    public static int getLostTime(List<TrafficLightCouple> coupleList){
        int totalLostTime = 0;
        for(TrafficLightCouple couple: coupleList){
            totalLostTime += WebsterUtil.VEHICLE_START_LOSS + couple.getClearingLostTime();
        }
        return totalLostTime;
    }

    /**
     * 获取所有相位的行人过街时间
     * @param coupleList
     * @return
     */
    public static int getWalkTime(List<TrafficLightCouple> coupleList){
        int totalWalkTime = 0;
        for(TrafficLightCouple couple: coupleList){
            totalWalkTime += WebsterUtil.WALKER_START_LOSS + couple.getWalkTime();
        }
        return totalWalkTime;
    }
    /**
     * 获取每个路口所有相位的总流量比
     * @param coupleList
     * @return
     */
    public static double getTotalFlow(List<TrafficLightCouple> coupleList){
        double totalFlow = 0d;
        for(TrafficLightCouple couple: coupleList) {
            totalFlow += couple.getFlowRate();
        }
        return totalFlow;
    }
    /**
     * 设置各个路口的信控方案
     * @param junctionCycleMap
     * @param junctionCoupleMap
     */
    public static void setJunctionSignal(Map<Integer,Integer> junctionCycleMap, Map<Integer,List<TrafficLightCouple>> junctionCoupleMap){
        for(int junctionId: junctionCycleMap.keySet()){
            int cycle = junctionCycleMap.get(junctionId);
            List<TrafficLightCouple> coupleList = junctionCoupleMap.get(junctionId);
            //计算不同路口下的红绿灯分配绿灯时间
            setGreenTime(cycle, coupleList);
            //计算不同路口下的红绿灯分配红黄蓝及起始红灯时间
            TrafficLightUtil.setTrafficLight(cycle, coupleList);
        }
    }
    /**
     * 为交通灯设置红绿黄配时
     * @param cycle
     * @param coupleList
     */
    public static void setGreenTime(int cycle, List<TrafficLightCouple> coupleList){
        int totalLostTime = WebsterUtil.getLostTime(coupleList);
        double totalFlowRate = WebsterUtil.getTotalFlow(coupleList);
        int totalWalkTime = WebsterUtil.getWalkTime(coupleList);
        int totalServiceableGreen = cycle - totalLostTime - totalWalkTime;
        int remainGreenTime = totalServiceableGreen;
        for(int i=0;i<coupleList.size()-1;i++){
            TrafficLightCouple couple = coupleList.get(i);
            //绿灯时间=车辆通行绿灯时间+行人最短通行时间
            int greenTime = (int)Math.round(totalServiceableGreen * couple.getFlowRate()/totalFlowRate);
            couple.setGreenTime(greenTime);
            remainGreenTime -= greenTime;
        }
        TrafficLightCouple last = coupleList.get(coupleList.size()-1);
        last.setGreenTime(remainGreenTime);
        //在各相位原有绿灯时间的基础上加入行人过街时间
        for(TrafficLightCouple couple: coupleList){
            //WebsterUtil.VEHICLE_START_LOSS
            couple.setGreenTime(WebsterUtil.VEHICLE_START_LOSS + WebsterUtil.WALKER_START_LOSS + couple.getGreenTime() + couple.getWalkTime());
        }
        justifyGreenTime(coupleList);
    }

    /**
     * 调整绿灯时间
     * @param coupleList
     */
    private static void justifyGreenTime(List<TrafficLightCouple> coupleList){
        List<TrafficLightCouple> largeList = new ArrayList<>();
        List<TrafficLightCouple> smallList = new ArrayList<>();
        for(TrafficLightCouple couple: coupleList){
            if(couple.getGreenTime()<10){
                smallList.add(couple);
            }else if(couple.getGreenTime()>10){
                largeList.add(couple);
            }
        }
        while(!largeList.isEmpty() && !smallList.isEmpty()){
            TrafficLightCouple max = getMax(largeList);
            TrafficLightCouple min = getMin(smallList);
            max.setGreenTime(max.getGreenTime()-1);
            min.setGreenTime(min.getGreenTime()+1);
            //重新获取绿灯时长大于10的红绿灯组
            List<TrafficLightCouple> tempList = new ArrayList<>();
            for(TrafficLightCouple couple: largeList){
                if(couple.getGreenTime()>10){
                    tempList.add(couple);
                }
            }
            largeList = tempList;
            //重新获取绿灯时长小于10的红绿灯组
            tempList = new ArrayList<>();
            for(TrafficLightCouple couple: smallList){
                if(couple.getGreenTime()<10){
                    tempList.add(couple);
                }
            }
            smallList = tempList;
        }
    }

    /**
     * 获取红绿灯组中绿灯最大的元素
     * @param coupleList
     * @return
     */
    private static TrafficLightCouple getMax(List<TrafficLightCouple> coupleList){
        TrafficLightCouple max = coupleList.get(0);
        for(int i=1;i<coupleList.size();i++){
            TrafficLightCouple couple = coupleList.get(i);
            if(couple.getGreenTime() > max.getGreenTime()){
                max = coupleList.get(i);
            }
        }
        return max;
    }
    /**
     * 获取红绿灯组中绿灯最小的元素
     * @param coupleList
     * @return
     */
    private static TrafficLightCouple getMin(List<TrafficLightCouple> coupleList){
        TrafficLightCouple min = coupleList.get(0);
        for(int i=1;i<coupleList.size();i++){
            TrafficLightCouple couple = coupleList.get(i);
            if(couple.getGreenTime() < min.getGreenTime()){
                min = coupleList.get(i);
            }
        }
        return min;
    }
}
