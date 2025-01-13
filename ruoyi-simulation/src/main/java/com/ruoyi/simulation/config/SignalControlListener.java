package com.ruoyi.simulation.config;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.SignalBaselMapper;
import com.ruoyi.simulation.domain.Junction;
import com.ruoyi.simulation.domain.SignalBase;
import com.ruoyi.simulation.domain.TrafficLight;
import com.ruoyi.simulation.util.TrafficLightCouple;
import com.ruoyi.simulation.util.TrafficLightCouple.Orientation;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 信控优化管理的监听器
 */
@Component
public class SignalControlListener implements ApplicationListener<ApplicationStartedEvent> {
    /**
     * 每个路口id对应的红绿灯集合
     */
    private static final Map<Integer,List<TrafficLight>> junctionLightMap = new HashMap<>();;
    @Resource
    private SignalBaselMapper signalControlMapper;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        initialTrafficLight();
    }
    /**
     * 初始化红绿灯数据
     */
    public void initialTrafficLight(){
        Map<Integer, TrafficLight> trafficLightMap = new HashMap<>();
        //获取不同交通灯对应的信控信息
        List<SignalBase> signalBaseList = this.signalControlMapper.getSignalBaseList();
        for(SignalBase signal: signalBaseList){
            //设置不同红绿灯对应的信控数据
            int trafficLightId = signal.getTrafficLightId();
            TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
            if(trafficLight==null){
                trafficLight = new TrafficLight(trafficLightId);
                trafficLight.setFromDirection(signal.getFromDirection());
                trafficLight.setTurnDirection(signal.getTurnDirection());
                trafficLightMap.put(trafficLightId, trafficLight);
                //将红绿灯按路口进行划分
                int junctionId = signal.getJunctionId();
                junctionLightMap.putIfAbsent(junctionId,new ArrayList<>());
                List<TrafficLight> trafficLightList = junctionLightMap.get(junctionId);
                trafficLightList.add(trafficLight);
            }
            if(signal.getLightStatus()== SignalBase.LightStatus.RED){
                trafficLight.setRedTime(trafficLight.getRedTime()+signal.getDuration());
            }else if(signal.getLightStatus()== SignalBase.LightStatus.GREEN||signal.getLightStatus()== SignalBase.LightStatus.GREEN_YELLOW){
                trafficLight.setGreenTime(trafficLight.getGreenTime()+signal.getDuration());
            }else if(signal.getLightStatus()== SignalBase.LightStatus.YELLOW){
                trafficLight.setYellowTime(trafficLight.getYellowTime()+signal.getDuration());
            }
            if (trafficLight.getStartLight()==null){
                trafficLight.setStartLight(signal.getLightStatus());
            }
        }
        //循环设置各个红绿灯参数
        setCarlaTrafficLight(callUE4Engine, new ArrayList<>(trafficLightMap.values()));
    }

    /**
     * 设置carla中的红绿灯参数
     * @param trafficLightList
     */
    public static void setCarlaTrafficLight(CallUE4Engine callUE4Engine, List<TrafficLight> trafficLightList){
        for(TrafficLight trafficLight: trafficLightList){
            int trafficLightId = trafficLight.getTrafficLightId();
            int redTime = trafficLight.getRedTime();
            int greenTime = trafficLight.getGreenTime();
            int yellowTime = trafficLight.getYellowTime();
            String flag = trafficLight.getStartLight().toString().toUpperCase().substring(0,1);
            String command = "trafficlights_contorl_time.py --traffic_light_id "+trafficLightId+" --red_time "+redTime+" --green_time "+greenTime+" --yellow_time "+yellowTime+" --flag "+flag;
            callUE4Engine.executeExample(command);
        }
    }
    /**
     * 获取信号控制
     * @param junctionId 路口id
     * @return 交通信号
     */
    public static JSONObject getSignalControl(int junctionId){
        JSONObject trafficData = new JSONObject();
        //获取指定路口的红绿灯时间
        List<TrafficLight> trafficLightList = junctionLightMap.get(junctionId);
        if(trafficLightList!=null){
            for(TrafficLight trafficLight: trafficLightList){
                String fromDirection = trafficLight.getFromDirection().toLowerCase();
                trafficData.putIfAbsent(fromDirection, new JSONObject());
                JSONObject directionMap = trafficData.getJSONObject(fromDirection);
                String turnDirection = trafficLight.getTurnDirection().toLowerCase();
                directionMap.put(turnDirection, new JSONObject());
                JSONObject signalMap = directionMap.getJSONObject(turnDirection);
                signalMap.put("redDurationTime", trafficLight.getRedTime());
                signalMap.put("greenDurationTime", trafficLight.getGreenTime());
                signalMap.put("initialLight", trafficLight.getStartLight().toString().toLowerCase());
            }
        }
        return trafficData;
    }

    /**
     * 对红绿灯下的交通指数进行预处理
     * @param trafficLightList
     * @return
     */
    public static List<TrafficLightCouple> mergeTrafficLight(List<TrafficLight> trafficLightList){
        Junction junction = ProcessCommandListener.indirectionCollection.getCurrentJunction();
        //存储trafficLightId与红绿黄三种灯时长TrafficLight的映射关系到Map中
        Map<Integer, TrafficLight> trafficLightMap = new HashMap<>();
        for(TrafficLight trafficLight: junction.getTrafficLightList()){
            trafficLightMap.put(trafficLight.getTrafficLightId(), trafficLight);
        }
        //将红绿黄数据的TrafficLight设置到交通指数对应的TrafficLight中
        for(TrafficLight trafficLight: trafficLightList){
            int trafficLightId = trafficLight.getTrafficLightId();
            TrafficLight temp = trafficLightMap.get(trafficLightId);
            trafficLight.setAverageDelay(temp.getAverageDelay());
            trafficLight.setAverageDelayRate(temp.getAverageDelayRate());
            trafficLight.setStopTimes(temp.getStopTimes());
            trafficLight.setStopTimesRate(temp.getStopTimesRate());
            trafficLight.setCongestionMileage(temp.getCongestionMileage());
            trafficLight.setWaitVehicle(temp.getWaitVehicle());
        }
        //将交通灯根据所在的方位加入到组合中
        Map<Orientation, TrafficLightCouple> coupleMap = new HashMap<>();
        for(TrafficLight trafficLight: trafficLightList){
            Orientation orientation = getOrientation(trafficLight.getFromDirection(), trafficLight.getTurnDirection());
            coupleMap.putIfAbsent(orientation, new TrafficLightCouple(orientation));
            TrafficLightCouple couple = coupleMap.get(orientation);
            couple.add(trafficLight);
        }
        //相对方向的红绿灯参数设置为一致，以便能够是相对方向的红绿灯时间一致
        for(TrafficLightCouple trafficLightCouple: coupleMap.values()){
            trafficLightCouple.justifyIndirection();
        }
        return new ArrayList<>(coupleMap.values());
    }
    /**
     * 获取交通灯所在的组合方位
     * @param formDirection
     * @param turnDirection
     * @return
     */
    public static Orientation getOrientation(String formDirection, String turnDirection){
        if(StringUtils.equals(turnDirection,"FORWARD")) {
            if (StringUtils.equals(formDirection, "North") || StringUtils.equals(formDirection, "South")) {
                return Orientation.NORTH_SOUTH_FORWARD;
            } else {
                return Orientation.WEST_EAST_FORWARD;
            }
        } else {
            if (StringUtils.equals(formDirection, "North") || StringUtils.equals(formDirection, "South")) {
                return Orientation.NORTH_SOUTH_LEFT;
            }else {
                return Orientation.WEST_EAST_LEFT;
            }
        }
    }

    /**
     * 使用自动调控进行信控优化
     * @param callUE4Engine
     */
    public static void automaticRegulation(CallUE4Engine callUE4Engine){
        //获取指定路口的红绿灯集合
        List<TrafficLight> trafficLightList = SignalControlListener.junctionLightMap.get(ProcessCommandListener.junctionId);
        //计算总的红绿灯周期
        TrafficLight trafficLight = trafficLightList.get(0);
        int period = trafficLight.getGreenTime() +trafficLight.getRedTime() +trafficLight.getYellowTime();
        List<TrafficLightCouple> largeList = new ArrayList<>();
        //将红绿灯按大方向分组合并
        List<TrafficLightCouple> coupleList = mergeTrafficLight(trafficLightList);
        double totalIndirection = getTotalIndirection(coupleList);
        if(totalIndirection!=0d){
            //为绿灯时间低于18秒的红绿灯分配时间
            int remainTime = assignLowerTime(period, totalIndirection, coupleList, largeList);
            //为绿灯时间高于18秒的红绿灯分配时间
            assignHigherTime(period, remainTime, largeList);
            //循环设置各个红绿灯参数
            SignalControlListener.setCarlaTrafficLight(callUE4Engine, trafficLightList);
        }
    }
    /**
     * 计算总的指数
     */
    private static double getTotalIndirection(List<TrafficLightCouple> coupleList){
        double totalIndirection = 0d;
        for(TrafficLightCouple couple: coupleList){
            totalIndirection += couple.getIndirection();
        }
        return totalIndirection;
    }
    /**
     * 为绿灯时间低于18秒的红绿灯分配时间
     * @param period
     * @param totalIndirection
     * @param coupleList
     * @param largeList
     */
    private static int assignLowerTime(int period, double totalIndirection, List<TrafficLightCouple> coupleList, List<TrafficLightCouple> largeList){
        List<TrafficLightCouple> smallList = new ArrayList<>();
        //根据排队车辆数对红绿灯设置自动调优
        for(TrafficLightCouple couple: coupleList) {
            double rate = couple.getIndirection() / totalIndirection;
            int assignTime = (int) Math.ceil(period * rate);
            Orientation orientation = couple.getOrientation();
            if(orientation.equals(Orientation.NORTH_SOUTH_FORWARD)||orientation.equals(Orientation.WEST_EAST_FORWARD)){
                //直行道路绿灯时间一般不少于15秒，加上3秒的黄灯时间，一共18秒
                if(assignTime<18){
                    smallList.add(couple);
                }else{
                    largeList.add(couple);
                }
            }else if(orientation.equals(Orientation.NORTH_SOUTH_LEFT)||orientation.equals(Orientation.WEST_EAST_LEFT)){
                if(assignTime<18){
                    smallList.add(couple);
                }else {
                    largeList.add(couple);
                }
            }
        }
        //如果绿灯时间小于15秒，则设置为15秒
        int remainTime = period;
        for(TrafficLightCouple couple: smallList){
            couple.setLightTime(period - 18, 15, 3);
            remainTime -= 18;
        }
        return remainTime;
    }
    /**
     * 为绿灯时间高于18秒的红绿灯分配时间
     * @param period
     * @param remainTime
     * @param largeList
     */
    public static void assignHigherTime(int period, int remainTime, List<TrafficLightCouple> largeList){
        //计算剩余的指数
        double totalIndirection = 0d;
        for(TrafficLightCouple couple: largeList){
            totalIndirection += couple.getIndirection();
        }
        Collections.sort(largeList);
        int remainPeriod = remainTime;
        for(int i=0;i<largeList.size()-1;i++){
            TrafficLightCouple couple = largeList.get(i);
            double rate = couple.getIndirection() / totalIndirection;
            int assignTime = (int)Math.ceil(rate * remainPeriod);
            couple.setLightTime(period - assignTime, assignTime - 3, 3);
            remainTime -= assignTime;
        }
        TrafficLightCouple couple = largeList.get(largeList.size()-1);
        couple.setLightTime(period - remainTime, remainTime -3, 3);
    }
    /**
     * 直行优先调控
     * @param callUE4Engine
     */
    public static void directPriority(CallUE4Engine callUE4Engine){
        //获取指定路口的红绿灯集合
        List<TrafficLight> trafficLightList = SignalControlListener.junctionLightMap.get(ProcessCommandListener.junctionId);
        //计算总的红绿灯周期
        TrafficLight trafficLight = trafficLightList.get(0);
        int period = trafficLight.getGreenTime() +trafficLight.getRedTime() +trafficLight.getYellowTime();
        List<TrafficLightCouple> coupleList = mergeTrafficLight(trafficLightList);

        int remainTime = period;
        List<TrafficLightCouple> forwardList = new ArrayList<>();
        for(TrafficLightCouple couple:coupleList){
            Orientation orientation = couple.getOrientation();
            if(orientation.equals(Orientation.NORTH_SOUTH_LEFT)||orientation.equals(Orientation.WEST_EAST_LEFT)){
                couple.setLightTime(period - 18, 15, 3);
                remainTime -= 18;
            }else if(orientation.equals(Orientation.NORTH_SOUTH_FORWARD)||orientation.equals(Orientation.WEST_EAST_FORWARD)){
                forwardList.add(couple);
            }
        }
        int forwardTime = remainTime / forwardList.size();
        for(int i=0;i<forwardList.size()-1;i++){
            TrafficLightCouple couple = forwardList.get(i);
            couple.setLightTime(period - forwardTime, forwardTime - 3, 3);
            remainTime -= forwardTime;
        }
        TrafficLightCouple couple = forwardList.get(forwardList.size()-1);
        couple.setLightTime(period - remainTime, remainTime -3, 3);
        SignalControlListener.setCarlaTrafficLight(callUE4Engine, trafficLightList);
    }
}
