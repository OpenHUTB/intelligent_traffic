package com.ruoyi.simulation.listener;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.*;
import com.ruoyi.simulation.domain.*;
import com.ruoyi.simulation.util.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 原始信控数据管理的监听器
 */
@Component
public class SignalControlListener implements ServletContextListener {
    /**
     * 每个路口id对应的红绿灯集合
     */
    private static final Map<Integer,List<TrafficLight>> junctionLightMap = new HashMap<>();;
    @Resource
    private SignalBaselMapper signalbaselMapper;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    private TrafficLightMapper trafficLightMapper;
    @Resource
    private DurationMapper phaseMapper;
    @Resource
    private FlowRecordMapper recordMapper;
    @Resource
    private GreenGroupMapper greenGroupMapper;
    @Resource
    public RedisTemplate<String,Object> redisTemplate;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        List<TrafficLight> trafficLightList = initialTrafficLight();
        this.fixedRegulation(trafficLightList);
        //临时存储信控方案
        this.setTemporarySignal(trafficLightList);
        TrafficLightUtil.setCarlaTrafficLight(redisTemplate, trafficLightList);
        this.callUE4Engine.executeExample("traffic_light_settings.py");
    }

    /**
     * 初始化红绿灯数据
     * @return
     */
    public List<TrafficLight> initialTrafficLight(){
        //获取不同交通灯对应的信控信息
        List<Signalbase> signalbaseList = this.signalbaselMapper.getSignalBaseList();
        List<TrafficLight> trafficLightList = this.trafficLightMapper.selectList(new QueryWrapper<>());
        Map<Integer, TrafficLight>  trafficLightMap = getTrafficLightMap(trafficLightList);
        for(Signalbase signal: signalbaseList){
            //设置不同红绿灯对应的信控数据
            int trafficLightId = signal.getTrafficLightId();
            TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
            if(signal.getLightStatus()==Signalbase.LightStatus.RED){
                if(trafficLight.getRedTime()==null){
                    trafficLight.setRedTime(0);
                }
                trafficLight.setRedTime(trafficLight.getRedTime()+signal.getDuration());
                if(trafficLight.getGreenTime()==null || trafficLight.getGreenTime()==0){
                    trafficLight.setPrefixTime(trafficLight.getPrefixTime()+signal.getDuration());
                }
            }else if(signal.getLightStatus()==Signalbase.LightStatus.GREEN || signal.getLightStatus()==Signalbase.LightStatus.GREEN_YELLOW){
                //获取绿灯所在相位
                if(trafficLight.getGreenPhase()==null){
                    int phaseId = signal.getPhase();
                    trafficLight.setGreenPhase(phaseId);
                }
                trafficLight.setGreenTime(trafficLight.getGreenTime()+signal.getDuration());
            }else if(signal.getLightStatus()== Signalbase.LightStatus.YELLOW){
                if(trafficLight.getYellowTime()==null){
                    trafficLight.setYellowTime(0);
                }
                trafficLight.setYellowTime(trafficLight.getYellowTime()+signal.getDuration());
            }
        }
        return trafficLightList;
    }

    /**
     * 获取交通灯信息
     * @param trafficLightList
     * @return
     */
    private Map<Integer, TrafficLight> getTrafficLightMap(List<TrafficLight> trafficLightList){
        Map<Integer, TrafficLight> trafficLightMap = new HashMap<>();
        for(TrafficLight trafficLight : trafficLightList){
            trafficLightMap.put(trafficLight.getTrafficLightId(), trafficLight);
        }
        return trafficLightMap;
    }
    /**
     * 固定周期信控优化
     * @param trafficLightList
     * @throws InterruptedException
     */
    public void fixedRegulation(List<TrafficLight> trafficLightList) {
        //获取所有的绿波组
        List<GreenGroup> groupList = this.greenGroupMapper.getGroupList();
        //获取区域中的红绿灯时段划分信息列表
        List<Duration> phaseList = this.phaseMapper.selectList(new QueryWrapper<>());
        int durationId = this.getDurationId(phaseList);
        //获取指定时段下各个交通灯的平均流量记录
        LambdaQueryWrapper<FlowRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowRecord::getDurationId, durationId);
        List<FlowRecord> recordList = this.recordMapper.selectList(queryWrapper);
        FixedRegulation.assignSignalControl(trafficLightList, recordList, groupList);
    }
    private int getDurationId(List<Duration> phaseList){
        return 1;
    }
    /**
     * 自适应信控优化
     * @throws InterruptedException
     */
    public void automaticRegulation() {
        //获取不同路口对应的红绿灯集合
        List<TrafficLight> trafficLightList = this.trafficLightMapper.selectList(new QueryWrapper<>());
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(3 * 60000);
                        AutoRegulation.assignSignalControl(trafficLightList);
                        //临时存储信控方案
                        setTemporarySignal(trafficLightList);
                        TrafficLightUtil.setCarlaTrafficLight(redisTemplate, trafficLightList);
                        Thread.sleep(27 * 60000);
                    } catch (InterruptedException e) {
                        LoggerUtil.printLoggerStace(e);
                    }
                } while(true);
            }
        }).start();
    }
    /**
     * 临时存储信控方案信息
     * @param trafficLightList
     */
    private void setTemporarySignal(List<TrafficLight> trafficLightList){
        for(TrafficLight trafficLight: trafficLightList){
            //将红绿灯按路口进行划分
            int junctionId = trafficLight.getJunctionId();
            junctionLightMap.putIfAbsent(junctionId, new ArrayList<>());
            List<TrafficLight> tempList = junctionLightMap.get(junctionId);
            int index = ListUtil.getIndex(tempList, trafficLight);
            if(index!=-1){
                tempList.set(index, trafficLight);
            }else{
                tempList.add(trafficLight);
            }
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
                signalMap.put("yellowDurationTime", trafficLight.getYellowTime());
                signalMap.put("prefixDurationTime", trafficLight.getPrefixTime());
            }
        }
        return trafficData;
    }
}
